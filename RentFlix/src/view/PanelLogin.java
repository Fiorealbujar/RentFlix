package view;

import controller.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel de inicio de sesión de RentFlix.
 * <p>
 * Formulario con selector de tipo de usuario (combo "Cliente" o "Empleado /
 * Administrador"), campo de usuario, contraseña y botones.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelLogin extends JPanel {

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_ACENTO = new Color(0xE50914);

	private JTextField txtUsuario;
	private JPasswordField txtContrasenia;
	private JComboBox<String> cmbRol;
	private JButton btnEntrar;
	private JButton btnCancelar;
	private JLabel lblError;

	/**
	 * Constructor que inicializa el panel y construye el formulario de login.
	 */
	
	public PanelLogin() {
		setBackground(COLOR_FONDO);
		setLayout(new GridBagLayout());
		initComponents();
	}

	/**
	 * Inicializa y construye la tarjeta central con el formulario de login.
	 */
	
	private void initComponents() {
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Color.WHITE);
		tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)),
				new EmptyBorder(36, 44, 36, 44)));
		tarjeta.setMaximumSize(new Dimension(400, 500));

		JLabel lblTitulo = new JLabel("Iniciar sesión");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblTitulo.setForeground(new Color(0x1a1a2e));
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSub = new JLabel("Bienvenido de nuevo a RentFlix");
		lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblSub.setForeground(new Color(0x888888));
		lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

		cmbRol = new JComboBox<>(new String[] { "Cliente", "Empleado / Administrador" });
		cmbRol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		cmbRol.setFont(new Font("SansSerif", Font.PLAIN, 13));
		cmbRol.setAlignmentX(Component.CENTER_ALIGNMENT);

		txtUsuario = buildCampo("Usuario");
		txtContrasenia = new JPasswordField();
		txtContrasenia.putClientProperty("JTextField.placeholderText", "Contraseña");
		txtContrasenia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		txtContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 13));
		txtContrasenia.setAlignmentX(Component.CENTER_ALIGNMENT);

		lblError = new JLabel(" ");
		lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblError.setForeground(COLOR_ACENTO);
		lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

		btnEntrar = buildBoton("Entrar", COLOR_ACENTO, Color.WHITE);
		btnEntrar.setActionCommand("LOGIN");

		btnCancelar = buildBoton("Cancelar", new Color(0xEEEEEE), new Color(0x333333));
		btnCancelar.setActionCommand("CANCELAR_LOGIN");

		tarjeta.add(lblTitulo);
		tarjeta.add(Box.createVerticalStrut(6));
		tarjeta.add(lblSub);
		tarjeta.add(Box.createVerticalStrut(24));
		tarjeta.add(buildLabel("Tipo de usuario"));
		tarjeta.add(Box.createVerticalStrut(4));
		tarjeta.add(cmbRol);
		tarjeta.add(Box.createVerticalStrut(14));
		tarjeta.add(buildLabel("Usuario"));
		tarjeta.add(Box.createVerticalStrut(4));
		tarjeta.add(txtUsuario);
		tarjeta.add(Box.createVerticalStrut(14));
		tarjeta.add(buildLabel("Contraseña"));
		tarjeta.add(Box.createVerticalStrut(4));
		tarjeta.add(txtContrasenia);
		tarjeta.add(Box.createVerticalStrut(8));
		tarjeta.add(lblError);
		tarjeta.add(Box.createVerticalStrut(20));
		tarjeta.add(btnEntrar);
		tarjeta.add(Box.createVerticalStrut(8));
		tarjeta.add(btnCancelar);

		add(tarjeta);
	}

	/**
	 * Construye un campo de texto con placeholder para el formulario.
	 *
	 * @param placeholder texto de ayuda que se muestra cuando el campo está vacío
	 * @return campo de texto configurado
	 */
	
	private JTextField buildCampo(String placeholder) {
		JTextField campo = new JTextField();
		campo.putClientProperty("JTextField.placeholderText", placeholder);
		campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
		campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		campo.setAlignmentX(Component.CENTER_ALIGNMENT);
		return campo;
	}

	/**
	 * Construye una etiqueta con el estilo del formulario.
	 *
	 * @param texto texto de la etiqueta
	 * @return etiqueta configurada
	 */
	
	private JLabel buildLabel(String texto) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
		lbl.setForeground(new Color(0x444444));
		lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		return lbl;
	}

	/**
	 * Construye un botón con el estilo estándar del formulario de login.
	 *
	 * @param texto  texto del botón
	 * @param fondo  color de fondo
	 * @param letra  color del texto
	 * @return botón configurado
	 */
	
	private JButton buildBoton(String texto, Color fondo, Color letra) {
		JButton btn = new JButton(texto);
		btn.setFont(new Font("SansSerif", Font.BOLD, 14));
		btn.setBackground(fondo);
		btn.setForeground(letra);
		btn.setFocusPainted(false);
		btn.setBorder(new EmptyBorder(10, 0, 10, 0));
		btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
		btn.setAlignmentX(Component.CENTER_ALIGNMENT);
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return btn;
	}

	/**
	 * Muestra un mensaje de error bajo el formulario.
	 *
	 * @param mensaje texto del error a mostrar
	 */
	
	public void mostrarError(String mensaje) {
		lblError.setText(mensaje);
	}

	/**
	 * Limpia los campos de usuario y contraseña y resetea el combo de rol.
	 */
	
	public void limpiar() {
		txtUsuario.setText("");
		txtContrasenia.setText("");
		lblError.setText(" ");
		cmbRol.setSelectedIndex(0);
	}

	/**
	 * Indica si el tipo de usuario seleccionado en el combo es empleado o administrador.
	 *
	 * @return {@code true} si se seleccionó "Empleado / Administrador"
	 */
	
	public boolean esRolEmpleado() {
		return cmbRol.getSelectedIndex() == 1;
	}

	/**
	 * Devuelve el nombre de usuario introducido.
	 *
	 * @return nombre de usuario
	 */
	
	public String getUsuario() {
		return txtUsuario.getText().trim();
	}

	/**
	 * Devuelve la contraseña introducida.
	 *
	 * @return contraseña en texto plano
	 */
	
	public String getContrasenia() {
		return new String(txtContrasenia.getPassword());
	}

	/**
	 * Registra el controlador como listener de los botones del formulario.
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	
	public void setControlador(Controlador controlador) {
		btnEntrar.addActionListener(controlador);
		btnCancelar.addActionListener(controlador);
	}
}