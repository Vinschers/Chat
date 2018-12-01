public class AvisoDeEntradaNaSala extends Enviavel
{
    public AvisoDeEntradaNaSala() throws Exception
    {
        super();
    }
    public String toString()
    {
        return "<center><h2><i>" + super.getUsuario() + " entrou na sala</i></h2></center>"; 
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