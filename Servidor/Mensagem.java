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

        this.mensagem = msg.replace("<", "&lt;").replace(">", "&gt;");

        ArrayList<Integer> indicesAsterisco = new ArrayList<Integer>();
        ArrayList<Integer> indicesTil = new ArrayList<Integer>();
        ArrayList<Integer> indicesUnderline = new ArrayList<Integer>();
        for (int i = 0; i < this.mensagem.length(); i++)
        {
            switch (this.mensagem.charAt(i))
            {
                case '*':
                    indicesAsterisco.add(i);
                    break;
                case '~':
                    indicesTil.add(i);
                    break;
                case '_':
                    indicesUnderline.add(i);
                    break;
            }
        }

        this.mensagem = this.substituirPares(this.mensagem, indicesAsterisco, 1, "<b>", "</b>");
        this.mensagem = this.substituirPares(this.mensagem, indicesTil, 1, "<strike>", "</strike>");
        this.mensagem = this.substituirPares(this.mensagem, indicesUnderline, 1, "<i>", "</i>");

        this.mensagem = this.mensagem.replace(" ", "&nbsp;");

        this.destinatarios = dest;
    }

    protected String substituirPares(String string, ArrayList<Integer> indices, int tamanhoValorASerSubstituido, String primeiroValor, String ultimoValor)
    {
        for (int i = 0; i < (indices.size()%2==0?indices.size():indices.size() -1); i += 2)
        {
            string = string.substring(0, indices.get(i)) + primeiroValor + string.substring(indices.get(i) + tamanhoValorASerSubstituido);
            string = string.substring(0, indices.get(i + 1) + primeiroValor.length() - tamanhoValorASerSubstituido) + ultimoValor + string.substring(indices.get(i + 1) + primeiroValor.length());
        }
        return string;
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
        return "<i>" + super.getHora() + "</i> <b>" + super.getUsuario() + ":</b> " + this.mensagem + "<br></font>";
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