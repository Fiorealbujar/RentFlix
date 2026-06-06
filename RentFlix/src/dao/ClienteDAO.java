package dao;

import model.Cliente;

import java.sql.*;
import java.util.ArrayList;

/**
 * Implementación de {@link IClienteDAO} para la base de datos SQLite.
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public class ClienteDAO implements IClienteDAO {

	private ConexionDB acceso;

	/**
	 * Constructor que inicializa la conexión a la base de datos.
	 */

	public ClienteDAO() {
		acceso = new ConexionDB();
	}

	/**
	 * Mapea una fila del {@link ResultSet} a un objeto {@link Cliente}.
	 *
	 * @param rs fila del ResultSet
	 * @return objeto Cliente con todos sus campos rellenos
	 * @throws SQLException si ocurre un error al leer el ResultSet
	 */
	
	private Cliente mapear(ResultSet rs) throws SQLException {
		return new Cliente(rs.getInt("id_cliente"), rs.getString("nombre_cliente"), rs.getString("apellido_cliente"),
				rs.getString("email_cliente"), rs.getString("nombre_usuario"), rs.getString("contrasenia_cliente"),
				rs.getString("estado"));
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Cliente login(String nombreUsuario, String contrasenia) {
		Cliente cliente = null;
		String query = "SELECT * FROM Clientes " + "WHERE nombre_usuario = ? AND contrasenia_cliente = ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, nombreUsuario);
			ps.setString(2, contrasenia);
			rslt = ps.executeQuery();
			if (rslt.next()) {
				cliente = mapear(rslt);
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
		return cliente;
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public int registrar(Cliente cliente) throws RuntimeException {
		int res = 0;
		String query = "INSERT INTO Clientes (nombre_cliente, apellido_cliente, "
				+ "email_cliente, nombre_usuario, contrasenia_cliente, estado) " + "VALUES (?, ?, ?, ?, ?, 'activo')";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, cliente.getNombreCliente());
			ps.setString(2, cliente.getApellidoCliente());
			ps.setString(3, cliente.getEmailCliente());
			ps.setString(4, cliente.getNombreUsuario());
			ps.setString(5, cliente.getContraseniaCliente());
			res = ps.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} catch (SQLException e) {
			// Se propaga como RuntimeException para que el controlador pueda
			// detectar el campo duplicado (email_cliente o nombre_usuario)
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
	public ArrayList<Cliente> listarTodos() {
		ArrayList<Cliente> lista = new ArrayList<Cliente>();
		String query = "SELECT * FROM Clientes ORDER BY apellido_cliente ASC";

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
	public int actualizar(Cliente cliente) {
		int res = 0;
		String query = "UPDATE Clientes SET " + "nombre_cliente = ?, apellido_cliente = ?, "
				+ "email_cliente = ?, nombre_usuario = ?, estado = ? " + "WHERE id_cliente = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, cliente.getNombreCliente());
			ps.setString(2, cliente.getApellidoCliente());
			ps.setString(3, cliente.getEmailCliente());
			ps.setString(4, cliente.getNombreUsuario());
			ps.setString(5, cliente.getEstado());
			ps.setInt(6, cliente.getIdCliente());
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
	public int eliminar(int idCliente) {
		int res = 0;
		String query = "DELETE FROM Clientes WHERE id_cliente = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, idCliente);
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
	public int actualizarDatos(Cliente cliente, String nuevaContrasenia) throws RuntimeException {
		int res = 0;
		String query = "UPDATE Clientes SET " + "nombre_cliente = ?, apellido_cliente = ?, "
				+ "email_cliente = ?, nombre_usuario = ?, " + "contrasenia_cliente = ? " + "WHERE id_cliente = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, cliente.getNombreCliente());
			ps.setString(2, cliente.getApellidoCliente());
			ps.setString(3, cliente.getEmailCliente());
			ps.setString(4, cliente.getNombreUsuario());
			ps.setString(5, nuevaContrasenia);
			ps.setInt(6, cliente.getIdCliente());
			res = ps.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		} catch (SQLException e) {
			// Se propaga como RuntimeException para que el controlador pueda
			// detectar el campo duplicado (email_cliente o nombre_usuario)
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
}