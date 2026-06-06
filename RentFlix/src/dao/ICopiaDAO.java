package dao;

import model.Copia;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * {@link model.Copia}.
 * <p>
 * Declara las operaciones de consulta, creación, actualización y conteo de
 * copias.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public interface ICopiaDAO {

	/**
	 * Devuelve todas las copias con estado {@code disponible}, ordenadas por
	 * película y formato.
	 *
	 * @return lista de copias disponibles, o lista vacía si no hay ninguna
	 */
	ArrayList<Copia> listarTodasDisponibles();

	/**
	 * Devuelve las copias disponibles de una película concreta.
	 *
	 * @param idPelicula identificador de la película
	 * @return lista de copias disponibles de esa película
	 */
	ArrayList<Copia> listarDisponiblesPorPelicula(int idPelicula);

	/**
	 * Devuelve las copias disponibles de un formato concreto, ordenadas por
	 * película.
	 *
	 * @param formato formato físico: {@code DVD}, {@code Blu-ray} o
	 *                {@code 4K Ultra HD}
	 * @return lista de copias disponibles en ese formato
	 */
	ArrayList<Copia> listarDisponiblesPorFormato(String formato);

	/**
	 * Inserta una nueva copia en la base de datos.
	 *
	 * @param copia objeto con los datos de la nueva copia
	 * @return número de filas afectadas (1 si se insertó correctamente, 0 si falló)
	 */
	int crear(Copia copia);

	/**
	 * Actualiza el estado de una copia ({@code disponible} o {@code alquilada}).
	 *
	 * @param idCopia     identificador de la copia a actualizar
	 * @param nuevoEstado nuevo estado de la copia
	 * @return número de filas afectadas (1 si se actualizó, 0 si falló)
	 */
	int actualizarEstado(int idCopia, String nuevoEstado);

	/**
	 * Cuenta las copias en estado {@code disponible} de una película.
	 *
	 * @param idPelicula identificador de la película
	 * @return número de copias disponibles
	 */
	int contarDisponiblesPorPelicula(int idPelicula);

	/**
	 * Cuenta las copias en estado {@code alquilada} de una película. Se usa para
	 * comprobar si una película puede darse de baja.
	 *
	 * @param idPelicula identificador de la película
	 * @return número de copias actualmente alquiladas
	 */
	int contarAlquiladasPorPelicula(int idPelicula);
}