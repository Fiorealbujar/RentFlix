// ==========================================
// INTERFAZ: IPeliculaDAO.java
// ==========================================
package dao;

import model.Pelicula;
import java.util.List;

public interface IPeliculaDAO {
    // Catálogo completo (para todos los roles + invitado)
    List<Pelicula> listarTodas();

    // Buscar por título (para la barra de búsqueda)
    List<Pelicula> buscarPorTitulo(String titulo);

    // Añadir nueva película (empleado/admin)
    boolean agregar(Pelicula pelicula);

    // Editar película (admin)
    boolean actualizar(Pelicula pelicula);

    // Eliminar película (admin)
    boolean eliminar(int idPelicula);
}