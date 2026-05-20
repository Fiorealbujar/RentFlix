package dao;

import model.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    public List<Empleado> getAll() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT rowid, nombre_empleado, apellido_empleado, email_empleado, " +
                     "usuario_empleado, contrasenia_empleado, id_jefe FROM Empleados";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Empleado(
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),
                    rs.getInt(7)
                ));
            }
        } catch (SQLException e) {
            System.err.println("EmpleadoDAO.getAll: " + e.getMessage());
        }
        return lista;
    }

    // Buscar empleado por usuario y contraseña (para login)
    public Empleado login(String usuario, String contrasenia) {
        String sql = "SELECT rowid, nombre_empleado, apellido_empleado, email_empleado, " +
                     "usuario_empleado, contrasenia_empleado, id_jefe " +
                     "FROM Empleados WHERE usuario_empleado = ? AND contrasenia_empleado = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasenia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Empleado(
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),
                    rs.getInt(7)
                );
            }
        } catch (SQLException e) {
            System.err.println("EmpleadoDAO.login: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(Empleado e) {
        String sql = "INSERT INTO Empleados (nombre_empleado, apellido_empleado, email_empleado, " +
                     "usuario_empleado, contrasenia_empleado, id_jefe) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getUsuario());
            ps.setString(5, e.getContrasenia());
            ps.setInt(6, e.getIdJefe());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("EmpleadoDAO.insertar: " + ex.getMessage());
            return false;
        }
    }

    public boolean actualizar(Empleado e) {
        String sql = "UPDATE Empleados SET nombre_empleado=?, apellido_empleado=?, email_empleado=?, " +
                     "usuario_empleado=?, contrasenia_empleado=?, id_jefe=? WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellido());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getUsuario());
            ps.setString(5, e.getContrasenia());
            ps.setInt(6, e.getIdJefe());
            ps.setInt(7, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println("EmpleadoDAO.actualizar: " + ex.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Empleados WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("EmpleadoDAO.eliminar: " + e.getMessage());
            return false;
        }
    }
}
