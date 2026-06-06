package dao;

import model.Alquiler;

import java.sql.*;
import java.util.ArrayList;

/**
 * Implementación de {@link IAlquilerDAO} para la base de datos SQLite.
 * <p>
 * Gestiona todas las operaciones de persistencia sobre la tabla
 * {@code Alquileres}. Las consultas de listado utilizan JOINs con las tablas
 * Peliculas, Copias, Clientes y Pagos para enriquecer los objetos
 * {@link Alquiler} con datos necesarios para las vistas.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public class AlquilerDAO implements IAlquilerDAO {

	private ConexionDB acceso;

	/**
	 * Constructor que inicializa la conexión a la base de datos.
	 */
	public AlquilerDAO() {
		acceso = new ConexionDB();
	}

	private static final String SQL_CON_DETALLE = "SELECT a.*, " + "       p.nombre_pelicula, "
			+ "       c.nombre_cliente || ' ' || c.apellido_cliente AS nombre_cliente, "
			+ "       COALESCE(pg.monto_cobro, 0) AS monto_cobro " + "FROM Alquileres a "
			+ "JOIN Copias co     ON co.id_copia       = a.id_copia "
			+ "JOIN Peliculas p   ON p.id_pelicula     = co.id_pelicula "
			+ "JOIN Clientes c    ON c.id_cliente      = a.id_cliente "
			+ "LEFT JOIN Pagos pg ON pg.id_transaccion = a.id_transaccion ";

	/**
	 * Mapea una fila del {@link ResultSet} a un objeto {@link Alquiler}. Intenta
	 * poblar también los campos extra (película, cliente, importe).
	 *
	 * @param rs fila del ResultSet
	 * @return objeto Alquiler mapeado
	 * @throws SQLException si ocurre un error al leer el ResultSet
	 */
	private Alquiler mapear(ResultSet rs) throws SQLException {
		Alquiler a = new Alquiler(rs.getInt("id_alquiler"), rs.getInt("id_cliente"), rs.getInt("id_copia"),
				(Integer) rs.getObject("id_empleado"), (Integer) rs.getObject("id_transaccion"),
				rs.getString("fecha_alquiler"), rs.getString("fecha_devolucion_prevista"),
				rs.getString("fecha_devolucion_real"), rs.getString("estado_alquiler"));
		try {
			a.setNombrePelicula(rs.getString("nombre_pelicula"));
			a.setNombreCliente(rs.getString("nombre_cliente"));
			a.setMontoCobro(rs.getDouble("monto_cobro"));
		} catch (SQLException ignored) {
		}
		return a;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int crear(Alquiler alquiler) {
		int res = 0;
		String query = "INSERT INTO Alquileres (id_cliente, id_copia, id_empleado, "
				+ "id_transaccion, fecha_alquiler, fecha_devolucion_prevista, "
				+ "fecha_devolucion_real, estado_alquiler) VALUES (?,?,?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, alquiler.getIdCliente());
			ps.setInt(2, alquiler.getIdCopia());
			ps.setObject(3, alquiler.getIdEmpleado());
			ps.setObject(4, alquiler.getIdTransaccion());
			ps.setString(5, alquiler.getFechaAlquiler());
			ps.setString(6, alquiler.getFechaDevolucionPrevista());
			ps.setString(7, alquiler.getFechaDevolucionReal());
			ps.setString(8, alquiler.getEstadoAlquiler());
			res = ps.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Alquiler> listarPorCliente(int idCliente) {
		ArrayList<Alquiler> lista = new ArrayList<Alquiler>();
		String query = SQL_CON_DETALLE + "WHERE a.id_cliente = ? ORDER BY a.fecha_alquiler DESC";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
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
				if (rslt != null)
					rslt.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Alquiler> listarTodos() {
		ArrayList<Alquiler> lista = new ArrayList<Alquiler>();
		String query = SQL_CON_DETALLE + "ORDER BY a.fecha_alquiler DESC";

		Connection con = null;
		Statement stmt = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
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
				if (rslt != null)
					rslt.close();
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int solicitarDevolucion(int idAlquiler) {
		int res = 0;
		String query = "UPDATE Alquileres SET estado_alquiler = 'pendiente_devolucion' "
				+ "WHERE id_alquiler = ? AND estado_alquiler = 'activo'";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, idAlquiler);
			res = ps.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Realiza primero un SELECT para obtener el {@code id_copia} del alquiler, y
	 * después ejecuta el UPDATE. Devuelve el {@code id_copia} para que el
	 * controlador pueda restaurar el estado de la copia a {@code disponible}.
	 * </p>
	 */
	@Override
	public int aceptarDevolucion(int idAlquiler, String fechaDevolucionReal) {
		int idCopia = -1;

		// Primero obtenemos el id_copia del alquiler
		String querySelect = "SELECT id_copia FROM Alquileres WHERE id_alquiler = ? "
				+ "AND estado_alquiler = 'pendiente_devolucion'";
		String queryUpdate = "UPDATE Alquileres SET estado_alquiler = 'devuelto', "
				+ "fecha_devolucion_real = ? WHERE id_alquiler = ? " + "AND estado_alquiler = 'pendiente_devolucion'";

		Connection con = null;
		PreparedStatement psSelect = null;
		PreparedStatement psUpdate = null;
		ResultSet rs = null;

		try {
			con = acceso.getConexion();

			// Obtener id_copia
			psSelect = con.prepareStatement(querySelect);
			psSelect.setInt(1, idAlquiler);
			rs = psSelect.executeQuery();
			if (rs.next()) {
				idCopia = rs.getInt("id_copia");
			}

			// Si encontramos el alquiler, actualizamos su estado
			if (idCopia != -1) {
				psUpdate = con.prepareStatement(queryUpdate);
				psUpdate.setString(1, fechaDevolucionReal);
				psUpdate.setInt(2, idAlquiler);
				if (psUpdate.executeUpdate() == 0) {
					idCopia = -1; // El UPDATE no afectó filas, devolvemos -1
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (psSelect != null)
					psSelect.close();
				if (psUpdate != null)
					psUpdate.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idCopia;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int marcarVencidos() {
		int res = 0;
		String query = "UPDATE Alquileres SET estado_alquiler = 'vencido' " + "WHERE estado_alquiler = 'activo' "
				+ "AND fecha_devolucion_prevista < ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, java.time.LocalDate.now().toString());
			res = ps.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return res;
	}
}