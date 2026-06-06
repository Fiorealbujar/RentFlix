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
	ArrayList<Copia> listarTodasDisponibles();

	ArrayList<Copia> listarDisponiblesPorPelicula(int idPelicula);

	ArrayList<Copia> listarDisponiblesPorFormato(String formato);

	// Inserta una nueva copia y devuelve las filas afectadas (1 si ok, 0 si falla)
	int crear(Copia copia);

	int actualizarEstado(int idCopia, String nuevoEstado);

	int contarDisponiblesPorPelicula(int idPelicula);

	int contarAlquiladasPorPelicula(int idPelicula);
}