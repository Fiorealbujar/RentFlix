package dao;

import model.Pelicula;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {

    // Devuelve todas las películas
    public List<Pelicula> getAll() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT rowid, nombre_pelicula, director, duracion, genero, sinopsis, clasificacion_edad FROM Peliculas";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pelicula(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
                ));
            }
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.getAll: " + e.getMessage());
        }
        return lista;
    }

    // Cuenta el total de películas
    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Peliculas";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.contarTotal: " + e.getMessage());
        }
        return 0;
    }

    // Las N películas más alquiladas
    public List<String[]> getMasAlquiladas(int limite) {
        List<String[]> lista = new ArrayList<>();
        String sql =
            "SELECT p.nombre_pelicula, p.genero, COUNT(a.rowid) AS total " +
            "FROM Peliculas p " +
            "JOIN Copias co ON co.id_pelicula = p.rowid " +
            "JOIN Alquileres a ON a.id_copia = co.rowid " +
            "GROUP BY p.rowid " +
            "ORDER BY total DESC " +
            "LIMIT ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{ rs.getString(1), rs.getString(2) });
            }
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.getMasAlquiladas: " + e.getMessage());
        }
        return lista;
    }

    // Insertar nueva película
    public boolean insertar(Pelicula p) {
        String sql = "INSERT INTO Peliculas (nombre_pelicula, director, duracion, genero, sinopsis, clasificacion_edad) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDirector());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getGenero());
            ps.setString(5, p.getSinopsis());
            ps.setString(6, p.getClasificacionEdad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    // Actualizar película existente
    public boolean actualizar(Pelicula p) {
        String sql = "UPDATE Peliculas SET nombre_pelicula=?, director=?, duracion=?, genero=?, sinopsis=?, clasificacion_edad=? WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDirector());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getGenero());
            ps.setString(5, p.getSinopsis());
            ps.setString(6, p.getClasificacionEdad());
            ps.setInt(7, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    // Eliminar película por id
    public boolean eliminar(int id) {
        String sql = "DELETE FROM Peliculas WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PeliculaDAO.eliminar: " + e.getMessage());
            return false;
        }
    }
}
