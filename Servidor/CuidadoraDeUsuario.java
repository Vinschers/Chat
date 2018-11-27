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
            oos.writeObject(new SalasDisponiveis(this.salas));
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(conexao.getInputStream());
            ArrayList<Usuario> us = null;
            String nomeEscolhido = null;
            String nomeSala;
            ArrayList<Sala> vetSalas;
            Sala salaEscolhida = null;
            boolean jaExiste;
            while(houveErro)
            {
                houveErro = false;
                System.out.println("Esperando o nome");
                nomeEscolhido = (String)ois.readObject();
                System.out.println("Esperando o nome da sala");
                nomeSala = (String)ois.readObject();
                System.out.println("Leu tudo");
                vetSalas = this.salas.getSalas();
                salaEscolhida = null;
                int codErro = 0;
                for (int i = 0; i < vetSalas.size(); i++)
                {
                    if(vetSalas.get(i).getNome().equals(nomeSala))
                    {
                        salaEscolhida = vetSalas.get(i);
                        System.out.println("Achou a sala");
                        break;
                    }
                }
                System.out.println("Saiu do loop, vai comecar a verificar");
                if (salaEscolhida == null)
                {
                    oos.writeObject(new AvisoErro("Sala nao encontrada"));
                    houveErro = true;
                    codErro = 0;
                }
                else if (salaEscolhida.isCheia())
                {
                    oos.writeObject(new AvisoErro("Sala ja esta cheia!"));
                    houveErro = true;
                    codErro = 1;
                }
                else if (nomeEscolhido == null || nomeEscolhido.equals(""))
                {
                    oos.writeObject(new AvisoErro("Nome invalido!"));
                    houveErro = true;
                    codErro = 2;
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
                            codErro = 3;
                            break;
                        }
                    }
                }
                oos.flush();
                if (houveErro)
                    System.out.println("Deu erro. Codigo: " + codErro);
            }
            System.out.println("Tudo certo, vai mandar ok");
            oos.writeObject("ok");
            oos.flush();
            System.out.println("Mandou.");
            this.usuario = new Usuario(this.conexao, oos, ois, nomeEscolhido, salaEscolhida);

            System.out.println("Usuario " + nomeEscolhido + " criado. Iniciando o loop com " + us.size() + " usuario(s) ja conectados.");
            for (int i = 0; i < us.size(); i++)
            {
                System.out.println(nomeEscolhido + " mandando para " + us.get(i).getNickname());
                this.usuario.envia(new AvisoDeEntradaNaSala(), us.get(i).getNickname());
                System.out.println("Mandou. Agora " + us.get(i).getNickname() + " esta mandando para " + nomeEscolhido);
                us.get(i).envia(new AvisoDeEntradaNaSala(), this.usuario.getNickname());
                System.out.println("Mandou");
            }
            salaEscolhida.adicionarUsuario(this.usuario);
            // Fazer várias vezes this.usuario.envia(new AvisoDeEntradaNaSala(i)), onde i é o nome de algum usuário na sala
            // Fazer várias vezes i.envia(new AvisoDeEntradaNaSala(usuario.getNome())), onde i é o nome de algum usuário na sala
            // Incluir o usuario na sala

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
                // receber mensagens, avisos de entrada na e de saida da sala
                // se for mensagem, pega nela o destinatario, acha o destinatario na sala e manda para ele a mensagem
            }
            while (!(recebido instanceof PedidoParaSairDaSala));

            // Remover this.usuario da sala
            // Mandar para todos da sala: new AvisoDeSaidaDaSala(this.usuario.getNome())
            salaEscolhida.removerUsuario(this.usuario);
            us = salaEscolhida.getUsuarios();
            for (int i = 0; i < us.size(); i++)
            {
                this.usuario.envia(new AvisoDeEntradaNaSala(), us.get(i).getNickname());
                us.get(i).envia(new AvisoDeEntradaNaSala(), this.usuario.getNickname());
            }
            this.usuario.fechaTudo();
        }
        catch(Exception e){}
    }
}