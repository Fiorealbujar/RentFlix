// ==========================================
// CLASE: ConexionDB.java
// Gestiona la conexión a SQLite.
// Patrón Singleton: una sola conexión para toda la app.
// ==========================================
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:DB/rentflix.db";
    private static Connection instancia = null;

    // Constructor privado: nadie puede instanciar esta clase desde fuera
    private ConexionDB() {}

    public static Connection getConexion() {
        try {
            if (instancia == null || instancia.isClosed()) {
                instancia = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar con la BD: " + e.getMessage());
        }
        return instancia;
    }

    public static void cerrarConexion() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                instancia = null;
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}