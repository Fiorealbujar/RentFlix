// ==========================================
// CLASE: CopiaDAO.java
// ==========================================
package dao;

import model.Copia;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CopiaDAO implements ICopiaDAO {

    private Connection con;

    public CopiaDAO() {
        this.con = ConexionDB.getConexion();
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
    public List<Copia> listarDisponiblesPorPelicula(int idPelicula) {
        List<Copia> lista = new ArrayList<>();
        String sql = "SELECT * FROM Copias WHERE id_pelicula = ? AND estado = 'disponible'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("CopiaDAO.listarDisponibles: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizarEstado(int idCopia, String nuevoEstado) {
        String sql = "UPDATE Copias SET estado = ? WHERE id_copia = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCopia);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CopiaDAO.actualizarEstado: " + e.getMessage());
            return false;
        }
    }
}