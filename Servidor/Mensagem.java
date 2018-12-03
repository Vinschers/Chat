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
        this.mensagem = this.mensagem.replace(" ", "&nbsp;");

        this.destinatarios = dest;
    }
    protected static String substituirPares(String string, ArrayList<Integer> indices, int tamanhoValorASerSubstituido, String primeiroValor, String ultimoValor)
    {
        for (int i = 0; i < (indices.size()%2==0?indices.size():indices.size() -1); i += 2)
        {
            string = string.substring(0, indices.get(i) + (primeiroValor.length() + ultimoValor.length() - 2 * tamanhoValorASerSubstituido) * i/2) + primeiroValor + string.substring(indices.get(i) + (primeiroValor.length() + ultimoValor.length() - 2 * tamanhoValorASerSubstituido) * i/2 + tamanhoValorASerSubstituido);
            string = string.substring(0, indices.get(i + 1) + (primeiroValor.length() + ultimoValor.length() - 2 * tamanhoValorASerSubstituido) * i/2 + primeiroValor.length() - tamanhoValorASerSubstituido) + ultimoValor + string.substring(indices.get(i + 1) + (primeiroValor.length() + ultimoValor.length() - 2 * tamanhoValorASerSubstituido) * i/2 + primeiroValor.length());
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
        {
            switch (msg.charAt(i))
            {
                case '*':
                    indicesAsterisco.add(i);
                    qtasCrasesJaForam = 0;
                    break;
                case '~':
                    indicesTil.add(i);
                    qtasCrasesJaForam = 0;
                    break;
                case '_':
                    indicesUnderline.add(i);
                    qtasCrasesJaForam = 0;
                    break;
                case '`':
                    qtasCrasesJaForam++;
                    if (qtasCrasesJaForam == 3)
                        indicesCrase.add(i - 2);
                    break;
                default:
                    qtasCrasesJaForam = 0;
                    break;
            }
        }
        msg = substituirPares(msg, indicesAsterisco, 1, "<span class=\"negrito\">", "</span>");
        msg = substituirPares(msg, indicesTil, 1, "<strike>", "</strike>");
        msg = substituirPares(msg, indicesUnderline, 1, "<i>", "</i>");
        msg = substituirPares(msg, indicesCrase, 3, "<font face=\"Lucida Console\">", "</font>");
        return msg;
    }
}