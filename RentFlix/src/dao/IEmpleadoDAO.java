// ==========================================
// INTERFAZ: IEmpleadoDAO.java — ACTUALIZADA
// Añade eliminar.
// ==========================================
package dao;

import model.Empleado;
import java.util.List;

public interface IEmpleadoDAO {
    Empleado login(String usuarioEmpleado, String contrasenia);
    boolean  crear(Empleado empleado);
    boolean  eliminar(int idEmpleado);
    List<Empleado> listarTodos();
}