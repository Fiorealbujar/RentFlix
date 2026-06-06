package view;

import controller.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel de alta de nuevas películas en el catálogo.
 * <p>
 * Formulario con título, director, duración, género, clasificación de edad,
 * formato y número de copias (spinner 1-10). Al guardar, el controlador crea la
 * película y las N copias con precio automático según el formato.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelAnadirPelicula extends JPanel {

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_ACENTO = new Color(0xE50914);
	private static final Color COLOR_ACTIVO = new Color(0x27AE60);

	private JTextField txtTitulo;
	private JTextField txtDirector;
	private JTextField txtDuracion;
	private JComboBox<String> cmbGenero;
	private JComboBox<String> cmbClasificacion;
	private JComboBox<String> cmbFormato;
	private JSpinner spinnerCopias;
	private JTextArea txtSinopsis;
	private JButton btnGuardar;
	private JButton btnLimpiar;
	private JLabel lblMensaje;

	/**
	 * Constructor que inicializa el panel y construye el formulario de alta.
	 */
	
	public PanelAnadirPelicula() {
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 24, 20, 24));
		initComponents();
	}

	/**
	 * Inicializa y añade los componentes principales del panel.
	 */
	
	private void initComponents() {
		add(buildTitulo(), BorderLayout.NORTH);
		add(buildFormulario(), BorderLayout.CENTER);
		add(buildAcciones(), BorderLayout.SOUTH);
	}

	/**
	 * Construye el panel con el título del formulario.
	 *
	 * @return panel de título configurado
	 */
	
	private JPanel buildTitulo() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0, 0, 16, 0));

		JLabel lbl = new JLabel("➕ Añadir nueva película");
		lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
		lbl.setForeground(COLOR_DARK);
		panel.add(lbl);
		return panel;
	}

	/**
	 * Construye el formulario con todos los campos de entrada de datos.
	 *
	 * @return panel del formulario configurado
	 */
	
	private JPanel buildFormulario() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)),
				new EmptyBorder(24, 28, 24, 28)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 6, 6, 6);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		txtTitulo = buildTextField();
		txtDirector = buildTextField();
		txtDuracion = buildTextField();

		cmbGenero = new JComboBox<>(new String[] { "Acción", "Aventura", "Animación", "Ciencia Ficción", "Comedia",
				"Drama", "Fantasía", "Musical", "Romance", "Suspense", "Terror", "Thriller" });
		cmbGenero.setFont(new Font("SansSerif", Font.PLAIN, 13));

		cmbClasificacion = new JComboBox<>(new String[] { "TP", "7", "12", "16", "18" });
		cmbClasificacion.setFont(new Font("SansSerif", Font.PLAIN, 13));

		cmbFormato = new JComboBox<>(new String[] { "DVD", "Blu-ray", "4K Ultra HD" });
		cmbFormato.setFont(new Font("SansSerif", Font.PLAIN, 13));

		// Spinner para numero de copias: minimo 1, maximo 10, valor inicial 1
		spinnerCopias = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
		spinnerCopias.setFont(new Font("SansSerif", Font.PLAIN, 13));

		txtSinopsis = new JTextArea(4, 20);
		txtSinopsis.setFont(new Font("SansSerif", Font.PLAIN, 13));
		txtSinopsis.setLineWrap(true);
		txtSinopsis.setWrapStyleWord(true);
		JScrollPane scrollSinopsis = new JScrollPane(txtSinopsis);

		// Fila 0
		agregarFila(panel, gbc, 0, "Título *", txtTitulo, "Director *", txtDirector);
		// Fila 1
		agregarFila(panel, gbc, 1, "Duración (min) *", txtDuracion, "Género *", cmbGenero);
		// Fila 2
		agregarFila(panel, gbc, 2, "Clasificación edad *", cmbClasificacion, "Formato copia *", cmbFormato);
		// Fila 3
		agregarFila(panel, gbc, 3, "Nº de copias *", spinnerCopias, "", new JLabel());
		// Fila 4: sinopsis ocupa todo el ancho
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		panel.add(buildLabel("Sinopsis"), gbc);
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		panel.add(scrollSinopsis, gbc);
		gbc.gridwidth = 1;

		return panel;
	}

	/**
	 * Añade una fila de dos campos con sus etiquetas al panel del formulario.
	 *
	 * @param panel  panel destino
	 * @param gbc    restricciones de layout
	 * @param fila   índice de fila en el GridBagLayout
	 * @param label1 etiqueta del primer campo
	 * @param comp1  primer componente de entrada
	 * @param label2 etiqueta del segundo campo
	 * @param comp2  segundo componente de entrada
	 */
	
	private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String label1, JComponent comp1,
			String label2, JComponent comp2) {
		gbc.gridy = fila;

		gbc.weightx = 0;
		gbc.gridx = 0;
		panel.add(buildLabel(label1), gbc);

		gbc.weightx = 1;
		gbc.gridx = 1;
		panel.add(comp1, gbc);

		gbc.weightx = 0;
		gbc.gridx = 2;
		panel.add(buildLabel(label2), gbc);

		gbc.weightx = 1;
		gbc.gridx = 3;
		panel.add(comp2, gbc);
	}

	/**
	 * Construye un campo de texto estándar con la fuente del formulario.
	 *
	 * @return campo de texto configurado
	 */
	
	private JTextField buildTextField() {
		JTextField campo = new JTextField();
		campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
		campo.setPreferredSize(new Dimension(200, 32));
		return campo;
	}

	/**
	 * Construye una etiqueta con el estilo de los campos obligatorios.
	 *
	 * @param texto texto de la etiqueta
	 * @return etiqueta configurada
	 */
	
	private JLabel buildLabel(String texto) {
		JLabel lbl = new JLabel(texto);
		lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
		lbl.setForeground(new Color(0x444444));
		return lbl;
	}

	/**
	 * Construye el panel inferior con los botones de guardar y limpiar.
	 *
	 * @return panel de acciones configurado
	 */
	
	private JPanel buildAcciones() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(14, 0, 0, 0));

		lblMensaje = new JLabel(" ");
		lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));

		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		botones.setOpaque(false);

		btnLimpiar = new JButton("🗑  Limpiar");
		btnLimpiar.setActionCommand("LIMPIAR_FORM_PELICULA");
		btnLimpiar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnLimpiar.setBackground(new Color(0xEEEEEE));
		btnLimpiar.setForeground(new Color(0x333333));
		btnLimpiar.setFocusPainted(false);
		btnLimpiar.setBorder(new EmptyBorder(9, 20, 9, 20));
		btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		btnGuardar = new JButton("💾  Guardar película");
		btnGuardar.setActionCommand("GUARDAR_PELICULA");
		btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnGuardar.setBackground(COLOR_DARK);
		btnGuardar.setForeground(Color.WHITE);
		btnGuardar.setFocusPainted(false);
		btnGuardar.setBorder(new EmptyBorder(9, 20, 9, 20));
		btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		botones.add(btnLimpiar);
		botones.add(btnGuardar);

		panel.add(lblMensaje, BorderLayout.WEST);
		panel.add(botones, BorderLayout.EAST);
		return panel;
	}

	/**
	 * Limpia todos los campos del formulario y resetea los combos y el spinner.
	 */
	public void limpiar() {
		txtTitulo.setText("");
		txtDirector.setText("");
		txtDuracion.setText("");
		txtSinopsis.setText("");
		cmbGenero.setSelectedIndex(0);
		cmbClasificacion.setSelectedIndex(0);
		cmbFormato.setSelectedIndex(0);
		spinnerCopias.setValue(1);
		lblMensaje.setText(" ");
		lblMensaje.setForeground(Color.BLACK);
	}

	/**
	 * Muestra un mensaje de resultado bajo el formulario.
	 *
	 * @param mensaje texto del mensaje
	 * @param esError {@code true} para rojo (error), {@code false} para verde
	 *                (éxito)
	 */
	public void mostrarMensaje(String mensaje, boolean esError) {
		lblMensaje.setText(mensaje);
		lblMensaje.setForeground(esError ? COLOR_ACENTO : COLOR_ACTIVO);
	}

	/**
	 * Comprueba que los campos obligatorios estén rellenos y la duración sea un
	 * entero válido.
	 *
	 * @return {@code true} si los datos son válidos
	 */
	public boolean datosValidos() {
		if (txtTitulo.getText().trim().isEmpty() || txtDirector.getText().trim().isEmpty()
				|| txtDuracion.getText().trim().isEmpty()) {
			mostrarMensaje("Rellena todos los campos obligatorios (*).", true);
			return false;
		}
		try {
			int duracion = Integer.parseInt(txtDuracion.getText().trim());
			if (duracion <= 0 || duracion > 600) {
				mostrarMensaje("La duración debe estar entre 1 y 600 minutos.", true);
				return false;
			}
		} catch (NumberFormatException e) {
			mostrarMensaje("La duración debe ser un número entero.", true);
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
		btnGuardar.addActionListener(controlador);
		btnLimpiar.addActionListener(controlador);
	}

	/**
	 * Devuelve el título introducido.
	 *
	 * @return título de la película
	 */
	public String getTitulo() {
		return txtTitulo.getText().trim();
	}

	/**
	 * Devuelve el director introducido.
	 *
	 * @return nombre del director
	 */
	public String getDirector() {
		return txtDirector.getText().trim();
	}

	/**
	 * Devuelve la duración introducida en minutos.
	 *
	 * @return duración en minutos
	 */
	public int getDuracion() {
		return Integer.parseInt(txtDuracion.getText().trim());
	}

	/**
	 * Devuelve el género seleccionado.
	 *
	 * @return género cinematográfico
	 */
	public String getGenero() {
		return (String) cmbGenero.getSelectedItem();
	}

	/**
	 * Devuelve la sinopsis introducida.
	 *
	 * @return sinopsis de la película
	 */
	public String getSinopsis() {
		return txtSinopsis.getText().trim();
	}

	/**
	 * Devuelve la clasificación de edad seleccionada.
	 *
	 * @return TP, 7, 12, 16 o 18
	 */
	public String getClasificacion() {
		return (String) cmbClasificacion.getSelectedItem();
	}

	/**
	 * Devuelve el formato de copia seleccionado.
	 *
	 * @return DVD, Blu-ray o 4K Ultra HD
	 */
	public String getFormato() {
		return (String) cmbFormato.getSelectedItem();
	}

	/**
	 * Devuelve el número de copias indicado en el spinner.
	 *
	 * @return número de copias (entre 1 y 10)
	 */
	public int getNumCopias() {
		return (int) spinnerCopias.getValue();
	}

}