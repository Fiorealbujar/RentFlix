package main;

import com.formdev.flatlaf.FlatLightLaf;
import controller.Controlador;
import view.*;

import javax.swing.*;

/**
 * Punto de entrada de la aplicación RentFlix.
 * <p>
 * Inicializa el tema visual {@code FlatLightLaf} de la librería FlatLaf,
 * localiza los textos de los diálogos {@link JOptionPane} al español, instancia
 * todas las vistas y el controlador, inyecta el controlador en cada vista
 * mediante {@code setControlador()} y lanza la ventana principal en el hilo de
 * despacho de eventos de Swing ({@link SwingUtilities#invokeLater}).
 * </p>
 *
 * @author Gabriel Fernández Cañadas
 * @version 1.0
 */
public class Main {

	/**
	 * Método principal de la aplicación.
	 * <p>
	 * Configura el tema visual, crea todas las vistas agrupadas por rol (cliente,
	 * empleado, administrador), instancia el controlador con todas las vistas y
	 * registra el controlador como listener en cada panel.
	 * </p>
	 *
	 * @param args argumentos de línea de comandos (no se usan)
	 */
	public static void main(String[] args) {
		FlatLightLaf.setup();

		UIManager.put("OptionPane.yesButtonText", "Sí");
		UIManager.put("OptionPane.noButtonText", "No");
		UIManager.put("OptionPane.okButtonText", "Aceptar");
		UIManager.put("OptionPane.cancelButtonText", "Cancelar");
		
		// Crear la interfaz en el hilo de eventos de Swing para evitar problemas
		// de concurrencia en los componentes gráficos
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				VentanaPrincipal ventana = new VentanaPrincipal();
				PanelLogin panelLogin = new PanelLogin();
				PanelRegistro panelRegistro = new PanelRegistro();

				//Cliente
				PanelCatalogo catInvitado = new PanelCatalogo(false);
				PanelCatalogo catCliente = new PanelCatalogo(false);
				PanelMisAlquileres misAlquileres = new PanelMisAlquileres();
				PanelMiCuenta panelMiCuenta = new PanelMiCuenta();
				PanelCliente panelCliente = new PanelCliente(catCliente, misAlquileres, panelMiCuenta);

				//Empleado
				PanelGestionAlquileres gestionAlqEmp = new PanelGestionAlquileres();
				PanelAnadirPelicula anadirPelEmp = new PanelAnadirPelicula();
				PanelGestionPeliculas gestionPelEmp = new PanelGestionPeliculas();
				PanelInformes informesEmp = new PanelInformes();
				PanelGestionClientes gestionClientesEmp = new PanelGestionClientes();
				PanelEmpleado panelEmpleado = new PanelEmpleado(gestionAlqEmp, anadirPelEmp, gestionPelEmp, informesEmp,
						gestionClientesEmp);

				//Admin
				PanelGestionAlquileres gestionAlqAdm = new PanelGestionAlquileres();
				PanelAnadirPelicula anadirPelAdm = new PanelAnadirPelicula();
				PanelGestionPeliculas gestionPelAdm = new PanelGestionPeliculas();
				PanelInformes informesAdm = new PanelInformes();
				PanelGestionClientes gestionClientesAdm = new PanelGestionClientes();
				PanelGestionEmpleados gestionEmpleados = new PanelGestionEmpleados();
				PanelAdmin panelAdmin = new PanelAdmin(gestionAlqAdm, anadirPelAdm, gestionPelAdm, informesAdm,
						gestionEmpleados, gestionClientesAdm);

				//Controlador
				Controlador controlador = new Controlador(ventana, catInvitado, catCliente, panelLogin, panelRegistro,
						panelCliente, misAlquileres, panelMiCuenta, panelEmpleado, gestionAlqEmp, anadirPelEmp,
						gestionPelEmp, informesEmp, gestionClientesEmp, panelAdmin, gestionAlqAdm, anadirPelAdm,
						gestionPelAdm, informesAdm, gestionClientesAdm, gestionEmpleados);

				//Inyectar controlador
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
				gestionClientesEmp.setControlador(controlador);
				panelAdmin.setControlador(controlador);
				gestionAlqAdm.setControlador(controlador);
				anadirPelAdm.setControlador(controlador);
				gestionPelAdm.setControlador(controlador);
				gestionClientesAdm.setControlador(controlador);
				gestionEmpleados.setControlador(controlador);

				ventana.hacerVisible();
			}
		});
	}
}