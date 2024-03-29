import java.net.*;
import java.io.*;
public class Enviavel implements Serializable
{
    protected String usuario;
    protected Hora hora;

    protected static final long serialVersionUID = 1L;

    public Enviavel()
    {
        this.hora = new Hora();
    }

    public boolean equals(Object obj)
    {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Enviavel e = (Enviavel)obj;
        if (!this.usuario.equals(e.usuario))
            return false;
        if (!this.hora.equals(e.hora))
            return false;
        return true;
    }
    public String toString()
    {
        String str = "Enviado às " + this.hora.toString() + " por " + this.usuario;
        return str;
    }
    public int hashCode()
    {
        int ret = 1;
        ret = ret * 3 + this.usuario.hashCode();
        ret = ret * 5 + this.hora.hashCode();
        return ret;
    }
    public String getHora()
    {
        return this.hora.toString();
    }
    public String getUsuario() throws NullPointerException
    {
        if (this.usuario == null)
            throw new NullPointerException("Usuario nao determinado");
        return this.usuario;
    }
    public void setUsuario (String u) throws Exception
    {
        if (u == null)
            throw new Exception("Usuário inválido");
        this.usuario = u;
    }
    public Object clone()
    {
        Enviavel en = null;
        try
        {
            en = new Enviavel(this);
        }
        catch(Exception e){}
        return en;
    }
    public Enviavel(Enviavel outro)
    {
        this.usuario = outro.usuario;
        this.hora = outro.hora;
    }
}