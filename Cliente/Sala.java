import java.util.*;
import java.lang.*;
import java.io.*;

public class Sala implements Cloneable
{
    protected String nome;
    protected int qtdMaxima;
    protected ArrayList<Usuario> usuarios;

    public Sala(String nome, int qtd) throws Exception
    {
        if (nome == null || nome.equals(""))
            throw new Exception("Nome de sala null ou vazio.");
        if (qtd <= 0)
            throw new Exception("Quantidade máxima de usuários menor ou igual a 0.");
        this.nome = nome;
        this.qtdMaxima = qtd;
        usuarios = new ArrayList<Usuario>();
    }

    public Sala(Sala s) throws Exception
    {
        if (s == null)
            throw new Exception("Sala era null.");
        this.nome = s.nome;
        this.qtdMaxima = s.qtdMaxima;
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
        if (!this.usuarios.equals(s.usuarios))
            return false;
        return true;
    }
    public int hashCode()
    {
        int ret = 1;
        ret = ret * 3 + nome.hashCode();
        ret = ret * 5 + new Integer(this.qtdMaxima).hashCode();
        ret = ret * 7 + usuarios.hashCode();
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
        ret += "\"" + this.nome + "\"            Usu\u00E1rios conectados: " + this.usuarios.size() + "/" + this.qtdMaxima;
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
        return this.usuarios.size();
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
            throw new Exception("Usu\u00E1rio estava null.");
        if (this.usuarios.size() + 1 > this.qtdMaxima)
            throw new Exception("Capacidade m\u00E1xima atingida.");
        for (int i = 0; i < this.usuarios.size(); i++)
            if (usuarios.get(i).getNickname() == u.getNickname())
                throw new Exception("Nickname j\u00E1 existe");
        usuarios.add(u);
    }
    public void removerUsuario(Usuario u) throws Exception
    {
        if (u == null)
            throw new Exception("Usu\u00E1rio inv\u00E1lido");
        this.usuarios.remove(u);
    }
    public boolean isCheia()
    {
        return this.qtdMaxima == this.usuarios.size();
    }
    public Usuario getUsuario(String nome) throws Exception
    {
        if (nome == null || nome.equals(""))
            throw new Exception("Nome inv\u00E1lido");
        Usuario ret = null;
        for (int i = 0; i < this.usuarios.size(); i++)
        {
            if (this.usuarios.get(i).getNickname().equals(nome))
                ret = new Usuario(this.usuarios.get(i));
        }
        if (ret == null)
            throw new Exception("Usu\u00E1rio nao encontrado");
        return ret;
    }
}