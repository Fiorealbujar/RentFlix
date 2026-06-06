// ==========================================
// CLASE: PanelGestionPeliculas.java
// CRUD completo de películas para el Admin.
// Editar y dar de baja desde la tabla directamente.
// ==========================================
package view;

import controller.Controlador;
import model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionPeliculas extends JPanel {

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_DARK = new Color(0x1a1a2e);

	private DefaultTableModel modeloTabla;
	private JTable tblPeliculas;
	private JButton btnEditar;
	private JButton btnDarDeBaja;
	private JTextField txtBuscar;
	private JButton btnBuscar;

	public PanelGestionPeliculas() {
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 24, 20, 24));
		initComponents();
	}

	private void initComponents() {
		add(buildSuperior(), BorderLayout.NORTH);
		add(buildTabla(), BorderLayout.CENTER);
		add(buildAcciones(), BorderLayout.SOUTH);
	}

	// ── Superior ────────────────────────────────────────────────────────────

	private JPanel buildSuperior() {
		JPanel panel = new JPanel(new BorderLayout(12, 0));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0, 0, 16, 0));

		JLabel lbl = new JLabel("🎞️  Gestión de películas");
		lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
		lbl.setForeground(COLOR_DARK);

		JLabel sublbl = new JLabel("  —  Selecciona una película para editarla o darla de baja");
		sublbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
		sublbl.setForeground(new Color(0x888888));

		JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		izquierda.setOpaque(false);
		izquierda.add(lbl);
		izquierda.add(sublbl);

		JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		controles.setOpaque(false);

		txtBuscar = new JTextField(18);
		txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar película...");

		btnBuscar = new JButton("Buscar");
		btnBuscar.setActionCommand("BUSCAR_PELICULA_GESTION");
		btnBuscar.setBackground(COLOR_DARK);
		btnBuscar.setForeground(Color.WHITE);
		btnBuscar.setFocusPainted(false);
		btnBuscar.setBorder(new EmptyBorder(6, 14, 6, 14));
		btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		controles.add(txtBuscar);
		controles.add(btnBuscar);

		panel.add(izquierda, BorderLayout.WEST);
		panel.add(controles, BorderLayout.EAST);
		return panel;
	}

	// ── Tabla ───────────────────────────────────────────────────────────────

	private JScrollPane buildTabla() {
		String[] columnas = { "ID", "Título", "Director", "Género", "Duración", "Clasificación", "Nº copias",
				"Sinopsis" };

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

		int[] anchos = { 40, 220, 130, 110, 80, 90, 80, 520 };
		for (int i = 0; i < anchos.length; i++) {
			tblPeliculas.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		}

		// Centrar columnas: ID, Género, Duración, Clasificación y Nº copias
		DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
		centrado.setHorizontalAlignment(SwingConstants.CENTER);
		for (int col : new int[] { 0, 3, 4, 5, 6 }) {
			tblPeliculas.getColumnModel().getColumn(col).setCellRenderer(centrado);
		}

		// Columna Sinopsis: ancho fijo + tooltip para ver el texto completo al pasar el
		// ratón
		tblPeliculas.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				lbl.setToolTipText(String.valueOf(value));
				return lbl;
			}
		});

		// Desactivar redimensionado automático para respetar los anchos definidos
		tblPeliculas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JScrollPane scroll = new JScrollPane(tblPeliculas);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
		return scroll;
	}

	// ── Acciones ────────────────────────────────────────────────────────────

	private JPanel buildAcciones() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(14, 0, 0, 0));

		btnEditar = buildBoton("✏️  Editar película", COLOR_DARK, Color.WHITE, "EDITAR_PELICULA");
		btnEditar.setEnabled(false);

		btnDarDeBaja = buildBoton("🚫  Dar de baja", new Color(0xE67E22), Color.WHITE, "DAR_DE_BAJA_PELICULA");
		btnDarDeBaja.setEnabled(false);

		panel.add(btnEditar);
		panel.add(btnDarDeBaja);
		return panel;
	}

	private JButton buildBoton(String texto, Color fondo, Color letra, String cmd) {
		JButton btn = new JButton(texto);
		btn.setActionCommand(cmd);
		btn.setFont(new Font("SansSerif", Font.BOLD, 13));
		btn.setBackground(fondo);
		btn.setForeground(letra);
		btn.setFocusPainted(false);
		btn.setBorder(new EmptyBorder(9, 20, 9, 20));
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return btn;
	}

	// ── Métodos públicos para el Controlador ────────────────────────────────

	/**
	 * Carga la lista de películas en la tabla junto con el número de copias
	 * disponibles.
	 *
	 * @param peliculas lista de películas a mostrar
	 * @param copias    lista de conteos de copias disponibles por película
	 */
	public void cargarPeliculas(List<Pelicula> peliculas, List<Integer> copias) {
		modeloTabla.setRowCount(0);
		for (int i = 0; i < peliculas.size(); i++) {
			Pelicula p = peliculas.get(i);
			int numCopias = copias.get(i);
			modeloTabla.addRow(new Object[] { p.getId(), p.getNombrePelicula(), p.getDirector(), p.getGenero(),
					p.getDuracion() + " min", p.getClasificacionEdad(), numCopias, p.getSinopsis() });
		}
		actualizarBotones();
	}

	public void actualizarBotones() {
		boolean haySeleccion = tblPeliculas.getSelectedRow() >= 0;
		btnEditar.setEnabled(haySeleccion);
		btnDarDeBaja.setEnabled(haySeleccion);
	}

	// Devuelve el ID de la película seleccionada
	public int getIdPeliculaSeleccionada() {
		int fila = tblPeliculas.getSelectedRow();
		if (fila < 0)
			return -1;
		return (int) modeloTabla.getValueAt(fila, 0);
	}

	// Devuelve todos los datos de la fila seleccionada como objeto Pelicula
	public Pelicula getPeliculaSeleccionada() {
	    int fila = tblPeliculas.getSelectedRow();
	    if (fila < 0)
	        return null;

	    String duracionStr = String.valueOf(modeloTabla.getValueAt(fila, 4)).replace(" min", "");

	    return new Pelicula(
	        (int) modeloTabla.getValueAt(fila, 0),
	        (String) modeloTabla.getValueAt(fila, 1),
	        (String) modeloTabla.getValueAt(fila, 2),
	        Integer.parseInt(duracionStr),
	        (String) modeloTabla.getValueAt(fila, 3),
	        (String) modeloTabla.getValueAt(fila, 7),
	        (String) modeloTabla.getValueAt(fila, 5),
	        "activa"
	    );
	}

	public void setControlador(Controlador controlador) {
		btnEditar.addActionListener(controlador);
		btnDarDeBaja.addActionListener(controlador);
		btnBuscar.addActionListener(controlador);

		tblPeliculas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					actualizarBotones();
				}
			}
		});
	}

	public JTextField getTxtBuscar() {
		return txtBuscar;
	}

}