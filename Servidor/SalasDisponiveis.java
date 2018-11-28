import java.util.*;

public class SalasDisponiveis extends Enviavel
{
    protected ArrayList<SalaSerializable> salas;
    
    public SalasDisponiveis(Salas s) throws Exception
    {
        if (s == null)
            throw new Exception("Salas vazias");

        ArrayList<Sala> salasNaoSerializable = s.getSalas();

        this.salas = new ArrayList<SalaSerializable>();

        for (int i = 0; i < salasNaoSerializable.size(); i++)
            this.salas.add(new SalaSerializable(salasNaoSerializable.get(i)));

            System.out.println(this.salas.get(0).toString());
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