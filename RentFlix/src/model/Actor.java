package model;

public class Actor {
    private int id;
    private String nombre;

    public Actor() {}

    public Actor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }
    public String getNombre()       { return nombre; }
    public void setNombre(String n) { this.nombre = n; }

    @Override public String toString() { return nombre; }
}
