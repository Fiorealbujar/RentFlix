package dao;

import model.Cliente;
import java.util.ArrayList;

public interface IClienteDAO {
	Cliente login(String nombreUsuario, String contrasenia);

	int registrar(Cliente cliente);

	Cliente obtenerPorId(int idCliente);

	ArrayList<Cliente> listarTodos();

	int actualizar(Cliente cliente);

	int eliminar(int idCliente);
}