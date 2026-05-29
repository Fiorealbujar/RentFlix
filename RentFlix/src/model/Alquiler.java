// ==========================================
// CLASE: Alquiler.java
// Representa la tabla: Alquileres
// ==========================================
package model;

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

	public Alquiler() {
	}

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

	public int getIdAlquiler() {
		return idAlquiler;
	}

	public void setIdAlquiler(int idAlquiler) {
		this.idAlquiler = idAlquiler;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public int getIdCopia() {
		return idCopia;
	}

	public void setIdCopia(int idCopia) {
		this.idCopia = idCopia;
	}

	public Integer getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Integer idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public Integer getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Integer idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	public String getFechaAlquiler() {
		return fechaAlquiler;
	}

	public void setFechaAlquiler(String fechaAlquiler) {
		this.fechaAlquiler = fechaAlquiler;
	}

	public String getFechaDevolucionPrevista() {
		return fechaDevolucionPrevista;
	}

	public void setFechaDevolucionPrevista(String fechaDevolucionPrevista) {
		this.fechaDevolucionPrevista = fechaDevolucionPrevista;
	}

	public String getFechaDevolucionReal() {
		return fechaDevolucionReal;
	}

	public void setFechaDevolucionReal(String fechaDevolucionReal) {
		this.fechaDevolucionReal = fechaDevolucionReal;
	}

	public String getEstadoAlquiler() {
		return estadoAlquiler;
	}

	public void setEstadoAlquiler(String estadoAlquiler) {
		this.estadoAlquiler = estadoAlquiler;
	}

	// Getters y setters de campos extra (JOINs)
	public String getNombrePelicula() {
		return nombrePelicula;
	}

	public void setNombrePelicula(String nombrePelicula) {
		this.nombrePelicula = nombrePelicula;
	}

	public String getNombreCliente() {
		return nombreCliente;
	}

	public void setNombreCliente(String nombreCliente) {
		this.nombreCliente = nombreCliente;
	}

	public double getMontoCobro() {
		return montoCobro;
	}

	public void setMontoCobro(double montoCobro) {
		this.montoCobro = montoCobro;
	}

	@Override
	public String toString() {
		return "Alquiler #" + idAlquiler + " | " + estadoAlquiler + " | Desde: " + fechaAlquiler + " | Hasta: "
				+ fechaDevolucionPrevista;
	}
}