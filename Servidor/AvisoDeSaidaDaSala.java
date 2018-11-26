public class AvisoDeSaidaDaSala extends Enviavel
{
    public AvisoDeSaidaDaSala(Usuario u)
    {
        if (u == null)
            throw new Exception("Usuário inválido");
        super(u);
    }
    public String toString()
    {
        return "<b><i>" + super.getUsuario() + " saiu da sala</i></b>"; 
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