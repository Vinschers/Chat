public class AvisoDeSaidaDaSala extends Enviavel
{
    public AvisoDeSaidaDaSala(Usuario u) throws Exception
    {
        super(u);
    }
    public String toString()
    {
        return "<center><b><i>" + super.getUsuario() + " saiu da sala</i></b></center>"; 
    }
    public int hashCode()
    {
        return super.hashCode();
    }
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}