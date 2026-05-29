package dao;

import model.Pelicula;
import java.util.ArrayList;

public interface IPeliculaDAO {
	ArrayList<Pelicula> listarTodas();

	ArrayList<Pelicula> buscarPorTitulo(String titulo);

	int agregar(Pelicula pelicula);

	int actualizar(Pelicula pelicula);

	int eliminar(int idPelicula);
}