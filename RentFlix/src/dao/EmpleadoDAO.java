package dao;

import model.Empleado;

import java.sql.*;
import java.util.ArrayList;

public class EmpleadoDAO implements IEmpleadoDAO {

    private ConexionDB acceso;

    public EmpleadoDAO() {
        acceso = new ConexionDB();
    }

    private Empleado mapear(ResultSet rs) throws SQLException {
        return new Empleado(
            rs.getInt("id_empleado"),
            rs.getString("nombre_empleado"),
            rs.getString("apellido_empleado"),
            rs.getString("email_empleado"),
            rs.getString("usuario_empleado"),
            rs.getString("contrasenia_empleado"),
            (Integer) rs.getObject("id_jefe")
        );
    }

    @Override
    public Empleado login(String usuarioEmpleado, String contrasenia) {
        Empleado empleado = null;
        String query = "SELECT * FROM Empleados " +
                       "WHERE usuario_empleado = ? AND contrasenia_empleado = ?";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rslt       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
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
                if (rslt != null) rslt.close();
                if (ps   != null) ps.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return empleado;
    }

    @Override
    public int crear(Empleado empleado) {
        int res = 0;
        String query = "INSERT INTO Empleados (nombre_empleado, apellido_empleado, " +
                       "email_empleado, usuario_empleado, " +
                       "contrasenia_empleado, id_jefe) VALUES (?,?,?,?,?,?)";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, empleado.getNombreEmpleado());
            ps.setString(2, empleado.getApellidoEmpleado());
            ps.setString(3, empleado.getEmailEmpleado());
            ps.setString(4, empleado.getUsuarioEmpleado());
            ps.setString(5, empleado.getContraseniaEmpleado());
            ps.setObject(6, empleado.getIdJefe());
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
    public int eliminar(int idEmpleado) {
        int res = 0;
        String query = "DELETE FROM Empleados WHERE id_empleado = ?";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1, idEmpleado);
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
    public ArrayList<Empleado> listarTodos() {
        ArrayList<Empleado> lista = new ArrayList<Empleado>();
        String query = "SELECT * FROM Empleados";

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
}