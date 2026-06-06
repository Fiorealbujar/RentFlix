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

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_DARK = new Color(0x1a1a2e);

	private JLabel lblNombre;
	private JLabel lblApellido;
	private JLabel lblEmail;
	private JLabel lblUsuario;

	private JButton btnModificar;

	public PanelMiCuenta() {
		setBackground(COLOR_FONDO);
		setLayout(new GridBagLayout());
		initComponents();
	}

	private void initComponents() {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.6;
		gbc.insets = new Insets(40, 0, 40, 0);
		add(buildTarjeta(), gbc);
	}

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

	// ── Cabecera ──────────────────────────────────────────────────────────────

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

	// ── Datos ─────────────────────────────────────────────────────────────────

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

	// ── Acciones ──────────────────────────────────────────────────────────────

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

	// ── Métodos para el Controlador ───────────────────────────────────────────

	public void cargarDatos(Cliente cliente) {
		lblNombre.setText(cliente.getNombreCliente());
		lblApellido.setText(cliente.getApellidoCliente());
		lblEmail.setText(cliente.getEmailCliente());
		lblUsuario.setText(cliente.getNombreUsuario());
		revalidate();
		repaint();
	}

	public void setControlador(Controlador controlador) {
		btnModificar.addActionListener(controlador);
	}

}