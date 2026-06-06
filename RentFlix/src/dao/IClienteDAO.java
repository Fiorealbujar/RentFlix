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

	/**
	 * Verifica las credenciales de un cliente y devuelve su objeto si son
	 * correctas.
	 *
	 * @param nombreUsuario nombre de usuario introducido en el login
	 * @param contrasenia   contraseña introducida en el login
	 * @return objeto {@link model.Cliente} si las credenciales son válidas, o
	 *         {@code null} si no coinciden o el cliente está bloqueado
	 */
	Cliente login(String nombreUsuario, String contrasenia);

	/**
	 * Inserta un nuevo cliente en la base de datos con estado {@code activo}.
	 *
	 * @param cliente objeto con los datos del nuevo cliente
	 * @return número de filas afectadas (1 si se insertó correctamente, 0 si falló)
	 * @throws RuntimeException si el usuario o email ya existe en la base de datos
	 */
	int registrar(Cliente cliente) throws RuntimeException;

	/**
	 * Devuelve todos los clientes del sistema ordenados por apellido.
	 *
	 * @return lista de todos los clientes
	 */
	ArrayList<Cliente> listarTodos();

	/**
	 * Actualiza el estado de un cliente (activo/bloqueado) sin modificar su
	 * contraseña.
	 *
	 * @param cliente objeto con los datos actualizados (debe incluir el id)
	 * @return número de filas afectadas (1 si se actualizó, 0 si falló)
	 */
	int actualizar(Cliente cliente);

	/**
	 * Elimina un cliente de la base de datos de forma permanente.
	 *
	 * @param idCliente identificador del cliente a eliminar
	 * @return número de filas afectadas (1 si se eliminó, 0 si falló)
	 */
	int eliminar(int idCliente);

	/**
	 * Actualiza los datos personales y la contraseña de un cliente autenticado.
	 *
	 * @param cliente          objeto con los datos personales actualizados
	 * @param nuevaContrasenia nueva contraseña en texto plano
	 * @return número de filas afectadas (1 si se actualizó, 0 si falló)
	 * @throws RuntimeException si el nuevo usuario o email ya está en uso
	 */
	int actualizarDatos(Cliente cliente, String nuevaContrasenia) throws RuntimeException;
}