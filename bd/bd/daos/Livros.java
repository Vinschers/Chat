package BDSQLServer.daos;

import java.sql.*;
import BD.*;
import BD.core.*;
import BD.dbos.*;

public class Salas
{
    public static boolean cadastrado (int codigo) throws Exception
    {
        boolean retorno = false;

        try
        {
            String sql;

            sql = "SELECT * " +
                  "FROM SALAS " +
                  "WHERE CODIGO = ?";

            BDSQLServer.COMANDO.prepareStatement (sql);

            BDSQLServer.COMANDO.setInt (1, codigo);

            MeuResultSet resultado = (MeuResultSet)BDSQLServer.COMANDO.executeQuery ();

            retorno = resultado.first(); 
        }
        catch (SQLException erro)
        {
            throw new Exception ("Erro ao procurar livro");
        }

        return retorno;
    }

    public static void incluir (Sala sala) throws Exception
    {
        if (sala==null)
            throw new Exception ("Sala nao fornecida");

        try
        {
            String sql;

            sql = "INSERT INTO SALAS " +
                  "(CODIGO,NOME,CAPACIDADE) " +
                  "VALUES " +
                  "(?,?,?)";

            BDSQLServer.COMANDO.prepareStatement (sql);

            BDSQLServer.COMANDO.setInt    (1, sala.getCodigo ());
            BDSQLServer.COMANDO.setString (2, sala.getNome ());
            BDSQLServer.COMANDO.setFloat  (3, sala.getPreco ());

            BDSQLServer.COMANDO.executeUpdate ();
            BDSQLServer.COMANDO.commit        ();
        }
        catch (SQLException erro)
        {
            throw new Exception ("Erro ao inserir sala");
        }
    }

    public static void excluir (int codigo) throws Exception
    {
        if (!cadastrado (codigo))
            throw new Exception ("Nao cadastrado");

        try
        {
            String sql;

            sql = "DELETE FROM SALAS " +
                  "WHERE CODIGO=?";

            BDSQLServer.COMANDO.prepareStatement (sql);

            BDSQLServer.COMANDO.setInt (1, codigo);

            BDSQLServer.COMANDO.executeUpdate ();
            BDSQLServer.COMANDO.commit        ();        }
        catch (SQLException erro)
        {
            throw new Exception ("Erro ao excluir sala");
        }
    }

    public static void alterar (Sala sala) throws Exception
    {
        if (sala==null)
            throw new Exception ("Sala nao fornecido");

        if (!cadastrado (sala.getCodigo()))
            throw new Exception ("Nao cadastrada");

        try
        {
            String sql;

            sql = "UPDATE SALAS " +
                  "SET NOME=? " +
                  "SET PRECO=? " +
                  "WHERE CODIGO = ?";

            BDSQLServer.COMANDO.prepareStatement (sql);

            BDSQLServer.COMANDO.setString (1, sala.getNome ());
            BDSQLServer.COMANDO.setFloat  (2, sala.getPreco ());
            BDSQLServer.COMANDO.setInt    (3, sala.getCodigo ());

            BDSQLServer.COMANDO.executeUpdate ();
            BDSQLServer.COMANDO.commit        ();
        }
        catch (SQLException erro)
        {
            throw new Exception ("Erro ao atualizar dados de sala");
        }
    }

    public static Sala getSala (int codigo) throws Exception
    {
        Sala sala = null;

        try
        {
            String sql;

            sql = "SELECT * " +
                  "FROM SALAS " +
                  "WHERE CODIGO = ?";

            BDSQLServer.COMANDO.prepareStatement (sql);

            BDSQLServer.COMANDO.setInt (1, codigo);

            MeuResultSet resultado = (MeuResultSet)BDSQLServer.COMANDO.executeQuery ();

            if (!resultado.first())
                throw new Exception ("Nao cadastrada");

            sala = new Sala (resultado.getInt   ("CODIGO"),
                               resultado.getString("NOME"),
                               resultado.getInt ("CAPACIDADE"));
        }
        catch (SQLException erro)
        {
            throw new Exception ("Erro ao procurar sala");
        }

        return sala;
    }

    public static MeuResultSet getSalas () throws Exception
    {
        MeuResultSet resultado = null;

        try
        {
            String sql;

            sql = "SELECT * " +
                  "FROM SALAS";

            BDSQLServer.COMANDO.prepareStatement (sql);

            resultado = (MeuResultSet)BDSQLServer.COMANDO.executeQuery ();
        }
        catch (SQLException erro)
        {
            throw new Exception ("Erro ao recuperar salas");
        }

        return resultado;
    }
}