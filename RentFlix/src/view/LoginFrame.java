package view;

import com.formdev.flatlaf.FlatClientProperties;
import dao.ClienteDAO;
import model.Cliente;
import security.SesionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public LoginFrame() {
        setTitle("RentFlix — Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setResizable(false);

        // Contenedor principal con margen
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Cabecera / Logo
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 4, 4));
        JLabel lblTitulo = new JLabel("RentFlix", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 28f));
        lblTitulo.setForeground(new Color(0x1D9E75)); // Tu color ACCENT

        JLabel lblSubtitulo = new JLabel("Ingresa tus credenciales para continuar", SwingConstants.CENTER);
        lblSubtitulo.setFont(lblSubtitulo.getFont().deriveFont(12f));
        lblSubtitulo.putClientProperty(FlatClientProperties.STYLE, "foreground: $Label.disabledForeground");

        panelHeader.add(lblTitulo);
        panelHeader.add(lblSubtitulo);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // Formulario central
        JPanel panelForm = new JPanel(new GridLayout(4, 1, 0, 10));
        panelForm.setBorder(new EmptyBorder(30, 0, 30, 0));

        txtUsuario = new JTextField();
        // Truco FlatLaf: Marcador de posición (Placeholder)
        txtUsuario.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nombre de usuario");
        txtUsuario.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contraseña");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        panelForm.add(new JLabel("Usuario:"));
        panelForm.add(txtUsuario);
        panelForm.add(new JLabel("Contraseña:"));
        panelForm.add(txtPassword);

        panelPrincipal.add(panelForm, BorderLayout.CENTER);

        // Botón inferior
        btnIngresar = new JButton("Iniciar Sesión");
        btnIngresar.setFont(btnIngresar.getFont().deriveFont(Font.BOLD, 14f));
        // Truco FlatLaf: Estilo de botón primario
        btnIngresar.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        btnIngresar.setBackground(new Color(0x1D9E75));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnIngresar.addActionListener(e -> ejecutarLogin());
        panelPrincipal.add(btnIngresar, BorderLayout.SOUTH);

        add(panelPrincipal);
        
        // Permitir hacer login pulsando Enter
        getRootPane().setDefaultButton(btnIngresar);
    }

    private void ejecutarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, completa todos los campos.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dao.EmpleadoDAO empleadoDAO = new dao.EmpleadoDAO();
        model.Empleado empleado = empleadoDAO.login(usuario, password);

        // 1. Comprobamos primero si el usuario es un Empleado / Administrador
        if (empleado != null) {
            // Mapeamos el empleado a un objeto Cliente temporal para el SesionManager
            Cliente adminMapeado = new Cliente(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getEmail(),
                empleado.getUsuario(),
                empleado.getContrasenia()
            );
            
            SesionManager.getInstancia().iniciarSesion(adminMapeado);

            SwingUtilities.invokeLater(() -> {
                MainFrame mf = new MainFrame();
                mf.setVisible(true);
            });
            this.dispose();

        } else {
            // 2. Si no es un empleado, comprobamos si es un Cliente regular
            Cliente cliente = clienteDAO.login(usuario, password);

            if (cliente != null) {
                SesionManager.getInstancia().iniciarSesion(cliente);

                SwingUtilities.invokeLater(() -> {
                    MainFrame mf = new MainFrame();
                    mf.setVisible(true);
                });
                this.dispose();
            } else {
                // 3. No existe en ninguna de las dos tablas
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}