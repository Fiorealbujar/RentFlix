// ==========================================
// CLASE: Pago.java
// Representa la tabla: Pagos
// ==========================================
package model;

public class Pago {

    private int    idTransaccion;
    private String metodoPago;   // "efectivo", "tarjeta", "transferencia"
    private double montoCobro;

    public Pago() {}

    public Pago(int idTransaccion, String metodoPago, double montoCobro) {
        this.idTransaccion = idTransaccion;
        this.metodoPago    = metodoPago;
        this.montoCobro    = montoCobro;
    }

    public int getIdTransaccion()                        { return idTransaccion; }
    public void setIdTransaccion(int idTransaccion)      { this.idTransaccion = idTransaccion; }

    public String getMetodoPago()                        { return metodoPago; }
    public void setMetodoPago(String metodoPago)         { this.metodoPago = metodoPago; }

    public double getMontoCobro()                        { return montoCobro; }
    public void setMontoCobro(double montoCobro)         { this.montoCobro = montoCobro; }

    @Override
    public String toString() {
        return metodoPago + " - " + String.format("%.2f€", montoCobro);
    }
}