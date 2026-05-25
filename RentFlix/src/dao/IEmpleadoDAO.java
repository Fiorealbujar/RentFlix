// ==========================================
// INTERFAZ: IEmpleadoDAO.java
// ==========================================
package dao;

import model.Empleado;
import java.util.List;

public interface IEmpleadoDAO {
    // Login de empleado (devuelve null si falla)
    Empleado login(String usuarioEmpleado, String contrasenia);

    // Solo el admin puede crear empleados
    boolean crear(Empleado empleado);

    // Listar todos los empleados (para el admin)
    List<Empleado> listarTodos();
}