// ==========================================
// CLASE: VentanaPrincipal.java — CORREGIDA
// Un solo botón rojo que cambia de texto
// según el estado de la sesión.
// ==========================================
package view;

import controller.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

	public static final int ANCHO = 1100;
	public static final int ALTO = 680;

	private JPanel panelContenido;
	private JButton btnLogin;
	private JButton btnRegistro;
	private JButton btnCerrarSesion;

	public VentanaPrincipal() {
		super("RentFlix 🎬");
		configurarVentana();
		construirUI();
	}

	private void configurarVentana() {
		setSize(ANCHO, ALTO);
		setMinimumSize(new Dimension(900, 580));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
	}

	private void construirUI() {
		add(buildTopBar(), BorderLayout.NORTH);
		panelContenido = new JPanel(new BorderLayout());
		add(panelContenido, BorderLayout.CENTER);
	}

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

	// ── Métodos para el controlador ─────────────────────────────────────────

	public void cargarPanel(JPanel panel) {
		panelContenido.removeAll();
		panelContenido.add(panel, BorderLayout.CENTER);
		panelContenido.revalidate();
		panelContenido.repaint();
	}

	public void modoInvitado() {
		btnLogin.setVisible(true);
		btnRegistro.setVisible(true);
		btnCerrarSesion.setVisible(false);
	}

	public void modoSesionActiva() {
		btnLogin.setVisible(false);
		btnRegistro.setVisible(false);
		btnCerrarSesion.setVisible(true);
	}

	public void hacerVisible() {
		setVisible(true);
	}

	public void setControlador(Controlador controlador) {
		btnLogin.addActionListener(controlador);
		btnRegistro.addActionListener(controlador);
		btnCerrarSesion.addActionListener(controlador);
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public JButton getBtnRegistro() {
		return btnRegistro;
	}

	public JButton getBtnCerrarSesion() {
		return btnCerrarSesion;
	}
}