package dao;

import model.Pago;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    public List<Pago> getAll() {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT rowid, metodo_pago, monto_cobro FROM Pagos";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pago(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
            }
        } catch (SQLException e) {
            System.err.println("PagoDAO.getAll: " + e.getMessage());
        }
        return lista;
    }

    public double sumTotal() {
        String sql = "SELECT COALESCE(SUM(monto_cobro), 0) FROM Pagos";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("PagoDAO.sumTotal: " + e.getMessage());
        }
        return 0;
    }

    public int insertar(Pago p) {
        String sql = "INSERT INTO Pagos (metodo_pago, monto_cobro) VALUES (?,?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getMetodoPago());
            ps.setDouble(2, p.getMontoCobro());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1); // devuelve el rowid generado
        } catch (SQLException e) {
            System.err.println("PagoDAO.insertar: " + e.getMessage());
        }
        return -1;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Pagos WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("PagoDAO.eliminar: " + e.getMessage());
            return false;
        }
    }
}
