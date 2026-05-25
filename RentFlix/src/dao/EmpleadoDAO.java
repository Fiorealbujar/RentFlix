// ==========================================
// CLASE: EmpleadoDAO.java
// ==========================================
package dao;

import model.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO implements IEmpleadoDAO {

    private Connection con;

    public EmpleadoDAO() {
        this.con = ConexionDB.getConexion();
    }

    private Empleado mapear(ResultSet rs) throws SQLException {
        // getObject permite recibir null para id_jefe (Integer, no int)
        Integer idJefe = (Integer) rs.getObject("id_jefe");
        return new Empleado(
            rs.getInt("id_empleado"),
            rs.getString("nombre_empleado"),
            rs.getString("apellido_empleado"),
            rs.getString("email_empleado"),
            rs.getString("usuario_empleado"),
            rs.getString("contrasenia_empleado"),
            idJefe
        );
    }

    @Override
    public Empleado login(String usuarioEmpleado, String contrasenia) {
        String sql = "SELECT * FROM Empleados WHERE usuario_empleado = ? " +
                     "AND contrasenia_empleado = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuarioEmpleado);
            ps.setString(2, contrasenia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("EmpleadoDAO.login: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean crear(Empleado empleado) {
        String sql = "INSERT INTO Empleados (nombre_empleado, apellido_empleado, " +
                     "email_empleado, usuario_empleado, contrasenia_empleado, id_jefe) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombreEmpleado());
            ps.setString(2, empleado.getApellidoEmpleado());
            ps.setString(3, empleado.getEmailEmpleado());
            ps.setString(4, empleado.getUsuarioEmpleado());
            ps.setString(5, empleado.getContraseniaEmpleado());
            // setObject maneja el null correctamente para id_jefe
            ps.setObject(6, empleado.getIdJefe());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("EmpleadoDAO.crear: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT * FROM Empleados";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("EmpleadoDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }
}