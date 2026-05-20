package model;

public class Copia {
    private int id;
    private int idPelicula;
    private String formato;
    private String estado;
    private double precioAlquiler;

    public Copia() {}

    public Copia(int id, int idPelicula, String formato, String estado, double precioAlquiler) {
        this.id = id;
        this.idPelicula = idPelicula;
        this.formato = formato;
        this.estado = estado;
        this.precioAlquiler = precioAlquiler;
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }
    public int getIdPelicula()           { return idPelicula; }
    public void setIdPelicula(int v)     { this.idPelicula = v; }
    public String getFormato()           { return formato; }
    public void setFormato(String v)     { this.formato = v; }
    public String getEstado()            { return estado; }
    public void setEstado(String v)      { this.estado = v; }
    public double getPrecioAlquiler()    { return precioAlquiler; }
    public void setPrecioAlquiler(double v) { this.precioAlquiler = v; }
}
