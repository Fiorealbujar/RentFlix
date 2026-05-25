// ==========================================
// CLASE: PanelGestionEmpleados.java
// Crear y listar empleados. Solo para Admin.
// ==========================================
package view;

import controller.Controlador;
import model.Empleado;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionEmpleados extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);
    private static final Color COLOR_GOLD   = new Color(0xF0C040);

    // Tabla de empleados
    private DefaultTableModel modeloTabla;
    private JTable            tblEmpleados;

    // Formulario de nuevo empleado
    private JTextField     txtNombre;
    private JTextField     txtApellido;
    private JTextField     txtEmail;
    private JTextField     txtUsuario;
    private JPasswordField txtContrasenia;
    private JButton        btnCrearEmpleado;
    private JButton        btnLimpiar;
    private JLabel         lblMensaje;

    public PanelGestionEmpleados() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        // Dividir en dos zonas: tabla (arriba) + formulario (abajo)
        JSplitPane splitPane = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            buildZonaTabla(),
            buildZonaFormulario()
        );
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);
        splitPane.setDividerSize(8);

        add(buildTitulo(),  BorderLayout.NORTH);
        add(splitPane,      BorderLayout.CENTER);
    }

    // ── Título ──────────────────────────────────────────────────────────────

    private JPanel buildTitulo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lbl = new JLabel("👥 Gestión de empleados");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(COLOR_DARK);
        panel.add(lbl);
        return panel;
    }

    // ── Zona tabla ──────────────────────────────────────────────────────────

    private JPanel buildZonaTabla() {
        JPanel zona = new JPanel(new BorderLayout());
        zona.setBackground(COLOR_FONDO);

        JLabel lblSub = new JLabel("Empleados registrados");
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSub.setForeground(COLOR_DARK);
        lblSub.setBorder(new EmptyBorder(0, 0, 8, 0));

        String[] columnas = {
            "ID", "Nombre", "Apellido",
            "Email", "Usuario", "Rol"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblEmpleados = new JTable(modeloTabla);
        tblEmpleados.setRowHeight(38);
        tblEmpleados.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblEmpleados.setShowHorizontalLines(true);
        tblEmpleados.setGridColor(new Color(0xEEEEEE));
        tblEmpleados.setSelectionBackground(new Color(0xFFE0E0));
        tblEmpleados.setSelectionForeground(COLOR_DARK);

        tblEmpleados.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );
        tblEmpleados.getTableHeader().setBackground(COLOR_DARK);
        tblEmpleados.getTableHeader().setForeground(Color.WHITE);

        int[] anchos = {40, 130, 130, 200, 130, 100};
        for (int i = 0; i < anchos.length; i++) {
            tblEmpleados.getColumnModel()
                        .getColumn(i)
                        .setPreferredWidth(anchos[i]);
        }

        // Renderer para la columna Rol con colores distintos
        tblEmpleados.getColumnModel().getColumn(5).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {

                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column
                    );
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
                    lbl.setOpaque(true);

                    if (!isSelected) {
                        if ("Administrador".equals(value)) {
                            lbl.setBackground(new Color(0xFFF9E6));
                            lbl.setForeground(new Color(0xB8860B));
                        } else {
                            lbl.setBackground(new Color(0xE3F2FD));
                            lbl.setForeground(new Color(0x1565C0));
                        }
                    }
                    return lbl;
                }
            }
        );

        JScrollPane scroll = new JScrollPane(tblEmpleados);
        scroll.setBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD))
        );

        zona.add(lblSub,  BorderLayout.NORTH);
        zona.add(scroll,  BorderLayout.CENTER);
        return zona;
    }

    // ── Zona formulario ─────────────────────────────────────────────────────

    private JPanel buildZonaFormulario() {
        JPanel zona = new JPanel(new BorderLayout());
        zona.setBackground(Color.WHITE);
        zona.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD)),
            new EmptyBorder(16, 20, 16, 20)
        ));

        JLabel lblSub = new JLabel("➕ Crear nuevo empleado");
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSub.setForeground(COLOR_DARK);
        lblSub.setBorder(new EmptyBorder(0, 0, 12, 0));

        // Campos en grid
        JPanel campos = new JPanel(new GridLayout(2, 4, 10, 10));
        campos.setOpaque(false);

        txtNombre      = buildCampo("Nombre *");
        txtApellido    = buildCampo("Apellido *");
        txtEmail       = buildCampo("Email *");
        txtUsuario     = buildCampo("Usuario *");
        txtContrasenia = new JPasswordField();
        txtContrasenia.putClientProperty(
            "JTextField.placeholderText", "Contraseña *"
        );
        txtContrasenia.setFont(new Font("SansSerif", Font.PLAIN, 13));

        campos.add(txtNombre);
        campos.add(txtApellido);
        campos.add(txtEmail);
        campos.add(txtUsuario);
        campos.add(txtContrasenia);
        campos.add(new JLabel()); // espacio vacío
        campos.add(new JLabel()); // espacio vacío

        // Botones
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        botones.setOpaque(false);

        btnLimpiar = new JButton("🗑  Limpiar");
        btnLimpiar.setActionCommand("LIMPIAR_FORM_EMPLEADO");
        btnLimpiar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnLimpiar.setBackground(new Color(0xEEEEEE));
        btnLimpiar.setForeground(new Color(0x333333));
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnLimpiar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCrearEmpleado = new JButton("👤  Crear empleado");
        btnCrearEmpleado.setActionCommand("CREAR_EMPLEADO");
        btnCrearEmpleado.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnCrearEmpleado.setBackground(COLOR_DARK);
        btnCrearEmpleado.setForeground(Color.WHITE);
        btnCrearEmpleado.setFocusPainted(false);
        btnCrearEmpleado.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnCrearEmpleado.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        botones.add(btnLimpiar);
        botones.add(btnCrearEmpleado);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel sur = new JPanel(new BorderLayout());
        sur.setOpaque(false);
        sur.setBorder(new EmptyBorder(10, 0, 0, 0));
        sur.add(lblMensaje, BorderLayout.WEST);
        sur.add(botones,    BorderLayout.EAST);

        zona.add(lblSub,  BorderLayout.NORTH);
        zona.add(campos,  BorderLayout.CENTER);
        zona.add(sur,     BorderLayout.SOUTH);
        return zona;
    }

    private JTextField buildCampo(String placeholder) {
        JTextField campo = new JTextField();
        campo.putClientProperty(
            "JTextField.placeholderText", placeholder
        );
        campo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return campo;
    }

    // ── Métodos públicos para el Controlador ────────────────────────────────

    public void cargarEmpleados(List<Empleado> empleados) {
        modeloTabla.setRowCount(0);
        for (Empleado e : empleados) {
            modeloTabla.addRow(new Object[]{
                e.getIdEmpleado(),
                e.getNombreEmpleado(),
                e.getApellidoEmpleado(),
                e.getEmailEmpleado(),
                e.getUsuarioEmpleado(),
                e.esAdministrador() ? "Administrador" : "Empleado"
            });
        }
    }

    public void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");
        lblMensaje.setText(" ");
    }

    public void mostrarMensaje(String mensaje, boolean esError) {
        lblMensaje.setText(mensaje);
        lblMensaje.setForeground(
            esError ? COLOR_ACENTO : new Color(0x27AE60)
        );
    }

    public boolean datosValidos() {
        return !txtNombre.getText().trim().isEmpty()
            && !txtApellido.getText().trim().isEmpty()
            && !txtEmail.getText().trim().isEmpty()
            && !txtUsuario.getText().trim().isEmpty()
            && txtContrasenia.getPassword().length > 0;
    }

    public void setControlador(Controlador controlador) {
        btnCrearEmpleado.addActionListener(controlador);
        btnLimpiar.addActionListener(controlador);
    }

    // ── Getters de datos ────────────────────────────────────────────────────

    public String getNombre()      { return txtNombre.getText().trim(); }
    public String getApellido()    { return txtApellido.getText().trim(); }
    public String getEmail()       { return txtEmail.getText().trim(); }
    public String getUsuario()     { return txtUsuario.getText().trim(); }
    public String getContrasenia() {
        return new String(txtContrasenia.getPassword());
    }
}