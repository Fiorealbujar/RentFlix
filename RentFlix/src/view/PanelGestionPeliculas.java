// ==========================================
// CLASE: PanelGestionPeliculas.java
// CRUD completo de películas para el Admin.
// Editar y eliminar desde la tabla directamente.
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

public class PanelGestionPeliculas extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);
    private static final Color COLOR_VERDE  = new Color(0x27AE60);

    private DefaultTableModel modeloTabla;
    private JTable            tblPeliculas;
    private JButton           btnEditar;
    private JButton           btnEliminar;

    public PanelGestionPeliculas() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildSuperior(), BorderLayout.NORTH);
        add(buildTabla(),    BorderLayout.CENTER);
        add(buildAcciones(), BorderLayout.SOUTH);
    }

    // ── Superior ────────────────────────────────────────────────────────────

    private JPanel buildSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel lbl = new JLabel("🎞️  Gestión de películas");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(COLOR_DARK);
        panel.add(lbl);

        JLabel sublbl = new JLabel(
            "  —  Selecciona una película para editarla o eliminarla"
        );
        sublbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sublbl.setForeground(new Color(0x888888));
        panel.add(sublbl);

        return panel;
    }

    // ── Tabla ───────────────────────────────────────────────────────────────

    private JScrollPane buildTabla() {
        String[] columnas = {
            "ID", "Título", "Director", "Género",
            "Duración", "Clasificación", "Sinopsis"
        };

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

        int[] anchos = {40, 220, 160, 110, 80, 100, 260};
        for (int i = 0; i < anchos.length; i++) {
            tblPeliculas.getColumnModel()
                        .getColumn(i)
                        .setPreferredWidth(anchos[i]);
        }

        // Centrar columnas ID, Duración y Clasificación
        DefaultTableCellRenderer centrado = new DefaultTableCellRenderer();
        centrado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int col : new int[]{0, 4, 5}) {
            tblPeliculas.getColumnModel()
                        .getColumn(col)
                        .setCellRenderer(centrado);
        }

        JScrollPane scroll = new JScrollPane(tblPeliculas);
        scroll.setBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD))
        );
        return scroll;
    }

    // ── Acciones ────────────────────────────────────────────────────────────

    private JPanel buildAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(14, 0, 0, 0));

        btnEditar = buildBoton(
            "✏️  Editar película", COLOR_DARK,
            Color.WHITE, "EDITAR_PELICULA"
        );
        btnEditar.setEnabled(false);

        btnEliminar = buildBoton(
            "🗑️  Eliminar película", COLOR_ACENTO,
            Color.WHITE, "ELIMINAR_PELICULA"
        );
        btnEliminar.setEnabled(false);

        panel.add(btnEditar);
        panel.add(btnEliminar);
        return panel;
    }

    private JButton buildBoton(String texto, Color fondo,
                                Color letra, String cmd) {
        JButton btn = new JButton(texto);
        btn.setActionCommand(cmd);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(fondo);
        btn.setForeground(letra);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(9, 20, 9, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ── Métodos públicos para el Controlador ────────────────────────────────

    public void cargarPeliculas(List<Pelicula> peliculas) {
        modeloTabla.setRowCount(0);
        for (Pelicula p : peliculas) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getNombrePelicula(),
                p.getDirector(),
                p.getGenero(),
                p.getDuracion() + " min",
                p.getClasificacionEdad(),
                p.getSinopsis()
            });
        }
        actualizarBotones();
    }

    public void actualizarBotones() {
        boolean haySeleccion = tblPeliculas.getSelectedRow() >= 0;
        btnEditar.setEnabled(haySeleccion);
        btnEliminar.setEnabled(haySeleccion);
    }

    // Devuelve el ID de la película seleccionada
    public int getIdPeliculaSeleccionada() {
        int fila = tblPeliculas.getSelectedRow();
        if (fila < 0) return -1;
        return (int) modeloTabla.getValueAt(fila, 0);
    }

    // Devuelve todos los datos de la fila seleccionada como objeto Pelicula
    public Pelicula getPeliculaSeleccionada() {
        int fila = tblPeliculas.getSelectedRow();
        if (fila < 0) return null;

        String duracionStr = String.valueOf(
            modeloTabla.getValueAt(fila, 4)
        ).replace(" min", "");

        return new Pelicula(
            (int) modeloTabla.getValueAt(fila, 0),
            (String) modeloTabla.getValueAt(fila, 1),
            (String) modeloTabla.getValueAt(fila, 2),
            Integer.parseInt(duracionStr),
            (String) modeloTabla.getValueAt(fila, 3),
            (String) modeloTabla.getValueAt(fila, 6),
            (String) modeloTabla.getValueAt(fila, 5)
        );
    }

    public void setControlador(Controlador controlador) {
        btnEditar.addActionListener(controlador);
        btnEliminar.addActionListener(controlador);

        tblPeliculas.getSelectionModel().addListSelectionListener(
            e -> {
                if (!e.getValueIsAdjusting()) actualizarBotones();
            }
        );
    }

    public JButton getBtnEditar()   { return btnEditar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}