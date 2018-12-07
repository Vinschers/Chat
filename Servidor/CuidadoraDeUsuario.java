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
    private ObjectOutputStream oos;
    private SalasDisponiveis sds;
    private ObjectInputStream ois;
    private ArrayList<Usuario> us = null;
    private ArrayList<Sala> vetSalas = this.salas.getSalas();
    private Sala salaEscolhida = null;

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
            enviarSalas(); //envia as salas disponíveis para o usuário escolher
            String nome = "";
            lerNomeESala(nome); //lê o nome e a sala do usuário e faz as verificações necessárias
            adicionarUsuario(nome); //instancia o usuário, adiciona na sala e manda aviso de entrada para todos
            interagir(); //método principal responsável por receber e enviar mensagens do usuário
            removerUsuario(); //quando o aviso de saida chegar, remove o usuário da sala
        }
        catch(Exception e){System.out.println(e.getMessage());}
    }
    protected void enviarSalas() throws Exception
    {
        oos = new ObjectOutputStream(conexao.getOutputStream()); //instancia o ObjectOutputStream
        sds = new SalasDisponiveis(this.salas); //instancia o objeto serializable que contém todas as salas disponíveis
        oos.writeObject(sds); //envia as salas disponíveis
        oos.flush();
    }
    protected void lerNomeESala(String nomeEscolhido) throws Exception
    {
        boolean houveErro = true;
        while(houveErro)
        {
            nomeEscolhido = (String)ois.readObject();
            String nomeSala = (String)ois.readObject();
            ois = new ObjectInputStream(conexao.getInputStream()); //instanciação do ObjectInputStream
            for (int i = 0; i < vetSalas.size(); i++)
            {
                if(vetSalas.get(i).getNome().equals(nomeSala))
                {
                    salaEscolhida = vetSalas.get(i); //acha a sala que o usuário escolheu
                    break;
                }
            }
            if (salaEscolhida != null)
                us = salaEscolhida.getUsuarios(); //pega os usuários dessa sala se ela existir
            houveErro = false;
            if (salaEscolhida == null) //se a sala não existir
            {
                oos.writeObject(new Aviso(3, "Sala n\u00E3o encontrada"));
                houveErro = true;
            }
            else if (salaEscolhida.isCheia()) //se a sala estiver cheia
            {
                oos.writeObject(new Aviso(3, "Sala j\u00E1 est\u00E1 cheia!"));
                houveErro = true;
            }
            else if (nomeEscolhido == null || nomeEscolhido.equals("")) //se o nome do usuário estiver vazio ou for nulo
            {
                oos.writeObject(new Aviso(3, "Nome inv\u00E1lido!"));
                houveErro = true;
            }
            else
            {
                for (int i = 0; i < us.size(); i++)
                {
                    if (us.get(i).getNickname().equals(nomeEscolhido)) //se houver algum usuário na sala com o nome do usuário atual
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
        oos.writeObject("ok"); // quando não houverem erros, um "ok" é mandado para identificar o sucesso
        oos.flush();
    }
    protected void adicionarUsuario(String nomeEscolhido) throws Exception
    {
        this.usuario = new Usuario(conexao, oos, ois, nomeEscolhido, salaEscolhida); //instanciação do usuário
        salaEscolhida.adicionarUsuario(this.usuario); //adiciona o usuário na sala
        for (int i = 0; i < us.size(); i++)
        {
            this.usuario.envia(new Aviso(1), us.get(i).getNickname()); //envia que o usuário entrou para todos presentes
            if (this.usuario != us.get(i))
                us.get(i).envia(new Aviso(1), this.usuario.getNickname()); //envia para o usuário todos que entraram, menos ele mesmo
        }
    }
    protected void removerUsuario() throws Exception
    {
        salaEscolhida.removerUsuario(this.usuario); //retira o usuário da sala
        us = salaEscolhida.getUsuarios();
        for (int i = 0; i < us.size(); i++)
            this.usuario.envia(new Aviso(2), us.get(i).getNickname()); // manda para todos os restantes que o usuário saiu
        this.usuario.fechaTudo(); //usuário fecha toda forma de conexão
    }
    protected void interagir() throws Exception
    {
        Enviavel recebido = null;
        Mensagem msg;
        Aviso aviso;
        ArrayList<String> dest;
        do
        {
            recebido = (Enviavel)ois.readObject();
            if (recebido instanceof Mensagem) //se o usuário receber uma mensagem
            {
                msg = (Mensagem)recebido;
                dest = msg.getDestinatarios(); //acessa os destinatários da mensagem
                for (int i = 1; i < dest.size(); i++)
                    this.usuario.envia(msg, dest.get(i)); //envia a mensagem para todos os destinatários
            }
            else if (recebido instanceof Aviso) //se o usuário receber um aviso
            {
                aviso = (Aviso)recebido;
                dest = aviso.getDestinatarios(); //acessa para quem o aviso é destinado
                if (aviso.getTipo() == 4) //se o aviso for de digitação de outro usuário
                    for (int i = 1; i < dest.size(); i++)
                        this.usuario.envia(aviso, dest.get(i)); //envia para todos que o usuário que mandou o aviso está digitando
            }
        }
        while (!(recebido instanceof PedidoParaSairDaSala));
    }
    public String toString()
    {
        return this.usuario.toString() + " - " + this.conexao;
    }
    public int hashCode()
    {
        int ret = super.hashCode();
        ret = ret * 3 + conexao.hashCode();
        ret = ret * 3 + salas.hashCode();
        ret = ret * 3 + usuario.hashCode();
        ret = ret * 3 + oos.hashCode();
        ret = ret * 3 + sds.hashCode();
        ret = ret * 3 + ois.hashCode();
        ret = ret * 3 + us.hashCode();
        ret = ret * 3 + vetSalas.hashCode();
        ret = ret * 3 + salaEscolhida.hashCode();

        return ret;
    }
    public boolean equals(Object obj)
    {
        if (!super.equals(obj))
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        CuidadoraDeUsuario c = (CuidadoraDeUsuario)obj;
        if (!c.conexao.equals(this.conexao))
            return false;
        if (!c.salas.equals(this.salas))
            return false;
        if (!c.usuario.equals(this.usuario))
            return false;
        if (!c.oos.equals(this.oos))
            return false;
        if (!c.sds.equals(this.sds))
            return false;
        if (!c.ois.equals(this.ois))
            return false;
        if (!c.us.equals(this.us))
            return false;
        if (!c.vetSalas.equals(this.vetSalas))
            return false;
        if (!c.salaEscolhida.equals(this.salaEscolhida))
            return false;
        return true;
    }
}