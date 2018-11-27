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
            System.out.println("Servidor iniciado!");
            for (;;)
            {
                Socket conexao = pedido.accept();
                System.out.println("Pedido aceito");
                CuidadoraDeUsuario cuidadora = new CuidadoraDeUsuario(conexao, salas);
                cuidadora.start();
            }
        }
        catch(Exception e) {System.err.println(e.getMessage());}
    }
}