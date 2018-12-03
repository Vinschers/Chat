import java.net.*;
import java.util.ArrayList;

//import bd.*;

public class Servidor
{
    public static void main(String[] args) 
    {
        try
        {
            Salas salas = new Salas();

            boolean puxarDoBD = true;

            if (puxarDoBD)
            {
                ArrayList<SalaBD> salasBD = SalasBD.getSalas();

                for (int i = 0; i < salasBD.size(); i++)
                {
                    SalaBD salaDoBanco = salasBD.get(i);
                    salas.adicionarSala(new Sala(salaDoBanco.getNome(), salaDoBanco.getCapacidade()));
                }
            }
            else
            {
                salas.adicionarSala(new Sala("Geral", 20));
                salas.adicionarSala(new Sala("Inform\u00E1tica", 10));
                salas.adicionarSala(new Sala("Programacao", 10));
                salas.adicionarSala(new Sala("DM1", 2));
                salas.adicionarSala(new Sala("DM2", 2));
            }

            System.out.println("Servidor iniciado!");

            ServerSocket pedido = new ServerSocket(12345, 5, null);
            for (;;)
            {
                Socket conexao = pedido.accept();
                System.out.println("O usuario de IP " + conexao.getInetAddress() + " conectou-se ao servidor");
                CuidadoraDeUsuario cuidadora = new CuidadoraDeUsuario(conexao, salas);
                cuidadora.start();
            }
        }
        catch(Exception e) {System.out.println("Deu erro");}
    }
}