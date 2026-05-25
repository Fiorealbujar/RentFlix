// ==========================================
// CLASE: PanelAdmin.java
// Panel principal del Administrador.
// Extiende las capacidades del Empleado.
// ==========================================
package view;

import controller.Controlador;
import model.Empleado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelAdmin extends JPanel {

    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);
    private static final Color COLOR_GOLD   = new Color(0xF0C040);
    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);

    private JLabel      lblBienvenida;
    private JButton     btnCerrarSesion;
    private JTabbedPane tabbedPane;

    // Sub-paneles
    private PanelCatalogo          panelCatalogo;
    private PanelGestionAlquileres panelGestionAlquileres;
    private PanelAnadirPelicula    panelAnadirPelicula;
    private PanelGestionPeliculas  panelGestionPeliculas;
    private PanelInformes          panelInformes;
    private PanelGestionEmpleados  panelGestionEmpleados;

    public PanelAdmin(PanelCatalogo panelCatalogo,
                      PanelGestionAlquileres panelGestionAlquileres,
                      PanelAnadirPelicula panelAnadirPelicula,
                      PanelGestionPeliculas panelGestionPeliculas,
                      PanelInformes panelInformes,
                      PanelGestionEmpleados panelGestionEmpleados) {
        this.panelCatalogo          = panelCatalogo;
        this.panelGestionAlquileres = panelGestionAlquileres;
        this.panelAnadirPelicula    = panelAnadirPelicula;
        this.panelGestionPeliculas  = panelGestionPeliculas;
        this.panelInformes          = panelInformes;
        this.panelGestionEmpleados  = panelGestionEmpleados;

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

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        izquierda.setOpaque(false);

        lblBienvenida = new JLabel("👑 Hola, Administrador");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);

        // Badge dorado para el admin
        JLabel badgeRol = new JLabel("ADMIN");
        badgeRol.setFont(new Font("SansSerif", Font.BOLD, 10));
        badgeRol.setForeground(COLOR_DARK);
        badgeRol.setBackground(COLOR_GOLD);
        badgeRol.setOpaque(true);
        badgeRol.setBorder(new EmptyBorder(3, 8, 3, 8));

        izquierda.add(lblBienvenida);
        izquierda.add(badgeRol);

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
        tabbedPane.addTab("📋  Alquileres",          panelGestionAlquileres);
        tabbedPane.addTab("➕  Añadir película",     panelAnadirPelicula);
        tabbedPane.addTab("🎞️  Gestión películas",   panelGestionPeliculas);
        tabbedPane.addTab("📊  Informes",            panelInformes);
        tabbedPane.addTab("👥  Empleados",           panelGestionEmpleados);

        return tabbedPane;
    }

    // ── Métodos públicos ────────────────────────────────────────────────────

    public void setBienvenida(Empleado empleado) {
        lblBienvenida.setText("👑 Hola, " + empleado.getNombreCompleto());
    }

    public void irAInformes() {
        tabbedPane.setSelectedIndex(4);
    }

    public void setControlador(Controlador controlador) {
        btnCerrarSesion.addActionListener(controlador);
    }

    public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
}