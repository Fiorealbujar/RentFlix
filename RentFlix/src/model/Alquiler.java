package model;

public class Alquiler {
    private int id;
    private int idCliente;
    private int idCopia;
    private int idEmpleado;
    private int idTransaccion;
    private String fechaAlquiler;
    private String fechaDevolucionPrevista;
    private String fechaDevolucionReal;
    private String estado;

    // Campos extra para mostrar en la tabla (resultado de JOINs)
    private String nombrePelicula;
    private String nombreCliente;
    private double montoCobro;

    public Alquiler() {}

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }
    public int getIdCliente()                 { return idCliente; }
    public void setIdCliente(int v)           { this.idCliente = v; }
    public int getIdCopia()                   { return idCopia; }
    public void setIdCopia(int v)             { this.idCopia = v; }
    public int getIdEmpleado()                { return idEmpleado; }
    public void setIdEmpleado(int v)          { this.idEmpleado = v; }
    public int getIdTransaccion()             { return idTransaccion; }
    public void setIdTransaccion(int v)       { this.idTransaccion = v; }
    public String getFechaAlquiler()          { return fechaAlquiler; }
    public void setFechaAlquiler(String v)    { this.fechaAlquiler = v; }
    public String getFechaDevolucionPrevista(){ return fechaDevolucionPrevista; }
    public void setFechaDevolucionPrevista(String v) { this.fechaDevolucionPrevista = v; }
    public String getFechaDevolucionReal()    { return fechaDevolucionReal; }
    public void setFechaDevolucionReal(String v)     { this.fechaDevolucionReal = v; }
    public String getEstado()                 { return estado; }
    public void setEstado(String v)           { this.estado = v; }
    public String getNombrePelicula()         { return nombrePelicula; }
    public void setNombrePelicula(String v)   { this.nombrePelicula = v; }
    public String getNombreCliente()          { return nombreCliente; }
    public void setNombreCliente(String v)    { this.nombreCliente = v; }
    public double getMontoCobro()             { return montoCobro; }
    public void setMontoCobro(double v)       { this.montoCobro = v; }
}
