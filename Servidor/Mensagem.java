import java.io.*;
import java.net.*;
import java.util.*;
public class Mensagem extends Enviavel
{
    protected String mensagem;
    protected ArrayList<String> destinatarios;
    public Mensagem(String msg, ArrayList<String> dest) throws Exception
    {
        super();
        if (msg == null || msg.equals(""))
            throw new Exception("Mensagem inválida!");
        if (dest == null)
            throw new Exception("Destinatário inválido");
        this.mensagem = msg.replace("<", "&lt;").replace(">", "&gt;").replace(" ", "&nbsp;");

        ArrayList<Integer> indices = new ArrayList<Integer>();

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
        return "<sub><i>" + super.getHora() + "</i></sub> <b>" + super.getUsuario() + ":</b> " + this.mensagem + "<br>";
    }

    public int hashCode()
    {
        int ret = super.hashCode();
        ret = 5 * ret + this.mensagem.hashCode();

        return ret;
    }

    public ArrayList<String> getDestinatarios()
    {
        return this.destinatarios;
    }
}