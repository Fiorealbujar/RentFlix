package dao;

import model.Alquiler;
import java.util.ArrayList;

public interface IAlquilerDAO {
	int crear(Alquiler alquiler);

	ArrayList<Alquiler> listarPorCliente(int idCliente);

	ArrayList<Alquiler> listarTodos();

	int solicitarDevolucion(int idAlquiler);

	int aceptarDevolucion(int idAlquiler, String fechaDevolucionReal);
}