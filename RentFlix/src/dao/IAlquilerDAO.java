// ==========================================
// INTERFAZ: IAlquilerDAO.java
// ==========================================
package dao;

import model.Alquiler;
import java.util.List;

public interface IAlquilerDAO {
    // Crear un nuevo alquiler
    boolean crear(Alquiler alquiler);

    // Listar alquileres de un cliente concreto
    List<Alquiler> listarPorCliente(int idCliente);

    // Listar todos los alquileres (empleado/admin)
    List<Alquiler> listarTodos();

    // Listar alquileres pendientes de devolución
    List<Alquiler> listarPendientesDevolucion();

    // Solicitar devolución (cliente) → cambia estado a "pendiente_devolucion"
    boolean solicitarDevolucion(int idAlquiler);

    // Aceptar devolución (empleado) → cambia estado a "devuelto"
    boolean aceptarDevolucion(int idAlquiler, String fechaDevolucionReal);
}