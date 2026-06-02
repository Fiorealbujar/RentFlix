// Main.java
package main;

import com.formdev.flatlaf.FlatLightLaf;
import controller.Controlador;
import view.*;

import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		FlatLightLaf.setup();
		
		UIManager.put("OptionPane.yesButtonText",    "Sí");
		UIManager.put("OptionPane.noButtonText",     "No");
		UIManager.put("OptionPane.okButtonText",     "Aceptar");
		UIManager.put("OptionPane.cancelButtonText", "Cancelar");

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				VentanaPrincipal ventana = new VentanaPrincipal();
				PanelLogin panelLogin = new PanelLogin();
				PanelRegistro panelRegistro = new PanelRegistro();

				// ── Cliente ──────────────────────────────────────────────────
				PanelCatalogo catInvitado = new PanelCatalogo(false);
				PanelCatalogo catCliente = new PanelCatalogo(false);
				PanelMisAlquileres misAlquileres = new PanelMisAlquileres();
				PanelMiCuenta panelMiCuenta = new PanelMiCuenta();
				PanelCliente panelCliente = new PanelCliente(catCliente, misAlquileres, panelMiCuenta);

				// ── Empleado ─────────────────────────────────────────────────
				PanelGestionAlquileres gestionAlqEmp = new PanelGestionAlquileres();
				PanelAnadirPelicula anadirPelEmp = new PanelAnadirPelicula();
				PanelGestionPeliculas gestionPelEmp = new PanelGestionPeliculas();
				PanelInformes informesEmp = new PanelInformes();
				PanelGestionClientes gestionClientesEmp = new PanelGestionClientes();
				PanelEmpleado panelEmpleado = new PanelEmpleado(gestionAlqEmp, anadirPelEmp, gestionPelEmp, informesEmp,
						gestionClientesEmp);

				// ── Admin ────────────────────────────────────────────────────
				PanelGestionAlquileres gestionAlqAdm = new PanelGestionAlquileres();
				PanelAnadirPelicula anadirPelAdm = new PanelAnadirPelicula();
				PanelGestionPeliculas gestionPelAdm = new PanelGestionPeliculas();
				PanelInformes informesAdm = new PanelInformes();
				PanelGestionClientes gestionClientesAdm = new PanelGestionClientes();
				PanelGestionEmpleados gestionEmpleados = new PanelGestionEmpleados();
				PanelAdmin panelAdmin = new PanelAdmin(gestionAlqAdm, anadirPelAdm, gestionPelAdm, informesAdm,
						gestionEmpleados, gestionClientesAdm);

				// ── Controlador ──────────────────────────────────────────────
				Controlador controlador = new Controlador(ventana, catInvitado, catCliente, panelLogin, panelRegistro,
						panelCliente, misAlquileres, panelMiCuenta, panelEmpleado, gestionAlqEmp, anadirPelEmp,
						gestionPelEmp, informesEmp, gestionClientesEmp, panelAdmin, gestionAlqAdm, anadirPelAdm,
						gestionPelAdm, informesAdm, gestionClientesAdm, gestionEmpleados);

				// ── Inyectar controlador ─────────────────────────────────────
				ventana.setControlador(controlador);
				catInvitado.setControlador(controlador);
				catCliente.setControlador(controlador);
				panelLogin.setControlador(controlador);
				panelRegistro.setControlador(controlador);
				panelCliente.setControlador(controlador);
				misAlquileres.setControlador(controlador);
				panelMiCuenta.setControlador(controlador);
				panelEmpleado.setControlador(controlador);
				gestionAlqEmp.setControlador(controlador);
				anadirPelEmp.setControlador(controlador);
				gestionPelEmp.setControlador(controlador);
				informesEmp.setControlador(controlador);
				gestionClientesEmp.setControlador(controlador);
				panelAdmin.setControlador(controlador);
				gestionAlqAdm.setControlador(controlador);
				anadirPelAdm.setControlador(controlador);
				gestionPelAdm.setControlador(controlador);
				informesAdm.setControlador(controlador);
				gestionClientesAdm.setControlador(controlador);
				gestionEmpleados.setControlador(controlador);

				ventana.hacerVisible();
			}
		});
	}
}