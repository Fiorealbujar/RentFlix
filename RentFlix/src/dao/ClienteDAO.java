// ClienteDAO.java — ACTUALIZADO
package dao;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements IClienteDAO {

    private Connection con;

    public ClienteDAO() {
        this.con = ConexionDB.getConexion();
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id_cliente"),
            rs.getString("nombre_cliente"),
            rs.getString("apellido_cliente"),
            rs.getString("email_cliente"),
            rs.getString("nombre_usuario"),
            rs.getString("contrasenia_cliente"),
            rs.getString("estado")
        );
    }

    @Override
    public Cliente login(String nombreUsuario, String contrasenia) {
        String sql = "SELECT * FROM Clientes " +
                     "WHERE nombre_usuario=? AND contrasenia_cliente=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasenia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("ClienteDAO.login: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean registrar(Cliente cliente) {
        String sql = "INSERT INTO Clientes (nombre_cliente, apellido_cliente, " +
                     "email_cliente, nombre_usuario, contrasenia_cliente, estado) " +
                     "VALUES (?, ?, ?, ?, ?, 'activo')";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCliente());
            ps.setString(2, cliente.getApellidoCliente());
            ps.setString(3, cliente.getEmailCliente());
            ps.setString(4, cliente.getNombreUsuario());
            ps.setString(5, cliente.getContraseniaCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClienteDAO.registrar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente obtenerPorId(int idCliente) {
        String sql = "SELECT * FROM Clientes WHERE id_cliente=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);
        } catch (SQLException e) {
            System.err.println("ClienteDAO.obtenerPorId: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY apellido_cliente ASC";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("ClienteDAO.listarTodos: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Clientes SET " +
                     "nombre_cliente=?, apellido_cliente=?, " +
                     "email_cliente=?, nombre_usuario=?, estado=? " +
                     "WHERE id_cliente=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCliente());
            ps.setString(2, cliente.getApellidoCliente());
            ps.setString(3, cliente.getEmailCliente());
            ps.setString(4, cliente.getNombreUsuario());
            ps.setString(5, cliente.getEstado());
            ps.setInt(6,    cliente.getIdCliente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClienteDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int idCliente) {
        String sql = "DELETE FROM Clientes WHERE id_cliente=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClienteDAO.eliminar: " + e.getMessage());
            return false;
        }
    }
}