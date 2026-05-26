// Controlador.java — DEFINITIVO
package controller;

import dao.*;
import model.*;
import view.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class Controlador implements ActionListener {

    // ── Vistas ────────────────────────────────────────────────────────────────
    private VentanaPrincipal   ventana;
    private PanelCatalogo      catCliente;       // exclusivo del cliente
    private PanelLogin         panelLogin;
    private PanelRegistro      panelRegistro;
    private PanelCliente       panelCliente;
    private PanelMisAlquileres misAlquileres;

    // Paneles exclusivos del empleado
    private PanelEmpleado          panelEmpleado;
    private PanelGestionAlquileres gestionAlqEmp;
    private PanelAnadirPelicula    anadirPelEmp;
    private PanelGestionPeliculas  gestionPelEmp;
    private PanelInformes          informesEmp;

    // Paneles exclusivos del admin
    private PanelAdmin             panelAdmin;
    private PanelGestionAlquileres gestionAlqAdm;
    private PanelAnadirPelicula    anadirPelAdm;
    private PanelGestionPeliculas  gestionPelAdm;
    private PanelInformes          informesAdm;
    private PanelGestionEmpleados  gestionEmpleados;

    // ── DAOs ──────────────────────────────────────────────────────────────────
    private IPeliculaDAO peliculaDAO;
    private IClienteDAO  clienteDAO;
    private IEmpleadoDAO empleadoDAO;
    private IAlquilerDAO alquilerDAO;
    private ICopiaDAO    copiaDAO;
    private IPagoDAO     pagoDAO;

    // ── Sesión ────────────────────────────────────────────────────────────────
    private Cliente  clienteActivo  = null;
    private Empleado empleadoActivo = null;

    // Cache para form de alquiler de empleado/admin
    private List<Cliente>  listaClientesCache;
    private List<Pelicula> listaPeliculasCache;
    // Flag para saber qué panel de gestión está activo
    private boolean esAdmin = false;

    public Controlador(VentanaPrincipal ventana,
                       PanelCatalogo catCliente,
                       PanelLogin panelLogin,
                       PanelRegistro panelRegistro,
                       PanelCliente panelCliente,
                       PanelMisAlquileres misAlquileres,
                       PanelEmpleado panelEmpleado,
                       PanelGestionAlquileres gestionAlqEmp,
                       PanelAnadirPelicula anadirPelEmp,
                       PanelGestionPeliculas gestionPelEmp,
                       PanelInformes informesEmp,
                       PanelAdmin panelAdmin,
                       PanelGestionAlquileres gestionAlqAdm,
                       PanelAnadirPelicula anadirPelAdm,
                       PanelGestionPeliculas gestionPelAdm,
                       PanelInformes informesAdm,
                       PanelGestionEmpleados gestionEmpleados) {

        this.ventana          = ventana;
        this.catCliente       = catCliente;
        this.panelLogin       = panelLogin;
        this.panelRegistro    = panelRegistro;
        this.panelCliente     = panelCliente;
        this.misAlquileres    = misAlquileres;
        this.panelEmpleado    = panelEmpleado;
        this.gestionAlqEmp    = gestionAlqEmp;
        this.anadirPelEmp     = anadirPelEmp;
        this.gestionPelEmp    = gestionPelEmp;
        this.informesEmp      = informesEmp;
        this.panelAdmin       = panelAdmin;
        this.gestionAlqAdm    = gestionAlqAdm;
        this.anadirPelAdm     = anadirPelAdm;
        this.gestionPelAdm    = gestionPelAdm;
        this.informesAdm      = informesAdm;
        this.gestionEmpleados = gestionEmpleados;

        peliculaDAO = new PeliculaDAO();
        clienteDAO  = new ClienteDAO();
        empleadoDAO = new EmpleadoDAO();
        alquilerDAO = new AlquilerDAO();
        copiaDAO    = new CopiaDAO();
        pagoDAO     = new PagoDAO();

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
                ventana.cargarPanel(catCliente);
                break;
            case "CERRAR_SESION":
                cerrarSesion();
                break;

            case "BUSCAR_PELICULA":
                buscarPelicula();
                break;
            case "FILTRAR_FORMATO_CATALOGO":
                filtrarFormato();
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
        }
    }

    // ── Helpers para obtener el panel activo según rol ────────────────────────

    private PanelGestionAlquileres getPanelGestionActivo() {
        return esAdmin ? gestionAlqAdm : gestionAlqEmp;
    }

    private PanelGestionPeliculas getPanelGestionPelActivo() {
        return esAdmin ? gestionPelAdm : gestionPelEmp;
    }

    private PanelAnadirPelicula getPanelAnadirActivo() {
        return esAdmin ? anadirPelAdm : anadirPelEmp;
    }

    private PanelInformes getPanelInformesActivo() {
        return esAdmin ? informesAdm : informesEmp;
    }

    // ── Modo invitado ─────────────────────────────────────────────────────────

    private void iniciarModoInvitado() {
        clienteActivo  = null;
        empleadoActivo = null;
        esAdmin        = false;
        recargarCatalogoCliente();
        catCliente.habilitarAcciones(false);
        ventana.modoInvitado();
        ventana.cargarPanel(catCliente);
    }

    // ── Catálogo cliente ──────────────────────────────────────────────────────

    private void recargarCatalogoCliente() {
        List<Pelicula> peliculas = peliculaDAO.listarTodas();
        List<Copia>    copias    = copiaDAO.listarTodasDisponibles();
        catCliente.cargarCopias(peliculas, copias);
    }

    private void buscarPelicula() {
        String termino = catCliente.getTxtBuscar().getText().trim();
        List<Pelicula> peliculas = termino.isEmpty()
            ? peliculaDAO.listarTodas()
            : peliculaDAO.buscarPorTitulo(termino);
        List<Copia> copias = copiaDAO.listarTodasDisponibles().stream()
            .filter(c -> peliculas.stream()
                .anyMatch(p -> p.getId() == c.getIdPelicula()))
            .collect(Collectors.toList());
        catCliente.cargarCopias(peliculas, copias);
    }

    private void filtrarFormato() {
        String filtro = catCliente.getFiltroFormato();
        List<Pelicula> peliculas = peliculaDAO.listarTodas();
        List<Copia> copias = (filtro == null)
            ? copiaDAO.listarTodasDisponibles()
            : copiaDAO.listarDisponiblesPorFormato(filtro);
        catCliente.cargarCopias(peliculas, copias);
    }

    // ── Alquiler cliente ──────────────────────────────────────────────────────

    private void alquilarDesdeClienteCatalogo() {
        if (clienteActivo == null) {
            JOptionPane.showMessageDialog(ventana,
                "Debes iniciar sesión para alquilar.",
                "Acceso requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo  = catCliente.getTituloSeleccionado();
        String formato = catCliente.getFormatoSeleccionado();

        if (titulo == null) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona una película del catálogo primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Pelicula> encontradas = peliculaDAO.buscarPorTitulo(titulo);
        if (encontradas.isEmpty()) return;

        List<Copia> copias = copiaDAO
            .listarDisponiblesPorPelicula(encontradas.get(0).getId())
            .stream()
            .filter(c -> c.getFormato().equals(formato))
            .collect(Collectors.toList());

        if (copias.isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                "No hay copias disponibles en formato " + formato + ".",
                "Sin stock", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        mostrarDialogoAlquilerCliente(encontradas.get(0), copias.get(0));
    }

    private void mostrarDialogoAlquilerCliente(Pelicula pelicula, Copia copia) {
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));

        JSpinner spinnerDias = new JSpinner(
            new SpinnerNumberModel(3, 1, 30, 1)
        );
        JComboBox<String> cmbPago = new JComboBox<>(
            new String[]{"efectivo", "tarjeta", "transferencia"}
        );

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

        int res = JOptionPane.showConfirmDialog(
            ventana, form, "Confirmar alquiler 🎬",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            int    dias   = (int) spinnerDias.getValue();
            String metodo = (String) cmbPago.getSelectedItem();
            double total  = copia.getPrecioAlquiler() * dias;
            registrarAlquiler(
                clienteActivo.getIdCliente(), copia,
                null, dias, metodo, total
            );
        }
    }

    // ── Alquiler empleado/admin ───────────────────────────────────────────────

    private void abrirFormAlquiler() {
        listaClientesCache  = clienteDAO.listarTodos();
        listaPeliculasCache = peliculaDAO.listarTodas();
        getPanelGestionActivo().cargarComboClientes(listaClientesCache);
        getPanelGestionActivo().cargarComboPeliculas(listaPeliculasCache);
        getPanelGestionActivo().mostrarFormAlquiler(true);
    }

    private void confirmarAlquilerEmpleado() {
        PanelGestionAlquileres panel = getPanelGestionActivo();
        int idxCliente  = panel.getIndexClienteSeleccionado();
        int idxPelicula = panel.getIndexPeliculaSeleccionada();

        if (idxCliente < 0 || idxPelicula < 0
                || listaClientesCache  == null
                || listaPeliculasCache == null) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona cliente y película.",
                "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente  cliente  = listaClientesCache.get(idxCliente);
        Pelicula pelicula = listaPeliculasCache.get(idxPelicula);
        String   formato  = panel.getFormatoSeleccionado();
        int      dias     = panel.getDiasAlquiler();
        String   metodo   = panel.getMetodoPagoSeleccionado();

        List<Copia> copias = copiaDAO
            .listarDisponiblesPorPelicula(pelicula.getId())
            .stream()
            .filter(c -> c.getFormato().equals(formato))
            .collect(Collectors.toList());

        if (copias.isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                "No hay copias disponibles en formato " + formato + ".",
                "Sin stock", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double total = copias.get(0).getPrecioAlquiler() * dias;
        registrarAlquiler(
            cliente.getIdCliente(), copias.get(0),
            empleadoActivo.getIdEmpleado(),
            dias, metodo, total
        );
        panel.mostrarFormAlquiler(false);
    }

    // ── Registro de alquiler compartido ──────────────────────────────────────

    private void registrarAlquiler(int idCliente, Copia copia,
                                    Integer idEmpleado, int dias,
                                    String metodoPago, double total) {
        int idTrans = pagoDAO.registrar(new Pago(0, metodoPago, total));
        if (idTrans == -1) {
            JOptionPane.showMessageDialog(ventana,
                "Error al procesar el pago.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.time.LocalDate hoy        = java.time.LocalDate.now();
        java.time.LocalDate devolucion = hoy.plusDays(dias);

        boolean ok = alquilerDAO.crear(new Alquiler(
            0, idCliente, copia.getIdCopia(),
            idEmpleado, idTrans,
            hoy.toString(), devolucion.toString(),
            null, "activo"
        ));

        if (ok) {
            copiaDAO.actualizarEstado(copia.getIdCopia(), "alquilada");
            JOptionPane.showMessageDialog(ventana,
                "¡Alquiler confirmado! 🎉\n" +
                "Devuelve antes del " + devolucion + "\n" +
                "Total: " + String.format("%.2f €", total),
                "Alquiler exitoso", JOptionPane.INFORMATION_MESSAGE);

            // Recargar según el rol
            if (clienteActivo != null && empleadoActivo == null) {
                recargarCatalogoCliente();
                misAlquileres.cargarAlquileres(
                    alquilerDAO.listarPorCliente(clienteActivo.getIdCliente())
                );
                panelCliente.irAMisAlquileres();
            } else if (empleadoActivo != null) {
                PanelGestionAlquileres panelGestion = getPanelGestionActivo();
                panelGestion.cargarAlquileres(alquilerDAO.listarTodos());
                if (esAdmin) cargarInformesAdmin();
                else         cargarInformesEmpleado();
            }
        } else {
            JOptionPane.showMessageDialog(ventana,
                "Error al registrar el alquiler.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Login y registro ──────────────────────────────────────────────────────

    private void procesarLogin() {
        String usuario    = panelLogin.getUsuario();
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
                if (emp.esAdministrador()) { esAdmin = true;  cargarPanelAdmin(); }
                else                       { esAdmin = false; cargarPanelEmpleado(); }
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
        Cliente nuevo = new Cliente(
            0,
            panelRegistro.getNombre(),
            panelRegistro.getApellido(),
            panelRegistro.getEmail(),
            panelRegistro.getUsuario(),
            panelRegistro.getContrasenia(),
            "activo"
        );
        if (clienteDAO.registrar(nuevo)) {
            JOptionPane.showMessageDialog(ventana,
                "¡Cuenta creada! Ya puedes iniciar sesión. 🎉",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
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
        // Habilitar botón alquilar DESPUÉS de cargar el panel
        catCliente.habilitarAcciones(true);
        misAlquileres.cargarAlquileres(
            alquilerDAO.listarPorCliente(clienteActivo.getIdCliente())
        );
        ventana.cargarPanel(panelCliente);
        // Forzar repintado para que el botón quede habilitado visiblemente
        catCliente.revalidate();
        catCliente.repaint();
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
        cargarInformesAdmin();
        ventana.cargarPanel(panelAdmin);
    }

    // ── Devoluciones ──────────────────────────────────────────────────────────

    private void procesarSolicitudDevolucion() {
        int id = misAlquileres.getIdAlquilerSeleccionado();
        if (id == -1) return;

        int conf = JOptionPane.showConfirmDialog(ventana,
            "¿Confirmas la solicitud de devolución?\n" +
            "Un empleado la procesará en breve.",
            "Solicitar devolución", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            if (alquilerDAO.solicitarDevolucion(id)) {
                JOptionPane.showMessageDialog(ventana,
                    "Solicitud enviada. ¡Gracias! 📦",
                    "Solicitud registrada", JOptionPane.INFORMATION_MESSAGE);
                misAlquileres.cargarAlquileres(
                    alquilerDAO.listarPorCliente(clienteActivo.getIdCliente())
                );
            }
        }
    }

    private void procesarAceptarDevolucion() {
        PanelGestionAlquileres panel = getPanelGestionActivo();
        int id = panel.getIdAlquilerSeleccionado();
        if (id == -1) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona un alquiler pendiente de devolución.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (alquilerDAO.aceptarDevolucion(
                id, java.time.LocalDate.now().toString())) {
            JOptionPane.showMessageDialog(ventana,
                "Devolución aceptada. ✅",
                "Devolución procesada", JOptionPane.INFORMATION_MESSAGE);
            panel.cargarAlquileres(alquilerDAO.listarTodos());
            if (esAdmin) cargarInformesAdmin();
            else         cargarInformesEmpleado();
        }
    }

    private void filtrarAlquileres() {
        PanelGestionAlquileres panel = getPanelGestionActivo();
        String filtro = panel.getFiltroEstado();
        List<Alquiler> lista = (filtro == null)
            ? alquilerDAO.listarTodos()
            : alquilerDAO.listarTodos().stream()
                .filter(a -> filtro.equalsIgnoreCase(a.getEstadoAlquiler()))
                .collect(Collectors.toList());
        panel.cargarAlquileres(lista);
    }

    // ── Películas ─────────────────────────────────────────────────────────────

    private void guardarPelicula() {
        PanelAnadirPelicula panel = getPanelAnadirActivo();
        if (!panel.datosValidos()) return;
        Pelicula nueva = new Pelicula(
            0, panel.getTitulo(), panel.getDirector(),
            panel.getDuracion(), panel.getGenero(),
            panel.getSinopsis(), panel.getClasificacion()
        );
        if (peliculaDAO.agregar(nueva)) {
            panel.mostrarMensaje("¡Película añadida! 🎬", false);
            panel.limpiar();
            getPanelGestionPelActivo().cargarPeliculas(peliculaDAO.listarTodas());
        } else {
            panel.mostrarMensaje("Error al guardar.", true);
        }
    }

    private void editarPelicula() {
        Pelicula p = getPanelGestionPelActivo().getPeliculaSeleccionada();
        if (p == null) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona una película.", "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTextField txtTitulo   = new JTextField(p.getNombrePelicula());
        JTextField txtDirector = new JTextField(p.getDirector());
        JTextField txtDuracion = new JTextField(String.valueOf(p.getDuracion()));
        JTextArea  txtSinopsis = new JTextArea(p.getSinopsis(), 3, 20);
        txtSinopsis.setLineWrap(true);

        JComboBox<String> cmbGenero = new JComboBox<>(new String[]{
            "Acción","Aventura","Animación","Ciencia Ficción",
            "Comedia","Drama","Fantasía","Musical",
            "Romance","Suspense","Terror","Thriller"
        });
        cmbGenero.setSelectedItem(p.getGenero());

        JComboBox<String> cmbClasif = new JComboBox<>(
            new String[]{"TP","7","12","16","18"}
        );
        cmbClasif.setSelectedItem(p.getClasificacionEdad());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.add(new JLabel("Título:"));          form.add(txtTitulo);
        form.add(new JLabel("Director:"));        form.add(txtDirector);
        form.add(new JLabel("Duración (min):"));  form.add(txtDuracion);
        form.add(new JLabel("Género:"));          form.add(cmbGenero);
        form.add(new JLabel("Clasificación:"));   form.add(cmbClasif);
        form.add(new JLabel("Sinopsis:"));
        form.add(new JScrollPane(txtSinopsis));

        int res = JOptionPane.showConfirmDialog(
            ventana, form,
            "Editar: " + p.getNombrePelicula(),
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (res == JOptionPane.OK_OPTION) {
            try {
                p.setNombrePelicula(txtTitulo.getText().trim());
                p.setDirector(txtDirector.getText().trim());
                p.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
                p.setGenero((String) cmbGenero.getSelectedItem());
                p.setClasificacionEdad((String) cmbClasif.getSelectedItem());
                p.setSinopsis(txtSinopsis.getText().trim());
                if (peliculaDAO.actualizar(p)) {
                    JOptionPane.showMessageDialog(ventana,
                        "Película actualizada. ✅",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    getPanelGestionPelActivo().cargarPeliculas(
                        peliculaDAO.listarTodas()
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ventana,
                    "La duración debe ser un número entero.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPelicula() {
        int id = getPanelGestionPelActivo().getIdPeliculaSeleccionada();
        if (id == -1) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona una película.", "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(ventana,
            "¿Seguro? Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            if (peliculaDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(ventana,
                    "Película eliminada. 🗑️", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                getPanelGestionPelActivo().cargarPeliculas(
                    peliculaDAO.listarTodas()
                );
            } else {
                JOptionPane.showMessageDialog(ventana,
                    "No se pudo eliminar. Puede tener alquileres asociados.",
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
        Empleado nuevo = new Empleado(
            0,
            gestionEmpleados.getNombre(),
            gestionEmpleados.getApellido(),
            gestionEmpleados.getEmail(),
            gestionEmpleados.getUsuario(),
            gestionEmpleados.getContrasenia(),
            empleadoActivo.getIdEmpleado()
        );
        if (empleadoDAO.crear(nuevo)) {
            gestionEmpleados.mostrarMensaje("Empleado creado. 👤", false);
            gestionEmpleados.limpiar();
            gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
        } else {
            gestionEmpleados.mostrarMensaje(
                "Error. El usuario puede ya existir.", true);
        }
    }

    private void eliminarEmpleado() {
        int id = gestionEmpleados.getIdEmpleadoSeleccionado();
        if (id == -1) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona un empleado.", "Sin selección",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (id == empleadoActivo.getIdEmpleado()) {
            JOptionPane.showMessageDialog(ventana,
                "No puedes eliminar tu propia cuenta.",
                "Operación no permitida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(ventana,
            "¿Seguro que quieres eliminar este empleado?",
            "Confirmar", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (conf == JOptionPane.YES_OPTION) {
            if (empleadoDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(ventana,
                    "Empleado eliminado. 🗑️", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                gestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
            } else {
                JOptionPane.showMessageDialog(ventana,
                    "Error al eliminar el empleado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Informes ──────────────────────────────────────────────────────────────

    private void cargarInformes() {
        if (esAdmin) cargarInformesAdmin();
        else         cargarInformesEmpleado();
    }

    private void cargarInformesEmpleado() {
        List<Alquiler> todos = alquilerDAO.listarTodos();
        double total = todos.stream()
            .mapToDouble(Alquiler::getMontoCobro).sum();
        informesEmp.cargarInformes(todos, total);
    }

    private void cargarInformesAdmin() {
        List<Alquiler> todos = alquilerDAO.listarTodos();
        double total = todos.stream()
            .mapToDouble(Alquiler::getMontoCobro).sum();
        informesAdm.cargarInformes(todos, total);
    }

    // ── Cerrar sesión ─────────────────────────────────────────────────────────

    private void cerrarSesion() {
        int conf = JOptionPane.showConfirmDialog(ventana,
            "¿Seguro que quieres cerrar sesión?",
            "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            iniciarModoInvitado();
        }
    }

    public Cliente  getClienteActivo()  { return clienteActivo; }
    public Empleado getEmpleadoActivo() { return empleadoActivo; }
}	