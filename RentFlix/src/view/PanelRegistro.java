// ==========================================
// CLASE: PanelRegistro.java
// Formulario de registro para nuevos clientes.
// ==========================================
package view;

import controller.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRegistro extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_ACENTO = new Color(0xE50914);

    private JTextField     txtNombre;
    private JTextField     txtApellido;
    private JTextField     txtEmail;
    private JTextField     txtUsuario;
    private JPasswordField txtContrasenia;
    private JButton        btnRegistrar;
    private JButton        btnCancelar;
    private JLabel         lblError;

    public PanelRegistro() {
        setBackground(COLOR_FONDO);
        setLayout(new GridBagLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD)),
            new EmptyBorder(36, 44, 36, 44)
        ));

        JLabel lblTitulo = new JLabel("Crear cuenta");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(0x1a1a2e));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Únete a RentFlix y empieza a alquilar");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSub.setForeground(new Color(0x888888));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombre     = buildCampo("Nombre");
        txtApellido   = buildCampo("Apellido");
        txtEmail      = buildCampo("Email");
        txtUsuario    = buildCampo("Nombre de usuario");
        txtContrasenia = new JPasswordField();
        txtContrasenia.putClientProperty("JTextField.placeholderText", "Contraseña");
        txtContrasenia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        txtContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 13));

        lblError = new JLabel(" ");
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setForeground(COLOR_ACENTO);
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistrar = buildBoton("Crear cuenta", COLOR_ACENTO, Color.WHITE);
        btnRegistrar.setActionCommand("REGISTRAR_CLIENTE");

        btnCancelar = buildBoton("Cancelar", new Color(0xEEEEEE), new Color(0x333333));
        btnCancelar.setActionCommand("CANCELAR_REGISTRO");

        tarjeta.add(lblTitulo);
        tarjeta.add(Box.createVerticalStrut(6));
        tarjeta.add(lblSub);
        tarjeta.add(Box.createVerticalStrut(24));
        tarjeta.add(buildLabel("Nombre"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtNombre);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(buildLabel("Apellido"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtApellido);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(buildLabel("Email"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtEmail);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(buildLabel("Usuario"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtUsuario);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(buildLabel("Contraseña"));
        tarjeta.add(Box.createVerticalStrut(4));
        tarjeta.add(txtContrasenia);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(lblError);
        tarjeta.add(Box.createVerticalStrut(20));
        tarjeta.add(btnRegistrar);
        tarjeta.add(Box.createVerticalStrut(8));
        tarjeta.add(btnCancelar);

        add(tarjeta);
    }

    private JTextField buildCampo(String placeholder) {
        JTextField campo = new JTextField();
        campo.putClientProperty("JTextField.placeholderText", placeholder);
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return campo;
    }

    private JLabel buildLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(0x444444));
        return lbl;
    }

    private JButton buildBoton(String texto, Color fondo, Color letra) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(fondo);
        btn.setForeground(letra);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 0, 10, 0));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Métodos públicos que usa el Controlador ─────────────────────────────

    public void mostrarError(String mensaje) { lblError.setText(mensaje); }

    public void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");
        lblError.setText(" ");
    }

    // Validación básica antes de enviar al controlador
    public boolean datosValidos() {
        return !txtNombre.getText().trim().isEmpty()
            && !txtApellido.getText().trim().isEmpty()
            && !txtEmail.getText().trim().isEmpty()
            && !txtUsuario.getText().trim().isEmpty()
            && txtContrasenia.getPassword().length > 0;
    }

    public void setControlador(Controlador controlador) {
        btnRegistrar.addActionListener(controlador);
        btnCancelar.addActionListener(controlador);
    }

    // Getters
    public String getNombre()       { return txtNombre.getText().trim(); }
    public String getApellido()     { return txtApellido.getText().trim(); }
    public String getEmail()        { return txtEmail.getText().trim(); }
    public String getUsuario()      { return txtUsuario.getText().trim(); }
    public String getContrasenia()  { return new String(txtContrasenia.getPassword()); }
}