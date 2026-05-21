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

    private static final Color ACCENT      = new Color(0x1D9E75);
    private static final Color BADGE_RED   = new Color(0xE50000);
    private static final Color BADGE_WARN  = new Color(0xEF9F27);

    private static Color bg()         { Color c = UIManager.getColor("Panel.background");        return c != null ? c : new Color(0xFAFAFA); }
    private static Color bgHover()    { Color c = UIManager.getColor("List.selectionBackground"); return c != null ? c : new Color(0xEFEFEF); }
    private static Color bgActive()   { return new Color(0xE1F5EE); }
    private static Color textNav()    { Color c = UIManager.getColor("Label.foreground");         return c != null ? c : new Color(0x555555); }
    private static Color textActive() { return new Color(0x0F6E56); }
    private static Color textLabel()  { Color c = UIManager.getColor("Label.disabledForeground"); return c != null ? c : new Color(0xAAAAAA); }
    private static Color borderCol()  { Color c = UIManager.getColor("Component.borderColor");    return c != null ? c : new Color(0xDDDDDD); }

    private final NavController controller;
    private final Map<Seccion, JPanel> navItems = new LinkedHashMap<>();
    private Seccion seccionActiva = Seccion.DASHBOARD;

    public Sidebar(NavController controller) {
        this.controller = controller;
        setPreferredSize(new Dimension(190, 0));
        setBackground(bg());
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, borderCol()));

        add(buildLogo(), BorderLayout.NORTH);
        add(buildNav(),  BorderLayout.CENTER);
        add(buildUserArea(), BorderLayout.SOUTH);
    }

    private JPanel buildLogo() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(bg());
        p.setBorder(new EmptyBorder(14, 14, 12, 14));
        p.add(buildLogoIcon());
        p.add(Box.createHorizontalStrut(10));

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setBackground(bg());

        JLabel name = new JLabel("RentFlix");
        name.setFont(name.getFont().deriveFont(Font.BOLD, 15f));
        name.setForeground(UIManager.getColor("Label.foreground") != null
                ? UIManager.getColor("Label.foreground") : new Color(0x1A1A1A));

        JLabel sub = new JLabel("v1.0 · DAM 2025");
        sub.setFont(sub.getFont().deriveFont(Font.PLAIN, 10f));
        sub.setForeground(textLabel());

        texts.add(name);
        texts.add(sub);
        p.add(texts);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(bg());
        wrapper.add(p, BorderLayout.CENTER);
        wrapper.add(new JSeparator(), BorderLayout.SOUTH);
        return wrapper;
    }

    private JPanel buildLogoIcon() {
        JPanel icon = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                FontMetrics fm = g2.getFontMetrics();
                String t = "RF";
                g2.drawString(t, (getWidth() - fm.stringWidth(t)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        icon.setPreferredSize(new Dimension(32, 32));
        icon.setOpaque(false);
        return icon;
    }

    private JPanel buildNav() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(bg());
        nav.setBorder(new EmptyBorder(10, 0, 10, 0));

        // 1. Comprobamos el rol del usuario actual
        security.SesionManager sesion = security.SesionManager.getInstancia();
        boolean esAdmin = sesion.esAdmin();

        // 2. Botones públicos (Los ven todos: Clientes y Administradores)
        nav.add(buildNavItem(Seccion.DASHBOARD, "Dashboard", null));
        nav.add(Box.createVerticalStrut(4));
        
        nav.add(buildNavItem(Seccion.CATALOGO, "Catálogo", null));
        nav.add(Box.createVerticalStrut(4));

        // 3. FILTRO DE SEGURIDAD: Solo se añaden al menú si es Administrador
        if (esAdmin) {
            nav.add(buildNavItem(Seccion.CLIENTES, "Clientes", null));
            nav.add(Box.createVerticalStrut(4));
            
            nav.add(buildNavItem(Seccion.ALQUILERES, "Alquileres", null));
            nav.add(Box.createVerticalStrut(4));
            
            nav.add(buildNavItem(Seccion.INVENTARIO, "Inventario", null));
            nav.add(Box.createVerticalStrut(4));
            
            nav.add(buildNavItem(Seccion.INFORMES, "Informes", null));
            nav.add(Box.createVerticalStrut(4));
        }

        // 4. Otro botón público al final
        nav.add(buildNavItem(Seccion.AJUSTES, "Ajustes", null));
        nav.add(Box.createVerticalStrut(4));

        return nav;
    }

    private JLabel buildSectionLabel(String texto) {
        JLabel lbl = new JLabel(texto.toUpperCase());
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 10f));
        lbl.setForeground(textLabel());
        lbl.setBorder(new EmptyBorder(4, 6, 4, 0));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel buildNavItem(Seccion seccion, String texto, JLabel badge) {
        JPanel item = new JPanel(new BorderLayout());
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        item.setAlignmentX(LEFT_ALIGNMENT);
        item.setBorder(new EmptyBorder(0, 4, 1, 4));
        item.setBackground(bg());

        JPanel inner = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        inner.setOpaque(false);

        JLabel textLabel = new JLabel(texto);
        textLabel.setFont(textLabel.getFont().deriveFont(Font.PLAIN, 13f));
        textLabel.setForeground(textNav());
        inner.add(textLabel);

        item.add(inner, BorderLayout.CENTER);
        if (badge != null) {
            JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
            badgeWrap.setOpaque(false);
            badgeWrap.add(badge);
            item.add(badgeWrap, BorderLayout.EAST);
        }

        if (seccion == seccionActiva) applyActiveStyle(item, textLabel);

        navItems.put(seccion, item);

        item.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                setActivo(seccion);
                controller.navegarA(seccion);
            }
            @Override public void mouseEntered(MouseEvent e) {
                if (seccionActiva != seccion) { item.setBackground(bgHover()); inner.setBackground(bgHover()); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (seccionActiva != seccion) { item.setBackground(bg()); inner.setBackground(bg()); }
            }
        });

        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return item;
    }

    private JLabel buildBadge(String texto, Color bgColor) {
        JLabel badge = new JLabel(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
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
        JPanel anterior = navItems.get(seccionActiva);
        if (anterior != null) {
            anterior.setBackground(bg());
            for (Component c : anterior.getComponents()) {
                c.setBackground(bg());
                if (c instanceof JPanel) {
                    for (Component cc : ((JPanel) c).getComponents()) {
                        if (cc instanceof JLabel) {
                            ((JLabel) cc).setForeground(textNav());
                            ((JLabel) cc).setFont(((JLabel) cc).getFont().deriveFont(Font.PLAIN));
                        }
                    }
                }
            }
        }
        seccionActiva = seccion;
        JPanel nuevo = navItems.get(seccion);
        if (nuevo != null) applyActiveStyle(nuevo, null);
    }

    private void applyActiveStyle(JPanel item, JLabel textLabel) {
        item.setBackground(bgActive());
        for (Component c : item.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(bgActive());
                for (Component cc : ((JPanel) c).getComponents()) {
                    if (cc instanceof JLabel) {
                        ((JLabel) cc).setForeground(textActive());
                        ((JLabel) cc).setFont(((JLabel) cc).getFont().deriveFont(Font.BOLD));
                    }
                }
            }
        }
        if (textLabel != null) {
            textLabel.setForeground(textActive());
            textLabel.setFont(textLabel.getFont().deriveFont(Font.BOLD));
        }
    }

    private JPanel buildUserArea() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(bg());
        wrapper.add(new JSeparator(), BorderLayout.NORTH);

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p.setBackground(bg());

     // --- OBTENER USUARIO ACTUAL DEL SESION MANAGER ---
        model.Cliente usuarioActual = security.SesionManager.getInstancia().getUsuarioLogueado();
        
        // Generar iniciales dinámicas (ej: Juan Pérez -> JP)
        String iniciales = "U";
        if (usuarioActual != null) {
            String n = usuarioActual.getNombre() != null ? usuarioActual.getNombre().trim() : "";
            String a = usuarioActual.getApellido() != null ? usuarioActual.getApellido().trim() : "";
            if (!n.isEmpty() && !a.isEmpty()) {
                iniciales = ("" + n.charAt(0) + a.charAt(0)).toUpperCase();
            } else if (!n.isEmpty()) {
                iniciales = ("" + n.charAt(0)).toUpperCase();
            }
        }
        final String textoAvatar = iniciales;

        // Panel del Avatar circular adaptado
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
                g2.drawString(textoAvatar, (getWidth() - fm.stringWidth(textoAvatar)) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        avatar.setPreferredSize(new Dimension(28, 28));
        avatar.setOpaque(false);
        p.add(avatar);

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setBackground(bg());

        // Nombre real del cliente logueado
        String nombreCompleto = (usuarioActual != null) ? (usuarioActual.getNombre() + " " + usuarioActual.getApellido()) : "Invitado";
        JLabel nombre = new JLabel(nombreCompleto);
        nombre.setFont(nombre.getFont().deriveFont(Font.BOLD, 12f));
        nombre.setForeground(UIManager.getColor("Label.foreground") != null
                ? UIManager.getColor("Label.foreground") : new Color(0x333333));

        // Rol dinámico usando tu método .esAdmin()
        String textoRol = (security.SesionManager.getInstancia().esAdmin()) ? "Administrador" : "Cliente";
        JLabel rol = new JLabel(textoRol);
        rol.setFont(rol.getFont().deriveFont(Font.PLAIN, 10f));

        JPanel texts1 = new JPanel();
        texts1.setLayout(new BoxLayout(texts1, BoxLayout.Y_AXIS));
        texts1.setBackground(bg());

        // 1. Conectamos con el Gestor de Sesión
        security.SesionManager sesion = security.SesionManager.getInstancia();
        
        // 2. Valores por defecto por si acaso no hay sesión activa
        String nombreMostrar = "Invitado";
        String rolMostrar = "Usuario";

        // 3. Si hay un cliente logueado, extraemos sus datos reales
        if (sesion.getUsuarioLogueado() != null) {
            nombreMostrar = sesion.getUsuarioLogueado().getNombre();
            rolMostrar = sesion.esAdmin() ? "Administrador" : "Cliente";
        }

        // 4. Creamos los JLabels con los textos dinámicos
        JLabel nombre1 = new JLabel(nombreMostrar); // <-- Aquí cambia
        nombre1.setFont(nombre1.getFont().deriveFont(Font.BOLD, 12f));
        nombre1.setForeground(UIManager.getColor("Label.foreground") != null
                ? UIManager.getColor("Label.foreground") : new Color(0x333333));

        JLabel rol1 = new JLabel(rolMostrar); // <-- Aquí cambia
        rol1.setFont(rol1.getFont().deriveFont(Font.PLAIN, 10f));
        rol1.setForeground(textLabel());

        texts1.add(nombre1); texts1.add(rol1);
        p.add(texts1);

        wrapper.add(p, BorderLayout.CENTER);
        return wrapper;
    }
}
