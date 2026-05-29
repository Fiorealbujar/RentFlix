// ==========================================
// CLASE: PeliculaDAO.java
// ==========================================
package dao;

import model.Pelicula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO implements IPeliculaDAO {

    private Connection con;

    public PeliculaDAO() {
        this.con = ConexionDB.getConexion();
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
    public List<Pelicula> listarTodas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM Peliculas ORDER BY nombre_pelicula ASC";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.listarTodas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Pelicula> buscarPorTitulo(String titulo) {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM Peliculas WHERE nombre_pelicula LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + titulo + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.buscarPorTitulo: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean agregar(Pelicula pelicula) {
        String sql = "INSERT INTO Peliculas (nombre_pelicula, director, duracion, " +
                     "genero, sinopsis, clasificacion_edad) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pelicula.getNombrePelicula());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3,    pelicula.getDuracion());
            ps.setString(4, pelicula.getGenero());
            ps.setString(5, pelicula.getSinopsis());
            ps.setString(6, pelicula.getClasificacionEdad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.agregar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean actualizar(Pelicula pelicula) {
        String sql = "UPDATE Peliculas SET nombre_pelicula=?, director=?, duracion=?, " +
                     "genero=?, sinopsis=?, clasificacion_edad=? WHERE id_pelicula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pelicula.getNombrePelicula());
            ps.setString(2, pelicula.getDirector());
            ps.setInt(3,    pelicula.getDuracion());
            ps.setString(4, pelicula.getGenero());
            ps.setString(5, pelicula.getSinopsis());
            ps.setString(6, pelicula.getClasificacionEdad());
            ps.setInt(7,    pelicula.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idPelicula) {
        String sql = "DELETE FROM Peliculas WHERE id_pelicula=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.eliminar: " + e.getMessage());
            return false;
        }
    }
}