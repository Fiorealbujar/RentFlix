// ==========================================
// INTERFAZ: IPagoDAO.java
// ==========================================
package dao;

import model.Pago;

public interface IPagoDAO {
    // Registrar un pago y devolver el id generado
    int registrar(Pago pago);
}