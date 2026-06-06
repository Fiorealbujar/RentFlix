package view;

import controller.Controlador;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel de datos personales del cliente autenticado.
 * <p>
 * Muestra en modo lectura los datos del cliente. El botón "Modificar datos"
 * abre un diálogo para editar los campos y cambiar la contraseña.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelMiCuenta extends JPanel {

	/** Color de fondo general del panel. */
	private static final Color COLOR_FONDO = new Color(0xF5F5F5);

	/** Color oscuro utilizado en títulos y botones. */
	private static final Color COLOR_DARK = new Color(0x1a1a2e);

	/** Etiqueta que muestra el nombre del cliente en modo lectura. */
	private JLabel lblNombre;

	/** Etiqueta que muestra el apellido del cliente en modo lectura. */
	private JLabel lblApellido;

	/** Etiqueta que muestra el email del cliente en modo lectura. */
	private JLabel lblEmail;

	/** Etiqueta que muestra el nombre de usuario del cliente en modo lectura. */
	private JLabel lblUsuario;

	/** Botón para abrir el diálogo de edición de datos personales. */
	private JButton btnModificar;

	/**
	 * Constructor que inicializa el panel y construye la tarjeta de datos
	 * personales.
	 */

	public PanelMiCuenta() {
		setBackground(COLOR_FONDO);
		setLayout(new GridBagLayout());
		initComponents();
	}

	/**
	 * Inicializa y añade la tarjeta central de datos personales.
	 */

	private void initComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.6;
		gbc.insets = new Insets(40, 0, 40, 0);
		add(buildTarjeta(), gbc);
	}

	/**
	 * Construye la tarjeta con cabecera, datos y botón de modificar.
	 *
	 * @return tarjeta configurada
	 */

	private JPanel buildTarjeta() {
		JPanel tarjeta = new JPanel(new BorderLayout(0, 16));
		tarjeta.setBackground(Color.WHITE);
		tarjeta.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true), new EmptyBorder(30, 36, 30, 36)));

		tarjeta.add(buildCabecera(), BorderLayout.NORTH);
		tarjeta.add(buildDatos(), BorderLayout.CENTER);
		tarjeta.add(buildAcciones(), BorderLayout.SOUTH);
		return tarjeta;
	}

	/**
	 * Construye la cabecera de la tarjeta con el avatar y el subtítulo.
	 *
	 * @return panel de cabecera configurado
	 */

	private JPanel buildCabecera() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0, 0, 10, 0));

		JPanel avatar = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(COLOR_DARK);
				g2.fillOval(0, 0, getWidth(), getHeight());
			}
		};
		avatar.setPreferredSize(new Dimension(52, 52));
		avatar.setOpaque(false);

		JPanel textos = new JPanel();
		textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
		textos.setOpaque(false);

		JLabel lblTitulo = new JLabel("Mi cuenta");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_DARK);

		JLabel lblSub = new JLabel("Información personal");
		lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblSub.setForeground(new Color(0x888888));

		textos.add(lblTitulo);
		textos.add(lblSub);

		panel.add(avatar);
		panel.add(textos);
		return panel;
	}

	/**
	 * Construye el panel con las filas de datos personales del cliente.
	 *
	 * @return panel de datos configurado
	 */

	private JPanel buildDatos() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 0, 10));
		panel.setOpaque(false);

		lblNombre = new JLabel("-");
		lblApellido = new JLabel("-");
		lblEmail = new JLabel("-");
		lblUsuario = new JLabel("-");

		panel.add(buildFila("Nombre", lblNombre));
		panel.add(buildFila("Apellido", lblApellido));
		panel.add(buildFila("Email", lblEmail));
		panel.add(buildFila("Usuario", lblUsuario));
		panel.add(buildFila("Contraseña", new JLabel("••••••••")));

		return panel;
	}

	/**
	 * Construye una fila individual con etiqueta y valor.
	 *
	 * @param etiqueta texto descriptivo del campo
	 * @param lblValor etiqueta donde se mostrará el valor
	 * @return fila configurada
	 */

	private JPanel buildFila(String etiqueta, JLabel lblValor) {
		JPanel fila = new JPanel(new BorderLayout());
		fila.setBackground(new Color(0xF8F8F8));
		fila.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0xEEEEEE), 1, true),
				new EmptyBorder(10, 14, 10, 14)));

		JLabel lbl = new JLabel(etiqueta);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
		lbl.setForeground(new Color(0x666666));
		lbl.setPreferredSize(new Dimension(100, 0));

		lblValor.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblValor.setForeground(COLOR_DARK);

		fila.add(lbl, BorderLayout.WEST);
		fila.add(lblValor, BorderLayout.CENTER);
		return fila;
	}

	/**
	 * Construye el panel inferior con el botón "Modificar datos".
	 *
	 * @return panel de acciones configurado
	 */

	private JPanel buildAcciones() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(10, 0, 0, 0));

		btnModificar = new JButton("✏️  Modificar datos");
		btnModificar.setActionCommand("MODIFICAR_DATOS_CLIENTE");
		btnModificar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnModificar.setBackground(COLOR_DARK);
		btnModificar.setForeground(Color.WHITE);
		btnModificar.setFocusPainted(false);
		btnModificar.setBorder(new EmptyBorder(9, 20, 9, 20));
		btnModificar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		panel.add(btnModificar);
		return panel;
	}

	/**
	 * Muestra los datos del cliente autenticado en los campos de la tarjeta.
	 *
	 * @param cliente cliente cuya información se va a mostrar
	 */

	public void cargarDatos(Cliente cliente) {
		lblNombre.setText(cliente.getNombreCliente());
		lblApellido.setText(cliente.getApellidoCliente());
		lblEmail.setText(cliente.getEmailCliente());
		lblUsuario.setText(cliente.getNombreUsuario());
		revalidate();
		repaint();
	}

	/**
	 * Registra el controlador como listener del botón "Modificar datos".
	 *
	 * @param controlador controlador principal de la aplicación
	 */

	public void setControlador(Controlador controlador) {
		btnModificar.addActionListener(controlador);
	}

}