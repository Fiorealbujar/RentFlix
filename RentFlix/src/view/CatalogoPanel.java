package view;

import controller.NavController;
import dao.PeliculaDAO;
import model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CatalogoPanel extends JPanel {

	private static final Color ACCENT = new Color(0x1D9E75);

	private static Color bg() {
		Color c = UIManager.getColor("Panel.background");
		return c != null ? c : new Color(0xF5F5F5);
	}

	private static Color panelBg() {
		Color c = UIManager.getColor("Table.background");
		return c != null ? c : Color.WHITE;
	}

	private static Color border() {
		Color c = UIManager.getColor("Component.borderColor");
		return c != null ? c : new Color(0xE8E8E8);
	}

	private static Color textPri() {
		Color c = UIManager.getColor("Label.foreground");
		return c != null ? c : new Color(0x1A1A1A);
	}

	private static Color textSec() {
		Color c = UIManager.getColor("Label.disabledForeground");
		return c != null ? c : new Color(0x999999);
	}

	private final PeliculaDAO peliculaDAO = new PeliculaDAO();
	private final NavController controller;

	public CatalogoPanel(NavController controller) {
		this.controller = controller;
		setBackground(bg());
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(16, 18, 16, 18));

		add(buildToolbar(), BorderLayout.NORTH);
		add(buildTablaPeliculas(), BorderLayout.CENTER);
	}

	private JPanel buildToolbar() {
		JPanel bar = new JPanel(new BorderLayout(10, 0));
		bar.setOpaque(false);
		bar.setBorder(new EmptyBorder(0, 0, 12, 0));

		JLabel title = new JLabel("Catálogo de películas");
		title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
		title.setForeground(textPri());

		JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		right.setOpaque(false);

		String[] filtros = { "Todas", "Acción", "Drama", "Comedia", "Sci-Fi" };
		JComboBox<String> combo = new JComboBox<>(filtros);
		combo.setFont(combo.getFont().deriveFont(12f));
		combo.setPreferredSize(new Dimension(130, 30));

		right.add(combo);
		right.add(buildPrimaryButton("+ Añadir película"));

		bar.add(title, BorderLayout.WEST);
		bar.add(right, BorderLayout.EAST);
		return bar;
	}

	public JPanel buildTablaPeliculas() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(panelBg());
		panel.setBorder(BorderFactory.createLineBorder(border(), 1, true));

		// Cabecera dinámica con el contador real de la Base de Datos
		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(panelBg());
		header.setBorder(new EmptyBorder(12, 14, 10, 14));
		
		JLabel title = new JLabel("Películas disponibles (" + peliculaDAO.contarTotal() + ")");
		title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
		title.setForeground(textPri());
		
		header.add(title, BorderLayout.WEST);
		panel.add(header, BorderLayout.NORTH);

		// Definición de columnas orientadas a Películas
		String[] cols = { "#", "Título", "Director", "Género", "Duración", "Clasificación" };
		List<Pelicula> peliculas = peliculaDAO.getAll();

		DefaultTableModel model = new DefaultTableModel(cols, 0) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		// Llenado de filas mapeando los getters reales de Pelicula.java
		for (Pelicula p : peliculas) {
			model.addRow(new Object[] { 
				p.getId(), 
				p.getNombre(), 
				p.getDirector(),
				p.getGenero(), 
				p.getDuracion() + " min", 
				p.getClasificacionEdad() 
			});
		}

		JTable table = new JTable(model);
		table.setFont(table.getFont().deriveFont(12f));
		table.setRowHeight(34);
		table.getTableHeader().setFont(table.getFont().deriveFont(Font.BOLD, 11f));
		table.getTableHeader().setForeground(textSec());
		table.getTableHeader().setBackground(bg());
		table.setShowHorizontalLines(true);
		table.setGridColor(border());
		table.setSelectionBackground(new Color(0xE1F5EE));
		table.setSelectionForeground(textPri());
		table.setIntercellSpacing(new Dimension(0, 0));

		// Ajuste proporcional de anchos de columna para el contenido de películas
		int[] widths = { 40, 210, 140, 110, 80, 90 };
		for (int i = 0; i < widths.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

		// Reutilizamos tu sistema de "Badges" estéticos adaptándolos a la Clasificación de Edad
		table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
				String clasificacion = (val != null) ? val.toString() : "Apta";
				JLabel lbl = new JLabel(clasificacion);
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
				lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 10f));
				lbl.setOpaque(true);
				lbl.setBorder(new EmptyBorder(3, 8, 3, 8));
				
				String lower = clasificacion.toLowerCase();
				if (lower.contains("18") || lower.contains("r") || lower.contains("adult")) {
					// Rojo / Restringido
					lbl.setBackground(new Color(0xFCEBEB));
					lbl.setForeground(new Color(0x791F1F));
				} else if (lower.contains("12") || lower.contains("13") || lower.contains("16") || lower.contains("pg")) {
					// Amarillo / Adolescentes
					lbl.setBackground(new Color(0xFAEEDA));
					lbl.setForeground(new Color(0x633806));
				} else {
					// Verde / Todo público (Apta, G, etc.)
					lbl.setBackground(new Color(0xE1F5EE));
					lbl.setForeground(new Color(0x085041));
				}
				return lbl;
			}
		});

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		panel.add(scroll, BorderLayout.CENTER);
		return panel;
	}

	private JButton buildPrimaryButton(String texto) {
		JButton btn = new JButton(texto) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(getModel().isPressed() ? ACCENT.darker() : ACCENT);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		btn.setFont(btn.getFont().deriveFont(Font.BOLD, 12f));
		btn.setForeground(Color.WHITE);
		btn.setContentAreaFilled(false);
		btn.setBorderPainted(false);
		btn.setFocusPainted(false);
		btn.setBorder(new EmptyBorder(6, 14, 6, 14));
		btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		return btn;
	}
}