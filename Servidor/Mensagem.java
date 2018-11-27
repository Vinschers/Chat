import java.io.*;
import java.net.*;
import java.util.*;
public class Mensagem extends Enviavel
{
    protected String mensagem;
    protected Usuario[] destinatarios;
    public Mensagem(Usuario u, String msg, Usuario[] dest) throws Exception
    {
        super(u);
        if (msg == null || msg.equals(""))
            throw new Exception("Mensagem inválida!");
        if (dest == null || dest.length() == 0)
            throw new Exception("Destinatário inválido");
        this.mensagem = msg;
        this.destinatarios = dest;
    }
    public String getMensagem()
    {
        return this.mensagem;
    }
    public boolean equals(Object o)
    {
        if (!super.equals(o))
            return false;
        if (o.getClass() != this.getClass())
            return false;
        Mensagem m = (Mensagem)o;
        if (!this.mensagem.equals(m.mensagem))
            return false;
        return true;
    }

    public String toString()
    {
        return super.getHora() + super.getUsuario() + ": " + this.mensagem;
    }

    public int hashCode()
    {
        int ret = super.hashCode();
        ret = 5 * ret + this.mensagem.hashCode();

        return ret;
    }

    public Usuario[] getDestinatarios()
    {
        return this.destinatarios;
    }
}
