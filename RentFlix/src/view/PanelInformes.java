// ==========================================
// CLASE: PanelInformes.java
// Estadísticas y resumen de ventas para el Admin.
// ==========================================
package view;

import controller.Controlador;
import model.Alquiler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PanelInformes extends JPanel {

    private static final Color COLOR_FONDO  = new Color(0xF5F5F5);
    private static final Color COLOR_DARK   = new Color(0x1a1a2e);
    private static final Color COLOR_ACENTO = new Color(0xE50914);
    private static final Color COLOR_VERDE  = new Color(0x27AE60);

    // Tarjetas de resumen
    private JLabel lblTotalAlquileres;
    private JLabel lblTotalIngresos;
    private JLabel lblAlquileresActivos;
    private JLabel lblPendientesDevolucion;

    // Tabla detalle
    private DefaultTableModel modeloTabla;
    private JTable            tblDetalle;
    private JButton           btnActualizar;

    public PanelInformes() {
        setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(0, 16));
        setBorder(new EmptyBorder(20, 24, 20, 24));
        initComponents();
    }

    private void initComponents() {
        add(buildTitulo(),   BorderLayout.NORTH);
        add(buildCuerpo(),   BorderLayout.CENTER);
    }

    // ── Título ──────────────────────────────────────────────────────────────

    private JPanel buildTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel lbl = new JLabel("📊 Informes de ventas");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 20));
        lbl.setForeground(COLOR_DARK);

        btnActualizar = new JButton("🔄  Actualizar");
        btnActualizar.setActionCommand("ACTUALIZAR_INFORMES");
        btnActualizar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnActualizar.setBackground(COLOR_DARK);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setBorder(new EmptyBorder(7, 16, 7, 16));
        btnActualizar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        panel.add(lbl,           BorderLayout.WEST);
        panel.add(btnActualizar, BorderLayout.EAST);
        return panel;
    }

    // ── Cuerpo: tarjetas + tabla ────────────────────────────────────────────

    private JPanel buildCuerpo() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setOpaque(false);

        panel.add(buildTarjetas(), BorderLayout.NORTH);
        panel.add(buildTabla(),    BorderLayout.CENTER);

        return panel;
    }

    // ── Tarjetas de resumen ──────────────────────────────────────────────────

    private JPanel buildTarjetas() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 12, 0));
        panel.setOpaque(false);

        lblTotalAlquileres       = new JLabel("0");
        lblTotalIngresos         = new JLabel("0,00 €");
        lblAlquileresActivos     = new JLabel("0");
        lblPendientesDevolucion  = new JLabel("0");

        panel.add(buildTarjeta(
            "Total alquileres", lblTotalAlquileres,
            "📋", COLOR_DARK, new Color(0xE8EAF6)
        ));
        panel.add(buildTarjeta(
            "Ingresos totales", lblTotalIngresos,
            "💰", COLOR_VERDE, new Color(0xE8F8F0)
        ));
        panel.add(buildTarjeta(
            "Alquileres activos", lblAlquileresActivos,
            "🎬", new Color(0x2980B9), new Color(0xE3F2FD)
        ));
        panel.add(buildTarjeta(
            "Pendientes devolución", lblPendientesDevolucion,
            "📦", COLOR_ACENTO, new Color(0xFDEDEC)
        ));

        return panel;
    }

    private JPanel buildTarjeta(String titulo, JLabel lblValor,
                                  String icono, Color colorValor,
                                  Color colorFondo) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD)),
            new EmptyBorder(16, 18, 16, 18)
        ));

        // Icono grande
        JLabel lblIcono = new JLabel(icono);
        lblIcono.setFont(new Font("SansSerif", Font.PLAIN, 26));
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcono.setBackground(colorFondo);
        lblIcono.setOpaque(true);
        lblIcono.setBorder(new EmptyBorder(8, 12, 8, 12));

        // Textos
        JPanel textos = new JPanel();
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        textos.setOpaque(false);
        textos.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTitulo.setForeground(new Color(0x888888));

        lblValor.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblValor.setForeground(colorValor);

        textos.add(lblTitulo);
        textos.add(lblValor);

        tarjeta.add(lblIcono, BorderLayout.WEST);
        tarjeta.add(textos,   BorderLayout.CENTER);
        return tarjeta;
    }

    // ── Tabla detalle ───────────────────────────────────────────────────────

    private JScrollPane buildTabla() {
        JLabel lblDetalle = new JLabel("Detalle de alquileres");
        lblDetalle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblDetalle.setForeground(COLOR_DARK);

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

        tblDetalle = new JTable(modeloTabla);
        tblDetalle.setRowHeight(36);
        tblDetalle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tblDetalle.setShowHorizontalLines(true);
        tblDetalle.setGridColor(new Color(0xEEEEEE));

        tblDetalle.getTableHeader().setFont(
            new Font("SansSerif", Font.BOLD, 12)
        );
        tblDetalle.getTableHeader().setBackground(COLOR_DARK);
        tblDetalle.getTableHeader().setForeground(Color.WHITE);

        int[] anchos = {40, 150, 180, 100, 110, 140, 90};
        for (int i = 0; i < anchos.length; i++) {
            tblDetalle.getColumnModel()
                      .getColumn(i)
                      .setPreferredWidth(anchos[i]);
        }

        JScrollPane scroll = new JScrollPane(tblDetalle);
        scroll.setBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD))
        );
        return scroll;
    }

    // ── Métodos públicos para el Controlador ────────────────────────────────

    public void cargarInformes(List<Alquiler> alquileres,
                                double totalIngresos) {
        // Actualizar tarjetas
        long totalAlq = alquileres.size();
        long activos  = alquileres.stream()
            .filter(a -> "activo".equalsIgnoreCase(a.getEstadoAlquiler()))
            .count();
        long pendientes = alquileres.stream()
            .filter(a -> "pendiente_devolucion"
                .equalsIgnoreCase(a.getEstadoAlquiler()))
            .count();

        lblTotalAlquileres.setText(String.valueOf(totalAlq));
        lblTotalIngresos.setText(String.format("%.2f €", totalIngresos));
        lblAlquileresActivos.setText(String.valueOf(activos));
        lblPendientesDevolucion.setText(String.valueOf(pendientes));

        // Cargar tabla detalle
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
    }

    public void setControlador(Controlador controlador) {
        btnActualizar.addActionListener(controlador);
    }

    public JButton getBtnActualizar() { return btnActualizar; }
}