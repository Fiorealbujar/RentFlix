// PanelCliente.java
package view;

import controller.Controlador;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelCliente extends JPanel {

    private static final Color COLOR_DARK  = new Color(0x1a1a2e);
    private static final Color COLOR_FONDO = new Color(0xF5F5F5);

    private JLabel      lblBienvenida;
    private JTabbedPane tabbedPane;

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
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(),   BorderLayout.CENTER);
    }

    // Solo bienvenida, sin botón cerrar sesión (lo gestiona la TopBar)
    private JPanel buildHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        header.setBackground(COLOR_DARK);

        lblBienvenida = new JLabel("👋 Hola, Cliente");
        lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblBienvenida.setForeground(Color.WHITE);

        JLabel badge = new JLabel("CLIENTE");
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(COLOR_DARK);
        badge.setBackground(new Color(0x5DADE2));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));

        header.add(lblBienvenida);
        header.add(badge);
        return header;
    }

    private JTabbedPane buildTabs() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabbedPane.addTab("🎬  Catálogo",       panelCatalogo);
        tabbedPane.addTab("📋  Mis alquileres", panelMisAlquileres);
        return tabbedPane;
    }

    public void setBienvenida(Cliente cliente) {
        lblBienvenida.setText("👋 Hola, " + cliente.getNombreCompleto());
    }

    public void irAMisAlquileres() {
        tabbedPane.setSelectedIndex(1);
    }

    public void setControlador(Controlador controlador) {}
}