// ==========================================
// CLASE: PanelCatalogo.java — CORREGIDA
// Muestra una fila por cada COPIA disponible.
// Incluye columna Formato y filtro por formato.
// Sin lógica: todo a través del controlador.
// ==========================================
package view;

import controller.Controlador;
import model.Copia;
import model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCatalogo extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);

    private JTextField        txtBuscar;
    private JButton           btnBuscar;
    private JButton           btnAlquilar;
    private JComboBox<String> cmbFiltroFormato;
    private JTable            tblPeliculas;
    private DefaultTableModel modeloTabla;

    // true = muestra columna ID (empleado/admin)
    // false = sin ID (cliente/invitado)
    private final boolean mostrarId;

    public PanelCatalogo(boolean mostrarId) {
        this.mostrarId = mostrarId;
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildSuperior(),  BorderLayout.NORTH);
        add(buildTabla(),     BorderLayout.CENTER);
        add(buildAcciones(),  BorderLayout.SOUTH);
    }

    // ── Superior: título + búsqueda + filtro ────────────────────────────────

    private JPanel buildSuperior() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel lblTitulo = new JLabel("Catálogo de películas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(COLOR_DARK);

        JPanel controles = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        controles.setOpaque(false);

        // Filtro por formato
        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setFont(new Font("SansSerif", Font.PLAIN, 13));

        cmbFiltroFormato = new JComboBox<>(
            new String[]{"Todos", "DVD", "Blu-ray", "4K Ultra HD"}
        );
        cmbFiltroFormato.setActionCommand("FILTRAR_FORMATO_CATALOGO");
        cmbFiltroFormato.setFont(new Font("SansSerif", Font.PLAIN, 13));
        cmbFiltroFormato.setPreferredSize(new Dimension(130, 30));

        // Búsqueda por título
        txtBuscar = new JTextField(18);
        txtBuscar.putClientProperty(
            "JTextField.placeholderText", "Buscar película..."
        );

        btnBuscar = new JButton("Buscar");
        btnBuscar.setActionCommand("BUSCAR_PELICULA");
        btnBuscar.setBackground(COLOR_DARK);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        controles.add(lblFormato);
        controles.add(cmbFiltroFormato);
        controles.add(Box.createHorizontalStrut(8));
        controles.add(txtBuscar);
        controles.add(btnBuscar);

        panel.add(lblTitulo,  BorderLayout.WEST);
        panel.add(controles,  BorderLayout.EAST);
        return panel;
    }

    // ── Tabla ───────────────────────────────────────────────────────────────

    private JScrollPane buildTabla() {
        // Columnas según rol
        String[] columnasSinId = {
            "Título", "Director", "Género",
            "Duración", "Clasificación", "Formato", "Precio/día"
        };
        String[] columnasConId = {
            "ID Copia", "Título", "Director", "Género",
            "Duración", "Clasificación", "Formato", "Precio/día"
        };

        String[] columnas = mostrarId ? columnasConId : columnasSinId;

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPeliculas = new JTable(modeloTabla);
        tblPeliculas.setRowHeight(38);
        tblPeliculas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPeliculas.setShowHorizontalLines(true);
        tblPeliculas.setGridColor(new Color(0xEEEEEE));
        tblPeliculas.setSelectionBackground(new Color(0xFFE0E0));
        tblPeliculas.setSelectionForeground(COLOR_DARK);

        tblPeliculas.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 13)
        );
        tblPeliculas.getTableHeader().setBackground(COLOR_DARK);
        tblPeliculas.getTableHeader().setForeground(Color.WHITE);

        // Anchos según si hay columna ID
        if (mostrarId) {
            int[] anchos = {70, 200, 140, 100, 80, 100, 100, 80};
            for (int i = 0; i < anchos.length; i++)
                tblPeliculas.getColumnModel().getColumn(i)
                            .setPreferredWidth(anchos[i]);
            centrar(new int[]{0, 4, 5, 6, 7});
        } else {
            int[] anchos = {220, 150, 100, 80, 100, 110, 80};
            for (int i = 0; i < anchos.length; i++)
                tblPeliculas.getColumnModel().getColumn(i)
                            .setPreferredWidth(anchos[i]);
            centrar(new int[]{3, 4, 5, 6});
        }

        JScrollPane scroll = new JScrollPane(tblPeliculas);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
        return scroll;
    }

    private void centrar(int[] cols) {
        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : cols)
            tblPeliculas.getColumnModel().getColumn(col).setCellRenderer(c);
    }

    // ── Acciones ────────────────────────────────────────────────────────────

    private JPanel buildAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        btnAlquilar = new JButton("🎬  Alquilar película");
        btnAlquilar.setActionCommand("ALQUILAR_PELICULA");
        btnAlquilar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAlquilar.setBackground(COLOR_ACENTO);
        btnAlquilar.setForeground(Color.WHITE);
        btnAlquilar.setFocusPainted(false);
        btnAlquilar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAlquilar.setBorder(new EmptyBorder(9, 20, 9, 20));
        btnAlquilar.setEnabled(false);

        panel.add(btnAlquilar);
        return panel;
    }

    // ── Métodos para el controlador ─────────────────────────────────────────

    /**
     * Carga la tabla con una fila por cada copia disponible.
     * Cada fila lleva la info de la película + el formato y precio de la copia.
     */
    public void cargarCopias(List<Pelicula> peliculas,
                              List<Copia> copias) {
        modeloTabla.setRowCount(0);
        for (Copia copia : copias) {
            // Buscar la película que corresponde a esta copia
            Pelicula pelicula = peliculas.stream()
                .filter(p -> p.getId() == copia.getIdPelicula())
                .findFirst()
                .orElse(null);
            if (pelicula == null) continue;

            if (mostrarId) {
                modeloTabla.addRow(new Object[]{
                    copia.getIdCopia(),
                    pelicula.getNombrePelicula(),
                    pelicula.getDirector(),
                    pelicula.getGenero(),
                    pelicula.getDuracion() + " min",
                    pelicula.getClasificacionEdad(),
                    copia.getFormato(),
                    String.format("%.2f €", copia.getPrecioAlquiler())
                });
            } else {
                modeloTabla.addRow(new Object[]{
                    pelicula.getNombrePelicula(),
                    pelicula.getDirector(),
                    pelicula.getGenero(),
                    pelicula.getDuracion() + " min",
                    pelicula.getClasificacionEdad(),
                    copia.getFormato(),
                    String.format("%.2f €", copia.getPrecioAlquiler())
                });
            }
        }
    }

    public void habilitarAcciones(boolean habilitar) {
        btnAlquilar.setEnabled(habilitar);
    }

    /**
     * Devuelve el id de la copia seleccionada.
     * Solo disponible cuando mostrarId = true.
     * Para vista cliente devuelve -1 (el controlador
     * debe usar getFilaSeleccionada y reconstruir).
     */
    public int getIdCopiaSeleccionada() {
        int fila = tblPeliculas.getSelectedRow();
        if (fila < 0) return -1;
        if (mostrarId) return (int) modeloTabla.getValueAt(fila, 0);
        return -1;
    }

    /**
     * Devuelve el título de la película seleccionada.
     */
    public String getTituloSeleccionado() {
        int fila = tblPeliculas.getSelectedRow();
        if (fila < 0) return null;
        int col = mostrarId ? 1 : 0;
        return (String) modeloTabla.getValueAt(fila, col);
    }

    /**
     * Devuelve el formato seleccionado.
     */
    public String getFormatoSeleccionado() {
        int fila = tblPeliculas.getSelectedRow();
        if (fila < 0) return null;
        int col = mostrarId ? 6 : 5;
        return (String) modeloTabla.getValueAt(fila, col);
    }

    public String getFiltroFormato() {
        String sel = (String) cmbFiltroFormato.getSelectedItem();
        return "Todos".equals(sel) ? null : sel;
    }

    public int getFilaSeleccionada() {
        return tblPeliculas.getSelectedRow();
    }

    public void setControlador(Controlador controlador) {
        btnBuscar.addActionListener(controlador);
        btnAlquilar.addActionListener(controlador);
        cmbFiltroFormato.addActionListener(controlador);
    }

    public JTextField        getTxtBuscar()        { return txtBuscar; }
    public JButton           getBtnBuscar()        { return btnBuscar; }
    public JButton           getBtnAlquilar()      { return btnAlquilar; }
    public JComboBox<String> getCmbFiltroFormato() { return cmbFiltroFormato; }
    public DefaultTableModel getModelo()           { return modeloTabla; }
    public boolean           isMostrarId()         { return mostrarId; }
}