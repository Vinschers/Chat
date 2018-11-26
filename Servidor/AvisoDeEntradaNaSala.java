public class AvisoDeEntradaNaSala extends Enviavel
{
    public AvisoDeEntradaNaSala(Usuario u) throws Exception
    {
        if (u == null)
            throw new Exception("Usuário inválido");
        super(u);
    }
    public String toString()
    {
        return "<b><i>" + super.getUsuario() + " entrou na sala</i></b>"; 
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