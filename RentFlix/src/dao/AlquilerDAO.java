package dao;

import model.Alquiler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlquilerDAO {

    // Alquileres con JOIN para mostrar en tabla (película, cliente, pago)
    public List<Alquiler> getAllConDetalle() {
        List<Alquiler> lista = new ArrayList<>();
        String sql =
            "SELECT a.rowid, a.id_cliente, a.id_copia, a.id_empleado, a.id_transaccion, " +
            "       a.fecha_alquiler, a.fecha_devolucion_prevista, a.fecha_devolucion_real, a.estado_alquiler, " +
            "       p.nombre_pelicula, " +
            "       c.nombre_cliente || ' ' || c.apellido_cliente AS nombre_cliente, " +
            "       COALESCE(pg.monto_cobro, 0) " +
            "FROM Alquileres a " +
            "JOIN Copias co    ON co.rowid = a.id_copia " +
            "JOIN Peliculas p  ON p.rowid  = co.id_pelicula " +
            "JOIN Clientes c   ON c.rowid  = a.id_cliente " +
            "LEFT JOIN Pagos pg ON pg.rowid = a.id_transaccion " +
            "ORDER BY a.rowid DESC";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Alquiler a = new Alquiler();
                a.setId(rs.getInt(1));
                a.setIdCliente(rs.getInt(2));
                a.setIdCopia(rs.getInt(3));
                a.setIdEmpleado(rs.getInt(4));
                a.setIdTransaccion(rs.getInt(5));
                a.setFechaAlquiler(rs.getString(6));
                a.setFechaDevolucionPrevista(rs.getString(7));
                a.setFechaDevolucionReal(rs.getString(8));
                a.setEstado(rs.getString(9));
                a.setNombrePelicula(rs.getString(10));
                a.setNombreCliente(rs.getString(11));
                a.setMontoCobro(rs.getDouble(12));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.getAllConDetalle: " + e.getMessage());
        }
        return lista;
    }

    public int contarPorEstado(String estado) {
        String sql = "SELECT COUNT(*) FROM Alquileres WHERE estado_alquiler = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.contarPorEstado: " + e.getMessage());
        }
        return 0;
    }

    public double sumIngresos() {
        String sql = "SELECT COALESCE(SUM(monto_cobro), 0) FROM Pagos";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.sumIngresos: " + e.getMessage());
        }
        return 0;
    }

    public boolean insertar(Alquiler a) {
        String sql = "INSERT INTO Alquileres (id_cliente, id_copia, id_empleado, id_transaccion, fecha_alquiler, fecha_devolucion_prevista, fecha_devolucion_real, estado_alquiler) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, a.getIdCliente());
            ps.setInt(2, a.getIdCopia());
            ps.setInt(3, a.getIdEmpleado());
            ps.setInt(4, a.getIdTransaccion());
            ps.setString(5, a.getFechaAlquiler());
            ps.setString(6, a.getFechaDevolucionPrevista());
            ps.setString(7, a.getFechaDevolucionReal());
            ps.setString(8, a.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE Alquileres SET estado_alquiler=? WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.actualizarEstado: " + e.getMessage());
            return false;
        }
    }
}
