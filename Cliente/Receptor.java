import java.io.*;
public class Receptor extends Thread
{
    protected Chat chat;
    protected boolean morta;
    protected ObjectInputStream receptor;
    public void run()
    {
        try
        {
            while (!morta)
            {
                if (chat != null)
                {
                    Enviavel recebido = (Enviavel) receptor.readObject();
                    chat.receber(recebido);
                }
                Thread.sleep(100);
            }
        }
        catch(Exception e) {System.out.println("Erro no Receptor: " + e.getMessage());}
    }
    public Receptor(Chat c, ObjectInputStream receptor) throws Exception
    {
        if (c == null)
            throw new Exception("Chat inválido");
        if (receptor == null)
            throw new Exception("Receptor inválido");
        this.chat = c;
        this.receptor = receptor;
        morta = false;
    }
    public void morrer()
    {
        this.morta = true;
    }
}