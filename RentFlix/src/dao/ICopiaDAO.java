// ==========================================
// INTERFAZ: ICopiaDAO.java — ACTUALIZADA
// Añade listarDisponibles (todas las copias
// disponibles para el catálogo).
// ==========================================
package dao;

import model.Copia;
import java.util.List;

public interface ICopiaDAO {
    List<Copia> listarTodasDisponibles();
    List<Copia> listarDisponiblesPorPelicula(int idPelicula);
    List<Copia> listarDisponiblesPorFormato(String formato);
    boolean     actualizarEstado(int idCopia, String nuevoEstado);
}