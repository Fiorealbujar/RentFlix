// ==========================================
// CLASE: Alquiler.java
// Representa la tabla: Alquileres
// ==========================================
package model;

/**
 * Representa un alquiler de una copia física de película por parte de un
 * cliente.
 * <p>
 * Mapea la tabla {@code Alquileres} de la base de datos SQLite. El estado del
 * alquiler puede ser {@code activo}, {@code pendiente_devolucion},
 * {@code devuelto} o {@code vencido}. El campo {@code idEmpleado} es nullable:
 * si es {@code null}, el alquiler fue registrado directamente por el cliente.
 * Los campos {@code nombrePelicula}, {@code nombreCliente} y {@code montoCobro}
 * no pertenecen a la tabla; se rellenan mediante JOINs en la capa DAO para
 * facilitar la visualización en las vistas.
 * </p>
 *
 * @author Gabriel Fernández Cañadas
 * @version 1.0
 */
public class Alquiler {

	private int idAlquiler;
	private int idCliente;
	private int idCopia;
	private Integer idEmpleado; // puede ser null (alquiler por el propio cliente)
	private Integer idTransaccion; // puede ser null hasta que se pague
	private String fechaAlquiler;
	private String fechaDevolucionPrevista;
	private String fechaDevolucionReal; // null si aún no se devolvió
	private String estadoAlquiler; // "activo", "pendiente_devolucion", "devuelto", "vencido"

	// Campos extra para mostrar en la vista (resultado de JOINs)
	private String nombrePelicula;
	private String nombreCliente;
	private double montoCobro;

	/**
	 * Constructor completo con todos los campos persistidos en la base de datos.
	 *
	 * @param idAlquiler              identificador único del alquiler
	 * @param idCliente               identificador del cliente que realiza el
	 *                                alquiler
	 * @param idCopia                 identificador de la copia física alquilada
	 * @param idEmpleado              identificador del empleado que registra la
	 *                                operación, o {@code null} si lo registró el
	 *                                propio cliente
	 * @param idTransaccion           identificador del pago asociado, o
	 *                                {@code null} si aún no se ha procesado
	 * @param fechaAlquiler           fecha de inicio del alquiler en formato
	 *                                {@code yyyy-MM-dd}
	 * @param fechaDevolucionPrevista fecha de devolución pactada en formato
	 *                                {@code yyyy-MM-dd}
	 * @param fechaDevolucionReal     fecha real de devolución, o {@code null} si el
	 *                                alquiler sigue activo
	 * @param estadoAlquiler          estado actual: {@code activo},
	 *                                {@code pendiente_devolucion}, {@code devuelto}
	 *                                o {@code vencido}
	 */
	public Alquiler(int idAlquiler, int idCliente, int idCopia, Integer idEmpleado, Integer idTransaccion,
			String fechaAlquiler, String fechaDevolucionPrevista, String fechaDevolucionReal, String estadoAlquiler) {
		this.idAlquiler = idAlquiler;
		this.idCliente = idCliente;
		this.idCopia = idCopia;
		this.idEmpleado = idEmpleado;
		this.idTransaccion = idTransaccion;
		this.fechaAlquiler = fechaAlquiler;
		this.fechaDevolucionPrevista = fechaDevolucionPrevista;
		this.fechaDevolucionReal = fechaDevolucionReal;
		this.estadoAlquiler = estadoAlquiler;
	}

	/**
	 * Devuelve el identificador único del alquiler.
	 *
	 * @return id del alquiler
	 */
	public int getIdAlquiler() {
		return idAlquiler;
	}

	/**
	 * Devuelve el identificador del cliente asociado al alquiler.
	 *
	 * @return id del cliente
	 */
	public int getIdCliente() {
		return idCliente;
	}

	/**
	 * Devuelve el identificador de la copia física alquilada.
	 *
	 * @return id de la copia
	 */
	public int getIdCopia() {
		return idCopia;
	}

	/**
	 * Devuelve el identificador del empleado que registró el alquiler. Si el
	 * alquiler fue registrado por el propio cliente, devuelve {@code null}.
	 *
	 * @return id del empleado, o {@code null}
	 */
	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	/**
	 * Devuelve el identificador de la transacción de pago asociada.
	 *
	 * @return id de la transacción, o {@code null} si aún no se ha procesado
	 */
	public Integer getIdTransaccion() {
		return idTransaccion;
	}

	/**
	 * Devuelve la fecha de inicio del alquiler.
	 *
	 * @return fecha en formato {@code yyyy-MM-dd}
	 */
	public String getFechaAlquiler() {
		return fechaAlquiler;
	}

	/**
	 * Devuelve la fecha de devolución pactada al registrar el alquiler.
	 *
	 * @return fecha prevista en formato {@code yyyy-MM-dd}
	 */
	public String getFechaDevolucionPrevista() {
		return fechaDevolucionPrevista;
	}

	/**
	 * Devuelve la fecha real en que se devolvió la copia.
	 *
	 * @return fecha real en formato {@code yyyy-MM-dd}, o {@code null} si no se ha
	 *         devuelto
	 */
	public String getFechaDevolucionReal() {
		return fechaDevolucionReal;
	}

	/**
	 * Devuelve el estado actual del alquiler.
	 *
	 * @return {@code activo}, {@code pendiente_devolucion}, {@code devuelto} o
	 *         {@code vencido}
	 */
	public String getEstadoAlquiler() {
		return estadoAlquiler;
	}

	// Getters y setters de campos extra (JOINs)

	/**
	 * Devuelve el título de la película asociada a este alquiler. Este campo se
	 * rellena mediante JOIN en la capa DAO, no pertenece a la tabla Alquileres.
	 *
	 * @return nombre de la película
	 */
	public String getNombrePelicula() {
		return nombrePelicula;
	}

	/**
	 * Establece el título de la película (campo de JOIN).
	 *
	 * @param nombrePelicula nombre de la película
	 */
	public void setNombrePelicula(String nombrePelicula) {
		this.nombrePelicula = nombrePelicula;
	}

	/**
	 * Devuelve el nombre completo del cliente asociado a este alquiler. Este campo
	 * se rellena mediante JOIN en la capa DAO, no pertenece a la tabla Alquileres.
	 *
	 * @return nombre completo del cliente
	 */
	public String getNombreCliente() {
		return nombreCliente;
	}

	/**
	 * Establece el nombre completo del cliente (campo de JOIN).
	 *
	 * @param nombreCliente nombre completo del cliente
	 */
	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	/**
	 * Devuelve el importe cobrado en este alquiler. Este campo se rellena mediante
	 * JOIN con la tabla Pagos en la capa DAO.
	 *
	 * @return importe cobrado en euros
	 */
	public double getMontoCobro() {
		return montoCobro;
	}

	/**
	 * Establece el importe cobrado (campo de JOIN).
	 *
	 * @param montoCobro importe en euros
	 */
	public void setMontoCobro(double montoCobro) {
		this.montoCobro = montoCobro;
	}

}