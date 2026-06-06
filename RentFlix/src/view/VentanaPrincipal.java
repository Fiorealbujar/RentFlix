package view;

import controller.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana principal de la aplicación RentFlix.
 * <p>
 * Contenedor raíz de todos los paneles. Gestiona la barra superior con logo y
 * botones de navegación cuya visibilidad cambia según el estado de sesión.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class VentanaPrincipal extends JFrame {

	private JPanel panelContenido;
	private JButton btnLogin;
	private JButton btnRegistro;
	private JButton btnCerrarSesion;

	/**
	 * Constructor que configura la ventana y construye la interfaz principal.
	 */
	
	public VentanaPrincipal() {
		super("RentFlix 🎬");
		configurarVentana();
		construirUI();
	}

	/**
	 * Configura las propiedades básicas de la ventana: tamaño, cierre y posición.
	 */
	
	private void configurarVentana() {
	    int ancho = 1100;
	    int alto = 680;
	    setSize(ancho, alto);
	    setMinimumSize(new Dimension(900, 580));
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);
	    setLayout(new BorderLayout());
	}

	/**
	 * Construye la interfaz añadiendo la barra superior y el panel de contenido.
	 */
	
	private void construirUI() {
		add(buildTopBar(), BorderLayout.NORTH);
		panelContenido = new JPanel(new BorderLayout());
		add(panelContenido, BorderLayout.CENTER);
	}

	/**
	 * Construye la barra superior con el logo y los botones de navegación.
	 *
	 * @return panel de la barra superior configurado
	 */
	
	private JPanel buildTopBar() {
		JPanel topBar = new JPanel(new BorderLayout());
		topBar.setBackground(new Color(0x1a1a2e));
		topBar.setBorder(new EmptyBorder(10, 20, 10, 20));

		JLabel lblLogo = new JLabel("🎬 RentFlix");
		lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
		lblLogo.setForeground(new Color(0xE50914));

		JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panelBotones.setOpaque(false);

		// Botones, todos en rojo, visibilidad gestionada por el controlador
		btnRegistro = buildBoton("Registrarse", "ABRIR_REGISTRO");
		btnLogin = buildBoton("Iniciar sesión", "ABRIR_LOGIN");
		btnCerrarSesion = buildBoton("Cerrar sesión", "CERRAR_SESION");
		btnCerrarSesion.setVisible(false);

		panelBotones.add(btnRegistro);
		panelBotones.add(btnLogin);
		panelBotones.add(btnCerrarSesion);

		topBar.add(lblLogo, BorderLayout.WEST);
		topBar.add(panelBotones, BorderLayout.EAST);
		return topBar;
	}

	/**
	 * Construye un botón con el estilo estándar de la barra de navegación.
	 *
	 * @param texto         texto del botón
	 * @param actionCommand comando de acción asociado al botón
	 * @return botón configurado
	 */
	
	private JButton buildBoton(String texto, String actionCommand) {
		JButton btn = new JButton(texto);
		btn.setActionCommand(actionCommand);
		btn.setFont(new Font("SansSerif", Font.BOLD, 13));
		btn.setForeground(Color.WHITE);
		btn.setBackground(new Color(0xE50914));
		btn.setBorder(new EmptyBorder(7, 16, 7, 16));
		btn.setFocusPainted(false);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return btn;
	}

	/**
	 * Sustituye el contenido central de la ventana por el panel indicado.
	 *
	 * @param panel nuevo panel a mostrar en el área de contenido
	 */
	
	public void cargarPanel(JPanel panel) {
		panelContenido.removeAll();
		panelContenido.add(panel, BorderLayout.CENTER);
		panelContenido.revalidate();
		panelContenido.repaint();
	}

	/**
	 * Configura la barra de navegación para el modo invitado: muestra los botones
	 * de login y registro, y oculta el de cerrar sesión.
	 */
	
	public void modoInvitado() {
		btnLogin.setVisible(true);
		btnRegistro.setVisible(true);
		btnCerrarSesion.setVisible(false);
	}

	/**
	 * Configura la barra de navegación para el modo con sesión activa: oculta los
	 * botones de login y registro, y muestra el de cerrar sesión.
	 */
	
	public void modoSesionActiva() {
		btnLogin.setVisible(false);
		btnRegistro.setVisible(false);
		btnCerrarSesion.setVisible(true);
	}

	/**
	 * Hace visible la ventana principal en el hilo de despacho de eventos de Swing.
	 */
	
	public void hacerVisible() {
		setVisible(true);
	}

	/**
	 * Registra el controlador como listener de los botones de la barra de navegación.
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	
	public void setControlador(Controlador controlador) {
		btnLogin.addActionListener(controlador);
		btnRegistro.addActionListener(controlador);
		btnCerrarSesion.addActionListener(controlador);
	}

}