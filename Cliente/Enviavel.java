import java.net.*;
import java.io.*;
public class Enviavel
{
    protected Usuario usuario;
    protected Hora hora;

    public Enviavel()
    {
        this.hora = new Hora();
    }

    public Enviavel(Usuario u) throws Exception
    {
        if (u == null)
            throw new Exception("Usuário inválido!");
        this.usuario = u;
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
        String str = "Enviado às " + this.hora.toString() + " por " + this.usuario.toString();
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
    public Usuario getUsuario() throws NullPointerException
    {
        if (this.usuario == null)
            throw new NullPointerException("Usuario nao determinado");
        return this.usuario;
    }
}