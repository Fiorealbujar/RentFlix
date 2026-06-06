package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gestiona la conexión a la base de datos SQLite del proyecto RentFlix.
 * <p>
 * Lee los parámetros de conexión (driver y URL) desde el fichero de propiedades
 * {@code DB/ConfiguracionDB.properties}. Cada llamada a {@link #getConexion()}
 * abre una nueva conexión que debe cerrarse explícitamente por el DAO que la
 * usa.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public class ConexionDB {

	/**
	 * Nombre de la clase del driver JDBC cargado desde el fichero de propiedades.
	 */
	private String driver;
	/**
	 * URL de conexión a la base de datos SQLite, cargada desde el fichero de
	 * propiedades.
	 */
	private String url;

	/**
	 * Constructor que carga los parámetros de conexión desde el fichero
	 * {@code DB/ConfiguracionDB.properties}.
	 */
	public ConexionDB() {
		Properties prop = new Properties();
		InputStream is = null;

		try {
			is = new FileInputStream("DB/ConfiguracionDB.properties");
			prop.load(is);
			driver = prop.getProperty("DRIVER");
			url = prop.getProperty("URL");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Abre y devuelve una nueva conexión a la base de datos SQLite.
	 * <p>
	 * Activa las restricciones de clave foránea mediante
	 * {@code PRAGMA foreign_keys = ON}, necesario porque SQLite las ignora por
	 * defecto. Esto garantiza que operaciones como borrar una película con copias
	 * alquiladas sean bloqueadas correctamente. El llamante es responsable de
	 * cerrar la conexión cuando termine de usarla.
	 * </p>
	 *
	 * @return objeto {@link Connection} listo para ejecutar consultas
	 * @throws ClassNotFoundException si el driver JDBC no se encuentra en el
	 *                                classpath
	 * @throws SQLException           si ocurre un error al establecer la conexión
	 */
	public Connection getConexion() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
		Connection con = DriverManager.getConnection(url);
		// Activar restricciones de clave foránea en SQLite
		con.createStatement().execute("PRAGMA foreign_keys = ON");
		return con;
	}
}