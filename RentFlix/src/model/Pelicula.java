// ==========================================
// CLASE: Pelicula.java
// Representa la tabla: Peliculas
// ==========================================
package model;

/**
 * Representa una película del catálogo del videoclub.
 * <p>
 * Mapea la tabla {@code Peliculas} de la base de datos SQLite. Una película
 * puede tener asociadas múltiples copias físicas en distintos formatos,
 * representadas por la entidad {@link Copia}. El precio de alquiler no reside
 * en esta clase sino en {@link Copia}, permitiendo precios diferenciados por
 * formato. El campo {@code estado} controla la visibilidad de la película en el
 * catálogo: una película {@code inactiva} ha sido dada de baja y no aparece en
 * el catálogo ni puede ser alquilada, pero se conserva en el historial.
 * </p>
 *
 * @author Gabriel Fernández Cañadas
 * @version 1.0
 */
public class Pelicula {

	/** Identificador único de la película. */
	private int idPelicula;
	/** Título de la película. */
	private String nombrePelicula;
	/** Nombre del director de la película. */
	private String director;
	/** Duración de la película en minutos. */
	private int duracion;
	/** Género cinematográfico de la película. */
	private String genero;
	/** Breve descripción argumental de la película. */
	private String sinopsis;
	/**
	 * Clasificación por edad: {@code TP}, {@code 7}, {@code 12}, {@code 16} o
	 * {@code 18}.
	 */
	private String clasificacionEdad;
	/** Estado de la película en el catálogo: {@code activa} o {@code inactiva}. */
	private String estado;

	/**
	 * Constructor completo con todos los campos de la tabla Peliculas.
	 *
	 * @param idPelicula        identificador único de la película
	 * @param nombrePelicula    título de la película
	 * @param director          nombre del director
	 * @param duracion          duración en minutos
	 * @param genero            género cinematográfico
	 * @param sinopsis          breve descripción argumental
	 * @param clasificacionEdad clasificación por edad: {@code TP}, {@code 7},
	 *                          {@code 12}, {@code 16} o {@code 18}
	 * @param estado            estado en el catálogo: {@code activa} o
	 *                          {@code inactiva}
	 */
	public Pelicula(int idPelicula, String nombrePelicula, String director, int duracion, String genero,
			String sinopsis, String clasificacionEdad, String estado) {
		this.idPelicula = idPelicula;
		this.nombrePelicula = nombrePelicula;
		this.director = director;
		this.duracion = duracion;
		this.genero = genero;
		this.sinopsis = sinopsis;
		this.clasificacionEdad = clasificacionEdad;
		this.estado = estado;
	}

	/**
	 * Devuelve el identificador único de la película.
	 *
	 * @return id de la película
	 */
	public int getId() {
		return idPelicula;
	}

	/**
	 * Devuelve el título de la película.
	 *
	 * @return título de la película
	 */
	public String getNombrePelicula() {
		return nombrePelicula;
	}

	/**
	 * Establece el título de la película.
	 *
	 * @param nombrePelicula título de la película
	 */
	public void setNombrePelicula(String nombrePelicula) {
		this.nombrePelicula = nombrePelicula;
	}

	/**
	 * Devuelve el nombre del director de la película.
	 *
	 * @return nombre del director
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * Establece el nombre del director de la película.
	 *
	 * @param director nombre del director
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * Devuelve la duración de la película en minutos.
	 *
	 * @return duración en minutos
	 */
	public int getDuracion() {
		return duracion;
	}

	/**
	 * Establece la duración de la película.
	 *
	 * @param duracion duración en minutos
	 */
	public void setDuracion(int duracion) {
		this.duracion = duracion;
	}

	/**
	 * Devuelve el género cinematográfico de la película.
	 *
	 * @return género de la película
	 */
	public String getGenero() {
		return genero;
	}

	/**
	 * Establece el género cinematográfico de la película.
	 *
	 * @param genero género de la película
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * Devuelve la sinopsis de la película.
	 *
	 * @return breve descripción argumental
	 */
	public String getSinopsis() {
		return sinopsis;
	}

	/**
	 * Establece la sinopsis de la película.
	 *
	 * @param sinopsis breve descripción argumental
	 */
	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	/**
	 * Devuelve la clasificación por edad de la película.
	 *
	 * @return {@code TP}, {@code 7}, {@code 12}, {@code 16} o {@code 18}
	 */
	public String getClasificacionEdad() {
		return clasificacionEdad;
	}

	/**
	 * Establece la clasificación por edad de la película.
	 *
	 * @param clasificacionEdad {@code TP}, {@code 7}, {@code 12}, {@code 16} o
	 *                          {@code 18}
	 */
	public void setClasificacionEdad(String clasificacionEdad) {
		this.clasificacionEdad = clasificacionEdad;
	}

}