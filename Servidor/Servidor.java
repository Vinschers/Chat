import java.net.*;

public class Servidor
{
    public static void main(String[] args) 
    {
        try
        {
            Salas salas = new Salas();
            // Adicionar salas do banco de dados

            ServerSocket pedido = new ServerSocket(12345);
            for (;;)
            {
                Socket conexao = pedido.accept();
                CuidadoraDeUsuario cuidadora = new CuidadoraDeUsuario(conexao, salas);
                cuidadora.start();
            }
        }
        catch(Exception e) {System.err.println(e.getMessage());}
    }
}