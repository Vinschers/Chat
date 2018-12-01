import java.net.Socket;
import java.io.*;
import java.util.*;

/**
 * Recebe as mensagens que o usuário deseja mandar e envia para os outros usuários na sala escolhida
 */
public class CuidadoraDeUsuario extends Thread
{
    private Socket  conexao;
    private Salas   salas;
    private Usuario usuario;

    public CuidadoraDeUsuario(Socket conexao, Salas s) throws Exception 
    {
        if (conexao == null)
            throw new Exception("Conex\u00E3o inexistente");
        if (s == null)
            throw new Exception("Salas vazias");
        this.conexao = conexao;
        this.salas = s;
    }

    public void run()
    {
        try
        {
            boolean houveErro = true;
            ObjectOutputStream oos = new ObjectOutputStream(conexao.getOutputStream());
            SalasDisponiveis sds = new SalasDisponiveis(this.salas);

            oos.writeObject(sds);
            oos.flush();
            
            ObjectInputStream ois = new ObjectInputStream(conexao.getInputStream());
            ArrayList<Usuario> us = null;
            String nomeEscolhido = null;
            String nomeSala;
            ArrayList<Sala> vetSalas = this.salas.getSalas();
            Sala salaEscolhida = null;

            while(houveErro)
            {
                houveErro = false;
                nomeEscolhido = (String)ois.readObject();
                nomeSala = (String)ois.readObject();
                salaEscolhida = null;
                for (int i = 0; i < vetSalas.size(); i++)
                {
                    if(vetSalas.get(i).getNome().equals(nomeSala))
                    {
                        salaEscolhida = vetSalas.get(i);
                        break;
                    }
                }
                if (salaEscolhida == null)
                {
                    oos.writeObject(new Aviso(3, "Sala n\u00E3o encontrada"));
                    houveErro = true;
                }
                else if (salaEscolhida.isCheia())
                {
                    oos.writeObject(new Aviso(3, "Sala j\u00E1 est\u00E1 cheia!"));
                    houveErro = true;
                }
                else if (nomeEscolhido == null || nomeEscolhido.equals(""))
                {
                    oos.writeObject(new Aviso(3, "Nome inv\u00E1lido!"));
                    houveErro = true;
                }
                else
                {
                    us = salaEscolhida.getUsuarios();
                    for (int i = 0; i < us.size(); i++)
                    {
                        if (us.get(i).getNickname().equals(nomeEscolhido))
                        {
                            oos.writeObject(new Aviso(3, "Nome de usu\u00E1rio j\u00E1 existe na sala!"));
                            houveErro = true;
                            break;
                        }
                    }
                }
                if (houveErro)
                    oos.flush();
            }
            oos.writeObject("ok");
            oos.flush();
            this.usuario = new Usuario(this.conexao, oos, ois, nomeEscolhido, salaEscolhida);
            salaEscolhida.adicionarUsuario(this.usuario);
            for (int i = 0; i < us.size(); i++)
            {
                this.usuario.envia(new Aviso(1), us.get(i).getNickname());
                if (this.usuario != us.get(i))
                    us.get(i).envia(new Aviso(1), this.usuario.getNickname());
            }
            Enviavel recebido = null;
            Mensagem msg;
            Aviso aviso;
            ArrayList<String> dest;
            do
            {
                recebido = (Enviavel)ois.readObject();
                if (recebido instanceof Mensagem)
                {
                    msg = (Mensagem)recebido;
                    dest = msg.getDestinatarios();
                    for (int i = 1; i < dest.size(); i++)
                        this.usuario.envia(msg, dest.get(i));
                }
                else if (recebido instanceof Aviso)
                {
                    aviso = (Aviso)recebido;
                    dest = aviso.getDestinatarios();
                    if (aviso.getTipo() == 4 || aviso.getTipo() == 5)
                    {
                        for (int i = 1; i < dest.size(); i++)
                            this.usuario.envia(aviso, dest.get(i));
                    }
                }
            }
            while (!(recebido instanceof PedidoParaSairDaSala));
            salaEscolhida.removerUsuario(this.usuario);
            us = salaEscolhida.getUsuarios();
            for (int i = 0; i < us.size(); i++)
                this.usuario.envia(new Aviso(2), us.get(i).getNickname());
            this.usuario.fechaTudo();
        }
        catch(Exception e){}
    }
}