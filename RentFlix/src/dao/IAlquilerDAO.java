package dao;

import model.Alquiler;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * {@link model.Alquiler}.
 * <p>
 * Declara las operaciones de creación, consulta, actualización de estado y
 * gestión del ciclo de vida de los alquileres (devolución y vencimiento).
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public interface IAlquilerDAO {

	/**
	 * Inserta un nuevo alquiler en la base de datos.
	 *
	 * @param alquiler objeto con los datos del alquiler a crear
	 * @return número de filas afectadas (1 si se insertó correctamente, 0 si falló)
	 */
	int crear(Alquiler alquiler);

	/**
	 * Devuelve todos los alquileres asociados a un cliente, ordenados por fecha
	 * descendente. Los objetos incluyen datos de película, cliente e importe
	 * obtenidos mediante JOIN.
	 *
	 * @param idCliente identificador del cliente
	 * @return lista de alquileres del cliente, o lista vacía si no tiene ninguno
	 */
	ArrayList<Alquiler> listarPorCliente(int idCliente);

	/**
	 * Devuelve todos los alquileres del sistema, ordenados por fecha descendente.
	 * Los objetos incluyen datos de película, cliente e importe obtenidos mediante
	 * JOIN.
	 *
	 * @return lista completa de alquileres
	 */
	ArrayList<Alquiler> listarTodos();

	/**
	 * Cambia el estado de un alquiler de {@code activo} a
	 * {@code pendiente_devolucion}. Solo actúa si el alquiler está en estado
	 * {@code activo}.
	 *
	 * @param idAlquiler identificador del alquiler
	 * @return número de filas afectadas (1 si se actualizó, 0 si no estaba activo)
	 */
	int solicitarDevolucion(int idAlquiler);

	/**
	 * Confirma la devolución de un alquiler en estado {@code pendiente_devolucion}.
	 * Cambia su estado a {@code devuelto} y registra la fecha de devolución real.
	 *
	 * @param idAlquiler          identificador del alquiler a cerrar
	 * @param fechaDevolucionReal fecha real de devolución en formato
	 *                            {@code yyyy-MM-dd}
	 * @return el {@code id_copia} asociado al alquiler si la operación fue exitosa,
	 *         o {@code -1} si falló o el alquiler no estaba en estado
	 *         {@code pendiente_devolucion}
	 */
	int aceptarDevolucion(int idAlquiler, String fechaDevolucionReal);

	/**
	 * Marca como {@code vencido} todos los alquileres en estado {@code activo} cuya
	 * fecha de devolución prevista sea anterior a la fecha actual. Se ejecuta
	 * automáticamente al iniciar sesión cualquier usuario.
	 *
	 * @return número de alquileres marcados como vencidos
	 */
	int marcarVencidos();
}