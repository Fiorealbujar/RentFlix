package model;

public class Pelicula {
    private int id;
    private String nombre;
    private String director;
    private int duracion;
    private String genero;
    private String sinopsis;
    private String clasificacionEdad;

    public Pelicula() {}

    public Pelicula(int id, String nombre, String director, int duracion,
                    String genero, String sinopsis, String clasificacionEdad) {
        this.id = id;
        this.nombre = nombre;
        this.director = director;
        this.duracion = duracion;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.clasificacionEdad = clasificacionEdad;
    }

    public int getId()                    { return id; }
    public void setId(int id)             { this.id = id; }
    public String getNombre()             { return nombre; }
    public void setNombre(String n)       { this.nombre = n; }
    public String getDirector()           { return director; }
    public void setDirector(String d)     { this.director = d; }
    public int getDuracion()              { return duracion; }
    public void setDuracion(int d)        { this.duracion = d; }
    public String getGenero()             { return genero; }
    public void setGenero(String g)       { this.genero = g; }
    public String getSinopsis()           { return sinopsis; }
    public void setSinopsis(String s)     { this.sinopsis = s; }
    public String getClasificacionEdad()  { return clasificacionEdad; }
    public void setClasificacionEdad(String c) { this.clasificacionEdad = c; }

    @Override public String toString() { return nombre; }
}
