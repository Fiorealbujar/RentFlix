package dao;

import model.Alquiler;

import java.sql.*;
import java.util.ArrayList;

public class AlquilerDAO implements IAlquilerDAO {

    private ConexionDB acceso;

    public AlquilerDAO() {
        acceso = new ConexionDB();
    }

    private static final String SQL_CON_DETALLE =
        "SELECT a.*, " +
        "       p.nombre_pelicula, " +
        "       c.nombre_cliente || ' ' || c.apellido_cliente AS nombre_cliente, " +
        "       COALESCE(pg.monto_cobro, 0) AS monto_cobro " +
        "FROM Alquileres a " +
        "JOIN Copias co     ON co.id_copia       = a.id_copia " +
        "JOIN Peliculas p   ON p.id_pelicula     = co.id_pelicula " +
        "JOIN Clientes c    ON c.id_cliente      = a.id_cliente " +
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
        try {
            a.setNombrePelicula(rs.getString("nombre_pelicula"));
            a.setNombreCliente(rs.getString("nombre_cliente"));
            a.setMontoCobro(rs.getDouble("monto_cobro"));
        } catch (SQLException ignored) {}
        return a;
    }

    @Override
    public int crear(Alquiler alquiler) {
        int res = 0;
        String query = "INSERT INTO Alquileres (id_cliente, id_copia, id_empleado, " +
                       "id_transaccion, fecha_alquiler, fecha_devolucion_prevista, " +
                       "fecha_devolucion_real, estado_alquiler) VALUES (?,?,?,?,?,?,?,?)";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1,    alquiler.getIdCliente());
            ps.setInt(2,    alquiler.getIdCopia());
            ps.setObject(3, alquiler.getIdEmpleado());
            ps.setObject(4, alquiler.getIdTransaccion());
            ps.setString(5, alquiler.getFechaAlquiler());
            ps.setString(6, alquiler.getFechaDevolucionPrevista());
            ps.setString(7, alquiler.getFechaDevolucionReal());
            ps.setString(8, alquiler.getEstadoAlquiler());
            res  = ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public ArrayList<Alquiler> listarPorCliente(int idCliente) {
        ArrayList<Alquiler> lista = new ArrayList<Alquiler>();
        String query = SQL_CON_DETALLE +
                       "WHERE a.id_cliente = ? ORDER BY a.fecha_alquiler DESC";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rslt       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            rslt = ps.executeQuery();
            while (rslt.next()) {
                lista.add(mapear(rslt));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rslt != null) rslt.close();
                if (ps   != null) ps.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    @Override
    public ArrayList<Alquiler> listarTodos() {
        ArrayList<Alquiler> lista = new ArrayList<Alquiler>();
        String query = SQL_CON_DETALLE + "ORDER BY a.fecha_alquiler DESC";

        Connection con  = null;
        Statement stmt  = null;
        ResultSet rslt  = null;

        try {
            con  = acceso.getConexion();
            stmt = con.createStatement();
            rslt = stmt.executeQuery(query);
            while (rslt.next()) {
                lista.add(mapear(rslt));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rslt != null) rslt.close();
                if (stmt != null) stmt.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }

    @Override
    public int solicitarDevolucion(int idAlquiler) {
        int res = 0;
        String query = "UPDATE Alquileres SET estado_alquiler = 'pendiente_devolucion' " +
                       "WHERE id_alquiler = ? AND estado_alquiler = 'activo'";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1, idAlquiler);
            res  = ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public int aceptarDevolucion(int idAlquiler, String fechaDevolucionReal) {
        int res = 0;
        String query = "UPDATE Alquileres SET estado_alquiler = 'devuelto', " +
                       "fecha_devolucion_real = ? " +
                       "WHERE id_alquiler = ? AND estado_alquiler = 'pendiente_devolucion'";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, fechaDevolucionReal);
            ps.setInt(2,    idAlquiler);
            res  = ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps  != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}