// ==========================================
// INTERFAZ: IClienteDAO.java
// Contrato que define las operaciones sobre Clientes.
// ==========================================
package dao;

import model.Cliente;
import java.util.List;

public interface IClienteDAO {
    // Login de cliente
    Cliente login(String nombreUsuario, String contrasenia);

    // Registro de nuevo cliente
    boolean registrar(Cliente cliente);

    // Obtener cliente por id
    Cliente obtenerPorId(int idCliente);

    // Listar todos los clientes (para el admin)
    List<Cliente> listarTodos();
}