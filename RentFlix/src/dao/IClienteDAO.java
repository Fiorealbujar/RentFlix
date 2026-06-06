package dao;

import model.Cliente;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * {@link model.Cliente}.
 * <p>
 * Declara las operaciones de autenticación, registro, consulta, actualización y
 * eliminación de clientes.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public interface IClienteDAO {
	Cliente login(String nombreUsuario, String contrasenia);

	int registrar(Cliente cliente) throws RuntimeException;

	ArrayList<Cliente> listarTodos();

	int actualizar(Cliente cliente);

	int eliminar(int idCliente);

	int actualizarDatos(Cliente cliente, String nuevaContrasenia) throws RuntimeException;
}