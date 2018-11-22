import java.net.*;
import java.io.*;
public class Enviavel
{
    protected Usuario usuario;
    protected String hora;

    public Enviavel(Usuario u, String h) throws Exception
    {
        if (u == null)
            throw new Exception("Usuário inválido!");
        if (h == null || h.equals(""))
            throw new Exception("Hora inválida!");
        this.usuario = u;
        this.hora = h;
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
        String str = "Enviado às " + this.hora + " por " + this.usuario.toString();
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
        return this.hora;
    }
    public Usuario getUsuario()
    {
        return this.usuario;
    }
}