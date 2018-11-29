package bd;

import java.sql.*;
import java.util.ArrayList;

public class SalasBD
{
    public static ArrayList<SalaBD> getSalas () throws Exception
    {
        ArrayList<SalaBD> salas = null;

        try
        {
            String sql;

            sql = "SELECT * " +
                  "FROM SALAS";

            BDSQLServer.COMANDO.prepareStatement (sql);

            MeuResultSet resultado = (MeuResultSet)BDSQLServer.COMANDO.executeQuery ();

            salas = new ArrayList<SalaBD>();
            while (!resultado.isLast())
            {
                resultado.next();
                salas.add(new SalaBD(resultado.getString(1), resultado.getInt(2)));
            }
        }
        catch (SQLException erro)
        {
            throw new Exception ("Erro ao recuperar salas");
        }

        return salas;
    }
}