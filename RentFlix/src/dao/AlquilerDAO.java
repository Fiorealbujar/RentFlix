// ==========================================
// CLASE: AlquilerDAO.java
// ==========================================
package dao;

import model.Alquiler;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlquilerDAO implements IAlquilerDAO {

    private Connection con;

    public AlquilerDAO() {
        this.con = ConexionDB.getConexion();
    }

    // JOIN completo: trae nombre de película y cliente en una sola consulta
    private static final String SQL_CON_DETALLE =
        "SELECT a.*, " +
        "       p.nombre_pelicula, " +
        "       c.nombre_cliente || ' ' || c.apellido_cliente AS nombre_cliente, " +
        "       COALESCE(pg.monto_cobro, 0) AS monto_cobro " +
        "FROM Alquileres a " +
        "JOIN Copias co    ON co.id_copia    = a.id_copia " +
        "JOIN Peliculas p  ON p.id_pelicula  = co.id_pelicula " +
        "JOIN Clientes c   ON c.id_cliente   = a.id_cliente " +
        "LEFT JOIN Pagos pg ON pg.id_transaccion = a.id_transaccion ";

    private Alquiler mapear(ResultSet rs) throws SQLException {
        Alquiler a = new Alquiler(
            rs.getInt("id_alquiler"),
            rs.getInt("id_cliente"),
            rs.getInt("id_copia"),
            (Integer) rs.getObject("id_empleado"),
            (Integer) rs.getObject("id_transaccion"),
            rs.getString("fecha_alquiler"),
            rs.getString("fecha_devolucion_prevista"),
            rs.getString("fecha_devolucion_real"),
            rs.getString("estado_alquiler")
        );
        // Campos extra del JOIN (pueden ser null si la query no los trae)
        try {
            a.setNombrePelicula(rs.getString("nombre_pelicula"));
            a.setNombreCliente(rs.getString("nombre_cliente"));
            a.setMontoCobro(rs.getDouble("monto_cobro"));
        } catch (SQLException ignored) {}
        return a;
    }

    @Override
    public boolean crear(Alquiler alquiler) {
        String sql = "INSERT INTO Alquileres (id_cliente, id_copia, id_empleado, " +
                     "id_transaccion, fecha_alquiler, fecha_devolucion_prevista, " +
                     "fecha_devolucion_real, estado_alquiler) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1,    alquiler.getIdCliente());
            ps.setInt(2,    alquiler.getIdCopia());
            ps.setObject(3, alquiler.getIdEmpleado());
            ps.setObject(4, alquiler.getIdTransaccion());
            ps.setString(5, alquiler.getFechaAlquiler());
            ps.setString(6, alquiler.getFechaDevolucionPrevista());
            ps.setString(7, alquiler.getFechaDevolucionReal());
            ps.setString(8, alquiler.getEstadoAlquiler());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Alquiler> listarPorCliente(int idCliente) {
        List<Alquiler> lista = new ArrayList<>();
        String sql = SQL_CON_DETALLE + "WHERE a.id_cliente = ? ORDER BY a.fecha_alquiler DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.listarPorCliente: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Alquiler> listarTodos() {
        List<Alquiler> lista = new ArrayList<>();
        String sql = SQL_CON_DETALLE + "ORDER BY a.fecha_alquiler DESC";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public List<Alquiler> listarPendientesDevolucion() {
        List<Alquiler> lista = new ArrayList<>();
        String sql = SQL_CON_DETALLE + "WHERE a.estado_alquiler = 'pendiente_devolucion'";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.listarPendientes: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean solicitarDevolucion(int idAlquiler) {
        String sql = "UPDATE Alquileres SET estado_alquiler = 'pendiente_devolucion' " +
                     "WHERE id_alquiler = ? AND estado_alquiler = 'activo'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idAlquiler);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.solicitarDevolucion: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aceptarDevolucion(int idAlquiler, String fechaDevolucionReal) {
        String sql = "UPDATE Alquileres SET estado_alquiler = 'devuelto', " +
                     "fecha_devolucion_real = ? " +
                     "WHERE id_alquiler = ? AND estado_alquiler = 'pendiente_devolucion'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaDevolucionReal);
            ps.setInt(2,    idAlquiler);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("AlquilerDAO.aceptarDevolucion: " + e.getMessage());
            return false;
        }
    }
}