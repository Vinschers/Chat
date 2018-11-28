import java.util.*;

public class SalasDisponiveis extends Enviavel
{
    protected ArrayList<SalaSerializable> salas;
    
    public SalasDisponiveis(Salas s) throws Exception
    {
        try
        {
        if (s == null)
            throw new Exception("Salas vazias");

        ArrayList<Sala> salasNaoSeralizable = s.getSalas();

        this.salas = new ArrayList<SalaSerializable>();

        for (int i = 0; i < salasNaoSeralizable.size(); i++)
            this.salas.add(new SalaSerializable(salasNaoSeralizable.get(i)));
        }
        catch (Exception ex) {System.out.println("Erro no construtor: " + ex.getMessage());}
    }
    public ArrayList<SalaSerializable> getSalas()
    {
        return this.salas;
    }
    public String toString()
    {
        return this.salas.size() + " salas disponiveis.";
    }
    public int hashCode()
    {
        return this.salas.hashCode();
    }
}