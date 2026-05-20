package dao;

import model.Copia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CopiaDAO {

    public List<Copia> getAll() {
        List<Copia> lista = new ArrayList<>();
        String sql = "SELECT rowid, id_pelicula, formato, estado, precio_alquiler FROM Copias";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Copia(
                    rs.getInt(1), rs.getInt(2),
                    rs.getString(3), rs.getString(4), rs.getDouble(5)
                ));
            }
        } catch (SQLException e) {
            System.err.println("CopiaDAO.getAll: " + e.getMessage());
        }
        return lista;
    }

    public int contarDisponibles() {
        String sql = "SELECT COUNT(*) FROM Copias WHERE estado = 'disponible'";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("CopiaDAO.contarDisponibles: " + e.getMessage());
        }
        return 0;
    }

    public boolean actualizarEstado(int id, String estado) {
        String sql = "UPDATE Copias SET estado=? WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("CopiaDAO.actualizarEstado: " + e.getMessage());
            return false;
        }
    }
}
