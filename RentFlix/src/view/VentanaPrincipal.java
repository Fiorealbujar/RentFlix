package view;

import controller.NavController;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final JPanel contentArea;
    private final NavController navController;

    public MainFrame() {
        setTitle("RentFlix — Gestión de videoclub");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 580));
        setLocationRelativeTo(null); // centrar en pantalla

        // Icono de la app (opcional: sustituir por imagen real)
        // setIconImage(new ImageIcon("src/resources/logo.png").getImage());

        // Área central de contenido (aquí se intercambian los paneles)
        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(new Color(0xF5F5F5));

        // Controlador de navegación
        navController = new NavController(this, contentArea);

        // Topbar
        TopBar topBar = new TopBar(navController);

        // Sidebar
        Sidebar sidebar = new Sidebar(navController);

        // Shell: sidebar + área de contenido
        JPanel shell = new JPanel(new BorderLayout());
        shell.add(sidebar,     BorderLayout.WEST);
        shell.add(contentArea, BorderLayout.CENTER);

        // Layout principal: topbar arriba, shell abajo
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(topBar, BorderLayout.NORTH);
        getContentPane().add(shell,  BorderLayout.CENTER);

        // Mostrar dashboard al arrancar
        navController.navegarA(NavController.Seccion.DASHBOARD);
    }

    public NavController getNavController() {
        return navController;
    }
}
