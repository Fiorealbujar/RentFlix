// ==========================================
// CLASE: PanelMisAlquileres.java
// Historial de alquileres del cliente.
// Permite solicitar la devolución de un alquiler activo.
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

public class PanelMisAlquileres extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_ACENTO = new Color(0xE50914);
    private static final Color COLOR_DARK   = new Color(0x1a1a2e);

    // Estados y sus colores
    private static final Color COLOR_ACTIVO    = new Color(0x27AE60);
    private static final Color COLOR_PENDIENTE = new Color(0xF39C12);
    private static final Color COLOR_DEVUELTO  = new Color(0x7F8C8D);
    private static final Color COLOR_VENCIDO   = new Color(0xE50914);

    private DefaultTableModel modeloTabla;
    private JTable            tblAlquileres;
    private JButton           btnSolicitarDevolucion;
    private JLabel            lblInfo;

    public PanelMisAlquileres() {
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

    // ── Título e info ───────────────────────────────────────────────────────

    private JPanel buildPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel lblTitulo = new JLabel("Mis alquileres");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_DARK);

        // Leyenda de estados
        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        leyenda.setOpaque(false);
        leyenda.add(buildChip("Activo",               COLOR_ACTIVO));
        leyenda.add(buildChip("Pendiente devolución", COLOR_PENDIENTE));
        leyenda.add(buildChip("Devuelto",             COLOR_DEVUELTO));
        leyenda.add(buildChip("Vencido",              COLOR_VENCIDO));

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(leyenda,   BorderLayout.EAST);
        return panel;
    }

    // Chip de color para la leyenda
    private JLabel buildChip(String texto, Color color) {
        JLabel chip = new JLabel("● " + texto);
        chip.setFont(new Font("SansSerif", Font.PLAIN, 11));
        chip.setForeground(color);
        return chip;
    }

    // ── Tabla ───────────────────────────────────────────────────────────────

    private JScrollPane buildTabla() {
        String[] columnas = {"#", "Película", "F. Alquiler", "F. Devolución prev.", "Estado", "Importe"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblAlquileres = new JTable(modeloTabla);
        tblAlquileres.setRowHeight(38);
        tblAlquileres.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblAlquileres.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tblAlquileres.getTableHeader().setBackground(COLOR_DARK);
        tblAlquileres.getTableHeader().setForeground(Color.WHITE);
        tblAlquileres.setSelectionBackground(new Color(0xFFE0E0));
        tblAlquileres.setSelectionForeground(COLOR_DARK);
        tblAlquileres.setShowHorizontalLines(true);
        tblAlquileres.setGridColor(new Color(0xEEEEEE));
        tblAlquileres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Anchos de columna
        int[] anchos = {40, 260, 120, 140, 160, 90};
        for (int i = 0; i < anchos.length; i++) {
            tblAlquileres.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        // Centrar columna #, fechas e importe
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : new int[]{0, 2, 3, 5}) {
            tblAlquileres.getColumnModel().getColumn(col).setCellRenderer(centrado);
        }

        // Renderer especial para la columna Estado (con colores)
        tblAlquileres.getColumnModel().getColumn(4).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);
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
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
        return scroll;
    }

    // ── Acciones ────────────────────────────────────────────────────────────

    private JPanel buildPanelAcciones() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(14, 0, 0, 0));

        lblInfo = new JLabel("Selecciona un alquiler activo para solicitar la devolución");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(0x888888));

        btnSolicitarDevolucion = new JButton("📦  Solicitar devolución");
        btnSolicitarDevolucion.setActionCommand("SOLICITAR_DEVOLUCION");
        btnSolicitarDevolucion.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSolicitarDevolucion.setBackground(new Color(0x1a1a2e));
        btnSolicitarDevolucion.setForeground(Color.WHITE);
        btnSolicitarDevolucion.setFocusPainted(false);
        btnSolicitarDevolucion.setBorder(new EmptyBorder(9, 20, 9, 20));
        btnSolicitarDevolucion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSolicitarDevolucion.setEnabled(false); // deshabilitado hasta seleccionar

        panel.add(lblInfo,                BorderLayout.WEST);
        panel.add(btnSolicitarDevolucion, BorderLayout.EAST);
        return panel;
    }

    // ── Métodos públicos que usa el Controlador ─────────────────────────────

    public void cargarAlquileres(List<Alquiler> alquileres) {
        modeloTabla.setRowCount(0);
        for (Alquiler a : alquileres) {
            modeloTabla.addRow(new Object[]{
                a.getIdAlquiler(),
                a.getNombrePelicula(),
                a.getFechaAlquiler(),
                a.getFechaDevolucionPrevista(),
                a.getEstadoAlquiler(),
                String.format("%.2f €", a.getMontoCobro())
            });
        }
        actualizarBotonDevolucion();
    }

    // Habilita el botón solo si la fila seleccionada está en estado "activo"
    public void actualizarBotonDevolucion() {
        int fila = tblAlquileres.getSelectedRow();
        if (fila >= 0) {
            String estado = String.valueOf(modeloTabla.getValueAt(fila, 4));
            btnSolicitarDevolucion.setEnabled("activo".equalsIgnoreCase(estado));
        } else {
            btnSolicitarDevolucion.setEnabled(false);
        }
    }

    // Devuelve el id del alquiler seleccionado en la tabla
    public int getIdAlquilerSeleccionado() {
        int fila = tblAlquileres.getSelectedRow();
        if (fila < 0) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public void setControlador(Controlador controlador) {
        btnSolicitarDevolucion.addActionListener(controlador);

        // Listener de selección: habilita/deshabilita el botón al seleccionar fila
        tblAlquileres.getSelectionModel().addListSelectionListener(
            e -> actualizarBotonDevolucion()
        );
    }

    public JButton getBtnSolicitarDevolucion() { return btnSolicitarDevolucion; }
}