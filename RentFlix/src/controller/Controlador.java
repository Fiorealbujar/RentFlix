// ==========================================
// CLASE: Controlador.java — VERSIÓN FINAL
// Gestiona los tres roles completos.
// ==========================================
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

    // ── Vistas ──────────────────────────────────────────────────────────────
    private VentanaPrincipal       ventana;
    private PanelCatalogo          panelCatalogo;
    private PanelLogin             panelLogin;
    private PanelRegistro          panelRegistro;
    private PanelCliente           panelCliente;
    private PanelMisAlquileres     panelMisAlquileres;
    private PanelEmpleado          panelEmpleado;
    private PanelGestionAlquileres panelGestionAlquileres;
    private PanelAnadirPelicula    panelAnadirPelicula;
    private PanelAdmin             panelAdmin;
    private PanelGestionPeliculas  panelGestionPeliculas;
    private PanelInformes          panelInformes;
    private PanelGestionEmpleados  panelGestionEmpleados;

    // ── DAOs ─────────────────────────────────────────────────────────────────
    private IPeliculaDAO peliculaDAO;
    private IClienteDAO  clienteDAO;
    private IEmpleadoDAO empleadoDAO;
    private IAlquilerDAO alquilerDAO;
    private ICopiaDAO    copiaDAO;
    private IPagoDAO     pagoDAO;

    // ── Sesión activa ────────────────────────────────────────────────────────
    private Cliente  clienteActivo  = null;
    private Empleado empleadoActivo = null;

    public Controlador(VentanaPrincipal ventana,
                       PanelCatalogo panelCatalogo,
                       PanelLogin panelLogin,
                       PanelRegistro panelRegistro,
                       PanelCliente panelCliente,
                       PanelMisAlquileres panelMisAlquileres,
                       PanelEmpleado panelEmpleado,
                       PanelGestionAlquileres panelGestionAlquileres,
                       PanelAnadirPelicula panelAnadirPelicula,
                       PanelAdmin panelAdmin,
                       PanelGestionPeliculas panelGestionPeliculas,
                       PanelInformes panelInformes,
                       PanelGestionEmpleados panelGestionEmpleados) {

        this.ventana                = ventana;
        this.panelCatalogo          = panelCatalogo;
        this.panelLogin             = panelLogin;
        this.panelRegistro          = panelRegistro;
        this.panelCliente           = panelCliente;
        this.panelMisAlquileres     = panelMisAlquileres;
        this.panelEmpleado          = panelEmpleado;
        this.panelGestionAlquileres = panelGestionAlquileres;
        this.panelAnadirPelicula    = panelAnadirPelicula;
        this.panelAdmin             = panelAdmin;
        this.panelGestionPeliculas  = panelGestionPeliculas;
        this.panelInformes          = panelInformes;
        this.panelGestionEmpleados  = panelGestionEmpleados;

        this.peliculaDAO = new PeliculaDAO();
        this.clienteDAO  = new ClienteDAO();
        this.empleadoDAO = new EmpleadoDAO();
        this.alquilerDAO = new AlquilerDAO();
        this.copiaDAO    = new CopiaDAO();
        this.pagoDAO     = new PagoDAO();

        iniciarModoInvitado();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {

            // ── Navegación ──────────────────────────────────────────────────
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
                ventana.cargarPanel(panelCatalogo);
                break;
            case "CERRAR_SESION":
                cerrarSesion();
                break;

            // ── Catálogo ────────────────────────────────────────────────────
            case "BUSCAR_PELICULA":
                buscarPelicula();
                break;
            case "ALQUILAR_PELICULA":
                iniciarAlquiler();
                break;

            // ── Sesión ──────────────────────────────────────────────────────
            case "LOGIN":
                procesarLogin();
                break;
            case "REGISTRAR_CLIENTE":
                procesarRegistro();
                break;

            // ── Cliente ─────────────────────────────────────────────────────
            case "SOLICITAR_DEVOLUCION":
                procesarSolicitudDevolucion();
                break;

            // ── Empleado ────────────────────────────────────────────────────
            case "FILTRAR_ALQUILERES":
                filtrarAlquileres();
                break;
            case "ACEPTAR_DEVOLUCION":
                procesarAceptarDevolucion();
                break;
            case "ALQUILAR_PARA_CLIENTE":
                iniciarAlquilerParaCliente();
                break;
            case "GUARDAR_PELICULA":
                guardarPelicula();
                break;
            case "LIMPIAR_FORM_PELICULA":
                panelAnadirPelicula.limpiar();
                break;

            // ── Admin ───────────────────────────────────────────────────────
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
            case "LIMPIAR_FORM_EMPLEADO":
                panelGestionEmpleados.limpiar();
                break;
        }
    }

    // ── Modo invitado ───────────────────────────────────────────────────────

    private void iniciarModoInvitado() {
        clienteActivo  = null;
        empleadoActivo = null;
        recargarCatalogo();
        panelCatalogo.habilitarAcciones(false);
        ventana.cargarPanel(panelCatalogo);
    }

    // ── Catálogo ────────────────────────────────────────────────────────────

    private void recargarCatalogo() {
        panelCatalogo.cargarPeliculas(peliculaDAO.listarTodas());
    }

    private void buscarPelicula() {
        String termino = panelCatalogo.getTxtBuscar().getText().trim();
        List<Pelicula> resultados = termino.isEmpty()
            ? peliculaDAO.listarTodas()
            : peliculaDAO.buscarPorTitulo(termino);
        panelCatalogo.cargarPeliculas(resultados);
    }

    // ── Login y registro ────────────────────────────────────────────────────

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
                // Decidir panel según si es admin o empleado
                if (emp.esAdministrador()) {
                    cargarPanelAdmin();
                } else {
                    cargarPanelEmpleado();
                }
            } else {
                panelLogin.mostrarError("Usuario o contraseña incorrectos.");
            }
        } else {
            Cliente cli = clienteDAO.login(usuario, contrasenia);
            if (cli != null) {
                clienteActivo = cli;
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
        boolean ok = clienteDAO.registrar(nuevo);
        if (ok) {
            JOptionPane.showMessageDialog(ventana,
                "¡Cuenta creada correctamente! Ya puedes iniciar sesión. 🎉",
                "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            panelRegistro.limpiar();
            ventana.cargarPanel(panelLogin);
        } else {
            panelRegistro.mostrarError("El usuario ya existe o hubo un error.");
        }
    }

    // ── Panel Cliente ───────────────────────────────────────────────────────

    private void cargarPanelCliente() {
        panelCliente.setBienvenida(clienteActivo);
        recargarCatalogo();
        panelCatalogo.habilitarAcciones(true);
        panelMisAlquileres.cargarAlquileres(
            alquilerDAO.listarPorCliente(clienteActivo.getIdCliente())
        );
        ventana.cargarPanel(panelCliente);
    }

    // ── Panel Empleado ──────────────────────────────────────────────────────

    private void cargarPanelEmpleado() {
        panelEmpleado.setBienvenida(empleadoActivo);
        recargarCatalogo();
        panelCatalogo.habilitarAcciones(true);
        panelGestionAlquileres.cargarAlquileres(alquilerDAO.listarTodos());
        ventana.cargarPanel(panelEmpleado);
    }

    // ── Panel Admin ─────────────────────────────────────────────────────────

    private void cargarPanelAdmin() {
        panelAdmin.setBienvenida(empleadoActivo);
        recargarCatalogo();
        panelCatalogo.habilitarAcciones(true);
        panelGestionAlquileres.cargarAlquileres(alquilerDAO.listarTodos());
        panelGestionPeliculas.cargarPeliculas(peliculaDAO.listarTodas());
        panelGestionEmpleados.cargarEmpleados(empleadoDAO.listarTodos());
        cargarInformes();
        ventana.cargarPanel(panelAdmin);
    }

    // ── Informes ────────────────────────────────────────────────────────────

    private void cargarInformes() {
        List<Alquiler> todos = alquilerDAO.listarTodos();
        double totalIngresos = todos.stream()
            .mapToDouble(Alquiler::getMontoCobro)
            .sum();
        panelInformes.cargarInformes(todos, totalIngresos);
    }

    // ── Gestión películas (Admin) ────────────────────────────────────────────

    private void editarPelicula() {
        Pelicula p = panelGestionPeliculas.getPeliculaSeleccionada();
        if (p == null) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona una película para editar.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Formulario de edición en un diálogo
        JTextField txtTitulo    = new JTextField(p.getNombrePelicula());
        JTextField txtDirector  = new JTextField(p.getDirector());
        JTextField txtDuracion  = new JTextField(
            String.valueOf(p.getDuracion())
        );
        JTextArea  txtSinopsis  = new JTextArea(p.getSinopsis(), 3, 20);
        txtSinopsis.setLineWrap(true);

        String[] generos = {
            "Acción","Aventura","Animación","Ciencia Ficción",
            "Comedia","Drama","Fantasía","Musical",
            "Romance","Suspense","Terror","Thriller"
        };
        JComboBox<String> cmbGenero = new JComboBox<>(generos);
        cmbGenero.setSelectedItem(p.getGenero());

        JComboBox<String> cmbClasif = new JComboBox<>(
            new String[]{"TP", "7", "12", "16", "18"}
        );
        cmbClasif.setSelectedItem(p.getClasificacionEdad());

        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        form.setBorder(new EmptyBorder(10, 10, 10, 10));
        form.add(new JLabel("Título:"));       form.add(txtTitulo);
        form.add(new JLabel("Director:"));     form.add(txtDirector);
        form.add(new JLabel("Duración (min):")); form.add(txtDuracion);
        form.add(new JLabel("Género:"));       form.add(cmbGenero);
        form.add(new JLabel("Clasificación:")); form.add(cmbClasif);
        form.add(new JLabel("Sinopsis:"));
        form.add(new JScrollPane(txtSinopsis));

        int resultado = JOptionPane.showConfirmDialog(
            ventana, form,
            "Editar película — " + p.getNombrePelicula(),
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                p.setNombrePelicula(txtTitulo.getText().trim());
                p.setDirector(txtDirector.getText().trim());
                p.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
                p.setGenero((String) cmbGenero.getSelectedItem());
                p.setClasificacionEdad((String) cmbClasif.getSelectedItem());
                p.setSinopsis(txtSinopsis.getText().trim());

                boolean ok = peliculaDAO.actualizar(p);
                if (ok) {
                    JOptionPane.showMessageDialog(ventana,
                        "Película actualizada correctamente. ✅",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    panelGestionPeliculas.cargarPeliculas(
                        peliculaDAO.listarTodas()
                    );
                    recargarCatalogo();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(ventana,
                    "La duración debe ser un número entero.",
                    "Error de datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarPelicula() {
        int id = panelGestionPeliculas.getIdPeliculaSeleccionada();
        if (id == -1) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona una película para eliminar.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(ventana,
            "¿Seguro que quieres eliminar esta película?\n" +
            "Esta acción no se puede deshacer.",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean ok = peliculaDAO.eliminar(id);
            if (ok) {
                JOptionPane.showMessageDialog(ventana,
                    "Película eliminada correctamente. 🗑️",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                panelGestionPeliculas.cargarPeliculas(
                    peliculaDAO.listarTodas()
                );
                recargarCatalogo();
            } else {
                JOptionPane.showMessageDialog(ventana,
                    "No se pudo eliminar. Puede tener alquileres asociados.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Gestión empleados (Admin) ────────────────────────────────────────────

    private void crearEmpleado() {
        if (!panelGestionEmpleados.datosValidos()) {
            panelGestionEmpleados.mostrarMensaje(
                "Rellena todos los campos obligatorios.", true
            );
            return;
        }

        // El nuevo empleado siempre tiene como jefe al admin actual
        Empleado nuevo = new Empleado(
            0,
            panelGestionEmpleados.getNombre(),
            panelGestionEmpleados.getApellido(),
            panelGestionEmpleados.getEmail(),
            panelGestionEmpleados.getUsuario(),
            panelGestionEmpleados.getContrasenia(),
            empleadoActivo.getIdEmpleado() // id_jefe = admin actual
        );

        boolean ok = empleadoDAO.crear(nuevo);
        if (ok) {
            panelGestionEmpleados.mostrarMensaje(
                "Empleado creado correctamente. 👤", false
            );
            panelGestionEmpleados.limpiar();
            panelGestionEmpleados.cargarEmpleados(
                empleadoDAO.listarTodos()
            );
        } else {
            panelGestionEmpleados.mostrarMensaje(
                "Error al crear el empleado. El usuario puede ya existir.", true
            );
        }
    }

    // ── Alquiler para cliente (Empleado y Admin) ────────────────────────────

    private void filtrarAlquileres() {
        String filtro = panelGestionAlquileres.getFiltroEstado();
        List<Alquiler> lista = (filtro == null)
            ? alquilerDAO.listarTodos()
            : alquilerDAO.listarTodos().stream()
                .filter(a -> filtro.equalsIgnoreCase(a.getEstadoAlquiler()))
                .collect(Collectors.toList());
        panelGestionAlquileres.cargarAlquileres(lista);
    }

    private void procesarAceptarDevolucion() {
        int idAlquiler = panelGestionAlquileres.getIdAlquilerSeleccionado();
        if (idAlquiler == -1) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona un alquiler pendiente de devolución.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String fechaHoy = java.time.LocalDate.now().toString();
        boolean ok = alquilerDAO.aceptarDevolucion(idAlquiler, fechaHoy);
        if (ok) {
            JOptionPane.showMessageDialog(ventana,
                "Devolución aceptada correctamente. ✅",
                "Devolución procesada", JOptionPane.INFORMATION_MESSAGE);
            panelGestionAlquileres.cargarAlquileres(
                alquilerDAO.listarTodos()
            );
            recargarCatalogo();
            // Si es admin, actualizar también los informes
            if (empleadoActivo != null && empleadoActivo.esAdministrador()) {
                cargarInformes();
            }
        }
    }

    private void iniciarAlquilerParaCliente() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                "No hay clientes registrados.",
                "Sin clientes", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] nombres = clientes.stream()
            .map(c -> c.getNombreCompleto() +
                      " (@" + c.getNombreUsuario() + ")")
            .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(
            ventana, "Selecciona el cliente:",
            "Alquilar para cliente",
            JOptionPane.PLAIN_MESSAGE, null,
            nombres, nombres[0]
        );
        if (seleccion == null) return;

        int idx = java.util.Arrays.asList(nombres).indexOf(seleccion);
        Cliente clienteSeleccionado = clientes.get(idx);

        int fila = panelCatalogo.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona una película del catálogo primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = (String) panelCatalogo.getModelo().getValueAt(fila, 1);
        List<Pelicula> encontradas = peliculaDAO.buscarPorTitulo(titulo);
        if (encontradas.isEmpty()) return;

        List<Copia> copias = copiaDAO.listarDisponiblesPorPelicula(
            encontradas.get(0).getId()
        );
        if (copias.isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                "No hay copias disponibles.",
                "Sin stock", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Cliente original = clienteActivo;
        clienteActivo = clienteSeleccionado;
        mostrarDialogoAlquiler(encontradas.get(0), copias.get(0));
        clienteActivo = original;
    }

    private void guardarPelicula() {
        if (!panelAnadirPelicula.datosValidos()) return;

        Pelicula nueva = new Pelicula(
            0,
            panelAnadirPelicula.getTitulo(),
            panelAnadirPelicula.getDirector(),
            panelAnadirPelicula.getDuracion(),
            panelAnadirPelicula.getGenero(),
            panelAnadirPelicula.getSinopsis(),
            panelAnadirPelicula.getClasificacion()
        );

        boolean ok = peliculaDAO.agregar(nueva);
        if (ok) {
            panelAnadirPelicula.mostrarMensaje(
                "¡Película añadida correctamente! 🎬", false
            );
            panelAnadirPelicula.limpiar();
            recargarCatalogo();
            // Si es admin, recargar también la gestión de películas
            if (empleadoActivo != null && empleadoActivo.esAdministrador()) {
                panelGestionPeliculas.cargarPeliculas(
                    peliculaDAO.listarTodas()
                );
            }
        } else {
            panelAnadirPelicula.mostrarMensaje(
                "Error al guardar la película.", true
            );
        }
    }

    // ── Alquiler ────────────────────────────────────────────────────────────

    private void iniciarAlquiler() {
        if (clienteActivo == null && empleadoActivo == null) {
            JOptionPane.showMessageDialog(ventana,
                "Debes iniciar sesión para alquilar.",
                "Acceso requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int fila = panelCatalogo.getFilaSeleccionada();
        if (fila < 0) {
            JOptionPane.showMessageDialog(ventana,
                "Selecciona una película primero.",
                "Sin selección", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String titulo = (String) panelCatalogo.getModelo().getValueAt(fila, 1);
        List<Pelicula> encontradas = peliculaDAO.buscarPorTitulo(titulo);
        if (encontradas.isEmpty()) return;

        List<Copia> copias = copiaDAO.listarDisponiblesPorPelicula(
            encontradas.get(0).getId()
        );
        if (copias.isEmpty()) {
            JOptionPane.showMessageDialog(ventana,
                "No hay copias disponibles.",
                "Sin stock", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        mostrarDialogoAlquiler(encontradas.get(0), copias.get(0));
    }

    private void mostrarDialogoAlquiler(Pelicula pelicula, Copia copia) {
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
        form.add(new JLabel(
            String.format("%.2f €", copia.getPrecioAlquiler())
        ));
        form.add(new JLabel("Días:"));
        form.add(spinnerDias);
        form.add(new JLabel("Método de pago:"));
        form.add(cmbPago);

        int resultado = JOptionPane.showConfirmDialog(
            ventana, form, "Confirmar alquiler 🎬",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (resultado == JOptionPane.OK_OPTION) {
            int dias = (int) spinnerDias.getValue();
            confirmarAlquiler(
                copia, dias,
                (String) cmbPago.getSelectedItem(),
                copia.getPrecioAlquiler() * dias
            );
        }
    }

    private void confirmarAlquiler(Copia copia, int dias,
                                    String metodoPago, double total) {
        int idTrans = pagoDAO.registrar(new Pago(0, metodoPago, total));
        if (idTrans == -1) {
            JOptionPane.showMessageDialog(ventana,
                "Error al procesar el pago.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.time.LocalDate hoy       = java.time.LocalDate.now();
        java.time.LocalDate devolucion = hoy.plusDays(dias);
        Integer idEmpleado = empleadoActivo != null
            ? empleadoActivo.getIdEmpleado() : null;

        boolean ok = alquilerDAO.crear(new Alquiler(
            0, clienteActivo.getIdCliente(), copia.getIdCopia(),
            idEmpleado, idTrans,
            hoy.toString(), devolucion.toString(), null, "activo"
        ));

        if (ok) {
            copiaDAO.actualizarEstado(copia.getIdCopia(), "alquilada");
            JOptionPane.showMessageDialog(ventana,
                "¡Alquiler confirmado! 🎉\nDevuelve antes del " +
                devolucion + "\nTotal: " +
                String.format("%.2f €", total),
                "Alquiler exitoso", JOptionPane.INFORMATION_MESSAGE);

            recargarCatalogo();
            if (clienteActivo != null) {
                panelMisAlquileres.cargarAlquileres(
                    alquilerDAO.listarPorCliente(
                        clienteActivo.getIdCliente()
                    )
                );
                panelCliente.irAMisAlquileres();
            }
            if (empleadoActivo != null) {
                panelGestionAlquileres.cargarAlquileres(
                    alquilerDAO.listarTodos()
                );
                panelEmpleado.irAGestionAlquileres();
                if (empleadoActivo.esAdministrador()) cargarInformes();
            }
        }
    }

    // ── Devolución cliente ──────────────────────────────────────────────────

    private void procesarSolicitudDevolucion() {
        int idAlquiler = panelMisAlquileres.getIdAlquilerSeleccionado();
        if (idAlquiler == -1) return;

        int conf = JOptionPane.showConfirmDialog(ventana,
            "¿Confirmas la solicitud de devolución?\n" +
            "Un empleado la procesará en breve.",
            "Solicitar devolución", JOptionPane.YES_NO_OPTION);

        if (conf == JOptionPane.YES_OPTION) {
            boolean ok = alquilerDAO.solicitarDevolucion(idAlquiler);
            if (ok) {
                JOptionPane.showMessageDialog(ventana,
                    "Solicitud enviada. ¡Gracias! 📦",
                    "Solicitud registrada",
                    JOptionPane.INFORMATION_MESSAGE);
                panelMisAlquileres.cargarAlquileres(
                    alquilerDAO.listarPorCliente(
                        clienteActivo.getIdCliente()
                    )
                );
            }
        }
    }

    // ── Cerrar sesión ───────────────────────────────────────────────────────

    private void cerrarSesion() {
        int conf = JOptionPane.showConfirmDialog(ventana,
            "¿Seguro que quieres cerrar sesión?",
            "Cerrar sesión", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            iniciarModoInvitado();
        }
    }

    // ── Getters ─────────────────────────────────────────────────────────────
    public Cliente  getClienteActivo()  { return clienteActivo; }
    public Empleado getEmpleadoActivo() { return empleadoActivo; }
}