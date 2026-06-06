package dao;

import model.Pelicula;

import java.sql.*;
import java.util.ArrayList;

/**
 * Implementación de {@link IPeliculaDAO} para la base de datos SQLite.
 * <p>
 * Gestiona todas las operaciones de persistencia sobre la tabla
 * {@code Peliculas}, incluyendo listado, búsqueda por título, alta, edición y
 * baja. Al insertar una nueva película devuelve el id generado para que el
 * controlador pueda crear las copias físicas asociadas.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public class PeliculaDAO implements IPeliculaDAO {

	private ConexionDB acceso;

	/**
	 * Constructor que inicializa la conexión a la base de datos.
	 */
	public PeliculaDAO() {
		acceso = new ConexionDB();
	}

	/**
	 * Mapea una fila del {@link ResultSet} a un objeto {@link Pelicula}.
	 *
	 * @param rs fila del ResultSet
	 * @return objeto Pelicula mapeado
	 * @throws SQLException si ocurre un error al leer el ResultSet
	 */
	private Pelicula mapear(ResultSet rs) throws SQLException {
	    return new Pelicula(
	        rs.getInt("id_pelicula"),
	        rs.getString("nombre_pelicula"),
	        rs.getString("director"),
	        rs.getInt("duracion"),
	        rs.getString("genero"),
	        rs.getString("sinopsis"),
	        rs.getString("clasificacion_edad"),
	        rs.getString("estado")
	    );
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Pelicula> listarTodas() {
		ArrayList<Pelicula> lista = new ArrayList<Pelicula>();
		// Solo devuelve películas activas
		String query = "SELECT * FROM Peliculas WHERE estado = 'activa' ORDER BY nombre_pelicula ASC";

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
	public ArrayList<Pelicula> buscarPorTitulo(String titulo) {
		ArrayList<Pelicula> lista = new ArrayList<Pelicula>();
		String query = "SELECT * FROM Peliculas WHERE nombre_pelicula LIKE ?";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rslt = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, "%" + titulo + "%");
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
	public int agregar(Pelicula pelicula) {
		int idGenerado = -1;
		// El campo 'estado' se omite intencionadamente: la BD asigna DEFAULT 'activa' automáticamente
		String query = "INSERT INTO Peliculas (nombre_pelicula, director, duracion, "
				+ "genero, sinopsis, clasificacion_edad) VALUES (?,?,?,?,?,?)";

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet keys = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, pelicula.getNombrePelicula());
			ps.setString(2, pelicula.getDirector());
			ps.setInt(3, pelicula.getDuracion());
			ps.setString(4, pelicula.getGenero());
			ps.setString(5, pelicula.getSinopsis());
			ps.setString(6, pelicula.getClasificacionEdad());
			ps.executeUpdate();
			keys = ps.getGeneratedKeys();
			if (keys.next()) {
				idGenerado = keys.getInt(1);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (keys != null)
					keys.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idGenerado;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int actualizar(Pelicula pelicula) {
		int res = 0;
		String query = "UPDATE Peliculas SET " + "nombre_pelicula = ?, director = ?, duracion = ?, "
				+ "genero = ?, sinopsis = ?, clasificacion_edad = ? " + "WHERE id_pelicula = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setString(1, pelicula.getNombrePelicula());
			ps.setString(2, pelicula.getDirector());
			ps.setInt(3, pelicula.getDuracion());
			ps.setString(4, pelicula.getGenero());
			ps.setString(5, pelicula.getSinopsis());
			ps.setString(6, pelicula.getClasificacionEdad());
			ps.setInt(7, pelicula.getId());
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
	public int darDeBaja(int idPelicula) {
		int res = 0;
		String query = "UPDATE Peliculas SET estado = 'inactiva' WHERE id_pelicula = ?";

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = acceso.getConexion();
			ps = con.prepareStatement(query);
			ps.setInt(1, idPelicula);
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