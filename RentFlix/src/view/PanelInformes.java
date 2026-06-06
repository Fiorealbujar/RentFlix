package view;

import controller.Controlador;
import model.Alquiler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Panel de informes de ventas para empleado y administrador.
 * <p>
 * Muestra cuatro tarjetas con indicadores: total de alquileres, ingresos
 * totales, alquileres activos y pendientes de devolución, más tabla de detalle.
 * </p>
 *
 * @author Fiorella Ruth Albújar Albino
 * @version 1.0
 */
public class PanelInformes extends JPanel {

	private static final Color COLOR_FONDO = new Color(0xF5F5F5);
	private static final Color COLOR_DARK = new Color(0x1a1a2e);
	private static final Color COLOR_ACENTO = new Color(0xE50914);
	private static final Color COLOR_VERDE = new Color(0x27AE60);

	private JLabel lblTotalAlquileres;
	private JLabel lblTotalIngresos;
	private JLabel lblAlquileresActivos;
	private JLabel lblPendientesDevolucion;

	private DefaultTableModel modeloTabla;
	private JTable tblDetalle;

	/**
	 * Constructor que inicializa el panel y construye sus componentes visuales.
	 */
	
	public PanelInformes() {
		setBackground(COLOR_FONDO);
		setLayout(new BorderLayout(0, 16));
		setBorder(new EmptyBorder(20, 24, 20, 24));
		initComponents();
	}

	/**
	 * Inicializa y añade los componentes principales del panel.
	 */
	
	private void initComponents() {
		add(buildTitulo(), BorderLayout.NORTH);
		add(buildCuerpo(), BorderLayout.CENTER);
	}

	/**
	 * Construye el panel con el título del panel de informes.
	 *
	 * @return panel de título configurado
	 */
	
	private JPanel buildTitulo() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(new EmptyBorder(0, 0, 16, 0));

		JLabel lbl = new JLabel("📊 Informes de ventas");
		lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
		lbl.setForeground(COLOR_DARK);

		panel.add(lbl, BorderLayout.WEST);
		return panel;
	}

	/**
	 * Construye el cuerpo del panel con las tarjetas de indicadores y la tabla de detalle.
	 *
	 * @return panel de cuerpo configurado
	 */
	
	private JPanel buildCuerpo() {
		JPanel panel = new JPanel(new BorderLayout(0, 16));
		panel.setOpaque(false);
		panel.add(buildTarjetas(), BorderLayout.NORTH);
		panel.add(buildTabla(), BorderLayout.CENTER);
		return panel;
	}

	/**
	 * Construye el panel con las cuatro tarjetas de indicadores resumidos.
	 *
	 * @return panel de tarjetas configurado
	 */
	
	private JPanel buildTarjetas() {
		JPanel panel = new JPanel(new GridLayout(1, 4, 12, 0));
		panel.setOpaque(false);

		lblTotalAlquileres = new JLabel("0");
		lblTotalIngresos = new JLabel("0,00 €");
		lblAlquileresActivos = new JLabel("0");
		lblPendientesDevolucion = new JLabel("0");

		panel.add(buildTarjeta("Total alquileres", lblTotalAlquileres, "📋", COLOR_DARK, new Color(0xE8EAF6)));
		panel.add(buildTarjeta("Ingresos totales", lblTotalIngresos, "💰", COLOR_VERDE, new Color(0xE8F8F0)));
		panel.add(buildTarjeta("Alquileres activos", lblAlquileresActivos, "🎬", new Color(0x2980B9),
				new Color(0xE3F2FD)));
		panel.add(buildTarjeta("Pendientes devolución", lblPendientesDevolucion, "📦", COLOR_ACENTO,
				new Color(0xFDEDEC)));

		return panel;
	}

	/**
	 * Construye una tarjeta individual de indicador con icono, título y valor.
	 *
	 * @param titulo     texto descriptivo de la tarjeta
	 * @param lblValor   etiqueta donde se mostrará el valor numérico
	 * @param icono      emoji o símbolo de la tarjeta
	 * @param colorValor color del valor numérico
	 * @param colorFondo color de fondo del icono
	 * @return tarjeta configurada
	 */
	
	private JPanel buildTarjeta(String titulo, JLabel lblValor, String icono, Color colorValor, Color colorFondo) {
		JPanel tarjeta = new JPanel(new BorderLayout());
		tarjeta.setBackground(Color.WHITE);
		tarjeta.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)),
				new EmptyBorder(16, 18, 16, 18)));

		JLabel lblIcono = new JLabel(icono);
		lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 26));
		lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
		lblIcono.setBackground(colorFondo);
		lblIcono.setOpaque(true);
		lblIcono.setBorder(new EmptyBorder(8, 12, 8, 12));

		JPanel textos = new JPanel();
		textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
		textos.setOpaque(false);
		textos.setBorder(new EmptyBorder(0, 14, 0, 0));

		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
		lblTitulo.setForeground(new Color(0x888888));

		lblValor.setFont(new Font("SansSerif", Font.BOLD, 26));
		lblValor.setForeground(colorValor);

		textos.add(lblTitulo);
		textos.add(lblValor);

		tarjeta.add(lblIcono, BorderLayout.WEST);
		tarjeta.add(textos, BorderLayout.CENTER);
		return tarjeta;
	}

	/**
	 * Construye la tabla de detalle con todos los alquileres del sistema.
	 *
	 * @return scroll pane con la tabla configurada
	 */
	
	private JScrollPane buildTabla() {
		String[] columnas = { "#", "Cliente", "Película", "F. Alquiler", "F. Dev. Prev.", "Estado", "Importe" };

		modeloTabla = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tblDetalle = new JTable(modeloTabla);
		tblDetalle.setRowHeight(36);
		tblDetalle.setFont(new Font("SansSerif", Font.PLAIN, 12));
		tblDetalle.setShowHorizontalLines(true);
		tblDetalle.setGridColor(new Color(0xEEEEEE));

		tblDetalle.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
		tblDetalle.getTableHeader().setBackground(COLOR_DARK);
		tblDetalle.getTableHeader().setForeground(Color.WHITE);

		int[] anchos = { 40, 150, 180, 100, 110, 140, 90 };
		for (int i = 0; i < anchos.length; i++) {
			tblDetalle.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
		}

		JScrollPane scroll = new JScrollPane(tblDetalle);
		scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
		return scroll;
	}

	/**
	 * Carga los datos de alquileres en las tarjetas de indicadores y en la tabla
	 * de detalle. Calcula el total de alquileres, alquileres activos y pendientes
	 * de devolución mediante iteración sobre la lista recibida.
	 *
	 * @param alquileres    lista completa de alquileres del sistema
	 * @param totalIngresos importe total acumulado de todos los alquileres
	 */
	
	public void cargarInformes(ArrayList<Alquiler> alquileres, double totalIngresos) {
		int totalAlq = alquileres.size();
		int activos = 0;
		int pendientes = 0;

		for (Alquiler a : alquileres) {
			if ("activo".equalsIgnoreCase(a.getEstadoAlquiler())) {
				activos++;
			}
			if ("pendiente_devolucion".equalsIgnoreCase(a.getEstadoAlquiler())) {
				pendientes++;
			}
		}

		lblTotalAlquileres.setText(String.valueOf(totalAlq));
		lblTotalIngresos.setText(String.format("%.2f €", totalIngresos));
		lblAlquileresActivos.setText(String.valueOf(activos));
		lblPendientesDevolucion.setText(String.valueOf(pendientes));

		// Cargar tabla detalle
		modeloTabla.setRowCount(0);
		for (Alquiler a : alquileres) {
			modeloTabla.addRow(new Object[] { a.getIdAlquiler(), a.getNombreCliente(), a.getNombrePelicula(),
					a.getFechaAlquiler(), a.getFechaDevolucionPrevista(), a.getEstadoAlquiler(),
					String.format("%.2f €", a.getMontoCobro()) });
		}
	}

}