package dao;

import model.Actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {

    public List<Actor> getAll() {
        List<Actor> lista = new ArrayList<>();
        String sql = "SELECT rowid, nombre_actor FROM Actores";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Actor(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            System.err.println("ActorDAO.getAll: " + e.getMessage());
        }
        return lista;
    }

    // Actores de una película concreta
    public List<Actor> getByPelicula(int idPelicula) {
        List<Actor> lista = new ArrayList<>();
        String sql = "SELECT a.rowid, a.nombre_actor " +
                     "FROM Actores a " +
                     "JOIN Pelicula_Actor pa ON pa.id_actor = a.rowid " +
                     "WHERE pa.id_pelicula = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Actor(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            System.err.println("ActorDAO.getByPelicula: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Actor a) {
        String sql = "INSERT INTO Actor (nombre_actor) VALUES (?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActorDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    // Vincular actor a película
    public boolean vincularAPelicula(int idPelicula, int idActor) {
        String sql = "INSERT OR IGNORE INTO Pelicula_Actor (id_pelicula, id_actor) VALUES (?,?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            ps.setInt(2, idActor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActorDAO.vincularAPelicula: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Actores WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActorDAO.eliminar: " + e.getMessage());
            return false;
        }
    }
}
