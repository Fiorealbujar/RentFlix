package view;

import controller.NavController;
import controller.NavController.Seccion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class Sidebar extends JPanel {

    // Colores del tema light
    private static final Color BG           = new Color(0xFAFAFA);
    private static final Color BG_HOVER     = new Color(0xEFEFEF);
    private static final Color BG_ACTIVE    = new Color(0xE1F5EE);
    private static final Color TEXT_NAV     = new Color(0x555555);
    private static final Color TEXT_ACTIVE  = new Color(0x0F6E56);
    private static final Color TEXT_LABEL   = new Color(0xAAAAAA);
    private static final Color ACCENT       = new Color(0x1D9E75);
    private static final Color BORDER       = new Color(0xDDDDDD);
    private static final Color BADGE_RED    = new Color(0xE50000);
    private static final Color BADGE_WARN   = new Color(0xEF9F27);

    private final NavController controller;
    private final Map<Seccion, JPanel> navItems = new LinkedHashMap<>();
    private Seccion seccionActiva = Seccion.DASHBOARD;

    public Sidebar(NavController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(190, 0));
        setBackground(BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        add(buildLogo(), BorderLayout.NORTH);
        add(buildNav(), BorderLayout.CENTER);
        add(buildUserArea(), BorderLayout.SOUTH);
    }

    // ── Logo ────────────────────────────────────────────────────────────────

    private JPanel buildLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(14, 14, 12, 14));
        p.add(buildLogoIcon());
        p.add(Box.createHorizontalStrut(10));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setBackground(BG);

        JLabel name = new JLabel("RentFlix");
        name.setFont(name.getFont().deriveFont(Font.BOLD, 15f));
        name.setForeground(new Color(0x1A1A1A));

        JLabel sub = new JLabel("v1.0 · DAM 2025");
        sub.setFont(sub.getFont().deriveFont(Font.PLAIN, 10f));
        sub.setForeground(TEXT_LABEL);

        texts.add(name);
        texts.add(sub);
        p.add(texts);

        // Separador inferior
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.add(p, BorderLayout.CENTER);
        wrapper.add(new JSeparator(), BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildLogoIcon() {
        JPanel icon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                FontMetrics fm = g2.getFontMetrics();
                String t = "RF";
                int x = (getWidth() - fm.stringWidth(t)) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(t, x, y);
            }
        };
        icon.setPreferredSize(new Dimension(32, 32));
        icon.setOpaque(false);
        return icon;
    }

    // ── Navegación ──────────────────────────────────────────────────────────

    private JPanel buildNav() {
        JPanel nav = new JPanel();
        nav.setBackground(BG);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBorder(new EmptyBorder(10, 8, 4, 8));

        nav.add(buildSectionLabel("Principal"));
        nav.add(buildNavItem(Seccion.DASHBOARD,   "Inicio",      null));
        nav.add(buildNavItem(Seccion.CATALOGO,    "Catálogo",    buildBadge("142", BADGE_RED)));
        nav.add(buildNavItem(Seccion.CLIENTES,    "Clientes",    null));
        nav.add(buildNavItem(Seccion.ALQUILERES,  "Alquileres",  buildBadge("3",   BADGE_WARN)));
        nav.add(Box.createVerticalStrut(6));
        nav.add(buildSectionLabel("Gestión"));
        nav.add(buildNavItem(Seccion.INVENTARIO,  "Inventario",  null));
        nav.add(buildNavItem(Seccion.INFORMES,    "Informes",    null));
        nav.add(buildNavItem(Seccion.AJUSTES,     "Ajustes",     null));

        // Rellenar espacio restante
        JPanel filler = new JPanel();
        filler.setBackground(BG);
        nav.add(filler);

        return nav;
    }

    private JLabel buildSectionLabel(String texto) {
        JLabel lbl = new JLabel(texto.toUpperCase());
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 10f));
        lbl.setForeground(TEXT_LABEL);
        lbl.setBorder(new EmptyBorder(4, 6, 4, 0));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel buildNavItem(Seccion seccion, String texto, JLabel badge) {
        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        item.setAlignmentX(LEFT_ALIGNMENT);
        item.setBorder(new EmptyBorder(0, 4, 1, 4));
        item.setBackground(BG);

        JPanel inner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        inner.setOpaque(false);

        JLabel textLabel = new JLabel(texto);
        textLabel.setFont(textLabel.getFont().deriveFont(Font.PLAIN, 13f));
        textLabel.setForeground(TEXT_NAV);
        inner.add(textLabel);

        item.add(inner, BorderLayout.CENTER);
        if (badge != null) {
            JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
            badgeWrap.setOpaque(false);
            badgeWrap.add(badge);
            item.add(badgeWrap, BorderLayout.EAST);
        }

        // Estado activo inicial
        if (seccion == seccionActiva) {
            applyActiveStyle(item, textLabel);
        }

        navItems.put(seccion, item);

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setActivo(seccion);
                controller.navegarA(seccion);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (seccionActiva != seccion) {
                    item.setBackground(BG_HOVER);
                    inner.setBackground(BG_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (seccionActiva != seccion) {
                    item.setBackground(BG);
                    inner.setBackground(BG);
                }
            }
        });

        // Hacer el cursor pointer
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return item;
    }

    private JLabel buildBadge(String texto, Color bg) {
        JLabel badge = new JLabel(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                super.paintComponent(g);
            }
        };
        badge.setFont(badge.getFont().deriveFont(Font.BOLD, 10f));
        badge.setForeground(Color.WHITE);
        badge.setBorder(new EmptyBorder(1, 6, 1, 6));
        badge.setOpaque(false);
        return badge;
    }

    public void setActivo(Seccion seccion) {
        // Resetear el anterior
        JPanel anterior = navItems.get(seccionActiva);
        if (anterior != null) {
            anterior.setBackground(BG);
            for (Component c : anterior.getComponents()) {
                c.setBackground(BG);
                if (c instanceof JPanel) {
                    for (Component cc : ((JPanel) c).getComponents()) {
                        if (cc instanceof JLabel) {
                            ((JLabel) cc).setForeground(TEXT_NAV);
                            ((JLabel) cc).setFont(((JLabel) cc).getFont().deriveFont(Font.PLAIN));
                        }
                    }
                }
            }
        }

        seccionActiva = seccion;
        JPanel nuevo = navItems.get(seccion);
        if (nuevo != null) {
            applyActiveStyle(nuevo, null);
        }
    }

    private void applyActiveStyle(JPanel item, JLabel textLabel) {
        item.setBackground(BG_ACTIVE);
        for (Component c : item.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(BG_ACTIVE);
                for (Component cc : ((JPanel) c).getComponents()) {
                    if (cc instanceof JLabel) {
                        ((JLabel) cc).setForeground(TEXT_ACTIVE);
                        ((JLabel) cc).setFont(((JLabel) cc).getFont().deriveFont(Font.BOLD));
                    }
                }
            }
        }
        if (textLabel != null) {
            textLabel.setForeground(TEXT_ACTIVE);
            textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD));
        }
    }

    // ── Usuario ─────────────────────────────────────────────────────────────

    private JPanel buildUserArea() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.add(new JSeparator(), BorderLayout.NORTH);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p.setBackground(BG);

        // Avatar con iniciales
        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0xEEEDFE));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0x3C3489));
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 11f));
                FontMetrics fm = g2.getFontMetrics();
                String t = "AP";
                g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        avatar.setPreferredSize(new Dimension(28, 28));
        avatar.setOpaque(false);
        p.add(avatar);

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setBackground(BG);

        JLabel nombre = new JLabel("Admin");
        nombre.setFont(nombre.getFont().deriveFont(Font.BOLD, 12f));
        nombre.setForeground(new Color(0x333333));

        JLabel rol = new JLabel("Administrador");
        rol.setFont(rol.getFont().deriveFont(Font.PLAIN, 10f));
        rol.setForeground(TEXT_LABEL);

        texts.add(nombre);
        texts.add(rol);
        p.add(texts);

        wrapper.add(p, BorderLayout.CENTER);
        return wrapper;
    }
}
