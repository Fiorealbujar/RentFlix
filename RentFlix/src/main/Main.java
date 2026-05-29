// Main.java
package main;

import com.formdev.flatlaf.FlatLightLaf;
import controller.Controlador;
import view.*;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		FlatLightLaf.setup();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				VentanaPrincipal ventana = new VentanaPrincipal();
				PanelLogin panelLogin = new PanelLogin();
				PanelRegistro panelRegistro = new PanelRegistro();

				PanelCatalogo catInvitado = new PanelCatalogo(false);
				PanelCatalogo catCliente = new PanelCatalogo(false);
				PanelMisAlquileres misAlquileres = new PanelMisAlquileres();
				PanelCliente panelCliente = new PanelCliente(catCliente, misAlquileres);

				PanelGestionAlquileres gestionAlqEmp = new PanelGestionAlquileres();
				PanelAnadirPelicula anadirPelEmp = new PanelAnadirPelicula();
				PanelGestionPeliculas gestionPelEmp = new PanelGestionPeliculas();
				PanelInformes informesEmp = new PanelInformes();
				PanelEmpleado panelEmpleado = new PanelEmpleado(gestionAlqEmp, anadirPelEmp, gestionPelEmp,
						informesEmp);

				PanelGestionAlquileres gestionAlqAdm = new PanelGestionAlquileres();
				PanelAnadirPelicula anadirPelAdm = new PanelAnadirPelicula();
				PanelGestionPeliculas gestionPelAdm = new PanelGestionPeliculas();
				PanelInformes informesAdm = new PanelInformes();
				PanelGestionEmpleados gestionEmpleados = new PanelGestionEmpleados();
				PanelGestionClientes gestionClientes = new PanelGestionClientes();
				PanelAdmin panelAdmin = new PanelAdmin(gestionAlqAdm, anadirPelAdm, gestionPelAdm, informesAdm,
						gestionEmpleados, gestionClientes);

				Controlador controlador = new Controlador(ventana, catInvitado, catCliente, panelLogin, panelRegistro,
						panelCliente, misAlquileres, panelEmpleado, gestionAlqEmp, anadirPelEmp, gestionPelEmp,
						informesEmp, panelAdmin, gestionAlqAdm, anadirPelAdm, gestionPelAdm, informesAdm,
						gestionEmpleados, gestionClientes);

				ventana.setControlador(controlador);
				catInvitado.setControlador(controlador);
				catCliente.setControlador(controlador);
				panelLogin.setControlador(controlador);
				panelRegistro.setControlador(controlador);
				panelCliente.setControlador(controlador);
				misAlquileres.setControlador(controlador);
				panelEmpleado.setControlador(controlador);
				gestionAlqEmp.setControlador(controlador);
				anadirPelEmp.setControlador(controlador);
				gestionPelEmp.setControlador(controlador);
				informesEmp.setControlador(controlador);
				panelAdmin.setControlador(controlador);
				gestionAlqAdm.setControlador(controlador);
				anadirPelAdm.setControlador(controlador);
				gestionPelAdm.setControlador(controlador);
				informesAdm.setControlador(controlador);
				gestionEmpleados.setControlador(controlador);
				gestionClientes.setControlador(controlador);

				ventana.hacerVisible();
			}
		});
	}
}