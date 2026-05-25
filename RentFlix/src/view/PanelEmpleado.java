// ==========================================
// CLASE: PanelEmpleado.java
// Panel principal del rol Empleado.
// ==========================================
package view;

import controller.Controlador;
import model.Empleado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelEmpleado extends JPanel {

    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);
    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);

    private JLabel      lblBienvenida;
    private JButton     btnCerrarSesion;
    private JTabbedPane tabbedPane;

    // Sub-paneles
    private PanelCatalogo          panelCatalogo;
    private PanelGestionAlquileres panelGestionAlquileres;
    private PanelAnadirPelicula    panelAnadirPelicula;

    public PanelEmpleado(PanelCatalogo panelCatalogo,
                         PanelGestionAlquileres panelGestionAlquileres,
                         PanelAnadirPelicula panelAnadirPelicula) {
        this.panelCatalogo          = panelCatalogo;
        this.panelGestionAlquileres = panelGestionAlquileres;
        this.panelAnadirPelicula    = panelAnadirPelicula;

        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);
    }

    // ── Header ──────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_DARK);
        header.setBorder(new EmptyBorder(12, 24, 12, 24));

        // Lado izquierdo: bienvenida + badge de rol
        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        izquierda.setOpaque(false);

        lblBienvenida = new JLabel("👋 Hola, Empleado");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel badgeRol = new JLabel("EMPLEADO");
        badgeRol.setFont(new Font("SansSerif", Font.BOLD, 10));
        badgeRol.setForeground(COLOR_DARK);
        badgeRol.setBackground(new Color(0xF0C040));
        badgeRol.setOpaque(true);
        badgeRol.setBorder(new EmptyBorder(3, 8, 3, 8));

        izquierda.add(lblBienvenida);
        izquierda.add(badgeRol);

        // Lado derecho: botón cerrar sesión
        btnCerrarSesion = new JButton("Cerrar sesión");
        btnCerrarSesion.setActionCommand("CERRAR_SESION");
        btnCerrarSesion.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnCerrarSesion.setBackground(COLOR_ACENTO);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(new EmptyBorder(7, 16, 7, 16));
        btnCerrarSesion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        header.add(izquierda,       BorderLayout.WEST);
        header.add(btnCerrarSesion, BorderLayout.EAST);
        return header;
    }

    // ── Pestañas ────────────────────────────────────────────────────────────

    private JTabbedPane buildTabs() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));

        tabbedPane.addTab("🎬  Catálogo",           panelCatalogo);
        tabbedPane.addTab("📋  Gestión alquileres",  panelGestionAlquileres);
        tabbedPane.addTab("➕  Añadir película",     panelAnadirPelicula);

        return tabbedPane;
    }

    // ── Métodos públicos ────────────────────────────────────────────────────

    public void setBienvenida(Empleado empleado) {
        lblBienvenida.setText("👋 Hola, " + empleado.getNombreCompleto());
    }

    public void irAGestionAlquileres() {
        tabbedPane.setSelectedIndex(1);
    }

    public void setControlador(Controlador controlador) {
        btnCerrarSesion.addActionListener(controlador);
    }

    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
}