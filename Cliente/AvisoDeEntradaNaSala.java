public class AvisoDeEntradaNaSala extends Enviavel
{
    public AvisoDeEntradaNaSala(Usuario u) throws Exception
    {
        super(u);
    }
    public String toString()
    {
        return "<center><b><i>" + super.getUsuario() + " entrou na sala</i></b></center>"; 
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