package view;

import controller.Controlador;
import model.Empleado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel principal del empleado de RentFlix.
 * <p>
 * Contenedor de pestañas con acceso a los módulos operativos del empleado:
 * alquileres, alta de películas, gestión de películas, informes y clientes.
 * Muestra en la cabecera el nombre del empleado con badge amarillo.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelEmpleado extends JPanel {

	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_GOLD = new Color(0xF0C040);
	private static final Color COLOR_FONDO = new Color(0xF5F5F5);

	private JLabel lblBienvenida;
	private JTabbedPane tabbedPane;

	private PanelGestionAlquileres panelGestionAlquileres;
	private PanelAnadirPelicula panelAnadirPelicula;
	private PanelGestionPeliculas panelGestionPeliculas;
	private PanelInformes panelInformes;
	private PanelGestionClientes panelGestionClientes;

	/**
	 * Constructor que recibe los subpaneles ya instanciados y los asigna
	 * a las pestañas correspondientes.
	 *
	 * @param panelGestionAlquileres panel de gestión de alquileres
	 * @param panelAnadirPelicula    panel de alta de películas
	 * @param panelGestionPeliculas  panel de gestión de películas
	 * @param panelInformes          panel de informes de ventas
	 * @param panelGestionClientes   panel de gestión de clientes
	 */
	
	public PanelEmpleado(PanelGestionAlquileres panelGestionAlquileres, PanelAnadirPelicula panelAnadirPelicula,
			PanelGestionPeliculas panelGestionPeliculas, PanelInformes panelInformes,
			PanelGestionClientes panelGestionClientes) {
		this.panelGestionAlquileres = panelGestionAlquileres;
		this.panelAnadirPelicula = panelAnadirPelicula;
		this.panelGestionPeliculas = panelGestionPeliculas;
		this.panelInformes = panelInformes;
		this.panelGestionClientes = panelGestionClientes;

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
	 * Construye la cabecera con el saludo de bienvenida y el badge de empleado.
	 *
	 * @return panel de cabecera configurado
	 */
	
	private JPanel buildHeader() {
		JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
		header.setBackground(COLOR_DARK);

		lblBienvenida = new JLabel("👋 Hola, Empleado");
		lblBienvenida.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblBienvenida.setForeground(Color.WHITE);

		JLabel badge = new JLabel("EMPLEADO");
		badge.setFont(new Font("SansSerif", Font.BOLD, 10));
		badge.setForeground(COLOR_DARK);
		badge.setBackground(COLOR_GOLD);
		badge.setOpaque(true);
		badge.setBorder(new EmptyBorder(3, 8, 3, 8));

		header.add(lblBienvenida);
		header.add(badge);
		return header;
	}

	/**
	 * Construye el contenedor de pestañas con todos los subpaneles del empleado.
	 *
	 * @return panel de pestañas configurado
	 */
	
	private JTabbedPane buildTabs() {
		tabbedPane = new JTabbedPane();
		tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 13));
		tabbedPane.addTab("📋  Alquileres", panelGestionAlquileres);
		tabbedPane.addTab("➕  Añadir película", panelAnadirPelicula);
		tabbedPane.addTab("🎞️  Gestión películas", panelGestionPeliculas);
		tabbedPane.addTab("📊  Informes", panelInformes);
		tabbedPane.addTab("💼  Clientes", panelGestionClientes);
		return tabbedPane;
	}

	/**
	 * Actualiza el texto de bienvenida con el nombre completo del empleado.
	 *
	 * @param emp empleado en sesión
	 */
	
	public void setBienvenida(Empleado emp) {
		lblBienvenida.setText("👋 Hola, " + emp.getNombreCompleto());
	}
	
	/**
	 * Requerido por el patrón de inicialización de la aplicación.
	 * <p>
	 * Este panel es un contenedor de pestañas sin botones propios, por lo que
	 * no registra listeners directamente. Los listeners de cada subpanel
	 * se registran en sus propios {@code setControlador()}.
	 * </p>
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	public void setControlador(Controlador controlador) {
	}
}