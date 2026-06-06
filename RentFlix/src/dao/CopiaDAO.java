package dao;

import model.Copia;

import java.sql.*;
import java.util.ArrayList;

/**
 * Implementación de {@link ICopiaDAO} para la base de datos SQLite.
 * <p>
 * Gestiona todas las operaciones de persistencia sobre la tabla {@code Copias}.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public class CopiaDAO implements ICopiaDAO {

	private ConexionDB acceso;

	/**
	 * Constructor que inicializa la conexión a la base de datos.
	 */
	
	public CopiaDAO() {
		acceso = new ConexionDB();
	}

	/**
	 * Mapea una fila del {@link ResultSet} a un objeto {@link Copia}.
	 *
	 * @param rs fila del ResultSet
	 * @return objeto Copia con todos sus campos rellenos
	 * @throws SQLException si ocurre un error al leer el ResultSet
	 */
	
	private Copia mapear(ResultSet rs) throws SQLException {
		return new Copia(rs.getInt("id_copia"), rs.getInt("id_pelicula"), rs.getString("formato"),
				rs.getString("estado"), rs.getDouble("precio_alquiler"));
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public ArrayList<Copia> listarTodasDisponibles() {
		ArrayList<Copia> lista = new ArrayList<Copia>();
		String query = "SELECT * FROM Copias WHERE estado = 'disponible' " + "ORDER BY id_pelicula, formato";

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
	public ArrayList<Copia> listarDisponiblesPorPelicula(int idPelicula) {
		ArrayList<Copia> lista = new ArrayList<Copia>();
		String query = "SELECT * FROM Copias " + "WHERE id_pelicula = ? AND estado = 'disponible'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, idPelicula);
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
	public ArrayList<Copia> listarDisponiblesPorFormato(String formato) {
		ArrayList<Copia> lista = new ArrayList<Copia>();
		String query = "SELECT * FROM Copias " + "WHERE formato = ? AND estado = 'disponible' "
				+ "ORDER BY id_pelicula";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, formato);
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
	public int crear(Copia copia) {
		int res = 0;
		String query = "INSERT INTO Copias (id_pelicula, formato, estado, precio_alquiler) " + "VALUES (?, ?, ?, ?)";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, copia.getIdPelicula());
			ps.setString(2, copia.getFormato());
			ps.setString(3, copia.getEstado());
			ps.setDouble(4, copia.getPrecioAlquiler());
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
	public int actualizarEstado(int idCopia, String nuevoEstado) {
		int res = 0;
		String query = "UPDATE Copias SET estado = ? WHERE id_copia = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, nuevoEstado);
			ps.setInt(2, idCopia);
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
	public int contarDisponiblesPorPelicula(int idPelicula) {
		int total = 0;
		String query = "SELECT COUNT(*) FROM Copias WHERE id_pelicula = ? AND estado = 'disponible'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, idPelicula);
			rslt = ps.executeQuery();
			if (rslt.next()) {
				total = rslt.getInt(1);
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
		return total;
	}

	/**
	 * {@inheritDoc}
	 */
	
	@Override
	public int contarAlquiladasPorPelicula(int idPelicula) {
		int total = 0;
		String query = "SELECT COUNT(*) FROM Copias WHERE id_pelicula = ? AND estado = 'alquilada'";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, idPelicula);
			rslt = ps.executeQuery();
			if (rslt.next()) {
				total = rslt.getInt(1);
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
		return total;
	}

}