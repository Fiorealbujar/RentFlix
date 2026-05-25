// ==========================================
// CLASE: VentanaPrincipal.java
// JFrame raíz de la aplicación.
// Gestiona el intercambio de paneles según el rol.
// ==========================================
package view;

import com.formdev.flatlaf.FlatLightLaf;
import controller.Controlador;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    public static final int ANCHO = 1100;
    public static final int ALTO  = 680;

    // Panel central intercambiable
    private JPanel panelContenido;

    // Referencia al controlador (se inyecta después)
    private Controlador controlador;

    // Paneles de la aplicación
    private PanelCatalogo   panelCatalogo;
    private PanelLogin      panelLogin;
    private PanelRegistro   panelRegistro;

    public VentanaPrincipal() {
        super("RentFlix 🎬");
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setSize(ANCHO, ALTO);
        setMinimumSize(new Dimension(900, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrar en pantalla
        setLayout(new BorderLayout());
    }

    private void construirUI() {
        // Barra superior con logo + botones de sesión
        add(buildTopBar(), BorderLayout.NORTH);

        // Panel central donde se cargarán los distintos paneles
        panelContenido = new JPanel(new BorderLayout());
        add(panelContenido, BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(0x1a1a2e));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logo
        JLabel lblLogo = new JLabel("🎬 RentFlix");
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblLogo.setForeground(new Color(0xE50914)); // rojo Netflix

        // Botones de sesión (derecha)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        JButton btnLogin    = buildBotonTopBar("Iniciar sesión");
        JButton btnRegistro = buildBotonTopBar("Registrarse");

        // Los ActionCommands permiten identificarlos en el controlador
        btnLogin.setActionCommand("ABRIR_LOGIN");
        btnRegistro.setActionCommand("ABRIR_REGISTRO");

        panelBotones.add(btnRegistro);
        panelBotones.add(btnLogin);

        topBar.add(lblLogo,       BorderLayout.WEST);
        topBar.add(panelBotones,  BorderLayout.EAST);

        return topBar;
    }

    private JButton buildBotonTopBar(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0xE50914));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Métodos públicos que usa el Controlador ─────────────────────────────

    // Cambia el panel central por el que se le pase
    public void cargarPanel(JPanel panel) {
        panelContenido.removeAll();
        panelContenido.add(panel, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }

    public void hacerVisible() { setVisible(true); }

    // Inyección del controlador: conecta los botones de la topBar
    public void setControlador(Controlador controlador) {
        this.controlador = controlador;
        // Conectamos los botones de la topBar al controlador
        for (Component c : ((JPanel)((BorderLayout)
                ((JPanel) getContentPane().getComponent(0))
                .getLayout()).getLayoutComponent(BorderLayout.EAST))
                .getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).addActionListener(controlador);
            }
        }
    }

    // Método más limpio: pasamos los botones directamente
    public void registrarBotonesTopBar(JButton btnLogin, JButton btnRegistro) {
        btnLogin.addActionListener(controlador);
        btnRegistro.addActionListener(controlador);
    }

    // Getters de paneles
    public PanelCatalogo getPanelCatalogo()   { return panelCatalogo; }
    public PanelLogin    getPanelLogin()       { return panelLogin; }
    public PanelRegistro getPanelRegistro()    { return panelRegistro; }

    public void setPanelCatalogo(PanelCatalogo p)   { this.panelCatalogo  = p; }
    public void setPanelLogin(PanelLogin p)         { this.panelLogin     = p; }
    public void setPanelRegistro(PanelRegistro p)   { this.panelRegistro  = p; }
}