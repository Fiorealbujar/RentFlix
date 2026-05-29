package controller;

import view.MainFrame;
import view.PanelCatalogo;
import view.DashboardPanel;

import javax.swing.*;
import java.awt.*;

public class Controlador {

	private final MainFrame frame;
	private final JPanel contentArea;

	// Paneles disponibles (se crean una sola vez, lazy)
	private DashboardPanel dashboardPanel;
	private PanelCatalogo catalogoPanel;

	public enum Seccion {
		DASHBOARD, CATALOGO, CLIENTES, ALQUILERES, INVENTARIO, INFORMES, AJUSTES
	}

	public Controlador(MainFrame frame, JPanel contentArea) {
		this.frame = frame;
		this.contentArea = contentArea;
	}

	public void navegarA(Seccion seccion) {
		contentArea.removeAll();
		contentArea.setLayout(new BorderLayout());

		JPanel panel = obtenerPanel(seccion);
		contentArea.add(panel, BorderLayout.CENTER);

		contentArea.revalidate();
		contentArea.repaint();
	}

	private JPanel obtenerPanel(Seccion seccion) {
		switch (seccion) {
		case DASHBOARD:
			if (dashboardPanel == null)
				dashboardPanel = new DashboardPanel(this);
			return dashboardPanel;
		case CATALOGO:
			if (catalogoPanel == null)
				catalogoPanel = new PanelCatalogo(this);
			return catalogoPanel;
		default:
			// Para secciones aún no implementadas, mostrar placeholder
			return crearPlaceholder(seccion.name());
		}
	}

	private JPanel crearPlaceholder(String nombre) {
		JPanel p = new JPanel(new BorderLayout());
		p.setBackground(new Color(0xF5F5F5));
		JLabel lbl = new JLabel(nombre + " — próximamente", SwingConstants.CENTER);
		lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 16f));
		lbl.setForeground(new Color(0xAAAAAA));
		p.add(lbl, BorderLayout.CENTER);
		return p;
	}

	public MainFrame getFrame() {
		return frame;
	}
}
