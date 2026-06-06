package dao;

import model.Empleado;

import java.sql.*;
import java.util.ArrayList;

/**
 * Implementación de {@link IEmpleadoDAO} para la base de datos SQLite.
 * <p>
 * Gestiona todas las operaciones de persistencia sobre la tabla
 * {@code Empleados}, incluyendo autenticación, alta, baja, listado y
 * actualización de datos.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */

public class EmpleadoDAO implements IEmpleadoDAO {

	/** Objeto de acceso a la base de datos SQLite utilizado por este DAO. */
	private ConexionDB acceso;

	/**
	 * Constructor que inicializa la conexión a la base de datos.
	 */
	public EmpleadoDAO() {
		acceso = new ConexionDB();
	}

	/**
	 * Mapea una fila del {@link ResultSet} a un objeto {@link Empleado}. Utiliza
	 * {@link ResultSet#getObject} para el campo {@code id_jefe}, que puede ser
	 * {@code null} cuando el empleado es administrador.
	 *
	 * @param rs fila del ResultSet
	 * @return objeto Empleado con todos sus campos rellenos
	 * @throws SQLException si ocurre un error al leer el ResultSet
	 */
	private Empleado mapear(ResultSet rs) throws SQLException {
		return new Empleado(rs.getInt("id_empleado"), rs.getString("nombre_empleado"),
				rs.getString("apellido_empleado"), rs.getString("email_empleado"), rs.getString("usuario_empleado"),
				rs.getString("contrasenia_empleado"), (Integer) rs.getObject("id_jefe"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Empleado login(String usuarioEmpleado, String contrasenia) {
		Empleado empleado = null;
		String query = "SELECT * FROM Empleados " + "WHERE usuario_empleado = ? AND contrasenia_empleado = ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, usuarioEmpleado);
			ps.setString(2, contrasenia);
			rslt = ps.executeQuery();
			if (rslt.next()) {
				empleado = mapear(rslt);
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
		return empleado;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int crear(Empleado empleado) throws RuntimeException {
		int res = 0;
		String query = "INSERT INTO Empleados (nombre_empleado, apellido_empleado, "
				+ "email_empleado, usuario_empleado, " + "contrasenia_empleado, id_jefe) VALUES (?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, empleado.getNombreEmpleado());
			ps.setString(2, empleado.getApellidoEmpleado());
			ps.setString(3, empleado.getEmailEmpleado());
			ps.setString(4, empleado.getUsuarioEmpleado());
			ps.setString(5, empleado.getContraseniaEmpleado());
			ps.setObject(6, empleado.getIdJefe());
			res = ps.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} catch (SQLException e) {
			// Se propaga como RuntimeException para que el controlador pueda
			// detectar el campo duplicado (email_empleado o usuario_empleado)
			// y mostrar el mensaje de error correspondiente al usuario
			throw new RuntimeException(e.getMessage());
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
	public int eliminar(int idEmpleado) {
		int res = 0;
		String query = "DELETE FROM Empleados WHERE id_empleado = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, idEmpleado);
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
	public ArrayList<Empleado> listarTodos() {
		ArrayList<Empleado> lista = new ArrayList<Empleado>();
		String query = "SELECT * FROM Empleados";

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
	public int actualizar(Empleado empleado) {
		int res = 0;
		String query = "UPDATE Empleados SET " + "nombre_empleado = ?, apellido_empleado = ?, "
				+ "email_empleado = ?, usuario_empleado = ? " + "WHERE id_empleado = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, empleado.getNombreEmpleado());
			ps.setString(2, empleado.getApellidoEmpleado());
			ps.setString(3, empleado.getEmailEmpleado());
			ps.setString(4, empleado.getUsuarioEmpleado());
			ps.setInt(5, empleado.getIdEmpleado());
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