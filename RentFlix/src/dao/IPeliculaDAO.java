package dao;

import model.Pelicula;
import java.util.ArrayList;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * {@link model.Pelicula}.
 * <p>
 * Declara las operaciones de consulta, búsqueda, alta, edición y baja de
 * películas del catálogo.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public interface IPeliculaDAO {

	/**
	 * Devuelve todas las películas activas del catálogo ordenadas alfabéticamente
	 * por título.
	 *
	 * @return lista de películas con estado 'activa'
	 */
	ArrayList<Pelicula> listarTodas();

	/**
	 * Busca películas cuyo título contenga el texto indicado (búsqueda parcial).
	 *
	 * @param titulo texto a buscar dentro del título
	 * @return lista de películas cuyo nombre contiene el texto, o lista vacía si no
	 *         hay coincidencias
	 */
	ArrayList<Pelicula> buscarPorTitulo(String titulo);

	/**
	 * Inserta una nueva película en la base de datos. Devuelve el id generado para
	 * que el controlador pueda crear las copias asociadas.
	 *
	 * @param pelicula objeto con los datos de la nueva película
	 * @return el {@code id_pelicula} generado por la base de datos, o {@code -1} si
	 *         falló
	 */
	int agregar(Pelicula pelicula);

	/**
	 * Actualiza los datos de una película existente (título, director, duración,
	 * género, sinopsis y clasificación de edad).
	 *
	 * @param pelicula objeto con los datos actualizados (debe incluir el id)
	 * @return número de filas afectadas (1 si se actualizó, 0 si falló)
	 */
	int actualizar(Pelicula pelicula);

	/**
	 * Da de baja una película cambiando su estado a 'inactiva'. La película deja de
	 * aparecer en el catálogo pero se conserva en el historial de alquileres.
	 *
	 * @param idPelicula identificador de la película a dar de baja
	 * @return número de filas afectadas (1 si ok, 0 si falló)
	 */
	int darDeBaja(int idPelicula);
}