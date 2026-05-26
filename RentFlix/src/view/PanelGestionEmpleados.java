// ==========================================
// CLASE: PanelGestionEmpleados.java — CORREGIDA
// Añade botón "Eliminar empleado".
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

    private DefaultTableModel modeloTabla;
    private JTable            tblEmpleados;
    private JButton           btnEliminarEmpleado;
    private JTextField        txtNombre;
    private JTextField        txtApellido;
    private JTextField        txtEmail;
    private JTextField        txtUsuario;
    private JPasswordField    txtContrasenia;
    private JButton           btnCrearEmpleado;
    private JButton           btnLimpiar;
    private JLabel            lblMensaje;

    public PanelGestionEmpleados() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildTitulo(),     BorderLayout.NORTH);
        add(buildTabla(),      BorderLayout.CENTER);
        add(buildFormulario(), BorderLayout.SOUTH);
    }

    private JLabel buildTitulo() {
        JLabel lbl = new JLabel("👥 Gestión de empleados");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(COLOR_DARK);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        return lbl;
    }

    // ── Tabla + botón eliminar ───────────────────────────────────────────────

    private JPanel buildTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        String[] columnas = {
            "ID", "Nombre", "Apellido", "Email", "Usuario", "Rol"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblEmpleados = new JTable(modeloTabla);
        tblEmpleados.setRowHeight(36);
        tblEmpleados.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
        for (int i = 0; i < anchos.length; i++)
            tblEmpleados.getColumnModel().getColumn(i)
                        .setPreferredWidth(anchos[i]);

        // Renderer colores Rol
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

        // Botón eliminar bajo la tabla
        btnEliminarEmpleado = new JButton("🗑️  Eliminar empleado");
        btnEliminarEmpleado.setActionCommand("ELIMINAR_EMPLEADO");
        btnEliminarEmpleado.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEliminarEmpleado.setBackground(COLOR_ACENTO);
        btnEliminarEmpleado.setForeground(Color.WHITE);
        btnEliminarEmpleado.setFocusPainted(false);
        btnEliminarEmpleado.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnEliminarEmpleado.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );
        btnEliminarEmpleado.setEnabled(false);

        JPanel filaBtnEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filaBtnEliminar.setOpaque(false);
        filaBtnEliminar.add(btnEliminarEmpleado);

        JScrollPane scroll = new JScrollPane(tblEmpleados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));

        panel.add(scroll,          BorderLayout.CENTER);
        panel.add(filaBtnEliminar, BorderLayout.SOUTH);
        return panel;
    }

    // ── Formulario crear empleado ────────────────────────────────────────────

    private JPanel buildFormulario() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD)),
            new EmptyBorder(16, 20, 16, 20)
        ));

        JLabel lblSub = new JLabel("➕ Crear nuevo empleado");
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblSub.setForeground(COLOR_DARK);

        JPanel campos = new JPanel(new GridLayout(1, 5, 10, 0));
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

        JPanel sur = new JPanel(new BorderLayout(10, 0));
        sur.setOpaque(false);
        sur.setBorder(new EmptyBorder(10, 0, 0, 0));

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
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

        sur.add(lblMensaje, BorderLayout.WEST);
        sur.add(botones,    BorderLayout.EAST);

        panel.add(lblSub,  BorderLayout.NORTH);
        panel.add(campos,  BorderLayout.CENTER);
        panel.add(sur,     BorderLayout.SOUTH);
        return panel;
    }

    private JTextField buildCampo(String placeholder) {
        JTextField f = new JTextField();
        f.putClientProperty("JTextField.placeholderText", placeholder);
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return f;
    }

    // ── Métodos para el controlador ─────────────────────────────────────────

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
        btnEliminarEmpleado.setEnabled(false);
    }

    public void actualizarBotonEliminar() {
        int fila = tblEmpleados.getSelectedRow();
        if (fila >= 0) {
            String rol = String.valueOf(modeloTabla.getValueAt(fila, 5));
            // No se puede eliminar al administrador
            btnEliminarEmpleado.setEnabled(!"Administrador".equals(rol));
        } else {
            btnEliminarEmpleado.setEnabled(false);
        }
    }

    public int getIdEmpleadoSeleccionado() {
        int fila = tblEmpleados.getSelectedRow();
        if (fila < 0) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public void limpiar() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtUsuario.setText("");
        txtContrasenia.setText("");
        lblMensaje.setText(" ");
    }

    public void mostrarMensaje(String msg, boolean esError) {
        lblMensaje.setText(msg);
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
        btnEliminarEmpleado.addActionListener(controlador);

        tblEmpleados.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) actualizarBotonEliminar(); }
        );
    }

    public String getNombre()      { return txtNombre.getText().trim(); }
    public String getApellido()    { return txtApellido.getText().trim(); }
    public String getEmail()       { return txtEmail.getText().trim(); }
    public String getUsuario()     { return txtUsuario.getText().trim(); }
    public String getContrasenia() {
        return new String(txtContrasenia.getPassword());
    }
}