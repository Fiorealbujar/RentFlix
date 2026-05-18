package view;

import controller.NavController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TopBar extends JPanel {

    private static final Color BG       = Color.WHITE;
    private static final Color BORDER   = new Color(0xE0E0E0);
    private static final Color ACCENT   = new Color(0x1D9E75);
    private static final Color TEXT_SEC = new Color(0x999999);

    private JLabel pageTitle;

    public TopBar(NavController controller) {
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                new EmptyBorder(8, 18, 8, 18)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));

        // Izquierda: título de página + fecha
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);

        pageTitle = new JLabel("Inicio");
        pageTitle.setFont(pageTitle.getFont().deriveFont(Font.BOLD, 15f));
        pageTitle.setForeground(new Color(0x1A1A1A));

        String fecha = LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", new Locale("es", "ES")));
        fecha = Character.toUpperCase(fecha.charAt(0)) + fecha.substring(1);
        JLabel fechaLabel = new JLabel("  ·  " + fecha);
        fechaLabel.setFont(fechaLabel.getFont().deriveFont(Font.PLAIN, 12f));
        fechaLabel.setForeground(TEXT_SEC);

        left.add(pageTitle);
        left.add(fechaLabel);

        // Derecha: búsqueda + campana + botón
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        right.add(buildSearchBox());
        right.add(buildIconButton("\uD83D\uDD14")); // campana unicode (fallback)
        right.add(buildPrimaryButton("+ Nuevo alquiler", controller));

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }

    private JPanel buildSearchBox() {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(new Color(0xF5F5F5));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));

        JLabel icon = new JLabel("⌕ ");
        icon.setForeground(TEXT_SEC);
        icon.setFont(icon.getFont().deriveFont(14f));

        JTextField field = new JTextField(18);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setBackground(new Color(0xF5F5F5));
        field.setFont(field.getFont().deriveFont(12f));
        field.setForeground(new Color(0x333333));

        // Placeholder manual
        field.setText("Buscar película o cliente...");
        field.setForeground(TEXT_SEC);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals("Buscar película o cliente...")) {
                    field.setText("");
                    field.setForeground(new Color(0x333333));
                }
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText("Buscar película o cliente...");
                    field.setForeground(TEXT_SEC);
                }
            }
        });

        box.add(icon, BorderLayout.WEST);
        box.add(field, BorderLayout.CENTER);
        return box;
    }

    private JButton buildIconButton(String icon) {
        JButton btn = new JButton(icon);
        btn.setFont(btn.getFont().deriveFont(14f));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0x666666));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xDDDDDD), 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton buildPrimaryButton(String texto, NavController controller) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? ACCENT.darker() :
                            getModel().isRollover() ? ACCENT.brighter() : ACCENT);
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

        btn.addActionListener(e ->
            controller.navegarA(NavController.Seccion.ALQUILERES)
        );

        return btn;
    }

    public void setPageTitle(String titulo) {
        pageTitle.setText(titulo);
    }
}
