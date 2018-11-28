import java.io.Serializable;
import java.util.*;

public class SalaSerializable implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected String nomeSala;
    protected ArrayList<String> usuarios;
    protected int capacidade;

    public SalaSerializable(Sala salaNaoSerializable) throws Exception
    {
        if (salaNaoSerializable == null)
            throw new Exception("Sala nula");
        this.nomeSala = salaNaoSerializable.getNome();
        this.capacidade = salaNaoSerializable.getCapacidade();
        ArrayList<Usuario> usuariosNaoSerializables = salaNaoSerializable.getUsuarios();

        this.usuarios = new ArrayList<String>();
        for (int i = 0; i < usuariosNaoSerializables.size(); i++)
            this.usuarios.add(usuariosNaoSerializables.get(i).getNickname());
    }

    public String getNomeSala()
    {
        return nomeSala;
    }
    public ArrayList<String> getUsuarios()
    {
        return usuarios;
    }
    public String toString() 
    {
        return "\"" + this.nomeSala + "\"            Usu\u00E1rios conectados: " + this.usuarios.size() + "/" + this.capacidade;
    }
    public boolean equals(Object obj) 
    {
        if (obj == this)
            return true;
        if (obj == null || !obj.getClass().equals(this.getClass()))
            return false;

        SalaSerializable ss = (SalaSerializable) obj;
        if (!this.nomeSala.equals(ss.nomeSala))
            return false;
        if (this.capacidade != ss.capacidade)
            return false;
        if (!this.usuarios.equals(ss.usuarios))
            return false;
        return true;
    }
    public int hashCode() 
    {
        int ret = 666;

        ret = ret * 2 + this.nomeSala.hashCode();
        ret = ret * 3 + Integer.valueOf(this.capacidade).hashCode();
        ret = ret * 5 + this.usuarios.hashCode();

        return ret;
    }
}