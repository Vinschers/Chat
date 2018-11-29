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
            throw new Exception("Conexão inexistente");
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
            boolean jaExiste;
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
                    oos.writeObject(new AvisoErro("Sala nao encontrada"));
                    houveErro = true;
                }
                else if (salaEscolhida.isCheia())
                {
                    oos.writeObject(new AvisoErro("Sala ja esta cheia!"));
                    houveErro = true;
                }
                else if (nomeEscolhido == null || nomeEscolhido.equals(""))
                {
                    oos.writeObject(new AvisoErro("Nome invalido!"));
                    houveErro = true;
                }
                else
                {
                    us = salaEscolhida.getUsuarios();
                    for (int i = 0; i < us.size(); i++)
                    {
                        if (us.get(i).getNickname() == nomeEscolhido)
                        {
                            oos.writeObject(new AvisoErro("Nome de usuário já existe na sala!"));
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
                this.usuario.envia(new AvisoDeEntradaNaSala(), us.get(i).getNickname());
                if (i != us.size() - 1)
                    s.get(i).envia(new AvisoDeEntradaNaSala(), this.usuario.getNickname());
            }
            Enviavel recebido = null;
            Mensagem aux;
            ArrayList<String> dest;
            do
            {
                recebido = (Enviavel)ois.readObject();
                if (recebido instanceof Mensagem)
                {
                    aux = (Mensagem)recebido;
                    dest = aux.getDestinatarios();
                    for (int i = 0; i < dest.size(); i++)
                        this.usuario.envia(aux, dest.get(i));
                }
            }
            while (!(recebido instanceof PedidoParaSairDaSala));
            salaEscolhida.removerUsuario(this.usuario);
            us = salaEscolhida.getUsuarios();
            for (int i = 0; i < us.size(); i++)
                this.usuario.envia(new AvisoDeSaidaDaSala(), us.get(i).getNickname());
            this.usuario.fechaTudo();
        }
        catch(Exception e){}
    }
}