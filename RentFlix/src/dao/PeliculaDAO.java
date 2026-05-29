// PeliculaDAO.java
package dao;

import model.Pelicula;

import java.sql.*;
import java.util.ArrayList;

public class PeliculaDAO implements IPeliculaDAO {

    private ConexionDB acceso;

    public PeliculaDAO() {
        acceso = new ConexionDB();
    }

    private Pelicula mapear(ResultSet rs) throws SQLException {
        return new Pelicula(
            rs.getInt("id_pelicula"),
            rs.getString("nombre_pelicula"),
            rs.getString("director"),
            rs.getInt("duracion"),
            rs.getString("genero"),
            rs.getString("sinopsis"),
            rs.getString("clasificacion_edad")
        );
    }

    @Override
    public ArrayList<Pelicula> listarTodas() {
        ArrayList<Pelicula> lista = new ArrayList<Pelicula>();
        String query = "SELECT * FROM Peliculas ORDER BY nombre_pelicula ASC";

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
    public ArrayList<Pelicula> buscarPorTitulo(String titulo) {
        ArrayList<Pelicula> lista = new ArrayList<Pelicula>();
        String query = "SELECT * FROM Peliculas WHERE nombre_pelicula LIKE ?";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rslt       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, "%" + titulo + "%");
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
    public int agregar(Pelicula pelicula) {
        int res = 0;
        String query = "INSERT INTO Peliculas (nombre_pelicula, director, duracion, " +
                       "genero, sinopsis, clasificacion_edad) VALUES (?,?,?,?,?,?)";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, pelicula.getNombrePelicula());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3,    pelicula.getDuracion());
            ps.setString(4, pelicula.getGenero());
            ps.setString(5, pelicula.getSinopsis());
            ps.setString(6, pelicula.getClasificacionEdad());
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

    @Override
    public int actualizar(Pelicula pelicula) {
        int res = 0;
        String query = "UPDATE Peliculas SET " +
                       "nombre_pelicula = ?, director = ?, duracion = ?, " +
                       "genero = ?, sinopsis = ?, clasificacion_edad = ? " +
                       "WHERE id_pelicula = ?";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, pelicula.getNombrePelicula());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3,    pelicula.getDuracion());
            ps.setString(4, pelicula.getGenero());
            ps.setString(5, pelicula.getSinopsis());
            ps.setString(6, pelicula.getClasificacionEdad());
            ps.setInt(7,    pelicula.getId());
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

    @Override
    public int eliminar(int idPelicula) {
        int res = 0;
        String query = "DELETE FROM Peliculas WHERE id_pelicula = ?";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1, idPelicula);
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