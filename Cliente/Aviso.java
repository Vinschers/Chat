import java.util.*;
public class Aviso extends Enviavel
{
    protected int tipo;
    protected String mensagem;
    protected ArrayList<String> destinatarios;

    /**
     * 
     * @param tipo 1: Entrada; 2: Saida; 3: Erro; 4: Digitando;
     * @throws Exception se o tipo for invalido
     */
    public Aviso(int tipo) throws Exception
    {
        super();
        if (tipo < 1 || tipo > 5)
            throw new Exception("Tipo de aviso invalido");
        this.tipo = tipo;
    }
    /**
    * @param tipo 1: Entrada; 2: Saida; 3: Erro; 4: Digitando;
    * @param msg A mensagem que sera exibida no aviso
    */
    public Aviso(int tipo, String msg) throws Exception
    {
        super();
        if (tipo < 1 || tipo > 4)
            throw new Exception("Tipo de aviso invalido");
        this.tipo = tipo;
        if (tipo == 3)
        {
            if (msg == null || msg.equals(""))
                throw new Exception("Erro errado");
            this.mensagem = msg;
        }
    }
    public Aviso(int tipo, ArrayList<String> dest) throws Exception
    {
        super();
        if (tipo < 1 || tipo > 4)
            throw new Exception("Tipo de aviso invalido");
        this.tipo = tipo;
        if (dest.size() <= 0)
            throw new Exception("Destinatarios invalidos");
        if (tipo < 4)
            throw new Exception("Tipo de aviso nao suporta esse construtor");
        destinatarios = new ArrayList<String>(dest);
    }
    public String toString()
    {
        String ret = "";
        if (this.tipo == 1 || this.tipo == 2)
        {
            ret += "<center><h2><i>";
            ret += super.getUsuario().replace(" ", "&nbsp;");
            if (tipo == 1)
                ret += " entrou na sala";
            else if (tipo == 2)
                ret += " saiu da sala";
            ret += "</i></h2></center>";
        }
        else if (this.tipo == 3)
            ret = this.mensagem;
        else
            ret = "";
        return ret;
    }
    public int hashCode()
    {
        return super.hashCode();
    }
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
    public int getTipo()
    {
        return this.tipo;
    }
    public ArrayList<String> getDestinatarios()
    {
        return this.destinatarios;
    }
}