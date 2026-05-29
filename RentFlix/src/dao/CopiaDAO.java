package dao;

import model.Copia;

import java.sql.*;
import java.util.ArrayList;

public class CopiaDAO implements ICopiaDAO {

    private ConexionDB acceso;

    public CopiaDAO() {
        acceso = new ConexionDB();
    }

    private Copia mapear(ResultSet rs) throws SQLException {
        return new Copia(
            rs.getInt("id_copia"),
            rs.getInt("id_pelicula"),
            rs.getString("formato"),
            rs.getString("estado"),
            rs.getDouble("precio_alquiler")
        );
    }

    @Override
    public ArrayList<Copia> listarTodasDisponibles() {
        ArrayList<Copia> lista = new ArrayList<Copia>();
        String query = "SELECT * FROM Copias WHERE estado = 'disponible' " +
                       "ORDER BY id_pelicula, formato";

        Connection con  = null;
        Statement stmt  = null;
        ResultSet rslt  = null;

        try {
            con  = acceso.getConexion();
            stmt = con.createStatement();
            rslt = stmt.executeQuery(query);
            while (rslt.next()) {
                lista.add(mapear(rslt));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rslt != null) rslt.close();
                if (stmt != null) stmt.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    @Override
    public ArrayList<Copia> listarDisponiblesPorPelicula(int idPelicula) {
        ArrayList<Copia> lista = new ArrayList<Copia>();
        String query = "SELECT * FROM Copias " +
                       "WHERE id_pelicula = ? AND estado = 'disponible'";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rslt       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1, idPelicula);
            rslt = ps.executeQuery();
            while (rslt.next()) {
                lista.add(mapear(rslt));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rslt != null) rslt.close();
                if (ps   != null) ps.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    @Override
    public ArrayList<Copia> listarDisponiblesPorFormato(String formato) {
        ArrayList<Copia> lista = new ArrayList<Copia>();
        String query = "SELECT * FROM Copias " +
                       "WHERE formato = ? AND estado = 'disponible' " +
                       "ORDER BY id_pelicula";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rslt       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, formato);
            rslt = ps.executeQuery();
            while (rslt.next()) {
                lista.add(mapear(rslt));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rslt != null) rslt.close();
                if (ps   != null) ps.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    @Override
    public int actualizarEstado(int idCopia, String nuevoEstado) {
        int res = 0;
        String query = "UPDATE Copias SET estado = ? WHERE id_copia = ?";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, nuevoEstado);
            ps.setInt(2,    idCopia);
            res  = ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}