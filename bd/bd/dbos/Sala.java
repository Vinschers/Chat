package bd.dbos;

public class Sala implements Cloneable
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
        if (preco <= 0)
            throw new Exception ("Preco invalido");

        this.preco = preco;
    }

    public int getCodigo ()
    {
        return this.codigo;
    }

    public String getNome ()
    {
        return this.nome;
    }

    public float getCapacidade ()
    {
        return this.capacidade;
    }

    public Sala (int codigo, String nome, int capacidade) throws Exception
    {
        this.setCodigo     (codigo);
        this.setNome       (nome);
        this.setCapacidade (capacidade);
    }

    public String toString ()
    {
        String ret="";

        ret+="Codigo....: "+this.codigo+"\n";
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

        if (!(obj instanceof Sala))
            return false;

        Sala liv = (Sala)obj;

        if (this.codigo!=liv.codigo)
            return false;

        if (this.nome.equals(liv.nome))
            return false;

        if (this.capacidade!=liv.capacidade)
            return false;

        return true;
    }

    public int hashCode ()
    {
        String ret=666;

        ret = 7*ret + new Integer(this.codigo).hashCode();
        ret = 7*ret + this.nome.hashCode();
        ret = 7*ret + new Integer(this.capacidade).hashCode();

        return ret;
    }


    public Sala (Sala modelo) throws Exception
    {
        this.codigo      = modelo.codigo; // nao clono, pq nao eh objeto
        this.nome        = modelo.nome;   // nao clono, pq nao eh clonavel
        this.capacidade  = modelo.capacidade;  // nao clono, pq nao eh objeto
    }

    public Object clone ()
    {
        Sala ret = null;

        try
        {
            ret = new Sala (this);
        }
        catch (Exception erro)
        {} // nao trato, pq this nunca � null e construtor de
           // copia da excecao qdo seu parametro for null

        return ret;
    }
}