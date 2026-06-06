package dao;

import model.Pago;

/**
 * Interfaz que define las operaciones de acceso a datos para la entidad
 * {@link model.Pago}.
 * <p>
 * Declara la operación de registro de pagos generados al formalizar un
 * alquiler.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public interface IPagoDAO {

	/**
	 * Inserta un nuevo pago en la base de datos y devuelve su id generado. Se llama
	 * siempre antes de crear el alquiler asociado.
	 *
	 * @param pago objeto con el método de pago y el importe cobrado
	 * @return el {@code id_transaccion} generado por la base de datos, o {@code -1}
	 *         si la inserción falló
	 */
	int registrar(Pago pago);
}