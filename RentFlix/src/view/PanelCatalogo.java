// ==========================================
// CLASE: PanelCatalogo.java
// Vista del catálogo de películas.
// Visible para todos los roles (incluso invitado).
// Los botones de acción se habilitan/deshabilitan según el rol.
// ==========================================
package view;

import controller.Controlador;
import model.Pelicula;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCatalogo extends JPanel {

    // Colores del tema
    private static final Color COLOR_FONDO      = new Color(0xF5F5F5);
    private static final Color COLOR_PANEL      = Color.WHITE;
    private static final Color COLOR_ACENTO     = new Color(0xE50914);
    private static final Color COLOR_TEXTO_SEC  = new Color(0x777777);

    // Componentes que el controlador necesita
    private JTextField       txtBuscar;
    private JButton          btnBuscar;
    private JButton          btnAlquilar;
    private JTable           tblPeliculas;
    private DefaultTableModel modeloTabla;

    public PanelCatalogo() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 0));
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildPanelSuperior(), BorderLayout.NORTH);
        add(buildPanelTabla(),    BorderLayout.CENTER);
        add(buildPanelAcciones(), BorderLayout.SOUTH);
    }

    // ── Barra de búsqueda ───────────────────────────────────────────────────

    private JPanel buildPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel lblTitulo = new JLabel("Catálogo de películas");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0x1a1a2e));

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelBusqueda.setOpaque(false);

        txtBuscar = new JTextField(22);
        txtBuscar.putClientProperty("JTextField.placeholderText", "Buscar película...");

        btnBuscar = new JButton("Buscar");
        btnBuscar.setActionCommand("BUSCAR_PELICULA");
        btnBuscar.setBackground(new Color(0x1a1a2e));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        panel.add(lblTitulo,     BorderLayout.WEST);
        panel.add(panelBusqueda, BorderLayout.EAST);
        return panel;
    }

    // ── Tabla de películas ──────────────────────────────────────────────────

    private JScrollPane buildPanelTabla() {
        String[] columnas = {"#", "Título", "Director", "Género", "Duración", "Clasificación"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tblPeliculas = new JTable(modeloTabla);
        tblPeliculas.setRowHeight(38);
        tblPeliculas.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tblPeliculas.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        tblPeliculas.getTableHeader().setBackground(new Color(0x1a1a2e));
        tblPeliculas.getTableHeader().setForeground(Color.WHITE);
        tblPeliculas.setSelectionBackground(new Color(0xFFE0E0));
        tblPeliculas.setSelectionForeground(new Color(0x1a1a2e));
        tblPeliculas.setShowHorizontalLines(true);
        tblPeliculas.setGridColor(new Color(0xEEEEEE));
        tblPeliculas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Anchos de columna
        int[] anchos = {40, 280, 180, 120, 90, 100};
        for (int i = 0; i < anchos.length; i++) {
            tblPeliculas.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }

        // Centrar columnas numéricas
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        tblPeliculas.getColumnModel().getColumn(0).setCellRenderer(centrado);
        tblPeliculas.getColumnModel().getColumn(4).setCellRenderer(centrado);
        tblPeliculas.getColumnModel().getColumn(5).setCellRenderer(centrado);

        JScrollPane scroll = new JScrollPane(tblPeliculas);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0xDDDDDD)));
        return scroll;
    }

    // ── Botones de acción ───────────────────────────────────────────────────

    private JPanel buildPanelAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(14, 0, 0, 0));

        btnAlquilar = new JButton("🎬  Alquilar película");
        btnAlquilar.setActionCommand("ALQUILAR_PELICULA");
        btnAlquilar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnAlquilar.setBackground(COLOR_ACENTO);
        btnAlquilar.setForeground(Color.WHITE);
        btnAlquilar.setFocusPainted(false);
        btnAlquilar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAlquilar.setBorder(new EmptyBorder(9, 20, 9, 20));

        // Por defecto deshabilitado (modo invitado)
        btnAlquilar.setEnabled(false);
        btnAlquilar.setToolTipText("Inicia sesión para alquilar películas");

        panel.add(btnAlquilar);
        return panel;
    }

    // ── Métodos públicos que usa el Controlador ─────────────────────────────

    // Carga la lista de películas en la tabla
    public void cargarPeliculas(List<Pelicula> peliculas) {
        modeloTabla.setRowCount(0); // limpiar tabla
        int contador = 1;
        for (Pelicula p : peliculas) {
            modeloTabla.addRow(new Object[]{
                contador++,
                p.getNombrePelicula(),
                p.getDirector(),
                p.getGenero(),
                p.getDuracion() + " min",
                p.getClasificacionEdad()
            });
        }
    }

    // Devuelve la película seleccionada en la tabla (fila)
    public int getFilaSeleccionada() {
        return tblPeliculas.getSelectedRow();
    }

    // Habilita o deshabilita el botón de alquilar según el rol
    public void habilitarAcciones(boolean habilitar) {
        btnAlquilar.setEnabled(habilitar);
        btnAlquilar.setToolTipText(habilitar ? null : "Inicia sesión para alquilar películas");
    }

    public void setControlador(Controlador controlador) {
        btnBuscar.addActionListener(controlador);
        btnAlquilar.addActionListener(controlador);
    }

    // Getters
    public JTextField getTxtBuscar()        { return txtBuscar; }
    public JButton    getBtnBuscar()        { return btnBuscar; }
    public JButton    getBtnAlquilar()      { return btnAlquilar; }
    public JTable     getTblPeliculas()     { return tblPeliculas; }
    public DefaultTableModel getModelo()    { return modeloTabla; }
}