import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Usuario implements Cloneable, Comparable
{
    protected String nickname;

    protected Sala sala;
    protected Socket conexao;
    protected PrintWriter transmissor; 
    protected BufferedReader receptor;

    public Usuario(Socket s, 
                   PrintWriter transmissor,
                   BufferedReader receptor,
                   String nickname, 
                   Sala sala) throws Exception
    {
        if (s == null)
            throw new Exception("Socket ausente");
        if (transmissor == null)
            throw new Exception("Transmissor ausente");
        if (receptor == null)
            throw new Exception("Receptor ausente");
        if (nickname == null || nickname.equals(""))
            throw new Exception("Nickname ausente");
        if (sala == null)
            throw new Exception("Sala ausente");

        this.conexao = s;
        this.transmissor = transmissor;
        this.receptor = receptor;
        this.nickname = nickname;
        this.sala = sala;
    }
    public String getNickname()
    {
        return this.nickname;
    }
    public void setNickname(String nickname) throws Exception
    {
        if (nickname == null || nickname.equals(""))
            throw new Exception("Nickname em branco!");
        this.nickname = nickname;
    }
    public String toString() 
    {
        return this.nickname + " - Conexao: " + this.conexao.toString();
    }
    public boolean equals(Object obj) 
    {
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        if (obj == this)
            return true;

        Usuario us = (Usuario) obj;

        if (!this.nickname.equals(us.nickname))
            return false;
        if (!this.salas.equals(us.salas))
            return false;
        if (!this.conexao.equals(us.conexao))
            return false;
        if (!this.transmissor.equals(us.transmissor))
            return false;
        if (!this.receptor.equals(us.receptor))
            return false;

        return true;
    }
    public int hashCode() 
    {
        int ret = 666;

        ret = 2 * ret + this.nickname.hashCode();
        ret = 2 * ret + this.salas.hashCode();
        ret = 2 * ret + this.conexao.hashCode();
        ret = 2 * ret + this.transmissor.hashCode();
        ret = 2 * ret + this.receptor.hashCode();

        return ret;
    }
    public int compareTo(Usuario outro)
    {
        return this.nickname.compareTo(outro.nickname);
    }
    public Usuario(Usuario modelo) throws Exception
    {
        if (modelo == null)
            throw new Exception("Modelo ausente");
        
        this.nickname = modelo.nickname;
        this.salas = (Salas)modelo.salas.clone();
        this.conexao = modelo.conexao;
        this.transmissor = modelo.transmissor;
        this.receptor = modelo.receptor;
    }
    public Object clone()
    {
        Object ret = null;

        try
        {
            ret = new Usuario(this);
        }
        catch (Exception ex) {}

        return ret;
    }
}