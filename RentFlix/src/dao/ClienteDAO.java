package dao;

import model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public List<Cliente> getAll() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT rowid, nombre_cliente, apellido_cliente, email_cliente, nombre_usuario, contrasenia_cliente FROM Clientes";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6)
                ));
            }
        } catch (SQLException e) {
            System.err.println("ClienteDAO.getAll: " + e.getMessage());
        }
        return lista;
    }

    public int contarTotal() {
        String sql = "SELECT COUNT(*) FROM Clientes";
        try (Statement st = ConexionDB.getConexion().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("ClienteDAO.contarTotal: " + e.getMessage());
        }
        return 0;
    }

    public boolean insertar(Cliente c) {
        String sql = "INSERT INTO Clientes (nombre_cliente, apellido_cliente, email_cliente, nombre_usuario, contrasenia_cliente) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getUsuario());
            ps.setString(5, c.getContrasenia());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClienteDAO.insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Cliente c) {
        String sql = "UPDATE Clientes SET nombre_cliente=?, apellido_cliente=?, email_cliente=?, nombre_usuario=?, contrasenia_cliente=? WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            ps.setString(2, c.getApellido());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getUsuario());
            ps.setString(5, c.getContrasenia());
            ps.setInt(6, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClienteDAO.actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM Clientes WHERE rowid=?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ClienteDAO.eliminar: " + e.getMessage());
            return false;
        }
    }
}
