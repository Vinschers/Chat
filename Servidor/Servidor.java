import java.net.*;
import java.util.ArrayList;

import bd.*;

public class Servidor
{
    public static void main(String[] args) 
    {
        try
        {
            boolean puxarDoBd = false;

            Salas salas = new Salas();

            /*ArrayList<SalaBD> salasBD = SalasBD.getSalas();

            for (int i = 0; i < salasBD.size(); i++)
            {
                SalaBD salaDoBanco = salasBD.get(i);
                salas.adicionarSala(new Sala(salaDoBanco.getNome(), salaDoBanco.getCapacidade()));
            }*/

            salas.adicionarSala(new Sala("Geral", 20));
            salas.adicionarSala(new Sala("Inform\u00E1tica", 10));
            salas.adicionarSala(new Sala("Programacao", 10));
            salas.adicionarSala(new Sala("DM1", 2));
            salas.adicionarSala(new Sala("DM2", 2));

            ServerSocket pedido = new ServerSocket(12345);
            System.out.println("Servidor iniciado!");
            for (;;)
            {
                Socket conexao = pedido.accept();
                System.out.println("O usuario de IP " + conexao.getInetAddress() + " conectou-se ao servidor");
                CuidadoraDeUsuario cuidadora = new CuidadoraDeUsuario(conexao, salas);
                cuidadora.start();
            }
        }
        catch(Exception e) {System.err.println(e.getMessage());}
    }
}