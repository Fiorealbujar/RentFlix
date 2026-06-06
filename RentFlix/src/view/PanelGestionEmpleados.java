// PanelGestionEmpleados.java
package view;

import controller.Controlador;
import model.Empleado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel de gestión de empleados, exclusivo del administrador.
 * <p>
 * Muestra la tabla de empleados con columnas ID, Nombre, Apellido, Email,
 * Usuario y Rol (Administrador/Empleado con color). Incluye un formulario
 * inferior para crear nuevos empleados. Los botones de editar y eliminar no se
 * habilitan para la fila del administrador activo.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelGestionEmpleados extends JPanel {

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_ACENTO = new Color(0xE50914);

	private DefaultTableModel modeloTabla;
	private JTable tblEmpleados;
	private JButton btnEditarEmpleado;
	private JButton btnEliminarEmpleado;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JTextField txtEmail;
	private JTextField txtUsuario;
	private JPasswordField txtContrasenia;
	private JButton btnCrearEmpleado;
	private JButton btnLimpiar;
	private JLabel lblMensaje;

	/**
	 * Constructor que inicializa el panel y construye sus componentes visuales.
	 */
	
	public PanelGestionEmpleados() {
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout(0, 12));
		setBorder(new EmptyBorder(20, 24, 20, 24));
		initComponents();
	}

	/**
	 * Inicializa y añade los componentes principales del panel.
	 */
	
	private void initComponents() {
		add(buildTitulo(), BorderLayout.NORTH);
		add(buildTabla(), BorderLayout.CENTER);
		add(buildFormulario(), BorderLayout.SOUTH);
	}

	/**
	 * Construye la etiqueta de título del panel.
	 *
	 * @return etiqueta de título configurada
	 */
	
	private JLabel buildTitulo() {
		JLabel lbl = new JLabel("👥 Gestión de empleados");
		lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
		lbl.setForeground(COLOR_DARK);
		lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
		return lbl;
	}

	/**
	 * Construye la tabla de empleados con sus columnas, renderers y botones de acción.
	 *
	 * @return panel con la tabla y los botones configurados
	 */
	
	private JPanel buildTabla() {
		JPanel panel = new JPanel(new BorderLayout(0, 8));
		panel.setOpaque(false);

		String[] columnas = { "ID", "Nombre", "Apellido", "Email", "Usuario", "Rol" };

		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblEmpleados = new JTable(modeloTabla);
		tblEmpleados.setRowHeight(36);
		tblEmpleados.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblEmpleados.setShowHorizontalLines(true);
		tblEmpleados.setGridColor(new Color(0xEEEEEE));
		tblEmpleados.setSelectionBackground(new Color(0xFFE0E0));
		tblEmpleados.setSelectionForeground(COLOR_DARK);

		tblEmpleados.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		tblEmpleados.getTableHeader().setBackground(COLOR_DARK);
		tblEmpleados.getTableHeader().setForeground(Color.WHITE);

		int[] anchos = { 40, 130, 130, 200, 130, 100 };
		for (int i = 0; i < anchos.length; i++) {
			tblEmpleados.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		}

		// Renderer colores Rol
		tblEmpleados.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
				lbl.setOpaque(true);
				if (!isSelected) {
					if ("Administrador".equals(value)) {
						lbl.setBackground(new Color(0xFFF9E6));
						lbl.setForeground(new Color(0xB8860B));
					} else {
						lbl.setBackground(new Color(0xE3F2FD));
						lbl.setForeground(new Color(0x1565C0));
					}
				}
				return lbl;
			}
		});

		// Botones bajo la tabla
		btnEditarEmpleado = new JButton("✏️  Editar empleado");
		btnEditarEmpleado.setActionCommand("EDITAR_EMPLEADO");
		btnEditarEmpleado.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditarEmpleado.setBackground(COLOR_DARK);
		btnEditarEmpleado.setForeground(Color.WHITE);
		btnEditarEmpleado.setFocusPainted(false);
		btnEditarEmpleado.setBorder(new EmptyBorder(8, 18, 8, 18));
		btnEditarEmpleado.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEditarEmpleado.setEnabled(false);

		btnEliminarEmpleado = new JButton("🗑️  Eliminar empleado");
		btnEliminarEmpleado.setActionCommand("ELIMINAR_EMPLEADO");
		btnEliminarEmpleado.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEliminarEmpleado.setBackground(COLOR_ACENTO);
		btnEliminarEmpleado.setForeground(Color.WHITE);
		btnEliminarEmpleado.setFocusPainted(false);
		btnEliminarEmpleado.setBorder(new EmptyBorder(8, 18, 8, 18));
		btnEliminarEmpleado.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEliminarEmpleado.setEnabled(false);

		JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		filaBotones.setOpaque(false);
		filaBotones.add(btnEditarEmpleado);
		filaBotones.add(btnEliminarEmpleado);

		JScrollPane scroll = new JScrollPane(tblEmpleados);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));

		panel.add(scroll, BorderLayout.CENTER);
		panel.add(filaBotones, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * Construye el formulario inferior para crear nuevos empleados.
	 *
	 * @return panel del formulario configurado
	 */
	
	private JPanel buildFormulario() {
		JPanel panel = new JPanel(new BorderLayout(0, 10));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)),
				new EmptyBorder(16, 20, 16, 20)));

		JLabel lblSub = new JLabel("➕ Crear nuevo empleado");
		lblSub.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblSub.setForeground(COLOR_DARK);

		JPanel campos = new JPanel(new GridLayout(1, 5, 10, 0));
		campos.setOpaque(false);

		txtNombre = buildCampo("Nombre *");
		txtApellido = buildCampo("Apellido *");
		txtEmail = buildCampo("Email *");
		txtUsuario = buildCampo("Usuario *");
		txtContrasenia = new JPasswordField();
		txtContrasenia.putClientProperty("JTextField.placeholderText", "Contraseña *");
		txtContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 13));

		campos.add(txtNombre);
		campos.add(txtApellido);
		campos.add(txtEmail);
		campos.add(txtUsuario);
		campos.add(txtContrasenia);

		JPanel sur = new JPanel(new BorderLayout(30, 0));
		sur.setOpaque(false);
		sur.setBorder(new EmptyBorder(10, 0, 0, 0));

		lblMensaje = new JLabel(" ");
		lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));

		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		botones.setOpaque(false);

		btnLimpiar = new JButton("🗑  Limpiar");
		btnLimpiar.setActionCommand("LIMPIAR_FORM_EMPLEADO");
		btnLimpiar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnLimpiar.setBackground(new Color(0xEEEEEE));
		btnLimpiar.setForeground(new Color(0x333333));
		btnLimpiar.setFocusPainted(false);
		btnLimpiar.setBorder(new EmptyBorder(8, 18, 8, 18));
		btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnCrearEmpleado = new JButton("👤  Crear empleado");
		btnCrearEmpleado.setActionCommand("CREAR_EMPLEADO");
		btnCrearEmpleado.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnCrearEmpleado.setBackground(COLOR_DARK);
		btnCrearEmpleado.setForeground(Color.WHITE);
		btnCrearEmpleado.setFocusPainted(false);
		btnCrearEmpleado.setBorder(new EmptyBorder(8, 18, 8, 18));
		btnCrearEmpleado.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		botones.add(btnLimpiar);
		botones.add(btnCrearEmpleado);

		sur.add(lblMensaje, BorderLayout.WEST);
		sur.add(botones, BorderLayout.EAST);

		panel.add(lblSub, BorderLayout.NORTH);
		panel.add(campos, BorderLayout.CENTER);
		panel.add(sur, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * Construye un campo de texto con placeholder para el formulario de creación.
	 *
	 * @param placeholder texto de ayuda que se muestra cuando el campo está vacío
	 * @return campo de texto configurado
	 */
	
	private JTextField buildCampo(String placeholder) {
		JTextField f = new JTextField();
		f.putClientProperty("JTextField.placeholderText", placeholder);
		f.setFont(new Font("SansSerif", Font.PLAIN, 13));
		return f;
	}

	public void cargarEmpleados(ArrayList<Empleado> empleados) {
		modeloTabla.setRowCount(0);
		for (Empleado e : empleados) {
			modeloTabla.addRow(new Object[] { e.getIdEmpleado(), e.getNombreEmpleado(), e.getApellidoEmpleado(),
					e.getEmailEmpleado(), e.getUsuarioEmpleado(), e.esAdministrador() ? "Administrador" : "Empleado" });
		}
		btnEditarEmpleado.setEnabled(false);
		btnEliminarEmpleado.setEnabled(false);
	}

	/**
	 * Habilita o deshabilita los botones de editar y eliminar según la fila
	 * seleccionada. Los botones permanecen deshabilitados si la fila corresponde
	 * al administrador.
	 */
	
	public void actualizarBotones() {
		int fila = tblEmpleados.getSelectedRow();
		if (fila >= 0) {
			String rol = String.valueOf(modeloTabla.getValueAt(fila, 5));
			// El admin no se puede editar ni eliminar
			boolean esAdmin = "Administrador".equals(rol);
			btnEditarEmpleado.setEnabled(!esAdmin);
			btnEliminarEmpleado.setEnabled(!esAdmin);
		} else {
			btnEditarEmpleado.setEnabled(false);
			btnEliminarEmpleado.setEnabled(false);
		}
	}

	/**
	 * Devuelve el id del empleado de la fila seleccionada.
	 *
	 * @return id del empleado, o {@code -1} si no hay selección
	 */
	
	public int getIdEmpleadoSeleccionado() {
		int fila = tblEmpleados.getSelectedRow();
		if (fila < 0) {
			return -1;
		}
		return (int) modeloTabla.getValueAt(fila, 0);
	}

	/**
	 * Devuelve un objeto {@link model.Empleado} con los datos de la fila
	 * seleccionada. La contraseña e {@code id_jefe} no se incluyen.
	 *
	 * @return empleado seleccionado, o {@code null} si no hay selección
	 */
	
	public Empleado getEmpleadoSeleccionado() {
		int fila = tblEmpleados.getSelectedRow();
		if (fila < 0) {
			return null;
		}
		return new Empleado((int) modeloTabla.getValueAt(fila, 0), (String) modeloTabla.getValueAt(fila, 1),
				(String) modeloTabla.getValueAt(fila, 2), (String) modeloTabla.getValueAt(fila, 3),
				(String) modeloTabla.getValueAt(fila, 4), "", // contraseña no se muestra en tabla
				null // id_jefe no se muestra en tabla
		);
	}

	/**
	 * Limpia todos los campos del formulario de creación de empleado.
	 */
	
	public void limpiar() {
		txtNombre.setText("");
		txtApellido.setText("");
		txtEmail.setText("");
		txtUsuario.setText("");
		txtContrasenia.setText("");
		lblMensaje.setText(" ");
	}

	/**
	 * Muestra un mensaje de resultado bajo el formulario de creación.
	 *
	 * @param msg     texto del mensaje
	 * @param esError {@code true} para rojo (error), {@code false} para verde (éxito)
	 */
	
	public void mostrarMensaje(String msg, boolean esError) {
		lblMensaje.setText(msg);
		lblMensaje.setForeground(esError ? COLOR_ACENTO : new Color(0x27AE60));
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
			mostrarMensaje(errores.toString().trim(), true);
			return false;
		}

		return true;
	}

	/**
	 * Registra el controlador como listener de los botones y la tabla.
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	
	public void setControlador(Controlador controlador) {
		btnCrearEmpleado.addActionListener(controlador);
		btnLimpiar.addActionListener(controlador);
		btnEditarEmpleado.addActionListener(controlador);
		btnEliminarEmpleado.addActionListener(controlador);

		tblEmpleados.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					actualizarBotones();
				}
			}
		});
	}

	/**
	 * Devuelve el nombre introducido en el formulario.
	 *
	 * @return nombre del nuevo empleado
	 */
	public String getNombre() {
		return txtNombre.getText().trim();
	}

	/**
	 * Devuelve el apellido introducido en el formulario.
	 *
	 * @return apellido del nuevo empleado
	 */
	public String getApellido() {
		return txtApellido.getText().trim();
	}
	
	/**
	 * Devuelve el email introducido en el formulario.
	 *
	 * @return email del nuevo empleado
	 */
	public String getEmail() {
		return txtEmail.getText().trim();
	}

	/**
	 * Devuelve el usuario introducido en el formulario.
	 *
	 * @return usuario del nuevo empleado
	 */
	public String getUsuario() {
		return txtUsuario.getText().trim();
	}

	/**
	 * Devuelve la contraseña introducida en el formulario.
	 *
	 * @return contraseña del nuevo empleado
	 */
	public String getContrasenia() {
		return new String(txtContrasenia.getPassword());
	}
}