package security;

import model.Cliente;

public class SesionManager {
    private static SesionManager instancia;
    private Cliente usuarioLogueado;

    private SesionManager() {}

    public static SesionManager getInstancia() {
        if (instancia == null) {
            instancia = new SesionManager();
        }
        return instancia;
    }

    public void iniciarSesion(Cliente cliente) {
        this.usuarioLogueado = cliente;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    public Cliente getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public boolean esAdmin() {
        return usuarioLogueado != null && "admin".equalsIgnoreCase(usuarioLogueado.getUsuario());
    }
}