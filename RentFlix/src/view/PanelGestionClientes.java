// PanelGestionClientes.java
package view;

import controller.Controlador;
import model.Cliente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionClientes extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);

    private DefaultTableModel modeloTabla;
    private JTable            tblClientes;
    private JButton           btnEliminarCliente;
    private JButton           btnEditarCliente;

    public PanelGestionClientes() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 12));
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildTitulo(), BorderLayout.NORTH);
        add(buildTabla(),  BorderLayout.CENTER);
    }

    private JLabel buildTitulo() {
        JLabel lbl = new JLabel("🧑‍💼 Gestión de clientes");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(COLOR_DARK);
        lbl.setBorder(new EmptyBorder(0, 0, 4, 0));
        return lbl;
    }

    // ── Tabla + botones ──────────────────────────────────────────────────────

    private JPanel buildTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        String[] columnas = {
            "ID", "Nombre", "Apellido", "Email", "Usuario", "Estado"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblClientes = new JTable(modeloTabla);
        tblClientes.setRowHeight(36);
        tblClientes.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblClientes.setShowHorizontalLines(true);
        tblClientes.setGridColor(new Color(0xEEEEEE));
        tblClientes.setSelectionBackground(new Color(0xFFE0E0));
        tblClientes.setSelectionForeground(COLOR_DARK);

        tblClientes.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );
        tblClientes.getTableHeader().setBackground(COLOR_DARK);
        tblClientes.getTableHeader().setForeground(Color.WHITE);

        int[] anchos = {40, 130, 130, 220, 130, 90};
        for (int i = 0; i < anchos.length; i++)
            tblClientes.getColumnModel().getColumn(i)
                       .setPreferredWidth(anchos[i]);

        // Centrar ID y Estado
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tblClientes.getColumnModel().getColumn(0).setCellRenderer(centrado);

        // Renderer con color para columna Estado
        tblClientes.getColumnModel().getColumn(5).setCellRenderer(
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
                        if ("activo".equalsIgnoreCase(String.valueOf(value))) {
                            lbl.setBackground(new Color(0xE8F8F0));
                            lbl.setForeground(new Color(0x27AE60));
                        } else {
                            lbl.setBackground(new Color(0xFDEDEC));
                            lbl.setForeground(COLOR_ACENTO);
                        }
                    }
                    return lbl;
                }
            }
        );

        // Botones bajo la tabla
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filaBotones.setOpaque(false);

        btnEditarCliente = new JButton("✏️  Editar cliente");
        btnEditarCliente.setActionCommand("EDITAR_CLIENTE");
        btnEditarCliente.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEditarCliente.setBackground(COLOR_DARK);
        btnEditarCliente.setForeground(Color.WHITE);
        btnEditarCliente.setFocusPainted(false);
        btnEditarCliente.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnEditarCliente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditarCliente.setEnabled(false);

        btnEliminarCliente = new JButton("🗑️  Eliminar cliente");
        btnEliminarCliente.setActionCommand("ELIMINAR_CLIENTE");
        btnEliminarCliente.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnEliminarCliente.setBackground(COLOR_ACENTO);
        btnEliminarCliente.setForeground(Color.WHITE);
        btnEliminarCliente.setFocusPainted(false);
        btnEliminarCliente.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnEliminarCliente.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminarCliente.setEnabled(false);

        filaBotones.add(btnEditarCliente);
        filaBotones.add(btnEliminarCliente);

        JScrollPane scroll = new JScrollPane(tblClientes);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));

        panel.add(scroll,      BorderLayout.CENTER);
        panel.add(filaBotones, BorderLayout.SOUTH);
        return panel;
    }

    // ── Métodos para el controlador ─────────────────────────────────────────

    public void cargarClientes(List<Cliente> clientes) {
        modeloTabla.setRowCount(0);
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                c.getIdCliente(),
                c.getNombreCliente(),
                c.getApellidoCliente(),
                c.getEmailCliente(),
                c.getNombreUsuario(),
                c.getEstado()
            });
        }
        actualizarBotones();
    }

    public void actualizarBotones() {
        boolean haySeleccion = tblClientes.getSelectedRow() >= 0;
        btnEditarCliente.setEnabled(haySeleccion);
        btnEliminarCliente.setEnabled(haySeleccion);
    }

    public int getIdClienteSeleccionado() {
        int fila = tblClientes.getSelectedRow();
        if (fila < 0) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    // Devuelve un Cliente con los datos de la fila seleccionada
    public Cliente getClienteSeleccionado() {
        int fila = tblClientes.getSelectedRow();
        if (fila < 0) return null;
        return new Cliente(
            (int)    modeloTabla.getValueAt(fila, 0),
            (String) modeloTabla.getValueAt(fila, 1),
            (String) modeloTabla.getValueAt(fila, 2),
            (String) modeloTabla.getValueAt(fila, 3),
            (String) modeloTabla.getValueAt(fila, 4),
            "",   // contraseña no se muestra en tabla
            (String) modeloTabla.getValueAt(fila, 5)
        );
    }

    public void setControlador(Controlador controlador) {
        btnEditarCliente.addActionListener(controlador);
        btnEliminarCliente.addActionListener(controlador);

        tblClientes.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) actualizarBotones(); }
        );
    }
}