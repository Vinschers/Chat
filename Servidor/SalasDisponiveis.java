public class SalasDisponiveis extends Enviavel
{
    protected Salas salas;
    public SalasDisponiveis(Salas s) throws Exception
    {
        if (s == null)
            throw new Exception("Salas vazias");
        this.salas = new Salas(s);
    }
    public Salas getSalas()
    {
        return this.salas;
    }
    public String toString()
    {
        return this.salas.toString();
    }
    public int hashCode()
    {
        return this.salas.hashCode();
    }
}