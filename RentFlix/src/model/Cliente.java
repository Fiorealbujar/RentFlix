// ==========================================
// CLASE: Cliente.java
// Representa la tabla: Clientes
// ==========================================
package model;

public class Cliente {

    private int    idCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String emailCliente;
    private String nombreUsuario;
    private String contraseniaCliente;
    private String estado;            // "activo", "inactivo"

    public Cliente() {}

    public Cliente(int idCliente, String nombreCliente, String apellidoCliente,
                   String emailCliente, String nombreUsuario,
                   String contraseniaCliente, String estado) {
        this.idCliente         = idCliente;
        this.nombreCliente     = nombreCliente;
        this.apellidoCliente   = apellidoCliente;
        this.emailCliente      = emailCliente;
        this.nombreUsuario     = nombreUsuario;
        this.contraseniaCliente = contraseniaCliente;
        this.estado            = estado;
    }

    public int getIdCliente()                            { return idCliente; }
    public void setIdCliente(int idCliente)              { this.idCliente = idCliente; }

    public String getNombreCliente()                     { return nombreCliente; }
    public void setNombreCliente(String nombreCliente)   { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente()                           { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente)       { this.apellidoCliente = apellidoCliente; }

    public String getEmailCliente()                      { return emailCliente; }
    public void setEmailCliente(String emailCliente)     { this.emailCliente = emailCliente; }

    public String getNombreUsuario()                         { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario)       { this.nombreUsuario = nombreUsuario; }

    public String getContraseniaCliente()                            { return contraseniaCliente; }
    public void setContraseniaCliente(String contraseniaCliente)     { this.contraseniaCliente = contraseniaCliente; }

    public String getEstado()                            { return estado; }
    public void setEstado(String estado)                 { this.estado = estado; }

    // Nombre completo para mostrar en la vista
    public String getNombreCompleto() {
        return nombreCliente + " " + apellidoCliente;
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " (@" + nombreUsuario + ")";
    }
}