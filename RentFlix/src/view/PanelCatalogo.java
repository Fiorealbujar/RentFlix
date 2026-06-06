package view;

import controller.Controlador;
import model.Copia;
import model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel del catálogo de películas disponibles para alquilar.
 * <p>
 * Muestra las copias físicas disponibles con búsqueda por título y filtro por
 * formato. Disponible en modo invitado y para el cliente autenticado.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelCatalogo extends JPanel {

	/** Color de fondo general del panel. */
	private static final Color COLOR_FONDO = new Color(0xF5F5F5);

	/** Color oscuro utilizado en la cabecera y botones. */
	private static final Color COLOR_DARK = new Color(0x1a1a2e);

	/** Color de acento rojo del botón "Alquilar película". */
	private static final Color COLOR_ACENTO = new Color(0xE50914);

	/** Campo de texto para la búsqueda de películas por título. */
	private JTextField txtBuscar;

	/** Botón para ejecutar la búsqueda por título. */
	private JButton btnBuscar;

	/** Botón para iniciar el proceso de alquiler de la copia seleccionada. */
	private JButton btnAlquilar;

	/** Combo desplegable para filtrar las copias por formato. */
	private JComboBox<String> cmbFiltroFormato;

	/** Tabla que muestra las copias disponibles del catálogo. */
	private JTable tblPeliculas;

	/** Modelo de datos de la tabla del catálogo. */
	private DefaultTableModel modeloTabla;

	/** Indica si se debe mostrar la columna ID de copia en la tabla. */
	private final boolean mostrarId;

	/**
	 * Constructor que inicializa el catálogo en el modo indicado.
	 *
	 * @param mostrarId {@code true} para mostrar la columna ID de copia (modo
	 *                  empleado), {@code false} para ocultarla (modo cliente e
	 *                  invitado)
	 */

	public PanelCatalogo(boolean mostrarId) {
		this.mostrarId = mostrarId;
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 24, 20, 24));
		initComponents();
	}

	/**
	 * Inicializa y añade los componentes principales del panel.
	 */

	private void initComponents() {
		add(buildSuperior(), BorderLayout.NORTH);
		add(buildTabla(), BorderLayout.CENTER);
		add(buildAcciones(), BorderLayout.SOUTH);
	}

	/**
	 * Construye el panel superior con el título, el filtro de formato y la
	 * búsqueda.
	 *
	 * @return panel superior configurado
	 */

	private JPanel buildSuperior() {
		JPanel panel = new JPanel(new BorderLayout(12, 0));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0, 0, 12, 0));

		JLabel lblTitulo = new JLabel("Catálogo de películas");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_DARK);

		JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		controles.setOpaque(false);

		JLabel lblFormato = new JLabel("Formato:");
		lblFormato.setFont(new Font("SansSerif", Font.PLAIN, 13));

		cmbFiltroFormato = new JComboBox<>(new String[] { "Todos", "DVD", "Blu-ray", "4K Ultra HD" });
		cmbFiltroFormato.setActionCommand("FILTRAR_FORMATO_CATALOGO");
		cmbFiltroFormato.setFont(new Font("SansSerif", Font.PLAIN, 13));
		cmbFiltroFormato.setPreferredSize(new Dimension(130, 30));

		txtBuscar = new JTextField(18);
		txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar película...");

		btnBuscar = new JButton("Buscar");
		btnBuscar.setActionCommand("BUSCAR_PELICULA");
		btnBuscar.setBackground(COLOR_DARK);
		btnBuscar.setForeground(Color.WHITE);
		btnBuscar.setFocusPainted(false);
		btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		controles.add(lblFormato);
		controles.add(cmbFiltroFormato);
		controles.add(Box.createHorizontalStrut(8));
		controles.add(txtBuscar);
		controles.add(btnBuscar);

		panel.add(lblTitulo, BorderLayout.WEST);
		panel.add(controles, BorderLayout.EAST);
		return panel;
	}

	/**
	 * Construye la tabla del catálogo con sus columnas y renderers.
	 *
	 * @return scroll pane con la tabla configurada
	 */

	private JScrollPane buildTabla() {
		String[] columnasSinId = { "Título", "Director", "Género", "Duración", "Clasificación", "Formato",
				"Precio/día" };
		String[] columnasConId = { "ID Copia", "Título", "Director", "Género", "Duración", "Clasificación", "Formato",
				"Precio/día" };

		String[] columnas = mostrarId ? columnasConId : columnasSinId;

		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblPeliculas = new JTable(modeloTabla);
		tblPeliculas.setRowHeight(38);
		tblPeliculas.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblPeliculas.setShowHorizontalLines(true);
		tblPeliculas.setGridColor(new Color(0xEEEEEE));
		tblPeliculas.setSelectionBackground(new Color(0xFFE0E0));
		tblPeliculas.setSelectionForeground(COLOR_DARK);

		tblPeliculas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		tblPeliculas.getTableHeader().setBackground(COLOR_DARK);
		tblPeliculas.getTableHeader().setForeground(Color.WHITE);

		if (mostrarId) {
			int[] anchos = { 70, 200, 140, 100, 80, 100, 100, 80 };
			for (int i = 0; i < anchos.length; i++) {
				tblPeliculas.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
			}
			centrar(new int[] { 0, 4, 5, 6, 7 });
		} else {
			int[] anchos = { 220, 150, 100, 80, 100, 110, 80 };
			for (int i = 0; i < anchos.length; i++) {
				tblPeliculas.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
			}
			centrar(new int[] { 3, 4, 5, 6 });
		}

		JScrollPane scroll = new JScrollPane(tblPeliculas);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
		return scroll;
	}

	/**
	 * Aplica alineación centrada a las columnas indicadas.
	 *
	 * @param cols índices de las columnas a centrar
	 */

	private void centrar(int[] cols) {
		DefaultTableCellRenderer c = new DefaultTableCellRenderer();
		c.setHorizontalAlignment(SwingConstants.CENTER);
		for (int col : cols) {
			tblPeliculas.getColumnModel().getColumn(col).setCellRenderer(c);
		}
	}

	/**
	 * Construye el panel inferior con el botón "Alquilar película".
	 *
	 * @return panel de acciones configurado
	 */

	private JPanel buildAcciones() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(12, 0, 0, 0));

		btnAlquilar = new JButton("🎬  Alquilar película");
		btnAlquilar.setActionCommand("ALQUILAR_PELICULA");
		btnAlquilar.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnAlquilar.setBackground(COLOR_ACENTO);
		btnAlquilar.setForeground(Color.WHITE);
		btnAlquilar.setFocusPainted(false);
		btnAlquilar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAlquilar.setBorder(new EmptyBorder(9, 20, 9, 20));
		btnAlquilar.setEnabled(false);

		panel.add(btnAlquilar);
		return panel;
	}

	/**
	 * Carga las copias disponibles en la tabla cruzando cada copia con su película.
	 *
	 * @param peliculas lista de películas del catálogo
	 * @param copias    lista de copias disponibles
	 */
	public void cargarCopias(ArrayList<Pelicula> peliculas, ArrayList<Copia> copias) {
		modeloTabla.setRowCount(0);
		for (Copia copia : copias) {
			Pelicula pelicula = null;
			for (Pelicula p : peliculas) {
				if (p.getId() == copia.getIdPelicula()) {
					pelicula = p;
					break;
				}
			}
			if (pelicula == null) {
				continue;
			}
			if (mostrarId) {
				modeloTabla
						.addRow(new Object[] { copia.getIdCopia(), pelicula.getNombrePelicula(), pelicula.getDirector(),
								pelicula.getGenero(), pelicula.getDuracion() + " min", pelicula.getClasificacionEdad(),
								copia.getFormato(), String.format("%.2f €", copia.getPrecioAlquiler()) });
			} else {
				modeloTabla.addRow(new Object[] { pelicula.getNombrePelicula(), pelicula.getDirector(),
						pelicula.getGenero(), pelicula.getDuracion() + " min", pelicula.getClasificacionEdad(),
						copia.getFormato(), String.format("%.2f €", copia.getPrecioAlquiler()) });
			}
		}
	}

	/**
	 * Habilita o deshabilita el botón "Alquilar película".
	 *
	 * @param habilitar {@code true} para habilitar
	 */
	public void habilitarAcciones(boolean habilitar) {
		btnAlquilar.setEnabled(habilitar);
	}

	/**
	 * Devuelve el título de la película de la fila seleccionada.
	 *
	 * @return título de la película, o {@code null} si no hay selección
	 */

	public String getTituloSeleccionado() {
		int fila = tblPeliculas.getSelectedRow();
		if (fila < 0) {
			return null;
		}
		int col = mostrarId ? 1 : 0;
		return (String) modeloTabla.getValueAt(fila, col);
	}

	/**
	 * Devuelve el formato de la copia de la fila seleccionada.
	 *
	 * @return formato seleccionado, o {@code null} si no hay selección
	 */
	public String getFormatoSeleccionado() {
		int fila = tblPeliculas.getSelectedRow();
		if (fila < 0) {
			return null;
		}
		int col = mostrarId ? 6 : 5;
		return (String) modeloTabla.getValueAt(fila, col);
	}

	/**
	 * Devuelve el valor del filtro de formato seleccionado.
	 *
	 * @return formato seleccionado, o {@code null} si se eligió "Todos"
	 */
	public String getFiltroFormato() {
		String sel = (String) cmbFiltroFormato.getSelectedItem();
		if ("Todos".equals(sel)) {
			return null;
		}
		return sel;
	}

	/**
	 * Devuelve el índice de la fila seleccionada en la tabla.
	 *
	 * @return índice de la fila, o {@code -1} si no hay selección
	 */
	public int getFilaSeleccionada() {
		return tblPeliculas.getSelectedRow();
	}

	/**
	 * Registra un listener que habilita el botón "Alquilar" al seleccionar una
	 * fila. Permite que el controlador reaccione a la selección tanto en modo
	 * invitado como en modo cliente autenticado.
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	public void setSelectionListener(Controlador controlador) {
		tblPeliculas.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					btnAlquilar.setEnabled(tblPeliculas.getSelectedRow() >= 0);
				}
			}
		});
	}

	/**
	 * Registra el controlador como listener de los componentes interactivos.
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	public void setControlador(Controlador controlador) {
		btnBuscar.addActionListener(controlador);
		btnAlquilar.addActionListener(controlador);
		cmbFiltroFormato.addActionListener(controlador);
	}

	/**
	 * Devuelve el campo de texto de búsqueda.
	 *
	 * @return campo de búsqueda por título
	 */
	public JTextField getTxtBuscar() {
		return txtBuscar;
	}

	/**
	 * Devuelve el botón de búsqueda.
	 *
	 * @return botón "Buscar"
	 */
	public JButton getBtnBuscar() {
		return btnBuscar;
	}

	/**
	 * Devuelve el combo de filtro por formato.
	 *
	 * @return combo desplegable de formato
	 */
	public JComboBox<String> getCmbFiltroFormato() {
		return cmbFiltroFormato;
	}

}