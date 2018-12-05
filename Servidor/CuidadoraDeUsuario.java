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
            String nomeEscolhido = null;
            enviarSalas(oos, sds);
            String nomeSala = ;
            verificarErro(nomeSala);
            enviarAvisoDeEntrada();
            interagir();
            removerUsuario();
        }
        catch(Exception e){}
    }
    protected void enviarSalas(ObjectOutputStream oos, SalasDisponiveis sds)
    {
        oos = new ObjectOutputStream(conexao.getOutputStream());
        sds = new SalasDisponiveis(this.salas);
        oos.writeObject(sds);
        oos.flush();
    }
    protected void verificarErro(String nomeSala)
    {
        boolean houveErro = true;
        nomeEscolhido = (String)ois.readObject();
        nomeSala = (String)ois.readObject();
        salaEscolhida = null;
        ois = new ObjectInputStream(conexao.getInputStream());
        for (int i = 0; i < vetSalas.size(); i++)
        {
            if(vetSalas.get(i).getNome().equals(nomeSala))
            {
                salaEscolhida = vetSalas.get(i);
                break;
            }
        }
        us = salaEscolhida.getUsuarios();
        while(houveErro)
        {
            houveErro = false;
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
    }
    protected void enviarAvisoDeEntrada()
    {
        this.usuario = new Usuario(this.conexao, oos, ois, nomeEscolhido, salaEscolhida);
        salaEscolhida.adicionarUsuario(this.usuario);
        for (int i = 0; i < us.size(); i++)
        {
            this.usuario.envia(new Aviso(1), us.get(i).getNickname());
            if (this.usuario != us.get(i))
                us.get(i).envia(new Aviso(1), this.usuario.getNickname());
        }
    }
    protected void removerUsuario()
    {
        salaEscolhida.removerUsuario(this.usuario);
        us = salaEscolhida.getUsuarios();
        for (int i = 0; i < us.size(); i++)
            this.usuario.envia(new Aviso(2), us.get(i).getNickname());
        this.usuario.fechaTudo();
    }
    protected void interagir()
    {
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
                    for (int i = 1; i < dest.size(); i++)
                        this.usuario.envia(aviso, dest.get(i));
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