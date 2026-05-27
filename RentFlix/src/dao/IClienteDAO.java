// IClienteDAO.java — ACTUALIZADO
package dao;

import model.Cliente;
import java.util.List;

public interface IClienteDAO {
    Cliente login(String nombreUsuario, String contrasenia);
    boolean registrar(Cliente cliente);
    Cliente obtenerPorId(int idCliente);
    List<Cliente> listarTodos();
    boolean actualizar(Cliente cliente);
    boolean eliminar(int idCliente);
}