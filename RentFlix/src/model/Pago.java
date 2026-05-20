package model;

public class Pago {
    private int id;
    private String metodoPago;
    private double montoCobro;

    public Pago() {}

    public Pago(int id, String metodoPago, double montoCobro) {
        this.id = id;
        this.metodoPago = metodoPago;
        this.montoCobro = montoCobro;
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }
    public String getMetodoPago()        { return metodoPago; }
    public void setMetodoPago(String m)  { this.metodoPago = m; }
    public double getMontoCobro()        { return montoCobro; }
    public void setMontoCobro(double m)  { this.montoCobro = m; }

    @Override public String toString() { return metodoPago + " — " + String.format("%.2f €", montoCobro); }
}
