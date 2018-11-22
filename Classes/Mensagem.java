import java.io.*;
import java.net.*;
import java.util.*;
public class Mensagem extends Enviavel
{
    protected String mensagem;
    public Mensagem(Usuario u, String t, String msg) throws Exception
    {
        super(u, t);
        if (msg == null || msg.equals(""))
            throw new Exception("Mensagem inválida!");
        this.mensagem = msg;
    }
    public String getMensagem()
    {
        return this.mensagem;
    }
    //equals, toString e hashCode
}