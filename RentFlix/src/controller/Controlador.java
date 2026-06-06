package controller;

import dao.*;
import model.*;
import view.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Controlador principal de la aplicación RentFlix.
 * <p>
 * Implementa {@link ActionListener} y centraliza toda la lógica de negocio.
 * Gestiona la sesión activa, coordina las vistas con los DAOs y procesa todos
 * los eventos de la interfaz gráfica.
 * </p>
 *
 * @author Ana Belén Rueda Reina
 * @version 1.0
 */
public class Controlador implements ActionListener {

	// ── Vistas ────────────────────────────────────────────────────────────────
	private VentanaPrincipal ventana;
	private PanelCatalogo catInvitado;
	private PanelCatalogo catCliente;
	private PanelLogin panelLogin;
	private PanelRegistro panelRegistro;
	private PanelCliente panelCliente;
	private PanelMisAlquileres misAlquileres;
	private PanelMiCuenta panelMiCuenta;

	// Paneles exclusivos del empleado
	private PanelEmpleado panelEmpleado;
	private PanelGestionAlquileres gestionAlqEmp;
	private PanelAnadirPelicula anadirPelEmp;
	private PanelGestionPeliculas gestionPelEmp;
	private PanelInformes informesEmp;
	private PanelGestionClientes gestionClientesEmp;

	// Paneles exclusivos del admin
	private PanelAdmin panelAdmin;
	private PanelGestionAlquileres gestionAlqAdm;
	private PanelAnadirPelicula anadirPelAdm;
	private PanelGestionPeliculas gestionPelAdm;
	private PanelInformes informesAdm;
	private PanelGestionClientes gestionClientesAdm;
	private PanelGestionEmpleados gestionEmpleados;

	// ── DAOs ──────────────────────────────────────────────────────────────────
	private IPeliculaDAO peliculaDAO;
	private IClienteDAO clienteDAO;
	private IEmpleadoDAO empleadoDAO;
	private IAlquilerDAO alquilerDAO;
	private ICopiaDAO copiaDAO;
	private IPagoDAO pagoDAO;

	// ── Sesión ────────────────────────────────────────────────────────────────
	private Cliente clienteActivo = null;
	private Empleado empleadoActivo = null;

	private ArrayList<Cliente> listaClientesCache;
	private ArrayList<Pelicula> listaPeliculasCache;
	private boolean esAdmin = false;

	public Controlador(VentanaPrincipal ventana, PanelCatalogo catInvitado, PanelCatalogo catCliente,
			PanelLogin panelLogin, PanelRegistro panelRegistro, PanelCliente panelCliente,
			PanelMisAlquileres misAlquileres, PanelMiCuenta panelMiCuenta, PanelEmpleado panelEmpleado,
			PanelGestionAlquileres gestionAlqEmp, PanelAnadirPelicula anadirPelEmp, PanelGestionPeliculas gestionPelEmp,
			PanelInformes informesEmp, PanelGestionClientes gestionClientesEmp, PanelAdmin panelAdmin,
			PanelGestionAlquileres gestionAlqAdm, PanelAnadirPelicula anadirPelAdm, PanelGestionPeliculas gestionPelAdm,
			PanelInformes informesAdm, PanelGestionClientes gestionClientesAdm,
			PanelGestionEmpleados gestionEmpleados) {

		this.ventana = ventana;
		this.catInvitado = catInvitado;
		this.catCliente = catCliente;
		this.panelLogin = panelLogin;
		this.panelRegistro = panelRegistro;
		this.panelCliente = panelCliente;
		this.misAlquileres = misAlquileres;
		this.panelMiCuenta = panelMiCuenta;
		this.panelEmpleado = panelEmpleado;
		this.gestionAlqEmp = gestionAlqEmp;
		this.anadirPelEmp = anadirPelEmp;
		this.gestionPelEmp = gestionPelEmp;
		this.informesEmp = informesEmp;
		this.gestionClientesEmp = gestionClientesEmp;
		this.panelAdmin = panelAdmin;
		this.gestionAlqAdm = gestionAlqAdm;
		this.anadirPelAdm = anadirPelAdm;
		this.gestionPelAdm = gestionPelAdm;
		this.informesAdm = informesAdm;
		this.gestionClientesAdm = gestionClientesAdm;
		this.gestionEmpleados = gestionEmpleados;

		peliculaDAO = new PeliculaDAO();
		clienteDAO = new ClienteDAO();
		empleadoDAO = new EmpleadoDAO();
		alquilerDAO = new AlquilerDAO();
		copiaDAO = new CopiaDAO();
		pagoDAO = new PagoDAO();

		catInvitado.setSelectionListener(this);

		iniciarModoInvitado();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();

		// ── JButton ───────────────────────────────────────────────────────────
		if (src instanceof JButton) {
			switch (e.getActionCommand()) {

			// ── VentanaPrincipal ──────────────────────────────────────────
			case "ABRIR_LOGIN":
				panelLogin.limpiar();
				ventana.cargarPanel(panelLogin);
				break;
			case "ABRIR_REGISTRO":
				panelRegistro.limpiar();
				ventana.cargarPanel(panelRegistro);
				break;
			case "CERRAR_SESION":
				cerrarSesion();
				break;

			// ── PanelLogin ────────────────────────────────────────────────
			case "LOGIN":
				procesarLogin();
				break;
			case "CANCELAR_LOGIN":
				ventana.cargarPanel(catInvitado);
				break;

			// ── PanelRegistro ─────────────────────────────────────────────
			case "REGISTRAR_CLIENTE":
				procesarRegistro();
				break;
			case "CANCELAR_REGISTRO":
				ventana.cargarPanel(catInvitado);
				break;

			// ── PanelCatalogo ─────────────────────────────────────────────
			case "BUSCAR_PELICULA":
				buscarPelicula(e);
				break;

			case "BUSCAR_PELICULA_GESTION":
				buscarPeliculaGestion();
				break;

			case "ALQUILAR_PELICULA":
				alquilarDesdeClienteCatalogo();
				break;

			// ── PanelMisAlquileres ────────────────────────────────────────
			case "SOLICITAR_DEVOLUCION":
				procesarSolicitudDevolucion();
				break;

			// ── PanelMiCuenta ─────────────────────────────────────────────
			case "MODIFICAR_DATOS_CLIENTE":
				modificarDatosCliente();
				break;

			// ── PanelGestionAlquileres ────────────────────────────────────
			case "ABRIR_FORM_ALQUILER":
				abrirFormAlquiler();
				break;
			case "CANCELAR_FORM_ALQUILER":
				getPanelGestionActivo().mostrarFormAlquiler(false);
				break;
			case "CONFIRMAR_ALQUILER_EMPLEADO":
				confirmarAlquilerEmpleado();
				break;
			case "ACEPTAR_DEVOLUCION":
				procesarAceptarDevolucion();
				break;

			// ── PanelAnadirPelicula ───────────────────────────────────────
			case "GUARDAR_PELICULA":
				guardarPelicula();
				break;
			case "LIMPIAR_FORM_PELICULA":
				getPanelAnadirActivo().limpiar();
				break;

			// ── PanelGestionPeliculas ─────────────────────────────────────
			case "EDITAR_PELICULA":
				editarPelicula();
				break;
			case "DAR_DE_BAJA_PELICULA":
				darDeBajaPelicula();
				break;

			// ── PanelGestionEmpleados (solo admin) ────────────────────────
			case "CREAR_EMPLEADO":
				crearEmpleado();
				break;
			case "EDITAR_EMPLEADO":
				editarEmpleado();
				break;
			case "ELIMINAR_EMPLEADO":
				eliminarEmpleado();
				break;
			case "LIMPIAR_FORM_EMPLEADO":
				gestionEmpleados.limpiar();
				break;

			// ── PanelGestionClientes (empleado y admin) ───────────────────
			case "EDITAR_CLIENTE":
				editarCliente();
				break;
			case "BLOQUEAR_CLIENTE":
				bloquearClienteDesdeGestion();
				break;
			case "ELIMINAR_CLIENTE":
				eliminarCliente();
				break;

			// ── PanelGestionAlquileres — bloquear cliente con alquiler vencido ──
			case "BLOQUEAR_CLIENTE_ALQUILER":
				bloquearClienteDesdeAlquiler();
				break;
			}

			// ── JComboBox ─────────────────────────────────────────────────────────
		} else if (src instanceof JComboBox) {
			switch (e.getActionCommand()) {

			// ── PanelCatalogo ─────────────────────────────────────────────
			case "FILTRAR_FORMATO_CATALOGO":
				filtrarFormato(e);
				break;

			// ── PanelGestionAlquileres ────────────────────────────────────
			case "FILTRAR_ALQUILERES":
				filtrarAlquileres();
				break;

			// ── PanelMisAlquileres ────────────────────────────────────────
			case "FILTRAR_MIS_ALQUILERES":
				filtrarMisAlquileres();
				break;

			// ── PanelGestionClientes ──────────────────────────────────────
			case "FILTRAR_CLIENTES":
				filtrarClientes();
				break;
			}
		}
	}

	// ── Helpers ───────────────────────────────────────────────────────────────

	private PanelGestionAlquileres getPanelGestionActivo() {
		if (esAdmin) {
			return gestionAlqAdm;
		} else {
			return gestionAlqEmp;
		}
	}

	private PanelGestionPeliculas getPanelGestionPelActivo() {
		if (esAdmin) {
			return gestionPelAdm;
		} else {
			return gestionPelEmp;
		}
	}

	private PanelAnadirPelicula getPanelAnadirActivo() {
		if (esAdmin) {
			return anadirPelAdm;
		} else {
			return anadirPelEmp;
		}
	}

	private PanelGestionClientes getPanelGestionClientesActivo() {
		if (esAdmin) {
			return gestionClientesAdm;
		} else {
			return gestionClientesEmp;
		}
	}

	// ── Modo invitado ─────────────────────────────────────────────────────────

	// ── Helper conteo de copias
	// ─────────────────────────────────────────────────────

	private ArrayList<Integer> obtenerConteosCopias(ArrayList<Pelicula> peliculas) {
		ArrayList<Integer> conteos = new ArrayList<Integer>();
		for (Pelicula p : peliculas) {
			conteos.add(copiaDAO.contarDisponiblesPorPelicula(p.getId()));
		}
		return conteos;
	}

	private void iniciarModoInvitado() {
		clienteActivo = null;
		empleadoActivo = null;
		esAdmin = false;
		ArrayList<Pelicula> peliculas = peliculaDAO.listarTodas();
		ArrayList<Copia> copias = copiaDAO.listarTodasDisponibles();
		catInvitado.cargarCopias(peliculas, copias);
		catInvitado.habilitarAcciones(false);
		ventana.modoInvitado();
		ventana.cargarPanel(catInvitado);
	}

	// ── Catálogos ─────────────────────────────────────────────────────────────

	private void recargarCatalogoCliente() {
		ArrayList<Pelicula> peliculas = peliculaDAO.listarTodas();
		ArrayList<Copia> copias = copiaDAO.listarTodasDisponibles();
		catCliente.cargarCopias(peliculas, copias);
	}

	private void buscarPelicula(ActionEvent e) {
		PanelCatalogo origen;
		if (e.getSource() == catCliente.getBtnBuscar()) {
			origen = catCliente;
		} else {
			origen = catInvitado;
		}

		String termino = origen.getTxtBuscar().getText().trim();
		ArrayList<Pelicula> peliculas;
		if (termino.isEmpty()) {
			peliculas = peliculaDAO.listarTodas();
		} else {
			peliculas = peliculaDAO.buscarPorTitulo(termino);
		}

		ArrayList<Copia> todasCopias = copiaDAO.listarTodasDisponibles();
		ArrayList<Copia> copiasFiltradas = new ArrayList<Copia>();
		for (Copia c : todasCopias) {
			for (Pelicula p : peliculas) {
				if (p.getId() == c.getIdPelicula()) {
					copiasFiltradas.add(c);
					break;
				}
			}
		}
		origen.cargarCopias(peliculas, copiasFiltradas);
	}

	private void filtrarFormato(ActionEvent e) {
		PanelCatalogo origen;
		if (e.getSource() == catCliente.getCmbFiltroFormato()) {
			origen = catCliente;
		} else {
			origen = catInvitado;
		}

		String filtro = origen.getFiltroFormato();
		ArrayList<Pelicula> peliculas = peliculaDAO.listarTodas();
		ArrayList<Copia> copias;
		if (filtro == null) {
			copias = copiaDAO.listarTodasDisponibles();
		} else {
			copias = copiaDAO.listarDisponiblesPorFormato(filtro);
		}
		origen.cargarCopias(peliculas, copias);
	}

	// ── Alquiler cliente ──────────────────────────────────────────────────────

	private void alquilarDesdeClienteCatalogo() {
		if (clienteActivo == null) {
			int res = JOptionPane.showConfirmDialog(ventana,
					"Para alquilar necesitas iniciar sesión.\n" + "¿Quieres ir a iniciar sesión ahora?",
					"Inicio de sesión requerido", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (res == JOptionPane.YES_OPTION) {
				panelLogin.limpiar();
				ventana.cargarPanel(panelLogin);
			}
			return;
		}

		int fila = catCliente.getFilaSeleccionada();

		if (fila < 0) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una película del catálogo primero.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String titulo = catCliente.getTituloSeleccionado();
		String formato = catCliente.getFormatoSeleccionado();

		ArrayList<Pelicula> encontradas = peliculaDAO.buscarPorTitulo(titulo);
		if (encontradas.isEmpty()) {
			return;
		}

		ArrayList<Copia> todasCopias = copiaDAO.listarDisponiblesPorPelicula(encontradas.get(0).getId());
		ArrayList<Copia> copias = new ArrayList<Copia>();
		for (Copia c : todasCopias) {
			if (c.getFormato().equals(formato)) {
				copias.add(c);
			}
		}

		if (copias.isEmpty()) {
			JOptionPane.showMessageDialog(ventana, "No hay copias disponibles en formato " + formato + ".", "Sin stock",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		mostrarDialogoAlquilerCliente(encontradas.get(0), copias.get(0));
	}

	private void mostrarDialogoAlquilerCliente(Pelicula pelicula, Copia copia) {
		JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
		form.setBorder(new EmptyBorder(10, 10, 10, 10));

		JSpinner spinnerDias = new JSpinner(new SpinnerNumberModel(3, 1, 30, 1));
		JComboBox<String> cmbPago = new JComboBox<>(new String[] { "efectivo", "tarjeta", "transferencia" });

		form.add(new JLabel("Película:"));
		form.add(new JLabel(pelicula.getNombrePelicula()));
		form.add(new JLabel("Formato:"));
		form.add(new JLabel(copia.getFormato()));
		form.add(new JLabel("Precio/día:"));
		form.add(new JLabel(String.format("%.2f €", copia.getPrecioAlquiler())));
		form.add(new JLabel("Días:"));
		form.add(spinnerDias);
		form.add(new JLabel("Método de pago:"));
		form.add(cmbPago);

		int res = JOptionPane.showConfirmDialog(ventana, form, "Confirmar alquiler", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (res == JOptionPane.OK_OPTION) {
			int dias = (int) spinnerDias.getValue();
			String metodo = (String) cmbPago.getSelectedItem();
			double total = copia.getPrecioAlquiler() * dias;
			registrarAlquiler(clienteActivo.getIdCliente(), copia, null, dias, metodo, total);
		}
	}

	// ── Alquiler empleado/admin ───────────────────────────────────────────────

	private void abrirFormAlquiler() {
		listaClientesCache = clienteDAO.listarTodos();
		listaPeliculasCache = peliculaDAO.listarTodas();
		getPanelGestionActivo().cargarComboClientes(listaClientesCache);
		getPanelGestionActivo().cargarComboPeliculas(listaPeliculasCache);
		getPanelGestionActivo().mostrarFormAlquiler(true);
	}

	private void confirmarAlquilerEmpleado() {
		PanelGestionAlquileres panel = getPanelGestionActivo();
		int idxCliente = panel.getIndexClienteSeleccionado();
		int idxPelicula = panel.getIndexPeliculaSeleccionada();

		if (idxCliente < 0 || idxPelicula < 0 || listaClientesCache == null || listaPeliculasCache == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona cliente y película.", "Datos incompletos",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Cliente cliente = listaClientesCache.get(idxCliente);
		Pelicula pelicula = listaPeliculasCache.get(idxPelicula);
		String formato = panel.getFormatoSeleccionado();
		int dias = panel.getDiasAlquiler();
		String metodo = panel.getMetodoPagoSeleccionado();

		ArrayList<Copia> todasCopias = copiaDAO.listarDisponiblesPorPelicula(pelicula.getId());
		ArrayList<Copia> copias = new ArrayList<Copia>();
		for (Copia c : todasCopias) {
			if (c.getFormato().equals(formato)) {
				copias.add(c);
			}
		}

		if (copias.isEmpty()) {
			JOptionPane.showMessageDialog(ventana, "No hay copias disponibles en formato " + formato + ".", "Sin stock",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		double total = copias.get(0).getPrecioAlquiler() * dias;
		registrarAlquiler(cliente.getIdCliente(), copias.get(0), empleadoActivo.getIdEmpleado(), dias, metodo, total);
		panel.mostrarFormAlquiler(false);
	}

	// ── Registro de alquiler compartido ──────────────────────────────────────

	private void registrarAlquiler(int idCliente, Copia copia, Integer idEmpleado, int dias, String metodoPago,
			double total) {
		int idTrans = pagoDAO.registrar(new Pago(0, metodoPago, total));
		if (idTrans == -1) {
			JOptionPane.showMessageDialog(ventana, "Error al procesar el pago.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		java.time.LocalDate hoy = java.time.LocalDate.now();
		java.time.LocalDate devolucion = hoy.plusDays(dias);

		int resAlquiler = alquilerDAO.crear(new Alquiler(0, idCliente, copia.getIdCopia(), idEmpleado, idTrans,
				hoy.toString(), devolucion.toString(), null, "activo"));

		if (resAlquiler > 0) {
			copiaDAO.actualizarEstado(copia.getIdCopia(), "alquilada");
			JOptionPane.showMessageDialog(ventana, "¡Alquiler confirmado!\n" + "Devuelve antes del " + devolucion + "\n"
					+ "Total: " + String.format("%.2f €", total), "Alquiler exitoso", JOptionPane.INFORMATION_MESSAGE);

			if (clienteActivo != null && empleadoActivo == null) {
				recargarCatalogoCliente();
				misAlquileres.cargarAlquileres(alquilerDAO.listarPorCliente(clienteActivo.getIdCliente()));
				actualizarContadorActivosCliente();
				panelCliente.irAMisAlquileres();
			} else if (empleadoActivo != null) {
				getPanelGestionActivo().cargarAlquileres(alquilerDAO.listarTodos());
				if (esAdmin) {
					cargarInformesAdmin();
				} else {
					cargarInformesEmpleado();
				}
			}
		} else {
			JOptionPane.showMessageDialog(ventana, "Error al registrar el alquiler.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// ── Login y registro ──────────────────────────────────────────────────────

	private void procesarLogin() {
		String usuario = panelLogin.getUsuario();
		String contrasenia = panelLogin.getContrasenia();

		if (usuario.isEmpty() || contrasenia.isEmpty()) {
			panelLogin.mostrarError("Rellena todos los campos.");
			return;
		}

		if (panelLogin.esRolEmpleado()) {
			Empleado emp = empleadoDAO.login(usuario, contrasenia);
			if (emp != null) {
				empleadoActivo = emp;
				ventana.modoSesionActiva();
				if (emp.esAdministrador()) {
					esAdmin = true;
					cargarPanelAdmin();
				} else {
					esAdmin = false;
					cargarPanelEmpleado();
				}
			} else {
				panelLogin.mostrarError("Usuario o contraseña incorrectos.");
			}
		} else {
			Cliente cli = clienteDAO.login(usuario, contrasenia);
			if (cli != null) {
				if ("bloqueado".equalsIgnoreCase(cli.getEstado())) {
					panelLogin.mostrarError("Cuenta bloqueada. Contacta con el administrador.");
					return;
				}
				clienteActivo = cli;
				ventana.modoSesionActiva();
				cargarPanelCliente();
			} else {
				panelLogin.mostrarError("Usuario o contraseña incorrectos.");
			}
		}
	}

	private void procesarRegistro() {
		if (!panelRegistro.datosValidos()) {
			return;
		}
		Cliente nuevo = new Cliente(0, panelRegistro.getNombre(), panelRegistro.getApellido(), panelRegistro.getEmail(),
				panelRegistro.getUsuario(), panelRegistro.getContrasenia(), "activo");
		try {
			if (clienteDAO.registrar(nuevo) > 0) {
				JOptionPane.showMessageDialog(ventana, "¡Cuenta creada! Ya puedes iniciar sesión.", "Registro exitoso",
						JOptionPane.INFORMATION_MESSAGE);
				panelRegistro.limpiar();
				ventana.cargarPanel(panelLogin);
			} else {
				panelRegistro.mostrarError("No se pudo crear la cuenta. Inténtalo de nuevo.");
			}
		} catch (Exception ex) {
			String mensaje = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
			if (mensaje.contains("email_cliente")) {
				panelRegistro.mostrarError("Ese email ya está registrado.");
			} else if (mensaje.contains("nombre_usuario")) {
				panelRegistro.mostrarError("Ese nombre de usuario ya existe.");
			} else {
				panelRegistro.mostrarError("No se pudo crear la cuenta. Inténtalo de nuevo.");
			}
		}
	}

	// ── Cargar paneles por rol ────────────────────────────────────────────────

	private void cargarPanelCliente() {
		alquilerDAO.marcarVencidos();
		panelCliente.setBienvenida(clienteActivo);
		recargarCatalogoCliente();
		catCliente.habilitarAcciones(true);

		ArrayList<Alquiler> alquileres = alquilerDAO.listarPorCliente(clienteActivo.getIdCliente());
		misAlquileres.cargarAlquileres(alquileres);

		actualizarContadorActivosCliente();
		panelMiCuenta.cargarDatos(clienteActivo);
		ventana.cargarPanel(panelCliente);
	}

	private void cargarPanelEmpleado() {
		alquilerDAO.marcarVencidos();
		panelEmpleado.setBienvenida(empleadoActivo);
		gestionAlqEmp.cargarAlquileres(alquilerDAO.listarTodos());
		gestionPelEmp.cargarPeliculas(peliculaDAO.listarTodas(), obtenerConteosCopias(peliculaDAO.listarTodas()));
		gestionClientesEmp.cargarClientes(clienteDAO.listarTodos());
		cargarInformesEmpleado();
		ventana.cargarPanel(panelEmpleado);
	}

	private void cargarPanelAdmin() {
		alquilerDAO.marcarVencidos();
		panelAdmin.setBienvenida(empleadoActivo);
		gestionAlqAdm.cargarAlquileres(alquilerDAO.listarTodos());
		gestionPelAdm.cargarPeliculas(peliculaDAO.listarTodas(), obtenerConteosCopias(peliculaDAO.listarTodas()));
		gestionClientesAdm.cargarClientes(clienteDAO.listarTodos());
		gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
		cargarInformesAdmin();
		ventana.cargarPanel(panelAdmin);
	}

	// ── Devoluciones ──────────────────────────────────────────────────────────

	private void procesarSolicitudDevolucion() {
		int id = misAlquileres.getIdAlquilerSeleccionado();
		if (id == -1) {
			return;
		}

		int conf = JOptionPane.showConfirmDialog(ventana,
				"¿Confirmas la solicitud de devolución?\n" + "Un empleado la procesará en breve.",
				"Solicitar devolución", JOptionPane.YES_NO_OPTION);

		if (conf == JOptionPane.YES_OPTION) {
			if (alquilerDAO.solicitarDevolucion(id) > 0) {
				JOptionPane.showMessageDialog(ventana, "Solicitud enviada. ¡Gracias!", "Solicitud registrada",
						JOptionPane.INFORMATION_MESSAGE);
				misAlquileres.cargarAlquileres(alquilerDAO.listarPorCliente(clienteActivo.getIdCliente()));
				actualizarContadorActivosCliente();
			}
		}
	}

	private void procesarAceptarDevolucion() {
		PanelGestionAlquileres panel = getPanelGestionActivo();
		int id = panel.getIdAlquilerSeleccionado();
		if (id == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un alquiler pendiente de devolución.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// aceptarDevolucion devuelve el id_copia si tuvo exito, o -1 si fallo
		int idCopia = alquilerDAO.aceptarDevolucion(id, java.time.LocalDate.now().toString());
		if (idCopia > 0) {
			// La copia vuelve a estar disponible para nuevos alquileres
			copiaDAO.actualizarEstado(idCopia, "disponible");
			JOptionPane.showMessageDialog(ventana, "Devolución aceptada.", "Devolución procesada",
					JOptionPane.INFORMATION_MESSAGE);
			panel.cargarAlquileres(alquilerDAO.listarTodos());

			getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas(),
					obtenerConteosCopias(peliculaDAO.listarTodas()));

			if (esAdmin) {
				cargarInformesAdmin();
			} else {
				cargarInformesEmpleado();
			}
		}
	}

	private void filtrarAlquileres() {
		PanelGestionAlquileres panel = getPanelGestionActivo();
		String filtro = panel.getFiltroEstado();
		ArrayList<Alquiler> todos = alquilerDAO.listarTodos();

		if (filtro == null) {
			panel.cargarAlquileres(todos);
			return;
		}

		ArrayList<Alquiler> filtrados = new ArrayList<Alquiler>();
		for (Alquiler a : todos) {
			if (filtro.equalsIgnoreCase(a.getEstadoAlquiler())) {
				filtrados.add(a);
			}
		}
		panel.cargarAlquileres(filtrados);
	}

	private void filtrarMisAlquileres() {
		String filtro = misAlquileres.getFiltroEstado();
		ArrayList<Alquiler> todos = alquilerDAO.listarPorCliente(clienteActivo.getIdCliente());

		if (filtro == null) {
			misAlquileres.cargarAlquileres(todos);
			return;
		}

		ArrayList<Alquiler> filtrados = new ArrayList<Alquiler>();
		for (Alquiler a : todos) {
			if (filtro.equalsIgnoreCase(a.getEstadoAlquiler())) {
				filtrados.add(a);
			}
		}
		misAlquileres.cargarAlquileres(filtrados);
	}

	// ── Películas ─────────────────────────────────────────────────────────────

	private void guardarPelicula() {
		PanelAnadirPelicula panel = getPanelAnadirActivo();
		if (!panel.datosValidos()) {
			return;
		}
		Pelicula nueva = new Pelicula(0, panel.getTitulo(), panel.getDirector(), panel.getDuracion(), panel.getGenero(),
				panel.getSinopsis(), panel.getClasificacion(), "activa");
		int idPelicula = peliculaDAO.agregar(nueva);
		if (idPelicula > 0) {
			// Asignar precio segun el formato elegido
			double precio;
			if (panel.getFormato().equals("Blu-ray")) {
				precio = 5.00;
			} else if (panel.getFormato().equals("4K Ultra HD")) {
				precio = 7.50;
			} else {
				precio = 2.50; // DVD
			}
			// Crear N copias segun el numero indicado en el spinner
			int numCopias = panel.getNumCopias();
			for (int i = 0; i < numCopias; i++) {
				copiaDAO.crear(new Copia(0, idPelicula, panel.getFormato(), "disponible", precio));
			}
			panel.mostrarMensaje("¡Película añadida con " + numCopias + " copia(s)!", false);
			panel.limpiar();
			getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas(),
					obtenerConteosCopias(peliculaDAO.listarTodas()));
		} else {
			panel.mostrarMensaje("Error al guardar.", true);
		}
	}

	private void editarPelicula() {
		Pelicula p = getPanelGestionPelActivo().getPeliculaSeleccionada();
		if (p == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una película.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		JTextField txtTitulo = new JTextField(p.getNombrePelicula());
		JTextField txtDirector = new JTextField(p.getDirector());
		JTextField txtDuracion = new JTextField(String.valueOf(p.getDuracion()));
		JTextArea txtSinopsis = new JTextArea(p.getSinopsis(), 3, 20);
		txtSinopsis.setLineWrap(true);

		JComboBox<String> cmbGenero = new JComboBox<>(
				new String[] { "Acción", "Aventura", "Animación", "Ciencia Ficción", "Comedia", "Drama", "Fantasía",
						"Musical", "Romance", "Suspense", "Terror", "Thriller" });
		cmbGenero.setSelectedItem(p.getGenero());

		JComboBox<String> cmbClasif = new JComboBox<>(new String[] { "TP", "7", "12", "16", "18" });
		cmbClasif.setSelectedItem(p.getClasificacionEdad());

		JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
		form.setBorder(new EmptyBorder(10, 10, 10, 10));
		form.add(new JLabel("Título:"));
		form.add(txtTitulo);
		form.add(new JLabel("Director:"));
		form.add(txtDirector);
		form.add(new JLabel("Duración (min):"));
		form.add(txtDuracion);
		form.add(new JLabel("Género:"));
		form.add(cmbGenero);
		form.add(new JLabel("Clasificación:"));
		form.add(cmbClasif);
		form.add(new JLabel("Sinopsis:"));
		form.add(new JScrollPane(txtSinopsis));

		int res = JOptionPane.showConfirmDialog(ventana, form, "Editar: " + p.getNombrePelicula(),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (res == JOptionPane.OK_OPTION) {
			StringBuilder errores = new StringBuilder();
			String nuevoTitulo = txtTitulo.getText().trim();
			String nuevoDirector = txtDirector.getText().trim();
			if (nuevoTitulo.isEmpty()) {
				errores.append("· El título es obligatorio.\n");
			}
			if (nuevoDirector.isEmpty()) {
				errores.append("· El director es obligatorio.\n");
			}
			int duracion = 0;
			try {
				duracion = Integer.parseInt(txtDuracion.getText().trim());
				if (duracion <= 0 || duracion > 600) {
					errores.append("· La duración debe estar entre 1 y 600 minutos.\n");
				}
			} catch (NumberFormatException ex) {
				errores.append("· La duración debe ser un número entero.\n");
			}
			if (errores.length() > 0) {
				JOptionPane.showMessageDialog(ventana, errores.toString().trim(), "Datos incorrectos",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			try {
				p.setNombrePelicula(nuevoTitulo);
				p.setDirector(nuevoDirector);
				p.setDuracion(duracion);
				p.setGenero((String) cmbGenero.getSelectedItem());
				p.setClasificacionEdad((String) cmbClasif.getSelectedItem());
				p.setSinopsis(txtSinopsis.getText().trim());
				if (peliculaDAO.actualizar(p) > 0) {
					JOptionPane.showMessageDialog(ventana, "Película actualizada.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas(),
							obtenerConteosCopias(peliculaDAO.listarTodas()));
					getPanelGestionActivo().cargarAlquileres(alquilerDAO.listarTodos());
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ventana, "Error al actualizar la película.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void darDeBajaPelicula() {
	    int id = getPanelGestionPelActivo().getIdPeliculaSeleccionada();
	    if (id == -1) {
	        JOptionPane.showMessageDialog(ventana, "Selecciona una película.", "Sin selección",
	                JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	    // Comprobar primero si tiene copias alquiladas antes de preguntar
	    if (copiaDAO.contarAlquiladasPorPelicula(id) > 0) {
	        JOptionPane.showMessageDialog(ventana,
	                "No se puede dar de baja. La película tiene copias actualmente alquiladas.", "Error",
	                JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    int conf = JOptionPane.showConfirmDialog(ventana,
	            "¿Seguro que quieres dar de baja esta película?\n"
	                    + "Dejará de aparecer en el catálogo pero se conservará en el historial.",
	            "Confirmar baja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	    if (conf == JOptionPane.YES_OPTION) {
	        if (peliculaDAO.darDeBaja(id) > 0) {
	            JOptionPane.showMessageDialog(ventana, "Película dada de baja correctamente.", "Éxito",
	                    JOptionPane.INFORMATION_MESSAGE);
	            getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas(),
	                    obtenerConteosCopias(peliculaDAO.listarTodas()));
	        } else {
	            JOptionPane.showMessageDialog(ventana, "Error al dar de baja la película.", "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}

	// ── Empleados (solo Admin) ────────────────────────────────────────────────

	private void crearEmpleado() {
		if (!gestionEmpleados.datosValidos()) {
			return;
		}
		Empleado nuevo = new Empleado(0, gestionEmpleados.getNombre(), gestionEmpleados.getApellido(),
				gestionEmpleados.getEmail(), gestionEmpleados.getUsuario(), gestionEmpleados.getContrasenia(),
				empleadoActivo.getIdEmpleado());
		try {
			if (empleadoDAO.crear(nuevo) > 0) {
				gestionEmpleados.mostrarMensaje("Empleado creado correctamente.", false);
				gestionEmpleados.limpiar();
				gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
			} else {
				gestionEmpleados.mostrarMensaje("No se pudo crear el empleado. Inténtalo de nuevo.", true);
			}
		} catch (Exception ex) {
			String mensaje = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
			if (mensaje.contains("email_empleado")) {
				gestionEmpleados.mostrarMensaje("Ese email ya está registrado.", true);
			} else if (mensaje.contains("usuario_empleado")) {
				gestionEmpleados.mostrarMensaje("Ese nombre de usuario ya existe.", true);
			} else {
				gestionEmpleados.mostrarMensaje("No se pudo crear el empleado. Inténtalo de nuevo.", true);
			}
		}
	}

	private void editarEmpleado() {
		Empleado emp = gestionEmpleados.getEmpleadoSeleccionado();
		if (emp == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un empleado.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		JTextField txtNombre = new JTextField(emp.getNombreEmpleado());
		JTextField txtApellido = new JTextField(emp.getApellidoEmpleado());
		JTextField txtEmail = new JTextField(emp.getEmailEmpleado());
		JTextField txtUsuario = new JTextField(emp.getUsuarioEmpleado());

		JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
		form.setBorder(new EmptyBorder(10, 10, 10, 10));
		form.add(new JLabel("Nombre:"));
		form.add(txtNombre);
		form.add(new JLabel("Apellido:"));
		form.add(txtApellido);
		form.add(new JLabel("Email:"));
		form.add(txtEmail);
		form.add(new JLabel("Usuario:"));
		form.add(txtUsuario);

		int res = JOptionPane.showConfirmDialog(ventana, form, "Editar empleado: " + emp.getNombreCompleto(),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (res == JOptionPane.OK_OPTION) {
			String nuevoEmail = txtEmail.getText().trim();
			String nuevoUsuario = txtUsuario.getText().trim();

			StringBuilder errores = new StringBuilder();
			if (nuevoEmail.isEmpty()) {
				errores.append("· El email es obligatorio.\n");
			} else if (!nuevoEmail.contains("@") || !nuevoEmail.contains(".")) {
				errores.append("· El email no tiene un formato válido.\n");
			}
			if (nuevoUsuario.isEmpty()) {
				errores.append("· El usuario es obligatorio.\n");
			} else if (nuevoUsuario.contains(" ")) {
				errores.append("· El usuario no puede contener espacios.\n");
			}
			if (errores.length() > 0) {
				JOptionPane.showMessageDialog(ventana, errores.toString().trim(), "Datos incorrectos",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			emp.setNombreEmpleado(txtNombre.getText().trim());
			emp.setApellidoEmpleado(txtApellido.getText().trim());
			emp.setEmailEmpleado(nuevoEmail);
			emp.setUsuarioEmpleado(nuevoUsuario);

			if (empleadoDAO.actualizar(emp) > 0) {
				JOptionPane.showMessageDialog(ventana, "Empleado actualizado. ✅", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "No se pudo actualizar. El usuario o email ya existe.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void eliminarEmpleado() {
		int id = gestionEmpleados.getIdEmpleadoSeleccionado();
		if (id == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un empleado.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (id == empleadoActivo.getIdEmpleado()) {
			JOptionPane.showMessageDialog(ventana, "No puedes eliminar tu propia cuenta.", "Operación no permitida",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int conf = JOptionPane.showConfirmDialog(ventana, "¿Seguro que quieres eliminar este empleado?", "Confirmar",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (conf == JOptionPane.YES_OPTION) {
			if (empleadoDAO.eliminar(id) > 0) {
				JOptionPane.showMessageDialog(ventana, "Empleado eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "Error al eliminar el empleado.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ── Clientes (empleado y admin) ───────────────────────────────────────────

	private void editarCliente() {
		Cliente c = getPanelGestionClientesActivo().getClienteSeleccionado();
		if (c == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un cliente.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		JTextField txtNombre = new JTextField(c.getNombreCliente());
		JTextField txtApellido = new JTextField(c.getApellidoCliente());
		JTextField txtEmail = new JTextField(c.getEmailCliente());
		JTextField txtUsuario = new JTextField(c.getNombreUsuario());
		JComboBox<String> cmbEstado = new JComboBox<>(new String[] { "activo", "bloqueado" });
		cmbEstado.setSelectedItem(c.getEstado());

		JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
		form.setBorder(new EmptyBorder(10, 10, 10, 10));
		form.add(new JLabel("Nombre:"));
		form.add(txtNombre);
		form.add(new JLabel("Apellido:"));
		form.add(txtApellido);
		form.add(new JLabel("Email:"));
		form.add(txtEmail);
		form.add(new JLabel("Usuario:"));
		form.add(txtUsuario);
		form.add(new JLabel("Estado:"));
		form.add(cmbEstado);

		int res = JOptionPane.showConfirmDialog(ventana, form, "Editar cliente: " + c.getNombreCompleto(),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (res == JOptionPane.OK_OPTION) {
			String nuevoNombre = txtNombre.getText().trim();
			String nuevoApellido = txtApellido.getText().trim();
			String nuevoEmail = txtEmail.getText().trim();
			String nuevoUsuario = txtUsuario.getText().trim();

			StringBuilder errores = new StringBuilder();
			if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty()) {
				errores.append("· Nombre y apellido son obligatorios.\n");
			}
			if (nuevoEmail.isEmpty()) {
				errores.append("· El email es obligatorio.\n");
			} else if (!nuevoEmail.contains("@") || !nuevoEmail.contains(".")) {
				errores.append("· El email no tiene un formato válido.\n");
			}
			if (nuevoUsuario.isEmpty()) {
				errores.append("· El usuario es obligatorio.\n");
			} else if (nuevoUsuario.contains(" ")) {
				errores.append("· El usuario no puede contener espacios.\n");
			}
			if (errores.length() > 0) {
				JOptionPane.showMessageDialog(ventana, errores.toString().trim(), "Datos incorrectos",
						JOptionPane.WARNING_MESSAGE);
				return;
			}

			c.setNombreCliente(nuevoNombre);
			c.setApellidoCliente(nuevoApellido);
			c.setEmailCliente(nuevoEmail);
			c.setNombreUsuario(nuevoUsuario);
			c.setEstado((String) cmbEstado.getSelectedItem());

			if (clienteDAO.actualizar(c) > 0) {
				JOptionPane.showMessageDialog(ventana, "Cliente actualizado. ✅", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				getPanelGestionClientesActivo().cargarClientes(clienteDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "Error al actualizar el cliente.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void eliminarCliente() {
		int id = getPanelGestionClientesActivo().getIdClienteSeleccionado();
		if (id == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un cliente.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int conf = JOptionPane.showConfirmDialog(ventana,
				"¿Seguro que quieres eliminar este cliente?\n" + "Se eliminarán también sus datos.",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (conf == JOptionPane.YES_OPTION) {
			if (clienteDAO.eliminar(id) > 0) {
				JOptionPane.showMessageDialog(ventana, "Cliente eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				getPanelGestionClientesActivo().cargarClientes(clienteDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "No se pudo eliminar. Puede tener alquileres asociados.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void cargarInformesEmpleado() {
		ArrayList<Alquiler> todos = alquilerDAO.listarTodos();
		double total = 0;
		for (Alquiler a : todos) {
			total += a.getMontoCobro();
		}
		informesEmp.cargarInformes(todos, total);
	}

	private void cargarInformesAdmin() {
		ArrayList<Alquiler> todos = alquilerDAO.listarTodos();
		double total = 0;
		for (Alquiler a : todos) {
			total += a.getMontoCobro();
		}
		informesAdm.cargarInformes(todos, total);
	}

	// ── Contador activos cliente ──────────────────────────────────────────────

	private void actualizarContadorActivosCliente() {
		ArrayList<Alquiler> alquileres = alquilerDAO.listarPorCliente(clienteActivo.getIdCliente());
		int activos = 0;
		for (Alquiler a : alquileres) {
			if ("activo".equalsIgnoreCase(a.getEstadoAlquiler())) {
				activos++;
			}
		}
		panelCliente.actualizarContadorActivos(activos);
	}

	// ── Mi cuenta ─────────────────────────────────────────────────────────────

	private void modificarDatosCliente() {
		JTextField txtNombre = new JTextField(clienteActivo.getNombreCliente());
		JTextField txtApellido = new JTextField(clienteActivo.getApellidoCliente());
		JTextField txtEmail = new JTextField(clienteActivo.getEmailCliente());
		JTextField txtUsuario = new JTextField(clienteActivo.getNombreUsuario());

		JPasswordField txtContraActual = new JPasswordField();
		JPasswordField txtContraNueva = new JPasswordField();
		JPasswordField txtContraRepetir = new JPasswordField();

		JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
		form.setBorder(new EmptyBorder(10, 10, 10, 10));
		form.add(new JLabel("Nombre:"));
		form.add(txtNombre);
		form.add(new JLabel("Apellido:"));
		form.add(txtApellido);
		form.add(new JLabel("Email:"));
		form.add(txtEmail);
		form.add(new JLabel("Usuario:"));
		form.add(txtUsuario);
		form.add(new JLabel("── Cambiar contraseña (opcional) ──────"));
		form.add(new JLabel(""));
		form.add(new JLabel("Contraseña actual:"));
		form.add(txtContraActual);
		form.add(new JLabel("Nueva contraseña:"));
		form.add(txtContraNueva);
		form.add(new JLabel("Repetir contraseña:"));
		form.add(txtContraRepetir);

		int res = JOptionPane.showConfirmDialog(ventana, form, "Modificar mis datos", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (res != JOptionPane.OK_OPTION) {
			return;
		}

		String nuevoNombre = txtNombre.getText().trim();
		String nuevoApellido = txtApellido.getText().trim();
		String nuevoEmail = txtEmail.getText().trim();
		String nuevoUsuario = txtUsuario.getText().trim();

		StringBuilder errores = new StringBuilder();

		if (nuevoNombre.isEmpty() || nuevoApellido.isEmpty()) {
			errores.append("· Nombre y apellido son obligatorios.\n");
		}
		if (nuevoEmail.isEmpty()) {
			errores.append("· El email es obligatorio.\n");
		} else if (!nuevoEmail.contains("@") || !nuevoEmail.contains(".")) {
			errores.append("· El email no tiene un formato válido.\n");
		}
		if (nuevoUsuario.isEmpty()) {
			errores.append("· El usuario es obligatorio.\n");
		} else if (nuevoUsuario.contains(" ")) {
			errores.append("· El usuario no puede contener espacios.\n");
		}
		if (errores.length() > 0) {
			JOptionPane.showMessageDialog(ventana, errores.toString().trim(), "Datos incorrectos",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String contraActual = new String(txtContraActual.getPassword());
		String contraNueva = new String(txtContraNueva.getPassword());
		String contraRepetir = new String(txtContraRepetir.getPassword());
		String contraFinal = clienteActivo.getContraseniaCliente();

		if (!contraActual.isEmpty() || !contraNueva.isEmpty() || !contraRepetir.isEmpty()) {

			if (!contraActual.equals(clienteActivo.getContraseniaCliente())) {
				JOptionPane.showMessageDialog(ventana, "La contraseña actual no es correcta.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (contraNueva.isEmpty()) {
				JOptionPane.showMessageDialog(ventana, "La nueva contraseña no puede estar vacía.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (contraNueva.length() < 4) {
				JOptionPane.showMessageDialog(ventana, "La contraseña debe tener al menos 4 caracteres.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!contraNueva.equals(contraRepetir)) {
				JOptionPane.showMessageDialog(ventana, "Las contraseñas nuevas no coinciden.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			contraFinal = contraNueva;
		}

		clienteActivo.setNombreCliente(nuevoNombre);
		clienteActivo.setApellidoCliente(nuevoApellido);
		clienteActivo.setEmailCliente(nuevoEmail);
		clienteActivo.setNombreUsuario(nuevoUsuario);

		try {
			if (clienteDAO.actualizarDatos(clienteActivo, contraFinal) > 0) {
				clienteActivo.setContraseniaCliente(contraFinal);
				JOptionPane.showMessageDialog(ventana, "Datos actualizados correctamente. ✅", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				panelMiCuenta.cargarDatos(clienteActivo);
				panelCliente.setBienvenida(clienteActivo);
			} else {
				JOptionPane.showMessageDialog(ventana, "No se pudieron guardar los cambios.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception ex) {
			String mensaje = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
			if (mensaje.contains("email_cliente")) {
				JOptionPane.showMessageDialog(ventana, "Ese email ya está registrado.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else if (mensaje.contains("nombre_usuario")) {
				JOptionPane.showMessageDialog(ventana, "Ese nombre de usuario ya existe.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(ventana, "Error al guardar los cambios.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ── Bloquear / desbloquear cliente ──────────────────────────────────────────

	private void bloquearClienteDesdeGestion() {
		Cliente cliente = getPanelGestionClientesActivo().getClienteSeleccionado();
		if (cliente == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un cliente.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		// Alternar entre activo y bloqueado
		String nuevoEstado = "bloqueado".equalsIgnoreCase(cliente.getEstado()) ? "activo" : "bloqueado";
		String accion = "activo".equals(nuevoEstado) ? "desbloquear" : "bloquear";

		int conf = JOptionPane.showConfirmDialog(ventana,
				"¿Seguro que quieres " + accion + " al cliente " + cliente.getNombreCompleto() + "?", "Confirmar",
				JOptionPane.YES_NO_OPTION);

		if (conf == JOptionPane.YES_OPTION) {
			cliente.setEstado(nuevoEstado);
			if (clienteDAO.actualizar(cliente) > 0) {
				String msg = "activo".equals(nuevoEstado) ? "Cliente desbloqueado." : "Cliente bloqueado.";
				JOptionPane.showMessageDialog(ventana, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
				getPanelGestionClientesActivo().cargarClientes(clienteDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "Error al actualizar el estado del cliente.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void bloquearClienteDesdeAlquiler() {
		PanelGestionAlquileres panel = getPanelGestionActivo();
		int idAlquiler = panel.getIdAlquilerSeleccionado();
		if (idAlquiler == -1) {
			return;
		}
		String nombreCliente = panel.getNombreClienteSeleccionado();

		// Buscar el cliente
		ArrayList<Cliente> clientes = clienteDAO.listarTodos();
		Cliente cliente = null;
		for (Cliente cli : clientes) {
			if (cli.getNombreCompleto().equals(nombreCliente)) {
				cliente = cli;
				break;
			}
		}
		if (cliente == null) {
			JOptionPane.showMessageDialog(ventana, "No se pudo encontrar al cliente.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Comprobar si ya está bloqueado
		if ("bloqueado".equalsIgnoreCase(cliente.getEstado())) {
			JOptionPane.showMessageDialog(ventana, "El cliente " + nombreCliente + " ya está bloqueado.",
					"Cliente ya bloqueado", JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		int conf = JOptionPane.showConfirmDialog(ventana,
				"¿Seguro que quieres bloquear al cliente " + nombreCliente + "?\n" + "Tiene un alquiler vencido.",
				"Bloquear cliente", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (conf == JOptionPane.YES_OPTION) {
			cliente.setEstado("bloqueado");
			if (clienteDAO.actualizar(cliente) > 0) {
				JOptionPane.showMessageDialog(ventana, "Cliente bloqueado correctamente.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				panel.cargarAlquileres(alquilerDAO.listarTodos());
				getPanelGestionClientesActivo().cargarClientes(clienteDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "Error al bloquear el cliente.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ── Cerrar sesión
	// ─────────────────────────────────────────────────────────────

	private void cerrarSesion() {
		int conf = JOptionPane.showConfirmDialog(ventana, "¿Seguro que quieres cerrar sesión?", "Cerrar sesión",
				JOptionPane.YES_NO_OPTION);
		if (conf == JOptionPane.YES_OPTION) {
			iniciarModoInvitado();
		}
	}

	// ── Filtrar clientes
	// ──────────────────────────────────────────────────────────

	private void filtrarClientes() {
		String filtro = getPanelGestionClientesActivo().getFiltroEstado();
		ArrayList<Cliente> todos = clienteDAO.listarTodos();

		if (filtro == null) {
			getPanelGestionClientesActivo().cargarClientes(todos);
			return;
		}

		ArrayList<Cliente> filtrados = new ArrayList<Cliente>();
		for (Cliente cli : todos) {
			if (filtro.equalsIgnoreCase(cli.getEstado())) {
				filtrados.add(cli);
			}
		}
		getPanelGestionClientesActivo().cargarClientes(filtrados);
	}

	// ── Buscar película en gestión
	// ────────────────────────────────────────────────

	private void buscarPeliculaGestion() {
		String termino = getPanelGestionPelActivo().getTxtBuscar().getText().trim();
		ArrayList<Pelicula> peliculas;
		if (termino.isEmpty()) {
			peliculas = peliculaDAO.listarTodas();
		} else {
			peliculas = peliculaDAO.buscarPorTitulo(termino);
		}
		getPanelGestionPelActivo().cargarPeliculas(peliculas, obtenerConteosCopias(peliculas));
	}

}