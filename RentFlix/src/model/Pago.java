// ==========================================
// CLASE: Pago.java
// Representa la tabla: Pagos
// ==========================================
package model;

/**
 * Representa una transacción de pago generada al formalizar un alquiler.
 * <p>
 * Mapea la tabla {@code Pagos} de la base de datos SQLite. Cada alquiler genera
 * exactamente un pago (relación 1:1). El pago registra el método utilizado y el
 * importe total cobrado.
 * </p>
 *
 * @author Gabriel Fernández Cañadas
 * @version 1.0
 */
public class Pago {

	private int idTransaccion;
	private String metodoPago; // "efectivo", "tarjeta", "transferencia"
	private double montoCobro;

	/**
	 * Constructor completo con todos los campos de la tabla Pagos.
	 *
	 * @param idTransaccion identificador único de la transacción
	 * @param metodoPago    método de pago: {@code efectivo}, {@code tarjeta} o
	 *                      {@code transferencia}
	 * @param montoCobro    importe total cobrado en euros
	 */
	public Pago(int idTransaccion, String metodoPago, double montoCobro) {
		this.idTransaccion = idTransaccion;
		this.metodoPago = metodoPago;
		this.montoCobro = montoCobro;
	}
	
	/**
	 * Devuelve el método de pago utilizado en esta transacción.
	 *
	 * @return {@code efectivo}, {@code tarjeta} o {@code transferencia}
	 */
	public String getMetodoPago() {
		return metodoPago;
	}

	/**
	 * Devuelve el importe total cobrado en esta transacción.
	 *
	 * @return importe en euros
	 */
	public double getMontoCobro() {
		return montoCobro;
	}
	
}