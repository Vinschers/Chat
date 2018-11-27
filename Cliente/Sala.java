import java.util.*;
import java.lang.*;
import java.io.*;

public class Sala implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String nome;
    protected int qtdMaxima;
    protected int numeroUsuarios;
    protected ArrayList<Usuario> usuarios;

    public Sala(String nome, int qtd) throws Exception
    {
        if (nome == null || nome.equals(""))
            throw new Exception("Nome de sala null ou vazio.");
        if (qtd <= 0)
            throw new Exception("Quantidade máxima de usuários menor ou igual a 0.");
        this.nome = nome;
        this.qtdMaxima = qtd;
        numeroUsuarios = 0;
        usuarios = new ArrayList<Usuario>();
    }

    public Sala(Sala s) throws Exception
    {
        if (s == null)
            throw new Exception("Sala era null.");
        this.nome = s.nome;
        this.qtdMaxima = s.qtdMaxima;
        this.numeroUsuarios = s.numeroUsuarios;
        this.usuarios = new ArrayList<Usuario>(s.usuarios);
    }
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != this.getClass())
            return false;
        Sala s = (Sala)obj;
        if (s.nome != this.nome)
            return false;
        if (s.qtdMaxima != this.qtdMaxima)
            return false;
        if (s.numeroUsuarios != this.numeroUsuarios)
            return false;
        if (!this.usuarios.equals(s.usuarios))
            return false;
        return true;
    }
    public int hashCode()
    {
        int ret = 1;
        ret = ret * 3 + nome.hashCode();
        ret = ret * 5 + new Integer(this.qtdMaxima).hashCode();
        ret = ret * 7 + new Integer(this.numeroUsuarios).hashCode();
        ret = ret * 11 + usuarios.hashCode();
        return ret;
    }
    public Object clone()
    {
        Sala s = null;
        try {s = new Sala(this);}
        catch(Exception e) {}
        return s;
    }
    public String toString()
    {
        String ret = "";
        ret += "\"" + this.nome + "\" --> Usuários adicionados: " + this.numeroUsuarios + "/" + this.qtdMaxima;
        return ret;
    }

    public String getNome()
    {
        return this.nome;
    }
    public void setNome(String n) throws Exception
    {
        if (n == null || n.equals(""))
            throw new Exception("Nome vazio ou null.");
        this.nome = n;
    }

    public int getNumeroUsuarios()
    {
        return this.numeroUsuarios;
    }
    
    public int getCapacidade()
    {
        return this.qtdMaxima;
    }

    public ArrayList<Usuario> getUsuarios()
    {
        return this.usuarios;
    }

    public void adicionarUsuario(Usuario u) throws Exception
    {
        if (u == null)
            throw new Exception("Usuário estava null.");
        if (this.numeroUsuarios + 1 > this.qtdMaxima)
            throw new Exception("Capacidade máxima atingida.");
        for (int i = 0; i < this.numeroUsuarios; i++)
            if (usuarios.get(i).getNickname() == u.getNickname())
                throw new Exception("Nickname já existe");
        usuarios.add(u);
        this.numeroUsuarios++;
    }
    public void removerUsuario(Usuario u) throws Exception
    {
        if (u == null)
            throw new Exception("Usuário inválido");
        this.numeroUsuarios--;
        this.usuarios.remove(u);
    }
    public boolean isCheia()
    {
        return this.qtdMaxima == this.numeroUsuarios;
    }
}