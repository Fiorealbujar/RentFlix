// ==========================================
// CLASE: Actor.java
// Representa la tabla: Actores
// ==========================================
package model;

public class Actor {

    private int    idActor;
    private String nombreActor;

    public Actor() {}

    public Actor(int idActor, String nombreActor) {
        this.idActor     = idActor;
        this.nombreActor = nombreActor;
    }

    public int getIdActor()                      { return idActor; }
    public void setIdActor(int idActor)          { this.idActor = idActor; }

    public String getNombreActor()               { return nombreActor; }
    public void setNombreActor(String nombre)    { this.nombreActor = nombre; }

    @Override
    public String toString() { return nombreActor; }
}