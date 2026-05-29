package dao;

import model.Pago;

import java.sql.*;

public class PagoDAO implements IPagoDAO {

    private ConexionDB acceso;

    public PagoDAO() {
        acceso = new ConexionDB();
    }

    @Override
    public int registrar(Pago pago) {
        int res  = -1;
        String query = "INSERT INTO Pagos (metodo_pago, monto_cobro) VALUES (?, ?)";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet keys       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pago.getMetodoPago());
            ps.setDouble(2, pago.getMontoCobro());
            ps.executeUpdate();
            keys = ps.getGeneratedKeys();
            if (keys.next()) {
                res = keys.getInt(1);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (keys != null) keys.close();
                if (ps   != null) ps.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}