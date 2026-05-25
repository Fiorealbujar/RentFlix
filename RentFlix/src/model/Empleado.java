// ==========================================
// CLASE: Empleado.java
// Representa la tabla: Empleados
// Si id_jefe es NULL → es Administrador
// ==========================================
package model;

public class Empleado {

    private int     idEmpleado;
    private String  nombreEmpleado;
    private String  apellidoEmpleado;
    private String  emailEmpleado;
    private String  usuarioEmpleado;
    private String  contraseniaEmpleado;
    private Integer idJefe;   // NULL si es el administrador (jefe de todos)

    public Empleado() {}

    public Empleado(int idEmpleado, String nombreEmpleado, String apellidoEmpleado,
                    String emailEmpleado, String usuarioEmpleado,
                    String contraseniaEmpleado, Integer idJefe) {
        this.idEmpleado          = idEmpleado;
        this.nombreEmpleado      = nombreEmpleado;
        this.apellidoEmpleado    = apellidoEmpleado;
        this.emailEmpleado       = emailEmpleado;
        this.usuarioEmpleado     = usuarioEmpleado;
        this.contraseniaEmpleado = contraseniaEmpleado;
        this.idJefe              = idJefe;
    }

    public int getIdEmpleado()                               { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado)                { this.idEmpleado = idEmpleado; }

    public String getNombreEmpleado()                        { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado)     { this.nombreEmpleado = nombreEmpleado; }

    public String getApellidoEmpleado()                          { return apellidoEmpleado; }
    public void setApellidoEmpleado(String apellidoEmpleado)     { this.apellidoEmpleado = apellidoEmpleado; }

    public String getEmailEmpleado()                         { return emailEmpleado; }
    public void setEmailEmpleado(String emailEmpleado)       { this.emailEmpleado = emailEmpleado; }

    public String getUsuarioEmpleado()                           { return usuarioEmpleado; }
    public void setUsuarioEmpleado(String usuarioEmpleado)       { this.usuarioEmpleado = usuarioEmpleado; }

    public String getContraseniaEmpleado()                               { return contraseniaEmpleado; }
    public void setContraseniaEmpleado(String contraseniaEmpleado)       { this.contraseniaEmpleado = contraseniaEmpleado; }

    public Integer getIdJefe()                       { return idJefe; }
    public void setIdJefe(Integer idJefe)            { this.idJefe = idJefe; }

    // CLAVE: la lógica de roles vive aquí, sin necesidad de campo extra en la BD
    public boolean esAdministrador() {
        return this.idJefe == null;
    }

    public String getNombreCompleto() {
        return nombreEmpleado + " " + apellidoEmpleado;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (@" + usuarioEmpleado + ")"
             + (esAdministrador() ? " [ADMIN]" : " [Empleado]");
    }
}