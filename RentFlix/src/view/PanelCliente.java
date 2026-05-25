// ==========================================
// CLASE: PanelCliente.java
// Panel principal del rol Cliente.
// Usa JTabbedPane para navegar entre secciones.
// ==========================================
package view;

import controller.Controlador;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelCliente extends JPanel {

    private static final Color COLOR_FONDO      = new Color(0xF5F5F5);
    private static final Color COLOR_ACENTO     = new Color(0xE50914);
    private static final Color COLOR_DARK       = new Color(0x1a1a2e);

    private JTabbedPane tabbedPane;
    private JLabel      lblBienvenida;
    private JButton     btnCerrarSesion;

    // Sub-paneles
    private PanelCatalogo      panelCatalogo;
    private PanelMisAlquileres panelMisAlquileres;

    public PanelCliente(PanelCatalogo panelCatalogo,
                        PanelMisAlquileres panelMisAlquileres) {
        this.panelCatalogo      = panelCatalogo;
        this.panelMisAlquileres = panelMisAlquileres;

        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        add(buildHeader(),     BorderLayout.NORTH);
        add(buildTabs(),       BorderLayout.CENTER);
    }

    // ── Header del panel cliente ────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_DARK);
        header.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Bienvenida personalizada
        lblBienvenida = new JLabel("👋 Hola, Cliente");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);

        // Botón cerrar sesión
        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setActionCommand("CERRAR_SESION");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setBackground(new Color(0xE50914));
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(new EmptyBorder(7, 16, 7, 16));
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        header.add(lblBienvenida,   BorderLayout.WEST);
        header.add(btnCerrarSesion, BorderLayout.EAST);
        return header;
    }

    // ── Pestañas ────────────────────────────────────────────────────────────

    private JTabbedPane buildTabs() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        // Pestaña 1: Catálogo (reutilizamos el panel existente)
        tabbedPane.addTab("🎬  Catálogo", panelCatalogo);

        // Pestaña 2: Mis alquileres
        tabbedPane.addTab("📋  Mis alquileres", panelMisAlquileres);

        return tabbedPane;
    }

    // ── Métodos públicos que usa el Controlador ─────────────────────────────

    // Personaliza el saludo con el nombre del cliente
    public void setBienvenida(Cliente cliente) {
        lblBienvenida.setText("👋 Hola, " + cliente.getNombreCompleto());
    }

    public void setControlador(Controlador controlador) {
        btnCerrarSesion.addActionListener(controlador);
    }

    // Navega a la pestaña de mis alquileres
    public void irAMisAlquileres() {
        tabbedPane.setSelectedIndex(1);
    }

    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
}