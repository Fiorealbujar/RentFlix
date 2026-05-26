// Main.java — DEFINITIVO
// Cada panel tiene sus propios subpaneles, sin compartir
package main;

import com.formdev.flatlaf.FlatLightLaf;
import controller.Controlador;
import view.*;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {

            VentanaPrincipal   ventana       = new VentanaPrincipal();
            PanelLogin         panelLogin    = new PanelLogin();
            PanelRegistro      panelRegistro = new PanelRegistro();

            // ── Cliente ──────────────────────────────────────────────────────
            // El catálogo del cliente es exclusivo suyo
            PanelCatalogo      catCliente       = new PanelCatalogo(false);
            PanelMisAlquileres misAlquileres     = new PanelMisAlquileres();
            PanelCliente       panelCliente      = new PanelCliente(
                catCliente, misAlquileres
            );

            // ── Empleado ─────────────────────────────────────────────────────
            // Paneles exclusivos del empleado
            PanelGestionAlquileres gestionAlqEmp  = new PanelGestionAlquileres();
            PanelAnadirPelicula    anadirPelEmp    = new PanelAnadirPelicula();
            PanelGestionPeliculas  gestionPelEmp   = new PanelGestionPeliculas();
            PanelInformes          informesEmp     = new PanelInformes();
            PanelEmpleado          panelEmpleado   = new PanelEmpleado(
                gestionAlqEmp, anadirPelEmp, gestionPelEmp, informesEmp
            );

            // ── Admin ────────────────────────────────────────────────────────
            // Paneles exclusivos del admin
            PanelGestionAlquileres gestionAlqAdm  = new PanelGestionAlquileres();
            PanelAnadirPelicula    anadirPelAdm    = new PanelAnadirPelicula();
            PanelGestionPeliculas  gestionPelAdm   = new PanelGestionPeliculas();
            PanelInformes          informesAdm     = new PanelInformes();
            PanelGestionEmpleados  gestionEmpleados = new PanelGestionEmpleados();
            PanelAdmin             panelAdmin       = new PanelAdmin(
                gestionAlqAdm, anadirPelAdm,
                gestionPelAdm, informesAdm, gestionEmpleados
            );

            // ── Controlador ──────────────────────────────────────────────────
            Controlador controlador = new Controlador(
                ventana,
                catCliente,
                panelLogin, panelRegistro,
                panelCliente, misAlquileres,
                panelEmpleado,
                gestionAlqEmp, anadirPelEmp, gestionPelEmp, informesEmp,
                panelAdmin,
                gestionAlqAdm, anadirPelAdm, gestionPelAdm,
                informesAdm, gestionEmpleados
            );

            // ── Inyectar controlador ─────────────────────────────────────────
            ventana.setControlador(controlador);
            catCliente.setControlador(controlador);
            panelLogin.setControlador(controlador);
            panelRegistro.setControlador(controlador);
            panelCliente.setControlador(controlador);
            misAlquileres.setControlador(controlador);
            panelEmpleado.setControlador(controlador);
            gestionAlqEmp.setControlador(controlador);
            anadirPelEmp.setControlador(controlador);
            gestionPelEmp.setControlador(controlador);
            informesEmp.setControlador(controlador);
            panelAdmin.setControlador(controlador);
            gestionAlqAdm.setControlador(controlador);
            anadirPelAdm.setControlador(controlador);
            gestionPelAdm.setControlador(controlador);
            informesAdm.setControlador(controlador);
            gestionEmpleados.setControlador(controlador);

            ventana.hacerVisible();
        });
    }
}