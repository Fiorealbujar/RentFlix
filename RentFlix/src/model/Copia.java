// ==========================================
// CLASE: Copia.java
// Representa la tabla: Copias
// Una película puede tener varias copias físicas
// ==========================================
package model;

/**
 * Representa una copia física de una película disponible en el videoclub.
 * <p>
 * Mapea la tabla {@code Copias} de la base de datos SQLite. Una misma película
 * puede tener múltiples copias en distintos formatos (DVD, Blu-ray, 4K Ultra
 * HD). El campo {@code estado} refleja la disponibilidad actual de la copia:
 * {@code disponible} cuando está libre para alquilar, o {@code alquilada}
 * cuando tiene un alquiler activo. El precio de alquiler reside en esta entidad
 * (no en Peliculas) para permitir precios diferenciados por formato.
 * </p>
 *
 * @author Gabriel Fernández Cañadas
 * @version 1.0
 */
public class Copia {

	/** Identificador único de la copia física. */
	private int idCopia;
	/**
	 * Identificador de la película a la que pertenece esta copia (FK → Peliculas).
	 */
	private int idPelicula;
	/**
	 * Formato físico de la copia: {@code DVD}, {@code Blu-ray} o
	 * {@code 4K Ultra HD}.
	 */
	private String formato;
	/** Estado actual de la copia: {@code disponible} o {@code alquilada}. */
	private String estado;
	/** Precio de alquiler por día de esta copia en euros. */
	private double precioAlquiler;

	/**
	 * Constructor completo con todos los campos de la tabla Copias.
	 *
	 * @param idCopia        identificador único de la copia
	 * @param idPelicula     identificador de la película a la que pertenece (FK)
	 * @param formato        formato físico: {@code DVD}, {@code Blu-ray} o
	 *                       {@code 4K Ultra HD}
	 * @param estado         estado actual: {@code disponible} o {@code alquilada}
	 * @param precioAlquiler precio por día de alquiler en euros
	 */
	public Copia(int idCopia, int idPelicula, String formato, String estado, double precioAlquiler) {
		this.idCopia = idCopia;
		this.idPelicula = idPelicula;
		this.formato = formato;
		this.estado = estado;
		this.precioAlquiler = precioAlquiler;
	}

	/**
	 * Devuelve el identificador único de la copia.
	 *
	 * @return id de la copia
	 */
	public int getIdCopia() {
		return idCopia;
	}

	/**
	 * Devuelve el identificador de la película asociada a esta copia.
	 *
	 * @return id de la película (FK hacia Peliculas)
	 */
	public int getIdPelicula() {
		return idPelicula;
	}

	/**
	 * Devuelve el formato físico de la copia.
	 *
	 * @return {@code DVD}, {@code Blu-ray} o {@code 4K Ultra HD}
	 */
	public String getFormato() {
		return formato;
	}

	/**
	 * Devuelve el estado actual de la copia.
	 *
	 * @return {@code disponible} o {@code alquilada}
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Devuelve el precio de alquiler por día de esta copia.
	 *
	 * @return precio en euros
	 */
	public double getPrecioAlquiler() {
		return precioAlquiler;
	}

}