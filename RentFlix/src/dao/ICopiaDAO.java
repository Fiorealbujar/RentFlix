// ==========================================
// INTERFAZ: ICopiaDAO.java
// ==========================================
package dao;

import model.Copia;
import java.util.List;

public interface ICopiaDAO {
    // Copias disponibles de una película concreta
    List<Copia> listarDisponiblesPorPelicula(int idPelicula);

    // Cambiar estado de una copia (disponible ↔ alquilada)
    boolean actualizarEstado(int idCopia, String nuevoEstado);
}