//package bd;

public class SalaBD implements Cloneable
{
    private String nome;
    private int  capacidade;

    public void setNome (String nome) throws Exception
    {
        if (nome==null || nome.equals(""))
            throw new Exception ("Nome nao fornecido");

        this.nome = nome;
    }

    public void setCapacidade (int capacidade) throws Exception
    {
        if (capacidade <= 0)
            throw new Exception ("Capacidade invalida");

        this.capacidade = capacidade;
    }

    public String getNome ()
    {
        return this.nome;
    }

    public int getCapacidade ()
    {
        return this.capacidade;
    }

    public SalaBD (String nome, int capacidade) throws Exception
    {
        this.setNome       (nome);
        this.setCapacidade (capacidade);
    }

    public String toString ()
    {
        String ret="";

        ret+="Nome......: "+this.nome  +"\n";
        ret+="Capacidade: "+this.capacidade;

        return ret;
    }

    public boolean equals (Object obj)
    {
        if (this==obj)
            return true;

        if (obj==null)
            return false;

        if (!(obj instanceof SalaBD))
            return false;

        SalaBD liv = (SalaBD)obj;

        if (this.nome.equals(liv.nome))
            return false;

        if (this.capacidade!=liv.capacidade)
            return false;

        return true;
    }

    public int hashCode ()
    {
        int ret=666;

        ret = 7*ret + this.nome.hashCode();
        ret = 7*ret + new Integer(this.capacidade).hashCode();

        return ret;
    }


    public SalaBD (SalaBD modelo) throws Exception
    {
        this.nome        = modelo.nome;   // nao clono, pq nao eh clonavel
        this.capacidade  = modelo.capacidade;  // nao clono, pq nao eh objeto
    }

    public Object clone ()
    {
        SalaBD ret = null;

        try
        {
            ret = new SalaBD (this);
        }
        catch (Exception erro)
        {} // nao trato, pq this nunca � null e construtor de
           // copia da excecao qdo seu parametro for null

        return ret;
    }
}