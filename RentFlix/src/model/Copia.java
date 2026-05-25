// ==========================================
// CLASE: Copia.java
// Representa la tabla: Copias
// Una película puede tener varias copias físicas
// ==========================================
package model;

public class Copia {

    private int    idCopia;
    private int    idPelicula;       // FK → Peliculas
    private String formato;          // "DVD", "Blu-ray", "4K Ultra HD"
    private String estado;           // "disponible", "alquilada", "dañada"
    private double precioAlquiler;

    public Copia() {}

    public Copia(int idCopia, int idPelicula, String formato,
                 String estado, double precioAlquiler) {
        this.idCopia       = idCopia;
        this.idPelicula    = idPelicula;
        this.formato       = formato;
        this.estado        = estado;
        this.precioAlquiler = precioAlquiler;
    }

    public int getIdCopia()                        { return idCopia; }
    public void setIdCopia(int idCopia)            { this.idCopia = idCopia; }

    public int getIdPelicula()                     { return idPelicula; }
    public void setIdPelicula(int idPelicula)      { this.idPelicula = idPelicula; }

    public String getFormato()                     { return formato; }
    public void setFormato(String formato)         { this.formato = formato; }

    public String getEstado()                      { return estado; }
    public void setEstado(String estado)           { this.estado = estado; }

    public double getPrecioAlquiler()                      { return precioAlquiler; }
    public void setPrecioAlquiler(double precioAlquiler)   { this.precioAlquiler = precioAlquiler; }

    // Utilidad para la vista
    public boolean isDisponible() {
        return "disponible".equalsIgnoreCase(this.estado);
    }

    @Override
    public String toString() {
        return "Copia #" + idCopia + " | " + formato + " | " + estado
             + " | " + String.format("%.2f€", precioAlquiler);
    }
}