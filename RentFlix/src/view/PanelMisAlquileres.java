// PanelMisAlquileres.java
package view;

import controller.Controlador;
import model.Alquiler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelMisAlquileres extends JPanel {

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_ACTIVO = new Color(0x27AE60);
	private static final Color COLOR_PENDIENTE = new Color(0xF39C12);
	private static final Color COLOR_DEVUELTO = new Color(0x7F8C8D);
	private static final Color COLOR_VENCIDO = new Color(0xE50914);

	private DefaultTableModel modeloTabla;
	private JTable tblAlquileres;
	private JButton btnSolicitarDevolucion;
	private JComboBox<String> cmbFiltroEstado;
	private JLabel lblInfo;

	// Lista paralela para guardar los ids sin mostrarlos en la tabla
	private ArrayList<Integer> listaIds = new ArrayList<Integer>();

	public PanelMisAlquileres() {
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 24, 20, 24));
		initComponents();
	}

	private void initComponents() {
		add(buildPanelSuperior(), BorderLayout.NORTH);
		add(buildTabla(), BorderLayout.CENTER);
		add(buildPanelAcciones(), BorderLayout.SOUTH);
	}

	// ── Superior ─────────────────────────────────────────────────────────────

	private JPanel buildPanelSuperior() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0, 0, 16, 0));

		JLabel lblTitulo = new JLabel("Mis alquileres");
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
		lblTitulo.setForeground(COLOR_DARK);

		// Leyenda + filtro a la derecha
		JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
		derecha.setOpaque(false);

		derecha.add(buildChip("Activo", COLOR_ACTIVO));
		derecha.add(buildChip("Pendiente devolución", COLOR_PENDIENTE));
		derecha.add(buildChip("Devuelto", COLOR_DEVUELTO));
		derecha.add(buildChip("Vencido", COLOR_VENCIDO));

		derecha.add(Box.createHorizontalStrut(8));

		JLabel lblFiltro = new JLabel("Filtrar:");
		lblFiltro.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblFiltro.setForeground(COLOR_DARK);

		cmbFiltroEstado = new JComboBox<>(
				new String[] { "Todos", "activo", "pendiente_devolucion", "devuelto", "vencido" });
		cmbFiltroEstado.setActionCommand("FILTRAR_MIS_ALQUILERES");
		cmbFiltroEstado.setFont(new Font("SansSerif", Font.PLAIN, 12));
		cmbFiltroEstado.setPreferredSize(new Dimension(160, 28));

		derecha.add(lblFiltro);
		derecha.add(cmbFiltroEstado);

		panel.add(lblTitulo, BorderLayout.WEST);
		panel.add(derecha, BorderLayout.EAST);
		return panel;
	}

	private JLabel buildChip(String texto, Color color) {
		JLabel chip = new JLabel("● " + texto);
		chip.setFont(new Font("SansSerif", Font.PLAIN, 11));
		chip.setForeground(color);
		return chip;
	}

	// ── Tabla ─────────────────────────────────────────────────────────────────

	private JScrollPane buildTabla() {
		// Sin columna ID: el cliente no necesita verlo
		String[] columnas = { "Película", "F. Alquiler", "F. Devolución prev.", "Estado", "Importe" };

		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblAlquileres = new JTable(modeloTabla);
		tblAlquileres.setRowHeight(38);
		tblAlquileres.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblAlquileres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblAlquileres.setShowHorizontalLines(true);
		tblAlquileres.setGridColor(new Color(0xEEEEEE));
		tblAlquileres.setSelectionBackground(new Color(0xFFE0E0));
		tblAlquileres.setSelectionForeground(COLOR_DARK);

		tblAlquileres.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		tblAlquileres.getTableHeader().setBackground(COLOR_DARK);
		tblAlquileres.getTableHeader().setForeground(Color.WHITE);

		int[] anchos = { 280, 110, 140, 160, 90 };
		for (int i = 0; i < anchos.length; i++) {
			tblAlquileres.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		}

		// Centrar fechas e importe
		DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
		centrado.setHorizontalAlignment(SwingConstants.CENTER);
		for (int col : new int[] { 1, 2, 4 }) {
			tblAlquileres.getColumnModel().getColumn(col).setCellRenderer(centrado);
		}

		// Renderer con colores para Estado (columna 3)
		tblAlquileres.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
				lbl.setOpaque(true);
				if (!isSelected) {
					String estado = String.valueOf(value).toLowerCase();
					switch (estado) {
					case "activo":
						lbl.setBackground(new Color(0xE8F8F0));
						lbl.setForeground(COLOR_ACTIVO);
						break;
					case "pendiente_devolucion":
						lbl.setBackground(new Color(0xFEF9E7));
						lbl.setForeground(COLOR_PENDIENTE);
						break;
					case "devuelto":
						lbl.setBackground(new Color(0xF2F3F4));
						lbl.setForeground(COLOR_DEVUELTO);
						break;
					case "vencido":
						lbl.setBackground(new Color(0xFDEDEC));
						lbl.setForeground(COLOR_VENCIDO);
						break;
					default:
						lbl.setBackground(Color.WHITE);
						lbl.setForeground(Color.BLACK);
					}
				}
				return lbl;
			}
		});

		JScrollPane scroll = new JScrollPane(tblAlquileres);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
		return scroll;
	}

	// ── Acciones ──────────────────────────────────────────────────────────────

	private JPanel buildPanelAcciones() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(14, 0, 0, 0));

		lblInfo = new JLabel("Selecciona un alquiler activo para solicitar la devolución");
		lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblInfo.setForeground(new Color(0x888888));

		btnSolicitarDevolucion = new JButton("📦  Solicitar devolución");
		btnSolicitarDevolucion.setActionCommand("SOLICITAR_DEVOLUCION");
		btnSolicitarDevolucion.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnSolicitarDevolucion.setBackground(COLOR_DARK);
		btnSolicitarDevolucion.setForeground(Color.WHITE);
		btnSolicitarDevolucion.setFocusPainted(false);
		btnSolicitarDevolucion.setBorder(new EmptyBorder(9, 20, 9, 20));
		btnSolicitarDevolucion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSolicitarDevolucion.setEnabled(false);

		panel.add(lblInfo, BorderLayout.WEST);
		panel.add(btnSolicitarDevolucion, BorderLayout.EAST);
		return panel;
	}

	// ── Métodos para el Controlador ───────────────────────────────────────────

	public void cargarAlquileres(ArrayList<Alquiler> alquileres) {
		modeloTabla.setRowCount(0);
		listaIds.clear();
		for (Alquiler a : alquileres) {
			// Guardamos el id en la lista paralela
			listaIds.add(a.getIdAlquiler());
			modeloTabla.addRow(new Object[] { a.getNombrePelicula(), // col 0
					a.getFechaAlquiler(), // col 1
					a.getFechaDevolucionPrevista(), // col 2
					a.getEstadoAlquiler(), // col 3
					String.format("%.2f €", a.getMontoCobro()) // col 4
			});
		}
		actualizarBotonDevolucion();
	}

	public void actualizarBotonDevolucion() {
		int fila = tblAlquileres.getSelectedRow();
		if (fila >= 0) {
			String estado = String.valueOf(modeloTabla.getValueAt(fila, 3));
			btnSolicitarDevolucion.setEnabled("activo".equalsIgnoreCase(estado));
		} else {
			btnSolicitarDevolucion.setEnabled(false);
		}
	}

	// Recupera el id real desde la lista paralela
	public int getIdAlquilerSeleccionado() {
		int fila = tblAlquileres.getSelectedRow();
		if (fila < 0) {
			return -1;
		}
		return listaIds.get(fila);
	}

	public String getFiltroEstado() {
		String sel = (String) cmbFiltroEstado.getSelectedItem();
		if ("Todos".equals(sel)) {
			return null;
		}
		return sel;
	}

	public void setControlador(Controlador controlador) {
		btnSolicitarDevolucion.addActionListener(controlador);
		cmbFiltroEstado.addActionListener(controlador);

		tblAlquileres.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					actualizarBotonDevolucion();
				}
			}
		});
	}

	public JButton getBtnSolicitarDevolucion() {
		return btnSolicitarDevolucion;
	}

	public JComboBox<String> getCmbFiltroEstado() {
		return cmbFiltroEstado;
	}
}