package dao;

import model.Copia;
import java.util.ArrayList;

public interface ICopiaDAO {
	ArrayList<Copia> listarTodasDisponibles();

	ArrayList<Copia> listarDisponiblesPorPelicula(int idPelicula);

	ArrayList<Copia> listarDisponiblesPorFormato(String formato);

	int actualizarEstado(int idCopia, String nuevoEstado);
}