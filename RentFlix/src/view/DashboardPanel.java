package view;

import controller.NavController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private static final Color ACCENT = new Color(0x1D9E75);

    private static Color bg()      { Color c = UIManager.getColor("Panel.background");        return c != null ? c : new Color(0xF5F5F5); }
    private static Color panelBg() { Color c = UIManager.getColor("Table.background");         return c != null ? c : Color.WHITE; }
    private static Color border()  { Color c = UIManager.getColor("Component.borderColor");    return c != null ? c : new Color(0xE8E8E8); }
    private static Color textPri() { Color c = UIManager.getColor("Label.foreground");         return c != null ? c : new Color(0x1A1A1A); }
    private static Color textSec() { Color c = UIManager.getColor("Label.disabledForeground"); return c != null ? c : new Color(0x999999); }

    public DashboardPanel(NavController controller) {
        setLayout(new BorderLayout());
        setBackground(bg());

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(bg());
        inner.setBorder(new EmptyBorder(16, 18, 16, 18));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx   = 0;
        gbc.weightx = 1.0;
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.insets  = new Insets(0, 0, 14, 0);

        gbc.gridy = 0; gbc.weighty = 0;
        inner.add(buildStatsRow(), gbc);

        gbc.gridy = 1; gbc.weighty = 1.0;
        gbc.fill  = GridBagConstraints.BOTH;
        inner.add(buildMainRow(), gbc);

        gbc.gridy = 2; gbc.weighty = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        inner.add(buildFormPanel(), gbc);

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(bg());
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 10, 0));
        row.setOpaque(false);

        row.add(buildStatCard("Películas totales",  "142",   "+ 8 este mes",  new Color(0xE1F5EE), new Color(0x1D9E75)));
        row.add(buildStatCard("Clientes activos",   "87",    "+ 5 nuevos",    new Color(0xEEEDFE), new Color(0x534AB7)));
        row.add(buildStatCard("Alquileres activos", "24",    "3 con retraso", new Color(0xFAEEDA), new Color(0xBA7517)));
        row.add(buildStatCard("Ingresos mayo",      "312 €", "+ 18%",         new Color(0xE1F5EE), new Color(0x1D9E75)));

        return row;
    }

    private JPanel buildStatCard(String label, String valor, String delta, Color iconBg, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(panelBg());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(border(), 1, true),
                new EmptyBorder(13, 14, 13, 14)
        ));

        JPanel icon = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(iconColor);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14f));
                FontMetrics fm = g2.getFontMetrics();
                String t = "◆";
                g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        icon.setPreferredSize(new Dimension(30, 30));
        icon.setOpaque(false);

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 11f));
        lbl.setForeground(textSec());

        JLabel val = new JLabel(valor);
        val.setFont(val.getFont().deriveFont(Font.BOLD, 22f));
        val.setForeground(textPri());

        JLabel dlt = new JLabel(delta);
        dlt.setFont(dlt.getFont().deriveFont(Font.PLAIN, 11f));
        dlt.setForeground(iconColor);

        texts.add(lbl); texts.add(val); texts.add(dlt);
        card.add(icon, BorderLayout.EAST);
        card.add(texts, BorderLayout.CENTER);
        return card;
    }

    private JPanel buildMainRow() {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setOpaque(false);
        row.add(buildTablaAlquileres(), BorderLayout.CENTER);
        row.add(buildMasAlquiladas(),   BorderLayout.EAST);
        return row;
    }

    private JPanel buildTablaAlquileres() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(panelBg());
        panel.setBorder(BorderFactory.createLineBorder(border(), 1, true));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(panelBg());
        header.setBorder(new EmptyBorder(12, 14, 10, 14));

        JLabel title = new JLabel("Alquileres recientes");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
        title.setForeground(textPri());

        JLabel link = new JLabel("Ver todos →");
        link.setFont(link.getFont().deriveFont(Font.PLAIN, 11f));
        link.setForeground(ACCENT);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        header.add(title, BorderLayout.WEST);
        header.add(link,  BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        String[] cols = {"#", "Película", "Cliente", "Devolución", "Estado", "Precio"};
        Object[][] data = {
            {"101", "Dune: Parte Dos",  "García, M.",   "20 may", "Activo",   "5,00 €"},
            {"100", "Oppenheimer",      "López, J.",    "16 may", "Retraso",  "7,50 €"},
            {"099", "Barbie",           "Martínez, A.", "19 may", "Activo",   "5,00 €"},
            {"098", "Avatar 2",         "Ruiz, C.",     "14 may", "Devuelto", "5,00 €"},
            {"097", "Gladiator II",     "Sánchez, P.",  "13 may", "Devuelto", "5,00 €"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

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

        int[] widths = {40, 160, 120, 90, 80, 70};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(val.toString());
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 10f));
                lbl.setOpaque(true);
                lbl.setBorder(new EmptyBorder(3, 8, 3, 8));
                switch (val.toString()) {
                    case "Activo":   lbl.setBackground(new Color(0xE1F5EE)); lbl.setForeground(new Color(0x085041)); break;
                    case "Retraso":  lbl.setBackground(new Color(0xFAEEDA)); lbl.setForeground(new Color(0x633806)); break;
                    default:         lbl.setBackground(new Color(0xFCEBEB)); lbl.setForeground(new Color(0x791F1F));
                }
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMasAlquiladas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(panelBg());
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBorder(BorderFactory.createLineBorder(border(), 1, true));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(panelBg());
        header.setBorder(new EmptyBorder(12, 14, 10, 14));

        JLabel title = new JLabel("Más alquiladas");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
        title.setForeground(textPri());
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setBackground(panelBg());
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(4, 8, 8, 8));

        String[][] peliculas = {
            {"Dune 2",      "Sci-Fi · 2024",  "#E1F5EE", "#1D9E75"},
            {"Oppenheimer", "Drama · 2023",   "#FCEBEB", "#E24B4A"},
            {"Barbie",      "Comedia · 2023", "#EEEDFE", "#534AB7"},
            {"Avatar 2",    "Acción · 2022",  "#FAEEDA", "#BA7517"},
        };

        for (String[] p : peliculas) {
            list.add(buildMiniItem(p[0], p[1], p[2], p[3]));
            list.add(Box.createVerticalStrut(2));
        }

        panel.add(list, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMiniItem(String titulo, String genero, String bgHex, String dotHex) {
        JPanel item = new JPanel(new BorderLayout(8, 0));
        item.setBackground(panelBg());
        item.setBorder(new EmptyBorder(6, 6, 6, 6));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JPanel cover = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode(bgHex));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(Color.decode(dotHex));
                g2.setFont(g2.getFont().deriveFont(18f));
                g2.drawString("▶", 5, 26);
            }
        };
        cover.setPreferredSize(new Dimension(28, 38));
        cover.setOpaque(false);

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel tit = new JLabel(titulo);
        tit.setFont(tit.getFont().deriveFont(Font.BOLD, 12f));
        tit.setForeground(textPri());

        JLabel gen = new JLabel(genero);
        gen.setFont(gen.getFont().deriveFont(Font.PLAIN, 10f));
        gen.setForeground(textSec());

        texts.add(tit); texts.add(gen);

        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode(dotHex));
                g2.fillOval(0, (getHeight() - 8) / 2, 8, 8);
            }
        };
        dot.setPreferredSize(new Dimension(8, 38));
        dot.setOpaque(false);

        item.add(cover, BorderLayout.WEST);
        item.add(texts, BorderLayout.CENTER);
        item.add(dot,   BorderLayout.EAST);
        return item;
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(panelBg());
        panel.setBorder(BorderFactory.createLineBorder(border(), 1, true));

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(panelBg());
        header.setBorder(new EmptyBorder(10, 14, 6, 14));

        JLabel title = new JLabel("Registrar nuevo alquiler");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
        title.setForeground(textPri());
        header.add(title);
        panel.add(header, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridLayout(1, 4, 10, 0));
        fields.setBackground(panelBg());
        fields.setBorder(new EmptyBorder(4, 14, 12, 14));

        String[] clientes  = {"García Martínez, María", "López Ruiz, Juan", "Sánchez Pérez, Ana"};
        String[] peliculas = {"Dune: Parte Dos (2024)", "Oppenheimer (2023)", "Barbie (2023)"};

        fields.add(buildField("Cliente",          new JComboBox<>(clientes)));
        fields.add(buildField("Película",         new JComboBox<>(peliculas)));
        fields.add(buildField("Fecha alquiler",   new JTextField("18/05/2026")));
        fields.add(buildField("Fecha devolución", new JTextField("21/05/2026")));

        panel.add(fields, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actions.setBackground(panelBg());
        actions.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, border()));
        actions.add(buildSecondaryButton("Cancelar"));
        actions.add(buildPrimaryButton("✓  Confirmar alquiler"));
        panel.add(actions, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildField(String label, JComponent input) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 11f));
        lbl.setForeground(textSec());

        input.setFont(input.getFont().deriveFont(12f));
        if (input instanceof JTextField) {
            ((JTextField) input).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(border(), 1, true),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }

        p.add(lbl,   BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private JButton buildSecondaryButton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(btn.getFont().deriveFont(12f));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
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
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
