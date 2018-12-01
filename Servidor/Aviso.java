public class Aviso extends Enviavel
{
    protected int tipo;
    protected String mensagem;
    /**
    * @param tipo 1: Entrada; 2: Saida; 3: Erro; 4: Digitando; 5: Parou de digitar
    * @param msg A mensagem que sera exibida no aviso, se for de erro
    */
    public Aviso(int tipo, String msg) throws Exception
    {
        super();
        if (tipo < 1 || tipo > 5)
            throw new Exception("Tipo de aviso invalido");
        this.tipo = tipo;
        if (tipo == 3)
        {
            if (msg == null || msg.equals(""))
                throw new Exception("Erro errado");
            this.mensagem = msg;
        }
    }
    public String toString()
    {
        String ret = "";
        if (this.tipo != 3)
        {
            ret += "<center><h2><i>";
            ret += super.getUsuario();
            if (tipo == 1)
                ret += " entrou na sala";
            else if (tipo == 2)
                ret += " saiu da sala";
            else if (tipo == 4)
                ret += " está digitando...";
            else
                ret = "parou";
            ret += "</i></h2></center>";
        }
        else
            ret = this.mensagem;
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
}