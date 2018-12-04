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
            throw new Exception("Mensagem inv\00E1lida!");
        if (dest == null)
            throw new Exception("Destinatário inválido");

        this.mensagem = msg.replace("<", "&lt;").replace(">", "&gt;");

        this.destinatarios = dest;
    }
    protected static String substituirPares(String string, char caracterASerSubstituido, String primeiroValor, String ultimoValor)
    {
        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i = 0; i < string.length(); i++)
            if (string.charAt(i) == caracterASerSubstituido)
                indices.add(i);

        for (int i = 0; i < (indices.size()%2==0?indices.size():indices.size() -1); i += 2)
        {
            string = string.substring(0, indices.get(i) + (primeiroValor.length() + ultimoValor.length() - 2) * i/2) + primeiroValor + string.substring(indices.get(i) + (primeiroValor.length() + ultimoValor.length() - 2) * i/2 + 1);
            string = string.substring(0, indices.get(i + 1) + (primeiroValor.length() + ultimoValor.length() - 2) * i/2 + primeiroValor.length() - 1) + ultimoValor + string.substring(indices.get(i + 1) + (primeiroValor.length() + ultimoValor.length() - 2 * 1) * i/2 + primeiroValor.length());
        }
        return string;
    }
    public String toString()
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

    public void setMensagem(String nova)
    {
        this.mensagem = nova;
    }

    public Object clone()
    {
        Mensagem msg = null;
        try{
            msg = new Mensagem(this);
        }
        catch(Exception e){}
        return msg;
    }
    public Mensagem(Mensagem msg)
    {
        super(msg);
        this.mensagem = msg.mensagem;
        this.destinatarios = msg.destinatarios;
    }
    public static String formatarMensagem(String msg)
    {
        ArrayList<Integer> indicesAsterisco = new ArrayList<Integer>();
        ArrayList<Integer> indicesTil = new ArrayList<Integer>();
        ArrayList<Integer> indicesUnderline = new ArrayList<Integer>();
        ArrayList<Integer> indicesCrase = new ArrayList<Integer>();

        int qtasCrasesJaForam = 0;
        for (int i = 0; i < msg.length(); i++)
            if (msg.charAt(i) == '*')
                indicesAsterisco.add(i);
        msg = substituirPares(msg, '*', "<b>", "</b>");
        msg = substituirPares(msg, '~', "<strike>", "</strike>");
        msg = substituirPares(msg, '_', "<i>", "</i>");

        return msg;
    }
}