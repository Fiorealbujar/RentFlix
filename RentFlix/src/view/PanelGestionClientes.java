// PanelGestionClientes.java
package view;

import controller.Controlador;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel de gestión de clientes para empleado y administrador.
 * <p>
 * Muestra la tabla de clientes con columnas ID, Nombre, Apellido, Email,
 * Usuario y Estado (activo/bloqueado con color). Los botones de editar y
 * eliminar se habilitan al seleccionar una fila.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelGestionClientes extends JPanel {

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_ACENTO = new Color(0xE50914);

	private DefaultTableModel modeloTabla;
	private JTable tblClientes;
	private JButton btnEliminarCliente;
	private JButton btnEditarCliente;
	private JButton btnBloquearCliente;
	private JComboBox<String> cmbFiltroEstado;

	/**
	 * Constructor que inicializa el panel y construye sus componentes visuales.
	 */
	
	public PanelGestionClientes() {
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout(0, 12));
		setBorder(new EmptyBorder(20, 24, 20, 24));
		initComponents();
	}

	/**
	 * Inicializa y añade los componentes principales del panel.
	 */
	
	private void initComponents() {
		add(buildSuperior(), BorderLayout.NORTH);
		add(buildTabla(), BorderLayout.CENTER);
	}

	/**
	 * Construye la etiqueta de título del panel.
	 *
	 * @return etiqueta de título configurada
	 */
	
	private JLabel buildTitulo() {
		JLabel lbl = new JLabel("‍💼 Gestión de clientes");
		lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
		lbl.setForeground(COLOR_DARK);
		lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
		return lbl;
	}

	/**
	 * Construye el panel superior con el título y el combo de filtro por estado.
	 *
	 * @return panel superior configurado
	 */
	
	private JPanel buildSuperior() {
		JPanel panel = new JPanel(new BorderLayout(12, 0));
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0, 0, 8, 0));

		panel.add(buildTitulo(), BorderLayout.WEST);

		cmbFiltroEstado = new JComboBox<>(new String[] { "Todos", "activo", "bloqueado" });
		cmbFiltroEstado.setActionCommand("FILTRAR_CLIENTES");
		cmbFiltroEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));
		cmbFiltroEstado.setBackground(Color.WHITE);
		cmbFiltroEstado.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		derecha.setOpaque(false);
		derecha.add(new JLabel("Filtrar: "));
		derecha.add(cmbFiltroEstado);
		panel.add(derecha, BorderLayout.EAST);

		return panel;
	}

	/**
	 * Construye la tabla de clientes con sus columnas, renderers y botones de acción.
	 *
	 * @return panel con la tabla y los botones configurados
	 */
	
	private JPanel buildTabla() {
		JPanel panel = new JPanel(new BorderLayout(0, 8));
		panel.setOpaque(false);

		String[] columnas = { "ID", "Nombre", "Apellido", "Email", "Usuario", "Estado" };

		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblClientes = new JTable(modeloTabla);
		tblClientes.setRowHeight(36);
		tblClientes.setFont(new Font("SansSerif", Font.PLAIN, 13));
		tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblClientes.setShowHorizontalLines(true);
		tblClientes.setGridColor(new Color(0xEEEEEE));
		tblClientes.setSelectionBackground(new Color(0xFFE0E0));
		tblClientes.setSelectionForeground(COLOR_DARK);

		tblClientes.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		tblClientes.getTableHeader().setBackground(COLOR_DARK);
		tblClientes.getTableHeader().setForeground(Color.WHITE);

		int[] anchos = { 40, 130, 130, 220, 130, 90 };
		for (int i = 0; i < anchos.length; i++) {
			tblClientes.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		}

		// Centrar ID
		DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
		centrado.setHorizontalAlignment(SwingConstants.CENTER);
		tblClientes.getColumnModel().getColumn(0).setCellRenderer(centrado);

		// Renderer con color para columna Estado
		tblClientes.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
						column);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
				lbl.setOpaque(true);
				if (!isSelected) {
					if ("activo".equalsIgnoreCase(String.valueOf(value))) {
						lbl.setBackground(new Color(0xE8F8F0));
						lbl.setForeground(new Color(0x27AE60));
					} else {
						lbl.setBackground(new Color(0xFDEDEC));
						lbl.setForeground(COLOR_ACENTO);
					}
				}
				return lbl;
			}
		});

		// Botones bajo la tabla
		JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		filaBotones.setOpaque(false);

		btnEditarCliente = new JButton("✏️  Editar cliente");
		btnEditarCliente.setActionCommand("EDITAR_CLIENTE");
		btnEditarCliente.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEditarCliente.setBackground(COLOR_DARK);
		btnEditarCliente.setForeground(Color.WHITE);
		btnEditarCliente.setFocusPainted(false);
		btnEditarCliente.setBorder(new EmptyBorder(8, 18, 8, 18));
		btnEditarCliente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEditarCliente.setEnabled(false);

		btnBloquearCliente = new JButton("🚫  Bloquear / Desbloquear");
		btnBloquearCliente.setActionCommand("BLOQUEAR_CLIENTE");
		btnBloquearCliente.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnBloquearCliente.setBackground(new Color(0xE67E22));
		btnBloquearCliente.setForeground(Color.WHITE);
		btnBloquearCliente.setFocusPainted(false);
		btnBloquearCliente.setBorder(new EmptyBorder(8, 18, 8, 18));
		btnBloquearCliente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBloquearCliente.setEnabled(false);

		btnEliminarCliente = new JButton("🗑️  Eliminar cliente");
		btnEliminarCliente.setActionCommand("ELIMINAR_CLIENTE");
		btnEliminarCliente.setFont(new Font("SansSerif", Font.BOLD, 13));
		btnEliminarCliente.setBackground(COLOR_ACENTO);
		btnEliminarCliente.setForeground(Color.WHITE);
		btnEliminarCliente.setFocusPainted(false);
		btnEliminarCliente.setBorder(new EmptyBorder(8, 18, 8, 18));
		btnEliminarCliente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnEliminarCliente.setEnabled(false);

		filaBotones.add(btnEditarCliente);
		filaBotones.add(btnBloquearCliente);
		filaBotones.add(btnEliminarCliente);

		JScrollPane scroll = new JScrollPane(tblClientes);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));

		panel.add(scroll, BorderLayout.CENTER);
		panel.add(filaBotones, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * Carga la lista de clientes en la tabla.
	 *
	 * @param clientes lista de clientes a mostrar
	 */
	public void cargarClientes(ArrayList<Cliente> clientes) {
		modeloTabla.setRowCount(0);
		for (Cliente c : clientes) {
			modeloTabla.addRow(new Object[] { c.getIdCliente(), c.getNombreCliente(), c.getApellidoCliente(),
					c.getEmailCliente(), c.getNombreUsuario(), c.getEstado() });
		}
		actualizarBotones();
	}

	/**
	 * Habilita o deshabilita los botones de editar y eliminar según si hay fila
	 * seleccionada.
	 */
	public void actualizarBotones() {
		boolean haySeleccion = tblClientes.getSelectedRow() >= 0;
		btnEditarCliente.setEnabled(haySeleccion);
		btnBloquearCliente.setEnabled(haySeleccion);
		btnEliminarCliente.setEnabled(haySeleccion);
	}

	/**
	 * Devuelve el id del cliente de la fila seleccionada.
	 *
	 * @return id del cliente, o {@code -1} si no hay selección
	 */
	public int getIdClienteSeleccionado() {
		int fila = tblClientes.getSelectedRow();
		if (fila < 0) {
			return -1;
		}
		return (int) modeloTabla.getValueAt(fila, 0);
	}

	/**
	 * Devuelve un objeto {@link model.Cliente} con los datos de la fila
	 * seleccionada. La contraseña no se incluye (no se muestra en la tabla).
	 *
	 * @return cliente seleccionado, o {@code null} si no hay selección
	 */
	public Cliente getClienteSeleccionado() {
		int fila = tblClientes.getSelectedRow();
		if (fila < 0) {
			return null;
		}
		return new Cliente((int) modeloTabla.getValueAt(fila, 0), (String) modeloTabla.getValueAt(fila, 1),
				(String) modeloTabla.getValueAt(fila, 2), (String) modeloTabla.getValueAt(fila, 3),
				(String) modeloTabla.getValueAt(fila, 4), "", (String) modeloTabla.getValueAt(fila, 5));
	}

	/**
	 * Devuelve el estado seleccionado en el combo filtro, o {@code null} si se
	 * seleccionó "Todos".
	 *
	 * @return "activo", "bloqueado", o {@code null}
	 */
	public String getFiltroEstado() {
		String sel = (String) cmbFiltroEstado.getSelectedItem();
		return "Todos".equals(sel) ? null : sel;
	}

	/**
	 * Registra el controlador como listener de los botones y la tabla.
	 *
	 * @param controlador controlador principal de la aplicación
	 */
	public void setControlador(Controlador controlador) {
		btnEditarCliente.addActionListener(controlador);
		btnBloquearCliente.addActionListener(controlador);
		btnEliminarCliente.addActionListener(controlador);
		cmbFiltroEstado.addActionListener(controlador);

		tblClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					actualizarBotones();
				}
			}
		});
	}
}