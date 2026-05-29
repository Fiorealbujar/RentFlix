// ==========================================
// INTERFAZ: IActorDAO.java
// ==========================================
package dao;

import model.Actor;
import java.util.List;

public interface IActorDAO {
    // Actores de una película concreta (via Pelicula_Actor)
    List<Actor> listarPorPelicula(int idPelicula);

    // Listar todos los actores (para el admin)
    List<Actor> listarTodos();

    // Añadir un actor nuevo
    boolean agregar(Actor actor);

    // Vincular un actor existente a una película
    boolean vincularAPelicula(int idActor, int idPelicula);

    // Desvincular un actor de una película
    boolean desvincularDePelicula(int idActor, int idPelicula);
}