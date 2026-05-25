// ==========================================
// CLASE: ActorDAO.java
// ==========================================
package dao;

import model.Actor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO implements IActorDAO {

    private Connection con;

    public ActorDAO() {
        this.con = ConexionDB.getConexion();
    }

    private Actor mapear(ResultSet rs) throws SQLException {
        return new Actor(
            rs.getInt("id_actor"),
            rs.getString("nombre_actor")
        );
    }

    @Override
    public List<Actor> listarPorPelicula(int idPelicula) {
        List<Actor> lista = new ArrayList<>();
        // JOIN con la tabla intermedia Pelicula_Actor
        String sql = "SELECT a.* FROM Actores a " +
                     "JOIN Pelicula_Actor pa ON pa.id_actor = a.id_actor " +
                     "WHERE pa.id_pelicula = ? " +
                     "ORDER BY a.nombre_actor ASC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("ActorDAO.listarPorPelicula: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Actor> listarTodos() {
        List<Actor> lista = new ArrayList<>();
        String sql = "SELECT * FROM Actores ORDER BY nombre_actor ASC";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("ActorDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean agregar(Actor actor) {
        String sql = "INSERT INTO Actores (nombre_actor) VALUES (?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, actor.getNombreActor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActorDAO.agregar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean vincularAPelicula(int idActor, int idPelicula) {
        // INSERT OR IGNORE evita duplicados si el vínculo ya existe
        String sql = "INSERT OR IGNORE INTO Pelicula_Actor (id_pelicula, id_actor) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            ps.setInt(2, idActor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActorDAO.vincularAPelicula: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean desvincularDePelicula(int idActor, int idPelicula) {
        String sql = "DELETE FROM Pelicula_Actor WHERE id_actor = ? AND id_pelicula = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idActor);
            ps.setInt(2, idPelicula);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActorDAO.desvincularDePelicula: " + e.getMessage());
            return false;
        }
    }
}