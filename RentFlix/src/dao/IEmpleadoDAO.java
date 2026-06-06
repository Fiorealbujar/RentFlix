package dao;

import model.Empleado;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * {@link model.Empleado}.
 * <p>
 * Declara las operaciones de autenticación, creación, consulta, actualización y
 * eliminación de empleados.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public interface IEmpleadoDAO {

	/**
	 * Verifica las credenciales de un empleado y devuelve su objeto si son
	 * correctas.
	 *
	 * @param usuarioEmpleado nombre de usuario introducido en el login
	 * @param contrasenia     contraseña introducida en el login
	 * @return objeto {@link model.Empleado} si las credenciales son válidas, o
	 *         {@code null} si no coinciden
	 */
	Empleado login(String usuarioEmpleado, String contrasenia);

	/**
	 * Inserta un nuevo empleado en la base de datos. El administrador activo es
	 * asignado como jefe del nuevo empleado.
	 *
	 * @param empleado objeto con los datos del nuevo empleado
	 * @return número de filas afectadas (1 si se insertó correctamente, 0 si falló)
	 */
	int crear(Empleado empleado) throws RuntimeException;

	/**
	 * Elimina un empleado de la base de datos. No es posible eliminar al
	 * administrador activo desde el controlador.
	 *
	 * @param idEmpleado identificador del empleado a eliminar
	 * @return número de filas afectadas (1 si se eliminó, 0 si falló)
	 */
	int eliminar(int idEmpleado);

	/**
	 * Devuelve todos los empleados del sistema.
	 *
	 * @return lista de todos los empleados
	 */
	ArrayList<Empleado> listarTodos();

	/**
	 * Actualiza los datos de un empleado (nombre, apellido, email y usuario). No
	 * modifica la contraseña ni el campo {@code id_jefe}.
	 *
	 * @param empleado objeto con los datos actualizados
	 * @return número de filas afectadas (1 si se actualizó, 0 si falló)
	 */
	int actualizar(Empleado empleado);
}