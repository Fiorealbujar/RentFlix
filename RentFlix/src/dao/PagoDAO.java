// ==========================================
// CLASE: PagoDAO.java
// ==========================================
package dao;

import model.Pago;
import java.sql.*;

public class PagoDAO implements IPagoDAO {

    private Connection con;

    public PagoDAO() {
        this.con = ConexionDB.getConexion();
    }

    @Override
    public int registrar(Pago pago) {
        String sql = "INSERT INTO Pagos (metodo_pago, monto_cobro) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pago.getMetodoPago());
            ps.setDouble(2, pago.getMontoCobro());
            ps.executeUpdate();
            // Recuperamos el id autogenerado por SQLite
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            System.err.println("PagoDAO.registrar: " + e.getMessage());
        }
        return -1; // -1 = error
    }
}