package model;

public class Empleado {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String usuario;
    private String contrasenia;
    private int idJefe;

    public Empleado() {}

    public Empleado(int id, String nombre, String apellido, String email,
                    String usuario, String contrasenia, int idJefe) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.idJefe = idJefe;
    }

    public int getId()                   { return id; }
    public void setId(int id)            { this.id = id; }
    public String getNombre()            { return nombre; }
    public void setNombre(String n)      { this.nombre = n; }
    public String getApellido()          { return apellido; }
    public void setApellido(String a)    { this.apellido = a; }
    public String getEmail()             { return email; }
    public void setEmail(String e)       { this.email = e; }
    public String getUsuario()           { return usuario; }
    public void setUsuario(String u)     { this.usuario = u; }
    public String getContrasenia()       { return contrasenia; }
    public void setContrasenia(String c) { this.contrasenia = c; }
    public int getIdJefe()               { return idJefe; }
    public void setIdJefe(int v)         { this.idJefe = v; }

    @Override public String toString() { return apellido + ", " + nombre; }
}
