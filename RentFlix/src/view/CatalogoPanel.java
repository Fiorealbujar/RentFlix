package view;

import controller.NavController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CatalogoPanel extends JPanel {

    private static final Color ACCENT = new Color(0x1D9E75);

    private static Color bg()      { Color c = UIManager.getColor("Panel.background");        return c != null ? c : new Color(0xF5F5F5); }
    private static Color panelBg() { Color c = UIManager.getColor("Table.background");         return c != null ? c : Color.WHITE; }
    private static Color border()  { Color c = UIManager.getColor("Component.borderColor");    return c != null ? c : new Color(0xE8E8E8); }
    private static Color textPri() { Color c = UIManager.getColor("Label.foreground");         return c != null ? c : new Color(0x1A1A1A); }
    private static Color textSec() { Color c = UIManager.getColor("Label.disabledForeground"); return c != null ? c : new Color(0x999999); }

    public CatalogoPanel(NavController controller) {
        setBackground(bg());
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(16, 18, 16, 18));

        add(buildToolbar(), BorderLayout.NORTH);
        add(buildTabla(),   BorderLayout.CENTER);
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

        String[] filtros = {"Todas", "Disponibles", "Alquiladas"};
        JComboBox<String> combo = new JComboBox<>(filtros);
        combo.setFont(combo.getFont().deriveFont(12f));
        combo.setPreferredSize(new Dimension(130, 30));

        right.add(combo);
        right.add(buildPrimaryButton("+ Añadir película"));

        bar.add(title, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(panelBg());
        panel.setBorder(BorderFactory.createLineBorder(border(), 1, true));

        String[] cols = {"ID", "Título", "Género", "Director", "Año", "Precio/día", "Estado"};
        Object[][] data = {
            {"001", "Dune: Parte Dos",  "Sci-Fi",   "Denis Villeneuve",  "2024", "2,50 €", "Disponible"},
            {"002", "Oppenheimer",      "Drama",    "Christopher Nolan", "2023", "2,50 €", "Alquilada"},
            {"003", "Barbie",           "Comedia",  "Greta Gerwig",      "2023", "2,50 €", "Disponible"},
            {"004", "Avatar 2",         "Acción",   "James Cameron",     "2022", "2,50 €", "Disponible"},
            {"005", "Gladiator II",     "Acción",   "Ridley Scott",      "2024", "2,50 €", "Disponible"},
            {"006", "Wonka",            "Familiar", "Paul King",         "2023", "2,50 €", "Alquilada"},
            {"007", "Poor Things",      "Drama",    "Yorgos Lanthimos",  "2023", "2,50 €", "Disponible"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setFont(table.getFont().deriveFont(12f));
        table.setRowHeight(36);
        table.getTableHeader().setFont(table.getFont().deriveFont(Font.BOLD, 11f));
        table.getTableHeader().setForeground(textSec());
        table.getTableHeader().setBackground(bg());
        table.setShowHorizontalLines(true);
        table.setGridColor(border());
        table.setSelectionBackground(new Color(0xE1F5EE));
        table.setSelectionForeground(textPri());

        int[] widths = {40, 180, 90, 160, 50, 80, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(val.toString());
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 10f));
                lbl.setOpaque(true);
                lbl.setBorder(new EmptyBorder(3, 8, 3, 8));
                if ("Disponible".equals(val.toString())) {
                    lbl.setBackground(new Color(0xE1F5EE)); lbl.setForeground(new Color(0x085041));
                } else {
                    lbl.setBackground(new Color(0xFCEBEB)); lbl.setForeground(new Color(0x791F1F));
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
            @Override protected void paintComponent(Graphics g) {
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
