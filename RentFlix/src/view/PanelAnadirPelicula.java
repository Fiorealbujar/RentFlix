// ==========================================
// CLASE: PanelAnadirPelicula.java
// Formulario para añadir nuevas películas.
// Disponible para Empleado y Administrador.
// ==========================================
package view;

import controller.Controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelAnadirPelicula extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);

    private JTextField     txtTitulo;
    private JTextField     txtDirector;
    private JTextField     txtDuracion;
    private JComboBox<String> cmbGenero;
    private JComboBox<String> cmbClasificacion;
    private JTextArea      txtSinopsis;
    private JTextField     txtFormato;
    private JTextField     txtPrecio;
    private JButton        btnGuardar;
    private JButton        btnLimpiar;
    private JLabel         lblMensaje;

    public PanelAnadirPelicula() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildTitulo(),     BorderLayout.NORTH);
        add(buildFormulario(), BorderLayout.CENTER);
        add(buildAcciones(),   BorderLayout.SOUTH);
    }

    // ── Título ──────────────────────────────────────────────────────────────

    private JPanel buildTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel lbl = new JLabel("➕ Añadir nueva película");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(COLOR_DARK);
        panel.add(lbl);
        return panel;
    }

    // ── Formulario ──────────────────────────────────────────────────────────

    private JPanel buildFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD)),
            new EmptyBorder(24, 28, 24, 28)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 6, 6, 6);
        gbc.anchor  = GridBagConstraints.WEST;
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        // Columna de labels (0) y campos (1)
        // Columna de labels (2) y campos (3)

        txtTitulo    = buildTextField();
        txtDirector  = buildTextField();
        txtDuracion  = buildTextField();
        txtFormato   = buildTextField();
        txtPrecio    = buildTextField();

        cmbGenero = new JComboBox<>(new String[]{
            "Acción", "Aventura", "Animación", "Ciencia Ficción",
            "Comedia", "Drama", "Fantasía", "Musical",
            "Romance", "Suspense", "Terror", "Thriller"
        });
        cmbGenero.setFont(new Font("SansSerif", Font.PLAIN, 13));

        cmbClasificacion = new JComboBox<>(
            new String[]{"TP", "7", "12", "16", "18"}
        );
        cmbClasificacion.setFont(new Font("SansSerif", Font.PLAIN, 13));

        txtSinopsis = new JTextArea(4, 20);
        txtSinopsis.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtSinopsis.setLineWrap(true);
        txtSinopsis.setWrapStyleWord(true);
        JScrollPane scrollSinopsis = new JScrollPane(txtSinopsis);

        // Fila 0
        agregarFila(panel, gbc, 0,
            "Título *",    txtTitulo,
            "Director *",  txtDirector
        );
        // Fila 1
        agregarFila(panel, gbc, 1,
            "Duración (min) *", txtDuracion,
            "Género *",         cmbGenero
        );
        // Fila 2
        agregarFila(panel, gbc, 2,
            "Clasificación edad *", cmbClasificacion,
            "Formato copia *",      txtFormato
        );
        // Fila 3
        agregarFila(panel, gbc, 3,
            "Precio alquiler/día (€) *", txtPrecio,
            "", new JLabel()
        );
        // Fila 4: Sinopsis ocupa todo el ancho
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        panel.add(buildLabel("Sinopsis"), gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 3;
        panel.add(scrollSinopsis, gbc);
        gbc.gridwidth = 1;

        return panel;
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila,
                               String label1, JComponent comp1,
                               String label2, JComponent comp2) {
        gbc.gridy = fila;
        gbc.weightx = 0;
        gbc.gridx = 0; panel.add(buildLabel(label1), gbc);
        gbc.weightx = 1;
        gbc.gridx = 1; panel.add(comp1, gbc);
        gbc.weightx = 0;
        gbc.gridx = 2; panel.add(buildLabel(label2), gbc);
        gbc.weightx = 1;
        gbc.gridx = 3; panel.add(comp2, gbc);
    }

    private JTextField buildTextField() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campo.setPreferredSize(new Dimension(200, 32));
        return campo;
    }

    private JLabel buildLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(0x444444));
        return lbl;
    }

    // ── Acciones ────────────────────────────────────────────────────────────

    private JPanel buildAcciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(14, 0, 0, 0));

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);

        btnLimpiar = new JButton("🗑  Limpiar");
        btnLimpiar.setActionCommand("LIMPIAR_FORM_PELICULA");
        btnLimpiar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLimpiar.setBackground(new Color(0xEEEEEE));
        btnLimpiar.setForeground(new Color(0x333333));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(new EmptyBorder(9, 20, 9, 20));
        btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnGuardar = new JButton("💾  Guardar película");
        btnGuardar.setActionCommand("GUARDAR_PELICULA");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnGuardar.setBackground(COLOR_DARK);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBorder(new EmptyBorder(9, 20, 9, 20));
        btnGuardar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        botones.add(btnLimpiar);
        botones.add(btnGuardar);

        panel.add(lblMensaje, BorderLayout.WEST);
        panel.add(botones,    BorderLayout.EAST);
        return panel;
    }

    // ── Métodos públicos para el Controlador ────────────────────────────────

    public void limpiar() {
        txtTitulo.setText("");
        txtDirector.setText("");
        txtDuracion.setText("");
        txtSinopsis.setText("");
        txtFormato.setText("");
        txtPrecio.setText("");
        cmbGenero.setSelectedIndex(0);
        cmbClasificacion.setSelectedIndex(0);
        lblMensaje.setText(" ");
        lblMensaje.setForeground(Color.BLACK);
    }

    public void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(esError ? COLOR_ACENTO : COLOR_ACTIVO);
    }

    // Valida que los campos obligatorios no estén vacíos
    public boolean datosValidos() {
        if (txtTitulo.getText().trim().isEmpty()   ||
            txtDirector.getText().trim().isEmpty()  ||
            txtDuracion.getText().trim().isEmpty()  ||
            txtFormato.getText().trim().isEmpty()   ||
            txtPrecio.getText().trim().isEmpty()) {
            mostrarMensaje("Rellena todos los campos obligatorios (*).", true);
            return false;
        }
        try {
            Integer.parseInt(txtDuracion.getText().trim());
            Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mostrarMensaje("Duración y precio deben ser números.", true);
            return false;
        }
        return true;
    }

    public void setControlador(Controlador controlador) {
        btnGuardar.addActionListener(controlador);
        btnLimpiar.addActionListener(controlador);
    }

    // ── Getters de datos ────────────────────────────────────────────────────

    public String getTitulo()          { return txtTitulo.getText().trim(); }
    public String getDirector()        { return txtDirector.getText().trim(); }
    public int    getDuracion()        { return Integer.parseInt(txtDuracion.getText().trim()); }
    public String getGenero()          { return (String) cmbGenero.getSelectedItem(); }
    public String getSinopsis()        { return txtSinopsis.getText().trim(); }
    public String getClasificacion()   { return (String) cmbClasificacion.getSelectedItem(); }
    public String getFormato()         { return txtFormato.getText().trim(); }
    public double getPrecio()          { return Double.parseDouble(txtPrecio.getText().trim()); }

    public JButton getBtnGuardar()     { return btnGuardar; }
    public JButton getBtnLimpiar()     { return btnLimpiar; }

    // Color positivo para el mensaje de éxito
    private static final Color COLOR_ACTIVO = new Color(0x27AE60);
}