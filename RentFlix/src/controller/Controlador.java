// Controlador.java
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

public class Controlador implements ActionListener {

	// ── Vistas ────────────────────────────────────────────────────────────────
	private VentanaPrincipal ventana;
	private PanelCatalogo catInvitado;
	private PanelCatalogo catCliente;
	private PanelLogin panelLogin;
	private PanelRegistro panelRegistro;
	private PanelCliente panelCliente;
	private PanelMisAlquileres misAlquileres;

	private PanelEmpleado panelEmpleado;
	private PanelGestionAlquileres gestionAlqEmp;
	private PanelAnadirPelicula anadirPelEmp;
	private PanelGestionPeliculas gestionPelEmp;
	private PanelInformes informesEmp;

	private PanelAdmin panelAdmin;
	private PanelGestionAlquileres gestionAlqAdm;
	private PanelAnadirPelicula anadirPelAdm;
	private PanelGestionPeliculas gestionPelAdm;
	private PanelInformes informesAdm;
	private PanelGestionEmpleados gestionEmpleados;
	private PanelGestionClientes gestionClientes;

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
			PanelMisAlquileres misAlquileres, PanelEmpleado panelEmpleado, PanelGestionAlquileres gestionAlqEmp,
			PanelAnadirPelicula anadirPelEmp, PanelGestionPeliculas gestionPelEmp, PanelInformes informesEmp,
			PanelAdmin panelAdmin, PanelGestionAlquileres gestionAlqAdm, PanelAnadirPelicula anadirPelAdm,
			PanelGestionPeliculas gestionPelAdm, PanelInformes informesAdm, PanelGestionEmpleados gestionEmpleados,
			PanelGestionClientes gestionClientes) {

		this.ventana = ventana;
		this.catInvitado = catInvitado;
		this.catCliente = catCliente;
		this.panelLogin = panelLogin;
		this.panelRegistro = panelRegistro;
		this.panelCliente = panelCliente;
		this.misAlquileres = misAlquileres;
		this.panelEmpleado = panelEmpleado;
		this.gestionAlqEmp = gestionAlqEmp;
		this.anadirPelEmp = anadirPelEmp;
		this.gestionPelEmp = gestionPelEmp;
		this.informesEmp = informesEmp;
		this.panelAdmin = panelAdmin;
		this.gestionAlqAdm = gestionAlqAdm;
		this.anadirPelAdm = anadirPelAdm;
		this.gestionPelAdm = gestionPelAdm;
		this.informesAdm = informesAdm;
		this.gestionEmpleados = gestionEmpleados;
		this.gestionClientes = gestionClientes;

		peliculaDAO = new PeliculaDAO();
		clienteDAO = new ClienteDAO();
		empleadoDAO = new EmpleadoDAO();
		alquilerDAO = new AlquilerDAO();
		copiaDAO = new CopiaDAO();
		pagoDAO = new PagoDAO();

		iniciarModoInvitado();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {

		case "ABRIR_LOGIN":
			panelLogin.limpiar();
			ventana.cargarPanel(panelLogin);
			break;
		case "ABRIR_REGISTRO":
			panelRegistro.limpiar();
			ventana.cargarPanel(panelRegistro);
			break;
		case "CANCELAR_LOGIN":
		case "CANCELAR_REGISTRO":
			ventana.cargarPanel(catInvitado);
			break;
		case "CERRAR_SESION":
			cerrarSesion();
			break;

		case "BUSCAR_PELICULA":
			buscarPelicula(e);
			break;
		case "FILTRAR_FORMATO_CATALOGO":
			filtrarFormato(e);
			break;
		case "ALQUILAR_PELICULA":
			alquilarDesdeClienteCatalogo();
			break;

		case "LOGIN":
			procesarLogin();
			break;
		case "REGISTRAR_CLIENTE":
			procesarRegistro();
			break;

		case "SOLICITAR_DEVOLUCION":
			procesarSolicitudDevolucion();
			break;

		case "FILTRAR_ALQUILERES":
			filtrarAlquileres();
			break;
		case "ACEPTAR_DEVOLUCION":
			procesarAceptarDevolucion();
			break;
		case "ABRIR_FORM_ALQUILER":
			abrirFormAlquiler();
			break;
		case "CANCELAR_FORM_ALQUILER":
			getPanelGestionActivo().mostrarFormAlquiler(false);
			break;
		case "CONFIRMAR_ALQUILER_EMPLEADO":
			confirmarAlquilerEmpleado();
			break;

		case "GUARDAR_PELICULA":
			guardarPelicula();
			break;
		case "LIMPIAR_FORM_PELICULA":
			getPanelAnadirActivo().limpiar();
			break;
		case "EDITAR_PELICULA":
			editarPelicula();
			break;
		case "ELIMINAR_PELICULA":
			eliminarPelicula();
			break;

		case "ACTUALIZAR_INFORMES":
			cargarInformes();
			break;

		case "CREAR_EMPLEADO":
			crearEmpleado();
			break;
		case "ELIMINAR_EMPLEADO":
			eliminarEmpleado();
			break;
		case "LIMPIAR_FORM_EMPLEADO":
			gestionEmpleados.limpiar();
			break;

		case "EDITAR_CLIENTE":
			editarCliente();
			break;
		case "ELIMINAR_CLIENTE":
			eliminarCliente();
			break;
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

	// ── Modo invitado ─────────────────────────────────────────────────────────

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
			JOptionPane.showMessageDialog(ventana, "Debes iniciar sesión para alquilar.", "Acceso requerido",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int fila = catCliente.getFilaSeleccionada();
		if (fila < 0) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una película del catálogo primero.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String titulo = (String) catCliente.getModelo().getValueAt(fila, 0);
		String formato = (String) catCliente.getModelo().getValueAt(fila, 5);

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
			panelRegistro.mostrarError("Rellena todos los campos.");
			return;
		}
		Cliente nuevo = new Cliente(0, panelRegistro.getNombre(), panelRegistro.getApellido(), panelRegistro.getEmail(),
				panelRegistro.getUsuario(), panelRegistro.getContrasenia(), "activo");
		if (clienteDAO.registrar(nuevo) > 0) {
			JOptionPane.showMessageDialog(ventana, "¡Cuenta creada! Ya puedes iniciar sesión.", "Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE);
			panelRegistro.limpiar();
			ventana.cargarPanel(panelLogin);
		} else {
			panelRegistro.mostrarError("El usuario ya existe o hubo un error.");
		}
	}

	// ── Cargar paneles por rol ────────────────────────────────────────────────

	private void cargarPanelCliente() {
		panelCliente.setBienvenida(clienteActivo);
		recargarCatalogoCliente();
		catCliente.habilitarAcciones(true);
		misAlquileres.cargarAlquileres(alquilerDAO.listarPorCliente(clienteActivo.getIdCliente()));
		ventana.cargarPanel(panelCliente);
	}

	private void cargarPanelEmpleado() {
		panelEmpleado.setBienvenida(empleadoActivo);
		gestionAlqEmp.cargarAlquileres(alquilerDAO.listarTodos());
		gestionPelEmp.cargarPeliculas(peliculaDAO.listarTodas());
		cargarInformesEmpleado();
		ventana.cargarPanel(panelEmpleado);
	}

	private void cargarPanelAdmin() {
		panelAdmin.setBienvenida(empleadoActivo);
		gestionAlqAdm.cargarAlquileres(alquilerDAO.listarTodos());
		gestionPelAdm.cargarPeliculas(peliculaDAO.listarTodas());
		gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
		gestionClientes.cargarClientes(clienteDAO.listarTodos());
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
		if (alquilerDAO.aceptarDevolucion(id, java.time.LocalDate.now().toString()) > 0) {
			JOptionPane.showMessageDialog(ventana, "Devolución aceptada.", "Devolución procesada",
					JOptionPane.INFORMATION_MESSAGE);
			panel.cargarAlquileres(alquilerDAO.listarTodos());
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

	// ── Películas ─────────────────────────────────────────────────────────────

	private void guardarPelicula() {
		PanelAnadirPelicula panel = getPanelAnadirActivo();
		if (!panel.datosValidos()) {
			return;
		}
		Pelicula nueva = new Pelicula(0, panel.getTitulo(), panel.getDirector(), panel.getDuracion(), panel.getGenero(),
				panel.getSinopsis(), panel.getClasificacion());
		if (peliculaDAO.agregar(nueva) > 0) {
			panel.mostrarMensaje("¡Película añadida!", false);
			panel.limpiar();
			getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas());
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
			try {
				p.setNombrePelicula(txtTitulo.getText().trim());
				p.setDirector(txtDirector.getText().trim());
				p.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
				p.setGenero((String) cmbGenero.getSelectedItem());
				p.setClasificacionEdad((String) cmbClasif.getSelectedItem());
				p.setSinopsis(txtSinopsis.getText().trim());
				if (peliculaDAO.actualizar(p) > 0) {
					JOptionPane.showMessageDialog(ventana, "Película actualizada.", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas());
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(ventana, "La duración debe ser un número entero.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void eliminarPelicula() {
		int id = getPanelGestionPelActivo().getIdPeliculaSeleccionada();
		if (id == -1) {
			JOptionPane.showMessageDialog(ventana, "Selecciona una película.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		int conf = JOptionPane.showConfirmDialog(ventana, "¿Seguro? Esta acción no se puede deshacer.",
				"Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (conf == JOptionPane.YES_OPTION) {
			if (peliculaDAO.eliminar(id) > 0) {
				JOptionPane.showMessageDialog(ventana, "Película eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas());
			} else {
				JOptionPane.showMessageDialog(ventana, "No se pudo eliminar. Puede tener alquileres asociados.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ── Empleados (Admin) ─────────────────────────────────────────────────────

	private void crearEmpleado() {
		if (!gestionEmpleados.datosValidos()) {
			gestionEmpleados.mostrarMensaje("Rellena todos los campos.", true);
			return;
		}
		Empleado nuevo = new Empleado(0, gestionEmpleados.getNombre(), gestionEmpleados.getApellido(),
				gestionEmpleados.getEmail(), gestionEmpleados.getUsuario(), gestionEmpleados.getContrasenia(),
				empleadoActivo.getIdEmpleado());
		if (empleadoDAO.crear(nuevo) > 0) {
			gestionEmpleados.mostrarMensaje("Empleado creado.", false);
			gestionEmpleados.limpiar();
			gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
		} else {
			gestionEmpleados.mostrarMensaje("Error. El usuario puede ya existir.", true);
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

	// ── Clientes (Admin) ──────────────────────────────────────────────────────

	private void editarCliente() {
		Cliente c = gestionClientes.getClienteSeleccionado();
		if (c == null) {
			JOptionPane.showMessageDialog(ventana, "Selecciona un cliente.", "Sin selección",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		JTextField txtNombre = new JTextField(c.getNombreCliente());
		JTextField txtApellido = new JTextField(c.getApellidoCliente());
		JTextField txtEmail = new JTextField(c.getEmailCliente());
		JTextField txtUsuario = new JTextField(c.getNombreUsuario());
		JComboBox<String> cmbEstado = new JComboBox<>(new String[] { "activo", "inactivo" });
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
			c.setNombreCliente(txtNombre.getText().trim());
			c.setApellidoCliente(txtApellido.getText().trim());
			c.setEmailCliente(txtEmail.getText().trim());
			c.setNombreUsuario(txtUsuario.getText().trim());
			c.setEstado((String) cmbEstado.getSelectedItem());

			if (clienteDAO.actualizar(c) > 0) {
				JOptionPane.showMessageDialog(ventana, "Cliente actualizado.", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				gestionClientes.cargarClientes(clienteDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "Error al actualizar el cliente.", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void eliminarCliente() {
		int id = gestionClientes.getIdClienteSeleccionado();
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
				gestionClientes.cargarClientes(clienteDAO.listarTodos());
			} else {
				JOptionPane.showMessageDialog(ventana, "No se pudo eliminar. Puede tener alquileres asociados.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// ── Informes ──────────────────────────────────────────────────────────────

	private void cargarInformes() {
		if (esAdmin) {
			cargarInformesAdmin();
		} else {
			cargarInformesEmpleado();
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

	// ── Cerrar sesión ────────────────────────────────────────────────────────

	private void cerrarSesion() {
		int conf = JOptionPane.showConfirmDialog(ventana, "¿Seguro que quieres cerrar sesión?", "Cerrar sesión",
				JOptionPane.YES_NO_OPTION);
		if (conf == JOptionPane.YES_OPTION) {
			iniciarModoInvitado();
		}
	}

	public Cliente getClienteActivo() {
		return clienteActivo;
	}

	public Empleado getEmpleadoActivo() {
		return empleadoActivo;
	}
}