// ==========================================
// CLASE: PanelGestionAlquileres.java
// Vista de todos los alquileres para el empleado.
// Permite aceptar devoluciones pendientes y
// registrar alquileres en nombre de un cliente.
// ==========================================
package view;

import controller.Controlador;
import model.Alquiler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGestionAlquileres extends JPanel {

    private static final Color COLOR_FONDO     = new Color(0xF5F5F5);
    private static final Color COLOR_DARK      = new Color(0x1a1a2e);
    private static final Color COLOR_ACTIVO    = new Color(0x27AE60);
    private static final Color COLOR_PENDIENTE = new Color(0xF39C12);
    private static final Color COLOR_DEVUELTO  = new Color(0x7F8C8D);
    private static final Color COLOR_VENCIDO   = new Color(0xE50914);

    private DefaultTableModel modeloTabla;
    private JTable            tblAlquileres;
    private JButton           btnAceptarDevolucion;
    private JButton           btnAlquilarParaCliente;
    private JComboBox<String> cmbFiltroEstado;

    public PanelGestionAlquileres() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildPanelSuperior(), BorderLayout.NORTH);
        add(buildTabla(),         BorderLayout.CENTER);
        add(buildPanelAcciones(), BorderLayout.SOUTH);
    }

    // ── Superior: título + filtro ───────────────────────────────────────────

    private JPanel buildPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel lblTitulo = new JLabel("Gestión de alquileres");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_DARK);

        // Filtro por estado
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelFiltro.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar por estado:");
        lblFiltro.setFont(new Font("SansSerif", Font.PLAIN, 13));

        cmbFiltroEstado = new JComboBox<>(new String[]{
            "Todos", "activo", "pendiente_devolucion", "devuelto", "vencido"
        });
        cmbFiltroEstado.setActionCommand("FILTRAR_ALQUILERES");
        cmbFiltroEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbFiltroEstado.setPreferredSize(new Dimension(180, 30));

        panelFiltro.add(lblFiltro);
        panelFiltro.add(cmbFiltroEstado);

        panel.add(lblTitulo,    BorderLayout.WEST);
        panel.add(panelFiltro,  BorderLayout.EAST);
        return panel;
    }

    // ── Tabla ───────────────────────────────────────────────────────────────

    private JScrollPane buildTabla() {
        String[] columnas = {
            "#", "Cliente", "Película", "F. Alquiler",
            "F. Dev. Prev.", "Estado", "Importe"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblAlquileres = new JTable(modeloTabla);
        tblAlquileres.setRowHeight(38);
        tblAlquileres.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblAlquileres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblAlquileres.setShowHorizontalLines(true);
        tblAlquileres.setGridColor(new Color(0xEEEEEE));
        tblAlquileres.setSelectionBackground(new Color(0xFFE0E0));
        tblAlquileres.setSelectionForeground(COLOR_DARK);

        tblAlquileres.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );
        tblAlquileres.getTableHeader().setBackground(COLOR_DARK);
        tblAlquileres.getTableHeader().setForeground(Color.WHITE);

        // Anchos de columna
        int[] anchos = {40, 160, 200, 110, 110, 160, 90};
        for (int i = 0; i < anchos.length; i++) {
            tblAlquileres.getColumnModel()
                         .getColumn(i)
                         .setPreferredWidth(anchos[i]);
        }

        // Centrar columnas numéricas y de fechas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : new int[]{0, 3, 4, 6}) {
            tblAlquileres.getColumnModel()
                         .getColumn(col)
                         .setCellRenderer(centrado);
        }

        // Renderer con colores para Estado
        tblAlquileres.getColumnModel().getColumn(5).setCellRenderer(
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
                        switch (String.valueOf(value).toLowerCase()) {
                            case "activo":
                                lbl.setBackground(new Color(0xE8F8F0));
                                lbl.setForeground(COLOR_ACTIVO);
                                break;
                            case "pendiente_devolucion":
                                lbl.setBackground(new Color(0xFEF9E7));
                                lbl.setForeground(COLOR_PENDIENTE);
                                break;
                            case "devuelto":
                                lbl.setBackground(new Color(0xF2F3F4));
                                lbl.setForeground(COLOR_DEVUELTO);
                                break;
                            case "vencido":
                                lbl.setBackground(new Color(0xFDEDEC));
                                lbl.setForeground(COLOR_VENCIDO);
                                break;
                            default:
                                lbl.setBackground(Color.WHITE);
                                lbl.setForeground(Color.BLACK);
                        }
                    }
                    return lbl;
                }
            }
        );

        JScrollPane scroll = new JScrollPane(tblAlquileres);
        scroll.setBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD))
        );
        return scroll;
    }

    // ── Acciones ────────────────────────────────────────────────────────────

    private JPanel buildPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(14, 0, 0, 0));

        btnAlquilarParaCliente = buildBoton(
            "🎬  Alquilar para cliente",
            COLOR_DARK,
            Color.WHITE,
            "ALQUILAR_PARA_CLIENTE"
        );

        btnAceptarDevolucion = buildBoton(
            "✅  Aceptar devolución",
            COLOR_ACTIVO,
            Color.WHITE,
            "ACEPTAR_DEVOLUCION"
        );
        // Deshabilitado hasta seleccionar una fila pendiente
        btnAceptarDevolucion.setEnabled(false);

        panel.add(btnAlquilarParaCliente);
        panel.add(btnAceptarDevolucion);
        return panel;
    }

    private JButton buildBoton(String texto, Color fondo,
                                Color letra, String actionCommand) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(actionCommand);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(fondo);
        btn.setForeground(letra);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Métodos públicos para el Controlador ────────────────────────────────

    public void cargarAlquileres(List<Alquiler> alquileres) {
        modeloTabla.setRowCount(0);
        for (Alquiler a : alquileres) {
            modeloTabla.addRow(new Object[]{
                a.getIdAlquiler(),
                a.getNombreCliente(),
                a.getNombrePelicula(),
                a.getFechaAlquiler(),
                a.getFechaDevolucionPrevista(),
                a.getEstadoAlquiler(),
                String.format("%.2f €", a.getMontoCobro())
            });
        }
        actualizarBotones();
    }

    public void actualizarBotones() {
        int fila = tblAlquileres.getSelectedRow();
        if (fila >= 0) {
            String estado = String.valueOf(modeloTabla.getValueAt(fila, 5));
            btnAceptarDevolucion.setEnabled(
                "pendiente_devolucion".equalsIgnoreCase(estado)
            );
        } else {
            btnAceptarDevolucion.setEnabled(false);
        }
    }

    public int getIdAlquilerSeleccionado() {
        int fila = tblAlquileres.getSelectedRow();
        if (fila < 0) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public String getFiltroEstado() {
        String seleccion = (String) cmbFiltroEstado.getSelectedItem();
        return "Todos".equals(seleccion) ? null : seleccion;
    }

    public void setControlador(Controlador controlador) {
        btnAceptarDevolucion.addActionListener(controlador);
        btnAlquilarParaCliente.addActionListener(controlador);
        cmbFiltroEstado.addActionListener(controlador);

        tblAlquileres.getSelectionModel().addListSelectionListener(
            e -> {
                if (!e.getValueIsAdjusting()) actualizarBotones();
            }
        );
    }

    public JButton getBtnAceptarDevolucion()    { return btnAceptarDevolucion; }
    public JButton getBtnAlquilarParaCliente()  { return btnAlquilarParaCliente; }
    public JComboBox<String> getCmbFiltro()     { return cmbFiltroEstado; }
}