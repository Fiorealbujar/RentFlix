// PanelGestionAlquileres.java
package view;

import controller.Controlador;
import model.Alquiler;
import model.Cliente;
import model.Pelicula;

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
    private static final Color COLOR_PENDIENTE = new Color(0xB71C1C);
    private static final Color COLOR_DEVUELTO  = new Color(0x7F8C8D);
    private static final Color COLOR_VENCIDO   = new Color(0xE50914);

    private DefaultTableModel  modeloTabla;
    private JTable             tblAlquileres;
    private JButton            btnAceptarDevolucion;
    private JButton            btnNuevoAlquiler;
    private JComboBox<String>  cmbFiltroEstado;

    private JPanel             panelFormAlquiler;
    private JComboBox<String>  cmbClientes;
    private JComboBox<String>  cmbPeliculas;
    private JComboBox<String>  cmbFormatos;
    private JSpinner           spinnerDias;
    private JComboBox<String>  cmbMetodoPago;
    private JButton            btnConfirmarAlquiler;
    private JButton            btnCancelarAlquiler;

    public PanelGestionAlquileres() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildSuperior(), BorderLayout.NORTH);
        add(buildTabla(),    BorderLayout.CENTER);
        add(buildSur(),      BorderLayout.SOUTH);
    }

    // ── Superior ────────────────────────────────────────────────────────────

    private JPanel buildSuperior() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lbl = new JLabel("Gestión de alquileres");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(COLOR_DARK);

        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filtro.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar:");
        lblFiltro.setFont(new Font("SansSerif", Font.PLAIN, 13));

        cmbFiltroEstado = new JComboBox<>(new String[]{
            "Todos", "activo", "pendiente_devolucion", "devuelto", "vencido"
        });
        cmbFiltroEstado.setActionCommand("FILTRAR_ALQUILERES");
        cmbFiltroEstado.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbFiltroEstado.setPreferredSize(new Dimension(180, 30));

        filtro.add(lblFiltro);
        filtro.add(cmbFiltroEstado);

        panel.add(lbl,    BorderLayout.WEST);
        panel.add(filtro, BorderLayout.EAST);
        return panel;
    }

    // ── Tabla ───────────────────────────────────────────────────────────────

    private JScrollPane buildTabla() {
        String[] columnas = {
            "#", "Cliente", "Película", "Formato",
            "F. Alquiler", "F. Dev. Prev.", "Estado", "Importe"
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
        tblAlquileres.setSelectionBackground(new Color(0xFFCDD2));
        tblAlquileres.setSelectionForeground(COLOR_DARK);

        tblAlquileres.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );
        tblAlquileres.getTableHeader().setBackground(COLOR_DARK);
        tblAlquileres.getTableHeader().setForeground(Color.WHITE);

        int[] anchos = {40, 140, 180, 90, 100, 110, 150, 80};
        for (int i = 0; i < anchos.length; i++) {
            tblAlquileres.getColumnModel()
                         .getColumn(i)
                         .setPreferredWidth(anchos[i]);
        }

        // Renderer de fila completa: pinta toda la fila en rojo
        // si el estado es pendiente_devolucion
        RendererFilaAlquileres rendererFila = new RendererFilaAlquileres();

        // Aplicar a todas las columnas excepto la de Estado (col 6)
        // Las columnas numéricas también centradas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
                aplicarColorFila(table, c, row, isSelected);
                return c;
            }
        };

        DefaultTableCellRenderer normal = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
                );
                aplicarColorFila(table, c, row, isSelected);
                return c;
            }
        };

        // Columnas centradas: #(0), Formato(3), F.Alquiler(4), F.Dev(5), Importe(7)
        tblAlquileres.getColumnModel().getColumn(0).setCellRenderer(centrado);
        tblAlquileres.getColumnModel().getColumn(1).setCellRenderer(normal);
        tblAlquileres.getColumnModel().getColumn(2).setCellRenderer(normal);
        tblAlquileres.getColumnModel().getColumn(3).setCellRenderer(centrado);
        tblAlquileres.getColumnModel().getColumn(4).setCellRenderer(centrado);
        tblAlquileres.getColumnModel().getColumn(5).setCellRenderer(centrado);
        tblAlquileres.getColumnModel().getColumn(7).setCellRenderer(centrado);

        // Renderer especial para columna Estado (col 6): color + fondo de fila
        tblAlquileres.getColumnModel().getColumn(6).setCellRenderer(
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
                        String estado = String.valueOf(value).toLowerCase();
                        switch (estado) {
                            case "activo":
                                lbl.setBackground(new Color(0xE8F8F0));
                                lbl.setForeground(COLOR_ACTIVO);
                                break;
                            case "pendiente_devolucion":
                                lbl.setBackground(new Color(0xFFCDD2));
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

    // Método estático de utilidad: aplica el color de fondo de fila
    // según el estado de la columna 6 de esa fila
    private void aplicarColorFila(JTable table, Component c,
                                   int row, boolean isSelected) {
        if (!isSelected) {
            String estado = String.valueOf(
                table.getModel().getValueAt(row, 6)
            ).toLowerCase();
            if ("pendiente_devolucion".equals(estado)) {
                c.setBackground(new Color(0xFFCDD2));
                c.setForeground(new Color(0xB71C1C));
            } else {
                c.setBackground(table.getBackground());
                c.setForeground(table.getForeground());
            }
        }
    }

    // Renderer de fila completa (para posible uso futuro)
    private class RendererFilaAlquileres extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
            );
            aplicarColorFila(table, c, row, isSelected);
            return c;
        }
    }

    // ── Sur: botones + form nuevo alquiler ───────────────────────────────────

    private JPanel buildSur() {
        JPanel sur = new JPanel(new BorderLayout(0, 8));
        sur.setOpaque(false);
        sur.setBorder(new EmptyBorder(12, 0, 0, 0));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        botones.setOpaque(false);

        btnAceptarDevolucion = buildBoton(
            "✅  Aceptar devolución",
            COLOR_ACTIVO, Color.WHITE, "ACEPTAR_DEVOLUCION"
        );
        btnAceptarDevolucion.setEnabled(false);

        btnNuevoAlquiler = buildBoton(
            "🎬  Nuevo alquiler para cliente",
            COLOR_DARK, Color.WHITE, "ABRIR_FORM_ALQUILER"
        );

        botones.add(btnAceptarDevolucion);
        botones.add(btnNuevoAlquiler);

        panelFormAlquiler = buildFormAlquiler();
        panelFormAlquiler.setVisible(false);

        sur.add(botones,           BorderLayout.NORTH);
        sur.add(panelFormAlquiler, BorderLayout.CENTER);
        return sur;
    }

    private JPanel buildFormAlquiler() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD)),
            new EmptyBorder(14, 18, 14, 18)
        ));

        JLabel lbl = new JLabel("Nuevo alquiler");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(COLOR_DARK);

        JPanel campos = new JPanel(new GridLayout(1, 5, 10, 0));
        campos.setOpaque(false);

        cmbClientes   = new JComboBox<>();
        cmbPeliculas  = new JComboBox<>();
        cmbFormatos   = new JComboBox<>(
            new String[]{"DVD", "Blu-ray", "4K Ultra HD"}
        );
        spinnerDias   = new JSpinner(new SpinnerNumberModel(3, 1, 30, 1));
        cmbMetodoPago = new JComboBox<>(
            new String[]{"efectivo", "tarjeta", "transferencia"}
        );

        campos.add(buildLabeledField("Cliente",     cmbClientes));
        campos.add(buildLabeledField("Película",    cmbPeliculas));
        campos.add(buildLabeledField("Formato",     cmbFormatos));
        campos.add(buildLabeledField("Días",        spinnerDias));
        campos.add(buildLabeledField("Método pago", cmbMetodoPago));

        JPanel botonesForm = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botonesForm.setOpaque(false);

        btnCancelarAlquiler = buildBoton(
            "Cancelar",
            new Color(0xEEEEEE), new Color(0x333333), "CANCELAR_FORM_ALQUILER"
        );
        btnConfirmarAlquiler = buildBoton(
            "✓  Confirmar alquiler",
            COLOR_DARK, Color.WHITE, "CONFIRMAR_ALQUILER_EMPLEADO"
        );

        botonesForm.add(btnCancelarAlquiler);
        botonesForm.add(btnConfirmarAlquiler);

        panel.add(lbl,         BorderLayout.NORTH);
        panel.add(campos,      BorderLayout.CENTER);
        panel.add(botonesForm, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildLabeledField(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 11));
        lbl.setForeground(new Color(0x444444));
        comp.setFont(new Font("SansSerif", Font.PLAIN, 12));
        p.add(lbl,  BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }

    private JButton buildBoton(String texto, Color fondo,
                                Color letra, String cmd) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(cmd);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(fondo);
        btn.setForeground(letra);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 18, 9, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Métodos para el Controlador ─────────────────────────────────────────

    public void cargarAlquileres(List<Alquiler> alquileres) {
        modeloTabla.setRowCount(0);
        for (Alquiler a : alquileres) {
            modeloTabla.addRow(new Object[]{
                a.getIdAlquiler(),
                a.getNombreCliente(),
                a.getNombrePelicula(),
                "",
                a.getFechaAlquiler(),
                a.getFechaDevolucionPrevista(),
                a.getEstadoAlquiler(),
                String.format("%.2f €", a.getMontoCobro())
            });
        }
        actualizarBotonDevolucion();
    }

    public void actualizarBotonDevolucion() {
        int fila = tblAlquileres.getSelectedRow();
        if (fila >= 0) {
            String estado = String.valueOf(
                modeloTabla.getValueAt(fila, 6)
            );
            btnAceptarDevolucion.setEnabled(
                "pendiente_devolucion".equalsIgnoreCase(estado)
            );
        } else {
            btnAceptarDevolucion.setEnabled(false);
        }
    }

    public void mostrarFormAlquiler(boolean visible) {
        panelFormAlquiler.setVisible(visible);
        panelFormAlquiler.revalidate();
        panelFormAlquiler.repaint();
    }

    public void cargarComboClientes(List<Cliente> clientes) {
        cmbClientes.removeAllItems();
        for (Cliente c : clientes) {
            cmbClientes.addItem(
                c.getNombreCompleto() + " (@" + c.getNombreUsuario() + ")"
            );
        }
    }

    public void cargarComboPeliculas(List<Pelicula> peliculas) {
        cmbPeliculas.removeAllItems();
        for (Pelicula p : peliculas) {
            cmbPeliculas.addItem(p.getNombrePelicula());
        }
    }

    public int getIdAlquilerSeleccionado() {
        int fila = tblAlquileres.getSelectedRow();
        if (fila < 0) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    public String getFiltroEstado() {
        String sel = (String) cmbFiltroEstado.getSelectedItem();
        return "Todos".equals(sel) ? null : sel;
    }

    public int    getIndexClienteSeleccionado()  { return cmbClientes.getSelectedIndex(); }
    public int    getIndexPeliculaSeleccionada() { return cmbPeliculas.getSelectedIndex(); }
    public String getFormatoSeleccionado()       { return (String) cmbFormatos.getSelectedItem(); }
    public int    getDiasAlquiler()              { return (int) spinnerDias.getValue(); }
    public String getMetodoPagoSeleccionado()    { return (String) cmbMetodoPago.getSelectedItem(); }

    public void setControlador(Controlador controlador) {
        btnAceptarDevolucion.addActionListener(controlador);
        btnNuevoAlquiler.addActionListener(controlador);
        btnConfirmarAlquiler.addActionListener(controlador);
        btnCancelarAlquiler.addActionListener(controlador);
        cmbFiltroEstado.addActionListener(controlador);

        tblAlquileres.getSelectionModel().addListSelectionListener(
            e -> { if (!e.getValueIsAdjusting()) actualizarBotonDevolucion(); }
        );
    }

    public JButton           getBtnAceptarDevolucion() { return btnAceptarDevolucion; }
    public JButton           getBtnNuevoAlquiler()     { return btnNuevoAlquiler; }
    public JComboBox<String> getCmbFiltroEstado()      { return cmbFiltroEstado; }
}