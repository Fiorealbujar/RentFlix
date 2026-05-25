// ==========================================
// CLASE: Main.java — VERSIÓN FINAL
// ==========================================
package main;

import com.formdev.flatlaf.FlatLightLaf;
import controller.Controlador;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {

            // ── 1. Crear todas las vistas ────────────────────────────────────
            VentanaPrincipal       ventana                = new VentanaPrincipal();
            PanelCatalogo          panelCatalogo          = new PanelCatalogo();
            PanelLogin             panelLogin             = new PanelLogin();
            PanelRegistro          panelRegistro          = new PanelRegistro();
            PanelMisAlquileres     panelMisAlquileres     = new PanelMisAlquileres();
            PanelGestionAlquileres panelGestionAlquileres = new PanelGestionAlquileres();
            PanelAnadirPelicula    panelAnadirPelicula    = new PanelAnadirPelicula();
            PanelGestionPeliculas  panelGestionPeliculas  = new PanelGestionPeliculas();
            PanelInformes          panelInformes          = new PanelInformes();
            PanelGestionEmpleados  panelGestionEmpleados  = new PanelGestionEmpleados();

            PanelCliente  panelCliente  = new PanelCliente(
                panelCatalogo, panelMisAlquileres
            );
            PanelEmpleado panelEmpleado = new PanelEmpleado(
                panelCatalogo, panelGestionAlquileres, panelAnadirPelicula
            );
            PanelAdmin    panelAdmin    = new PanelAdmin(
                panelCatalogo, panelGestionAlquileres, panelAnadirPelicula,
                panelGestionPeliculas, panelInformes, panelGestionEmpleados
            );

            // ── 2. Crear controlador ─────────────────────────────────────────
            Controlador controlador = new Controlador(
                ventana, panelCatalogo, panelLogin, panelRegistro,
                panelCliente, panelMisAlquileres,
                panelEmpleado, panelGestionAlquileres, panelAnadirPelicula,
                panelAdmin, panelGestionPeliculas,
                panelInformes, panelGestionEmpleados
            );

            // ── 3. Inyectar controlador en todas las vistas ──────────────────
            ventana.setControlador(controlador);
            panelCatalogo.setControlador(controlador);
            panelLogin.setControlador(controlador);
            panelRegistro.setControlador(controlador);
            panelCliente.setControlador(controlador);
            panelMisAlquileres.setControlador(controlador);
            panelEmpleado.setControlador(controlador);
            panelGestionAlquileres.setControlador(controlador);
            panelAnadirPelicula.setControlador(controlador);
            panelAdmin.setControlador(controlador);
            panelGestionPeliculas.setControlador(controlador);
            panelInformes.setControlador(controlador);
            panelGestionEmpleados.setControlador(controlador);

            ventana.hacerVisible();
        });
    }
}