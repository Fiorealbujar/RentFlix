package view;

import controller.NavController;
import model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private static final Color BG        = new Color(0xF5F5F5);
    private static final Color PANEL_BG  = Color.WHITE;
    private static final Color BORDER    = new Color(0xE8E8E8);
    private static final Color ACCENT    = new Color(0x1D9E75);
    private static final Color TEXT_PRI  = new Color(0x1A1A1A);
    private static final Color TEXT_SEC  = new Color(0x999999);

    public DashboardPanel(NavController controller) {
        setBackground(BG);
        setLayout(new BorderLayout());

        // Área con scroll para todo el contenido
        JPanel inner = new JPanel();
        inner.setBackground(BG);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(16, 18, 16, 18));

        inner.add(buildStatsRow());
        inner.add(Box.createVerticalStrut(14));
       //  inner.add(buildMainRow());
        inner.add(Box.createVerticalStrut(14));
        inner.add(buildFormPanel());

        JScrollPane scroll = new JScrollPane(inner);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    // ── Tarjetas de estadísticas ─────────────────────────────────────────────

    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        /*  row.add(buildStatCard("Películas totales",  "142",    "+ 8 este mes",   new Color(0xE1F5EE), new Color(0x1D9E75)));
        row.add(buildStatCard("Clientes activos",   "87",     "+ 5 nuevos",     new Color(0xEEEDFE), new Color(0x534AB7)));
        row.add(buildStatCard("Alquileres activos", "24",     "3 con retraso",  new Color(0xFAEEDA), new Color(0xBA7517)));
        row.add(buildStatCard("Ingresos mayo",      "312 €",  "+ 18%",          new Color(0xE1F5EE), new Color(0x1D9E75)));
*/
        return row;
    }

    private JPanel buildStatCard(String label, String valor, String delta,
                                  Color iconBg, Color iconColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(13, 14, 13, 14)
        ));

        // Icono circular a la derecha
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

        // Textos
        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        JLabel lbl = new JLabel(label);
        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 11f));
        lbl.setForeground(TEXT_SEC);

        JLabel val = new JLabel(valor);
        val.setFont(val.getFont().deriveFont(Font.BOLD, 22f));
        val.setForeground(TEXT_PRI);

        JLabel dlt = new JLabel(delta);
        dlt.setFont(dlt.getFont().deriveFont(Font.PLAIN, 11f));
        dlt.setForeground(iconColor);

        texts.add(lbl);
        texts.add(val);
        texts.add(dlt);

        card.add(icon, BorderLayout.EAST);
        card.add(texts, BorderLayout.CENTER);
        return card;
    }

    // ── Fila central: tabla + panel lateral ─────────────────────────────────

    /*private JPanel buildMainRow() {
        JPanel row = new JPanel(new BorderLayout(14, 0));
        row.setOpaque(false);

        row.add(buildTablaAlquileres(), BorderLayout.CENTER);
        row.add(buildMasAlquiladas(),   BorderLayout.EAST);

        return row;
    }

    private JPanel buildTablaAlquileres() {
		return null;}
    	/*JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));

        // Cabecera
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.setBorder(new EmptyBorder(12, 14, 10, 14));
        JLabel title = new JLabel("Alquileres recientes");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
        title.setForeground(TEXT_PRI);
        JLabel link = new JLabel("Ver todos →");
        link.setFont(link.getFont().deriveFont(Font.PLAIN, 11f));
        link.setForeground(ACCENT);
        link.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        header.add(title, BorderLayout.WEST);
        header.add(link, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // Tabla
       String[] cols = {"#", "Película", "Cliente", "Devolución", "Estado", "Precio"};
        Object[][] data = {
            {"101", "Dune: Parte Dos",  "García, M.",    "20 may", "Activo",   "5,00 €"},
            {"100", "Oppenheimer",      "López, J.",     "16 may", "Retraso",  "7,50 €"},
            {"099", "Barbie",           "Martínez, A.",  "19 may", "Activo",   "5,00 €"},
            {"098", "Avatar 2",         "Ruiz, C.",      "14 may", "Devuelto", "5,00 €"},
            {"097", "Gladiator II",     "Sánchez, P.",   "13 may", "Devuelto", "5,00 €"},
        };

        DefaultTableModel model = new DefaultTableModel(data, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setFont(table.getFont().deriveFont(12f));
        table.setRowHeight(34);
        table.getTableHeader().setFont(table.getFont().deriveFont(Font.BOLD, 11f));
        table.getTableHeader().setForeground(TEXT_SEC);
        table.getTableHeader().setBackground(new Color(0xFAFAFA));
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(0xF5F5F5));
        table.setSelectionBackground(new Color(0xE1F5EE));
        table.setSelectionForeground(TEXT_PRI);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Anchos de columna
        int[] widths = {40, 160, 120, 90, 80, 70};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        // Renderer para badges de estado
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
                    case "Activo":
                        lbl.setBackground(new Color(0xE1F5EE));
                        lbl.setForeground(new Color(0x085041));
                        break;
                    case "Retraso":
                        lbl.setBackground(new Color(0xFAEEDA));
                        lbl.setForeground(new Color(0x633806));
                        break;
                    default:
                        lbl.setBackground(new Color(0xFCEBEB));
                        lbl.setForeground(new Color(0x791F1F));
                }
                return lbl;
            }
        });

        panel.add(new JScrollPane(table) {{
            setBorder(BorderFactory.createEmptyBorder());
        }}, BorderLayout.CENTER);

        return panel;
    }*/

    private JPanel buildMasAlquiladas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.setPreferredSize(new Dimension(220, 0));
        panel.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PANEL_BG);
        header.setBorder(new EmptyBorder(12, 14, 10, 14));
        JLabel title = new JLabel("Más alquiladas");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
        title.setForeground(TEXT_PRI);
        header.add(title, BorderLayout.WEST);
        panel.add(header, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setBackground(PANEL_BG);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBorder(new EmptyBorder(4, 8, 8, 8));

        /*String[][] peliculas = {
            {"Dune 2",       "Sci-Fi · 2024",   "#E1F5EE", "#1D9E75"},
            {"Oppenheimer",  "Drama · 2023",    "#FCEBEB", "#E24B4A"},
            {"Barbie",       "Comedia · 2023",  "#EEEDFE", "#534AB7"},
            {"Avatar 2",     "Acción · 2022",   "#FAEEDA", "#BA7517"},
        };

        for (String[] peli : peliculas) {
            list.add(buildMiniItem(peli[0], peli[1], peli[2], peli[3]));
            list.add(Box.createVerticalStrut(2));
        }

        panel.add(list, BorderLayout.CENTER);*/
        return panel;
    }

    private JPanel buildMiniItem(String titulo, String genero, String bgHex, String dotHex) {
        JPanel item = new JPanel(new BorderLayout(8, 0));
        item.setBackground(PANEL_BG);
        item.setBorder(new EmptyBorder(6, 6, 6, 6));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        // Cover placeholder
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
        tit.setForeground(TEXT_PRI);

        JLabel gen = new JLabel(genero);
        gen.setFont(gen.getFont().deriveFont(Font.PLAIN, 10f));
        gen.setForeground(TEXT_SEC);

        texts.add(tit);
        texts.add(gen);

        // Punto de disponibilidad
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

        item.add(cover,  BorderLayout.WEST);
        item.add(texts,  BorderLayout.CENTER);
        item.add(dot,    BorderLayout.EAST);
        return item;
    }

    // ── Formulario rápido de alquiler ────────────────────────────────────────

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_BG);
        panel.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));

        // Cabecera
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(PANEL_BG);
        header.setBorder(new EmptyBorder(10, 14, 6, 14));
        JLabel title = new JLabel("Registrar nuevo alquiler");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 13f));
        title.setForeground(TEXT_PRI);
        header.add(title);
        panel.add(header, BorderLayout.NORTH);

        // Campos en grid 2x2
        JPanel fields = new JPanel(new GridLayout(1, 4, 10, 0));
        fields.setBackground(PANEL_BG);
        fields.setBorder(new EmptyBorder(4, 14, 12, 14));

       /* String[] clientesEjemplo = {"García Martínez, María", "López Ruiz, Juan", "Sánchez Pérez, Ana"};
        String[] peliculasEjemplo = {"Dune: Parte Dos (2024)", "Oppenheimer (2023)", "Barbie (2023)"};

        fields.add(buildField("Cliente",           new JComboBox<>(clientesEjemplo)));
        fields.add(buildField("Película",          new JComboBox<>(peliculasEjemplo)));
        fields.add(buildField("Fecha alquiler",    new JTextField("18/05/2026")));
        fields.add(buildField("Fecha devolución",  new JTextField("21/05/2026")));*/

        panel.add(fields, BorderLayout.CENTER);

        // Botones
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        actions.setBackground(PANEL_BG);
        actions.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xF0F0F0)),
                new EmptyBorder(2, 14, 2, 14)
        ));
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
        lbl.setForeground(TEXT_SEC);

        input.setFont(input.getFont().deriveFont(12f));
        if (input instanceof JTextField) {
            ((JTextField) input).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                    new EmptyBorder(5, 8, 5, 8)
            ));
        }

        p.add(lbl, BorderLayout.NORTH);
        p.add(input, BorderLayout.CENTER);
        return p;
    }

    private JButton buildSecondaryButton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(btn.getFont().deriveFont(12f));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0x555555));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                new EmptyBorder(5, 14, 5, 14)
        ));
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
