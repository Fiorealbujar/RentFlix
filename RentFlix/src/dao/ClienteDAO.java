package dao;

import model.Cliente;

import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO implements IClienteDAO {

    private ConexionDB acceso;

    public ClienteDAO() {
        acceso = new ConexionDB();
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
        Cliente cliente = null;
        String query = "SELECT * FROM Clientes " +
                       "WHERE nombre_usuario = ? AND contrasenia_cliente = ?";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rslt       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
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
                if (rslt != null) rslt.close();
                if (ps   != null) ps.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cliente;
    }

    @Override
    public int registrar(Cliente cliente) {
        int res = 0;
        String query = "INSERT INTO Clientes (nombre_cliente, apellido_cliente, " +
                       "email_cliente, nombre_usuario, contrasenia_cliente, estado) " +
                       "VALUES (?, ?, ?, ?, ?, 'activo')";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, cliente.getNombreCliente());
            ps.setString(2, cliente.getApellidoCliente());
            ps.setString(3, cliente.getEmailCliente());
            ps.setString(4, cliente.getNombreUsuario());
            ps.setString(5, cliente.getContraseniaCliente());
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
    public Cliente obtenerPorId(int idCliente) {
        Cliente cliente = null;
        String query = "SELECT * FROM Clientes WHERE id_cliente = ?";

        Connection con       = null;
        PreparedStatement ps = null;
        ResultSet rslt       = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1, idCliente);
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
                if (rslt != null) rslt.close();
                if (ps   != null) ps.close();
                if (con  != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cliente;
    }

    @Override
    public ArrayList<Cliente> listarTodos() {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        String query = "SELECT * FROM Clientes ORDER BY apellido_cliente ASC";

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

    @Override
    public int actualizar(Cliente cliente) {
        int res = 0;
        String query = "UPDATE Clientes SET " +
                       "nombre_cliente = ?, apellido_cliente = ?, " +
                       "email_cliente = ?, nombre_usuario = ?, estado = ? " +
                       "WHERE id_cliente = ?";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setString(1, cliente.getNombreCliente());
            ps.setString(2, cliente.getApellidoCliente());
            ps.setString(3, cliente.getEmailCliente());
            ps.setString(4, cliente.getNombreUsuario());
            ps.setString(5, cliente.getEstado());
            ps.setInt(6,    cliente.getIdCliente());
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
    public int eliminar(int idCliente) {
        int res = 0;
        String query = "DELETE FROM Clientes WHERE id_cliente = ?";

        Connection con       = null;
        PreparedStatement ps = null;

        try {
            con  = acceso.getConexion();
            ps   = con.prepareStatement(query);
            ps.setInt(1, idCliente);
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
}