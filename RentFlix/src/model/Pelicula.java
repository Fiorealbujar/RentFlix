// ==========================================
// CLASE: Pelicula.java
// Representa la tabla: Peliculas
// ==========================================
package model;

public class Pelicula {

	private int idPelicula;
	private String nombrePelicula;
	private String director;
	private int duracion; // en minutos
	private String genero;
	private String sinopsis;
	private String clasificacionEdad; // "TP", "7", "12", "16", "18"

	public Pelicula() {
	}

	public Pelicula(int idPelicula, String nombrePelicula, String director, int duracion, String genero,
			String sinopsis, String clasificacionEdad) {
		this.idPelicula = idPelicula;
		this.nombrePelicula = nombrePelicula;
		this.director = director;
		this.duracion = duracion;
		this.genero = genero;
		this.sinopsis = sinopsis;
		this.clasificacionEdad = clasificacionEdad;
	}

	public int getId() {
		return idPelicula;
	}

	public void setId(int idPelicula) {
		this.idPelicula = idPelicula;
	}

	public String getNombrePelicula() {
		return nombrePelicula;
	}

	public void setNombrePelicula(String nombrePelicula) {
		this.nombrePelicula = nombrePelicula;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public int getDuracion() {
		return duracion;
	}

	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getSinopsis() {
		return sinopsis;
	}

	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	public String getClasificacionEdad() {
		return clasificacionEdad;
	}

	public void setClasificacionEdad(String clasificacionEdad) {
		this.clasificacionEdad = clasificacionEdad;
	}

	@Override
	public String toString() {
		return nombrePelicula + " (" + director + ") - " + genero + " [" + clasificacionEdad + "]";
	}
}