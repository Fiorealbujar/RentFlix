// PanelEmpleado.java
package view;

import controller.Controlador;
import model.Empleado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelEmpleado extends JPanel {

	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_FONDO = new Color(0xF5F5F5);

	private JLabel lblBienvenida;
	private JTabbedPane tabbedPane;

	// Sin catálogo: el form de alquiler está en PanelGestionAlquileres
	private PanelGestionAlquileres panelGestionAlquileres;
	private PanelAnadirPelicula panelAnadirPelicula;
	private PanelGestionPeliculas panelGestionPeliculas;
	private PanelInformes panelInformes;

	public PanelEmpleado(PanelGestionAlquileres panelGestionAlquileres, PanelAnadirPelicula panelAnadirPelicula,
			PanelGestionPeliculas panelGestionPeliculas, PanelInformes panelInformes) {
		this.panelGestionAlquileres = panelGestionAlquileres;
		this.panelAnadirPelicula = panelAnadirPelicula;
		this.panelGestionPeliculas = panelGestionPeliculas;
		this.panelInformes = panelInformes;
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout());
		initComponents();
	}

	private void initComponents() {
		add(buildHeader(), BorderLayout.NORTH);
		add(buildTabs(), BorderLayout.CENTER);
	}

	// Solo bienvenida y badge, sin botón cerrar sesión
	private JPanel buildHeader() {
		JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		header.setBackground(COLOR_DARK);

		lblBienvenida = new JLabel("👋 Hola, Empleado");
		lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblBienvenida.setForeground(Color.WHITE);

		JLabel badge = new JLabel("EMPLEADO");
		badge.setFont(new Font("SansSerif", Font.BOLD, 10));
		badge.setForeground(COLOR_DARK);
		badge.setBackground(new Color(0xF0C040));
		badge.setOpaque(true);
		badge.setBorder(new EmptyBorder(3, 8, 3, 8));

		header.add(lblBienvenida);
		header.add(badge);
		return header;
	}

	private JTabbedPane buildTabs() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
		tabbedPane.addTab("📋  Gestión alquileres", panelGestionAlquileres);
		tabbedPane.addTab("➕  Añadir película", panelAnadirPelicula);
		tabbedPane.addTab("🎞️  Gestión películas", panelGestionPeliculas);
		tabbedPane.addTab("📊  Informes", panelInformes);
		return tabbedPane;
	}

	public void setBienvenida(Empleado emp) {
		lblBienvenida.setText("👋 Hola, " + emp.getNombreCompleto());
	}

	public void irAGestionAlquileres() {
		tabbedPane.setSelectedIndex(0);
	}

	public void setControlador(Controlador controlador) {
	}
}