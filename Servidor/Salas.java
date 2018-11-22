import java.util.*;
import java.lang.*;

public class Salas implements Cloneable
{
    protected ArrayList<Sala> salas;
    protected int qtd;

    public Salas()
    {
        qtd = 0;
        salas = new ArrayList<Sala>();
    }

    public Salas(Salas s) throws Exception
    {
        if (s == null)
            throw new Exception("Sala modelo está null.");
        this.salas = new ArrayList<Sala>(s.salas);
        this.qtd = s.qtd;
    }
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != this.getClass())
            return false;
        Salas s = (Salas)obj;
        if (s.qtd != this.qtd)
            return false;
        if (!s.salas.equals(this.salas))
            return false;
        return true;
    }
    public String toString()
    {
        String ret = "";
        ret += "Salas disponíveis:\n";
        for (int i = 1; i <= this.qtd; i++)
            ret += i + "- " + salas.get(i-1).toString() + ";\n";
        return ret;
    }
    public int hashCode()
    {
        int ret = 1;
        ret = ret * 2 + salas.hashCode();
        ret = ret * 3 + new Integer(qtd).hashCode();
        return ret;
    }
    public Object clone()
    {
        Salas s = null;
        try {s = new Salas(this);}
        catch(Exception e) {}
        return s;
    }

    public void adicionarSala(Sala x) throws Exception
    {
        if (x == null)
            throw new Exception("Sala a ser adicionada está null.");
        salas.add(x);
    }
    public void removerSala(String nome) throws Exception
    {
        if (nome == null || nome.equals(""))
            throw new Exception("Nome vazio ou null.");
        boolean removido = false;
        if (salas.isEmpty())
            throw new Exception("Não há nenhuma sala.");
        for (int i = 0; i < this.qtd; i++)
            if (salas.get(i).getNome() == nome)
            {
                salas.remove(i);
                removido = true;
                break;
            }
        if (!removido)
            throw new Exception("Não foi possível localizar nenhuma sala com o nome \"" + nome + "\".");
    }
}