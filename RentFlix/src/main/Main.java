package main;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import view.LoginFrame;

import javax.swing.*;
import java.util.prefs.Preferences;

public class Main {

    public static void main(String[] args) {

        // Recuperar preferencia de tema guardada (por defecto: light)
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String tema = prefs.get("theme", "light");

        // Aplicar tema antes de crear cualquier ventana
        try {
            if (tema.equals("dark")) {
                FlatDarkLaf.setup();
            } else {
                FlatLightLaf.setup();
            }
        } catch (Exception e) {
            // Si FlatLaf falla, Swing usará el look por defecto
            e.printStackTrace();
        }

        // Lanzar la ventana principal en el hilo de eventos de Swing
     // Busca esto dentro del método main de tu archivo main/Main.java:
        SwingUtilities.invokeLater(() -> {
            // Cambiamos MainFrame por LoginFrame
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });
    }
}
