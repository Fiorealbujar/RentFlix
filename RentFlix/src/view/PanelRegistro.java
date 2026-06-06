// PanelRegistro.java
package view;

import controller.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel de registro de nuevos clientes en RentFlix.
 * <p>
 * Formulario con nombre, apellido, email, usuario y contraseña. Crea la cuenta
 * con estado {@code activo} y redirige al login.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelRegistro extends JPanel {

	/** Color de fondo general del panel. */
	private static final Color COLOR_FONDO = new Color(0xF5F5F5);

	/** Color de acento rojo para mensajes de error y botón principal. */
	private static final Color COLOR_ACENTO = new Color(0xE50914);

	/** Campo de texto para el nombre del nuevo cliente. */
	private JTextField txtNombre;

	/** Campo de texto para el apellido del nuevo cliente. */
	private JTextField txtApellido;

	/** Campo de texto para el email del nuevo cliente. */
	private JTextField txtEmail;

	/** Campo de texto para el nombre de usuario del nuevo cliente. */
	private JTextField txtUsuario;

	/** Campo de contraseña para el nuevo cliente. */
	private JPasswordField txtContrasenia;

	/** Botón para enviar el formulario y crear la cuenta. */
	private JButton btnRegistrar;

	/** Botón para cancelar el registro y volver al catálogo. */
	private JButton btnCancelar;

	/** Etiqueta para mostrar mensajes de error de validación. */
	private JLabel lblError;

	/**
	 * Constructor que inicializa el panel y construye el formulario de registro.
	 */
	public PanelRegistro() {
		setBackground(COLOR_FONDO);
		setLayout(new GridBagLayout());
		initComponents();
	}

	/**
	 * Inicializa y construye la tarjeta central con el formulario de registro.
	 */

	private void initComponents() {
		JPanel tarjeta = new JPanel();
		tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
		tarjeta.setBackground(Color.WHITE);
		tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)),
				new EmptyBorder(36, 44, 36, 44)));
		tarjeta.setMaximumSize(new Dimension(400, 600));

		JLabel lblTitulo = new JLabel("Crear cuenta");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
		lblTitulo.setForeground(new Color(0x1a1a2e));
		lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSub = new JLabel("Únete a RentFlix y empieza a alquilar");
		lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
		lblSub.setForeground(new Color(0x888888));
		lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

		txtNombre = buildCampo("Nombre");
		txtApellido = buildCampo("Apellido");
		txtEmail = buildCampo("Email");
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

		btnRegistrar = buildBoton("Crear cuenta", COLOR_ACENTO, Color.WHITE);
		btnRegistrar.setActionCommand("REGISTRAR_CLIENTE");

		btnCancelar = buildBoton("Cancelar", new Color(0xEEEEEE), new Color(0x333333));
		btnCancelar.setActionCommand("CANCELAR_REGISTRO");

		tarjeta.add(lblTitulo);
		tarjeta.add(Box.createVerticalStrut(6));
		tarjeta.add(lblSub);
		tarjeta.add(Box.createVerticalStrut(24));
		tarjeta.add(buildLabel("Nombre"));
		tarjeta.add(Box.createVerticalStrut(4));
		tarjeta.add(txtNombre);
		tarjeta.add(Box.createVerticalStrut(14));
		tarjeta.add(buildLabel("Apellido"));
		tarjeta.add(Box.createVerticalStrut(4));
		tarjeta.add(txtApellido);
		tarjeta.add(Box.createVerticalStrut(14));
		tarjeta.add(buildLabel("Email"));
		tarjeta.add(Box.createVerticalStrut(4));
		tarjeta.add(txtEmail);
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
		tarjeta.add(btnRegistrar);
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
	 * Construye un botón con el estilo estándar del formulario de registro.
	 *
	 * @param texto texto del botón
	 * @param fondo color de fondo
	 * @param letra color del texto
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
	 * Limpia todos los campos del formulario.
	 */
	public void limpiar() {
		txtNombre.setText("");
		txtApellido.setText("");
		txtEmail.setText("");
		txtUsuario.setText("");
		txtContrasenia.setText("");
		lblError.setText(" ");
	}

	/**
	 * Valida los campos del formulario con las siguientes reglas: - Ningún campo
	 * puede estar vacío - El email debe contener '@' y '.' - El usuario no puede
	 * contener espacios - La contraseña debe tener al menos 4 caracteres Muestra el
	 * mensaje de error correspondiente si alguna validación falla.
	 *
	 * @return {@code true} si todos los datos son válidos
	 */
	public boolean datosValidos() {
		StringBuilder errores = new StringBuilder();

		if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()) {
			errores.append("· Nombre y apellido son obligatorios.\n");
		}

		String email = txtEmail.getText().trim();
		if (email.isEmpty()) {
			errores.append("· El email es obligatorio.\n");
		} else if (!email.contains("@") || !email.contains(".")) {
			errores.append("· El email no tiene un formato válido.\n");
		}

		String usuario = txtUsuario.getText().trim();
		if (usuario.isEmpty()) {
			errores.append("· El usuario es obligatorio.\n");
		} else if (usuario.contains(" ")) {
			errores.append("· El usuario no puede contener espacios.\n");
		}

		int longitudContrasenia = txtContrasenia.getPassword().length;
		if (longitudContrasenia == 0) {
			errores.append("· La contraseña es obligatoria.\n");
		} else if (longitudContrasenia < 4) {
			errores.append("· La contraseña debe tener al menos 4 caracteres.\n");
		}

		if (errores.length() > 0) {
			mostrarError("<html>" + errores.toString().replace("\n", "<br>") + "</html>");
			return false;
		}

		return true;
	}

	/**
	 * Registra el controlador como listener de los botones del formulario.
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	public void setControlador(Controlador controlador) {
		btnRegistrar.addActionListener(controlador);
		btnCancelar.addActionListener(controlador);
	}

	/**
	 * Devuelve el nombre introducido.
	 *
	 * @return nombre del nuevo cliente
	 */
	public String getNombre() {
		return txtNombre.getText().trim();
	}

	/**
	 * Devuelve el apellido introducido.
	 *
	 * @return apellido del nuevo cliente
	 */
	public String getApellido() {
		return txtApellido.getText().trim();
	}

	/**
	 * Devuelve el email introducido.
	 *
	 * @return email del nuevo cliente
	 */
	public String getEmail() {
		return txtEmail.getText().trim();
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
}