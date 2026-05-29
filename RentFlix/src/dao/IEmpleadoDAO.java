package dao;

import model.Empleado;
import java.util.ArrayList;

public interface IEmpleadoDAO {
	Empleado login(String usuarioEmpleado, String contrasenia);

	int crear(Empleado empleado);

	int eliminar(int idEmpleado);

	ArrayList<Empleado> listarTodos();
}