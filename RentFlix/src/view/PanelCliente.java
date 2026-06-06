package view;

import controller.Controlador;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel principal del cliente autenticado en RentFlix.
 * <p>
 * Contenedor de 3 pestañas: Catálogo, Mis alquileres y Mi cuenta. Muestra en la
 * cabecera el nombre del cliente, su badge de rol y un indicador con el número
 * de alquileres activos en curso, que se actualiza al iniciar sesión, al
 * registrar un alquiler y al solicitar una devolución.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelCliente extends JPanel {

	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_ACTIVO = new Color(0x27AE60);

	private JLabel lblBienvenida;
	private JLabel lblContadorActivos;
	private JTabbedPane tabbedPane;

	private PanelCatalogo panelCatalogo;
	private PanelMisAlquileres panelMisAlquileres;
	private PanelMiCuenta panelMiCuenta;

	/**
	 * Constructor que recibe los subpaneles ya instanciados y los asigna
	 * a las pestañas correspondientes.
	 *
	 * @param panelCatalogo      panel del catálogo de películas
	 * @param panelMisAlquileres panel del historial de alquileres del cliente
	 * @param panelMiCuenta      panel de datos personales del cliente
	 */
	
	public PanelCliente(PanelCatalogo panelCatalogo, PanelMisAlquileres panelMisAlquileres,
			PanelMiCuenta panelMiCuenta) {
		this.panelCatalogo = panelCatalogo;
		this.panelMisAlquileres = panelMisAlquileres;
		this.panelMiCuenta = panelMiCuenta;
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout());
		initComponents();
	}

	/**
	 * Inicializa y añade los componentes principales del panel.
	 */
	
	private void initComponents() {
		add(buildHeader(), BorderLayout.NORTH);
		add(buildTabs(), BorderLayout.CENTER);
	}

	/**
	 * Construye la cabecera con el saludo, el badge de cliente y el contador de alquileres activos.
	 *
	 * @return panel de cabecera configurado
	 */
	
	private JPanel buildHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(COLOR_DARK);
		header.setBorder(new EmptyBorder(10, 20, 10, 20));

		JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		izquierda.setOpaque(false);

		lblBienvenida = new JLabel("👋 Hola, Cliente");
		lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblBienvenida.setForeground(Color.WHITE);

		JLabel badge = new JLabel("CLIENTE");
		badge.setFont(new Font("SansSerif", Font.BOLD, 10));
		badge.setForeground(COLOR_DARK);
		badge.setBackground(new Color(0x5DADE2));
		badge.setOpaque(true);
		badge.setBorder(new EmptyBorder(3, 8, 3, 8));

		izquierda.add(lblBienvenida);
		izquierda.add(badge);

		lblContadorActivos = new JLabel("");
		lblContadorActivos.setFont(new Font("SansSerif", Font.BOLD, 12));
		lblContadorActivos.setForeground(COLOR_ACTIVO);
		lblContadorActivos.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(COLOR_ACTIVO, 1, true), new EmptyBorder(4, 10, 4, 10)));
		lblContadorActivos.setVisible(false);

		header.add(izquierda, BorderLayout.WEST);
		header.add(lblContadorActivos, BorderLayout.EAST);
		return header;
	}

	/**
	 * Construye el contenedor de pestañas con los subpaneles del cliente.
	 *
	 * @return panel de pestañas configurado
	 */
	
	private JTabbedPane buildTabs() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
		tabbedPane.addTab("🎬  Catálogo", panelCatalogo);
		tabbedPane.addTab("📋  Mis alquileres", panelMisAlquileres);
		tabbedPane.addTab("👤  Mi cuenta", panelMiCuenta);
		return tabbedPane;
	}

	/**
	 * Actualiza el texto de bienvenida con el nombre completo del cliente.
	 *
	 * @param cliente cliente en sesión
	 */
	public void setBienvenida(Cliente cliente) {
		lblBienvenida.setText("👋 Hola, " + cliente.getNombreCompleto());
	}

	/**
	 * Actualiza el indicador de alquileres activos en la cabecera. Lo oculta si la
	 * cantidad es 0.
	 *
	 * @param cantidad número de alquileres en estado activo
	 */
	public void actualizarContadorActivos(int cantidad) {
		if (cantidad > 0) {
			lblContadorActivos.setText("🎬 " + cantidad + (cantidad == 1 ? " alquiler activo" : " alquileres activos"));
			lblContadorActivos.setVisible(true);
		} else {
			lblContadorActivos.setVisible(false);
		}
	}

	/**
	 * Navega a la pestaña "Mis alquileres".
	 */
	public void irAMisAlquileres() {
		tabbedPane.setSelectedIndex(1);
	}

	/**
	 * Registra el controlador (sin listeners adicionales en este panel).
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	public void setControlador(Controlador controlador) {
	}
}