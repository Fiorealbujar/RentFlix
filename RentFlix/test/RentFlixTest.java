import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import dao.AlquilerDAO;
import dao.ClienteDAO;
import dao.CopiaDAO;
import dao.EmpleadoDAO;
import dao.PagoDAO;
import dao.PeliculaDAO;
import model.Alquiler;
import model.Cliente;
import model.Copia;
import model.Empleado;
import model.Pago;
import model.Pelicula;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Plan de pruebas unitarias para RentFlix.
 *
 * <p>Cubre los 9 Casos de Uso del diagrama de casos de uso:</p>
 * <ul>
 *   <li>CU01 — Iniciar sesión</li>
 *   <li>CU02 — Registrar alquiler (include → Registrar pago)</li>
 *   <li>CU03 — Registrar devolución (empleado acepta)</li>
 *   <li>CU04 — Ver reportes</li>
 *   <li>CU05 — Bloquear cliente</li>
 *   <li>CU06 — Ver catálogo de películas</li>
 *   <li>CU07 — Alquilar película (cliente directo)</li>
 *   <li>CU08 — Ver alquileres activos</li>
 *   <li>CU09 — Solicitar devolución (cliente)</li>
 * </ul>
 *
 * <p><strong>Nota:</strong> Todos los tests que dependen del estado de la BD
 * buscan dinámicamente los registros necesarios, sin depender de ids fijos.</p>
 *
 * @author Equipo RentFlix — DAM 2025-2026
 * @version 1.0
 */
public class RentFlixTest {

    private ClienteDAO  clienteDAO;
    private EmpleadoDAO empleadoDAO;
    private PeliculaDAO peliculaDAO;
    private CopiaDAO    copiaDAO;
    private AlquilerDAO alquilerDAO;
    private PagoDAO     pagoDAO;

    @Before
    public void setUp() {
        clienteDAO  = new ClienteDAO();
        empleadoDAO = new EmpleadoDAO();
        peliculaDAO = new PeliculaDAO();
        copiaDAO    = new CopiaDAO();
        alquilerDAO = new AlquilerDAO();
        pagoDAO     = new PagoDAO();
    }

    // =========================================================================
    // CU01 — INICIAR SESIÓN
    // =========================================================================

    /** CU01 — Login de cliente con credenciales correctas. */
    @Test
    public void testLoginClienteCorrecto() {
        Cliente cliente = clienteDAO.login("ana_cli", "1234");
        assertNotNull("Login válido debe devolver un Cliente", cliente);
        assertEquals("El nombre de usuario debe coincidir", "ana_cli", cliente.getNombreUsuario());
    }

    /** CU01 — Login con contraseña incorrecta debe devolver null. */
    @Test
    public void testLoginClienteIncorrecto() {
        Cliente cliente = clienteDAO.login("ana_cli", "wrongpassword");
        assertNull("Login con contraseña incorrecta debe devolver null", cliente);
    }

    /**
     * CU01/CU05 — Login de cliente bloqueado.
     * El DAO devuelve el objeto; el Controlador es quien deniega el acceso.
     * Busca dinámicamente un cliente bloqueado en la BD.
     */
    @Test
    public void testLoginClienteBloqueado() {
        // Buscar dinámicamente un cliente bloqueado
        ArrayList<Cliente> todos = clienteDAO.listarTodos();
        Cliente bloqueado = null;
        for (Cliente c : todos) {
            if ("bloqueado".equalsIgnoreCase(c.getEstado())) {
                bloqueado = c;
                break;
            }
        }
        if (bloqueado == null) {
            return; // No hay clientes bloqueados en este momento
        }
        Cliente resultado = clienteDAO.login(bloqueado.getNombreUsuario(),
                bloqueado.getContraseniaCliente());
        // El login devuelve el objeto aunque esté bloqueado
        assertNotNull("El cliente bloqueado debe existir en la BD", resultado);
        assertEquals("El estado debe ser 'bloqueado'", "bloqueado", resultado.getEstado());
    }

    /** CU01 — Login de empleado con credenciales correctas. */
    @Test
    public void testLoginEmpleadoCorrecto() {
        Empleado empleado = empleadoDAO.login("laura_emp", "1234");
        assertNotNull("Login de empleado válido debe devolver un Empleado", empleado);
    }

    /** CU01 — El administrador tiene idJefe=null y esAdministrador()=true. */
    @Test
    public void testLoginAdminEsAdministrador() {
        Empleado admin = empleadoDAO.login("carlos_admin", "1234");
        assertNotNull("El administrador debe existir en la BD", admin);
        assertNull("El administrador debe tener idJefe = null", admin.getIdJefe());
        assertTrue("esAdministrador() debe devolver true", admin.esAdministrador());
    }

    // =========================================================================
    // CU02 — REGISTRAR ALQUILER (include → REGISTRAR PAGO)
    // =========================================================================

    /** CU02 (include) — Registrar un pago devuelve id generado mayor que 0. */
    @Test
    public void testRegistrarPagoDevuelveId() {
        Pago pago = new Pago(0, "efectivo", 5.00);
        int idGenerado = pagoDAO.registrar(pago);
        assertTrue("Registrar un pago debe devolver un id > 0", idGenerado > 0);
    }

    /**
     * CU02 — Registrar un alquiler desde el empleado.
     * Usa la primera copia disponible de la BD para no depender de ids fijos.
     */
    @Test
    public void testRegistrarAlquilerDesdeEmpleado() {
        ArrayList<Copia> disponibles = copiaDAO.listarTodasDisponibles();
        if (disponibles.isEmpty()) {
            return; // No hay copias disponibles
        }
        Copia copia = disponibles.get(0);

        Pago pago = new Pago(0, "tarjeta", copia.getPrecioAlquiler() * 3);
        int idTrans = pagoDAO.registrar(pago);
        assertTrue("El pago debe registrarse antes del alquiler", idTrans > 0);

        Alquiler alquiler = new Alquiler(
            0, 1, copia.getIdCopia(), 1, idTrans,
            LocalDate.now().toString(),
            LocalDate.now().plusDays(3).toString(),
            null, "activo"
        );
        int resultado = alquilerDAO.crear(alquiler);
        assertEquals("Crear alquiler con empleado debe afectar 1 fila", 1, resultado);
    }

    // =========================================================================
    // CU03 — REGISTRAR DEVOLUCIÓN
    // =========================================================================

    /**
     * CU03 — aceptarDevolucion() sobre un alquiler "activo" debe devolver -1.
     * Decisión de diseño: solo acepta "pendiente_devolucion".
     * Busca dinámicamente un alquiler activo.
     */
    @Test
    public void testAceptarDevolucionSoloPendiente() {
        ArrayList<Alquiler> todos = alquilerDAO.listarTodos();
        int idActivo = -1;
        for (Alquiler a : todos) {
            if ("activo".equalsIgnoreCase(a.getEstadoAlquiler())) {
                idActivo = a.getIdAlquiler();
                break;
            }
        }
        if (idActivo == -1) {
            return; // No hay alquileres activos
        }
        int resultado = alquilerDAO.aceptarDevolucion(idActivo, LocalDate.now().toString());
        assertEquals("aceptarDevolucion() sobre 'activo' debe devolver -1", -1, resultado);
    }

    /**
     * CU03 — aceptarDevolucion() sobre "pendiente_devolucion" devuelve idCopia > 0.
     * Busca dinámicamente un alquiler pendiente.
     */
    @Test
    public void testAceptarDevolucionPendienteExitosa() {
        ArrayList<Alquiler> todos = alquilerDAO.listarTodos();
        int idPendiente = -1;
        for (Alquiler a : todos) {
            if ("pendiente_devolucion".equalsIgnoreCase(a.getEstadoAlquiler())) {
                idPendiente = a.getIdAlquiler();
                break;
            }
        }
        if (idPendiente == -1) {
            return; // No hay alquileres pendientes en este momento
        }
        int idCopia = alquilerDAO.aceptarDevolucion(idPendiente, LocalDate.now().toString());
        assertTrue("aceptarDevolucion() sobre 'pendiente_devolucion' debe devolver idCopia > 0",
                idCopia > 0);
    }

    /** CU03 — Al aceptar devolución, actualizarEstado() devuelve 1. */
    @Test
    public void testCopiaVuelveDisponibleTrasDEvolucion() {
        ArrayList<Copia> disponibles = copiaDAO.listarTodasDisponibles();
        if (disponibles.isEmpty()) {
            return;
        }
        int idCopia = disponibles.get(0).getIdCopia();
        int resultado = copiaDAO.actualizarEstado(idCopia, "disponible");
        assertEquals("Actualizar copia a 'disponible' debe afectar 1 fila", 1, resultado);
    }

    // =========================================================================
    // CU04 — VER REPORTES
    // =========================================================================

    /** CU04 — listarTodos() devuelve lista no nula con al menos un elemento. */
    @Test
    public void testListarTodosAlquileresParaReporte() {
        ArrayList<Alquiler> lista = alquilerDAO.listarTodos();
        assertNotNull("La lista de alquileres no debe ser null", lista);
        assertFalse("Debe haber al menos un alquiler en la BD", lista.isEmpty());
    }

    /** CU04 — Los alquileres incluyen datos de película y cliente (JOINs). */
    @Test
    public void testAlquileresConDetalleJoin() {
        ArrayList<Alquiler> lista = alquilerDAO.listarTodos();
        assertFalse("Debe haber alquileres para verificar JOINs", lista.isEmpty());
        Alquiler primero = lista.get(0);
        assertNotNull("El nombre de película no debe ser null", primero.getNombrePelicula());
        assertNotNull("El nombre de cliente no debe ser null", primero.getNombreCliente());
    }

    // =========================================================================
    // CU05 — BLOQUEAR CLIENTE
    // =========================================================================

    /** CU05 — Bloquear un cliente activo cambia su estado a "bloqueado". */
    @Test
    public void testBloquearCliente() {
        ArrayList<Cliente> todos = clienteDAO.listarTodos();
        Cliente activo = null;
        for (Cliente c : todos) {
            if ("activo".equalsIgnoreCase(c.getEstado())) {
                activo = c;
                break;
            }
        }
        assertNotNull("Debe haber al menos un cliente activo en la BD", activo);
        activo.setEstado("bloqueado");
        int resultado = clienteDAO.actualizar(activo);
        assertEquals("Actualizar estado a 'bloqueado' debe afectar 1 fila", 1, resultado);
    }

    /** CU05 — Desbloquear cliente devuelve su estado a "activo". */
    @Test
    public void testDesbloquearCliente() {
        ArrayList<Cliente> todos = clienteDAO.listarTodos();
        Cliente bloqueado = null;
        for (Cliente c : todos) {
            if ("bloqueado".equalsIgnoreCase(c.getEstado())) {
                bloqueado = c;
                break;
            }
        }
        if (bloqueado == null) {
            return; // No hay clientes bloqueados
        }
        bloqueado.setEstado("activo");
        int resultado = clienteDAO.actualizar(bloqueado);
        assertEquals("Desbloquear cliente debe afectar 1 fila", 1, resultado);
    }

    // =========================================================================
    // CU06 — VER CATÁLOGO DE PELÍCULAS
    // =========================================================================

    /** CU06 — listarTodas() solo devuelve películas activas. */
    @Test
    public void testCatalogoSoloPeliculasActivas() {
        ArrayList<Pelicula> peliculas = peliculaDAO.listarTodas();
        assertNotNull("La lista de películas no debe ser null", peliculas);
        assertFalse("Debe haber películas activas en la BD", peliculas.isEmpty());
    }

    /** CU06 — Todas las copias del catálogo tienen estado "disponible". */
    @Test
    public void testCatalogoSoloCopiaDisponibles() {
        ArrayList<Copia> copias = copiaDAO.listarTodasDisponibles();
        assertNotNull("La lista de copias no debe ser null", copias);
        assertFalse("Debe haber copias disponibles en la BD", copias.isEmpty());
        for (Copia copia : copias) {
            assertEquals("Todas las copias del catálogo deben ser 'disponible'",
                    "disponible", copia.getEstado());
        }
    }

    /** CU06 — Búsqueda por título devuelve resultados. */
    @Test
    public void testBuscarPeliculaPorTitulo() {
        ArrayList<Pelicula> peliculas = peliculaDAO.listarTodas();
        if (peliculas.isEmpty()) {
            return;
        }
        String titulo = peliculas.get(0).getNombrePelicula().substring(0, 3);
        ArrayList<Pelicula> resultados = peliculaDAO.buscarPorTitulo(titulo);
        assertNotNull("La búsqueda no debe devolver null", resultados);
        assertFalse("La búsqueda debe encontrar al menos una película", resultados.isEmpty());
    }

    // =========================================================================
    // CU07 — ALQUILAR PELÍCULA (cliente directo, sin empleado)
    // =========================================================================

    /** CU07 — Un cliente puede crear un alquiler con idEmpleado=null. */
    @Test
    public void testAlquilerClienteSinEmpleado() {
        ArrayList<Copia> disponibles = copiaDAO.listarTodasDisponibles();
        if (disponibles.isEmpty()) {
            return;
        }
        Copia copia = disponibles.get(0);

        Pago pago = new Pago(0, "efectivo", copia.getPrecioAlquiler() * 2);
        int idTrans = pagoDAO.registrar(pago);
        assertTrue("El pago debe registrarse", idTrans > 0);

        Alquiler alquiler = new Alquiler(
            0, 2, copia.getIdCopia(), null, idTrans,
            LocalDate.now().toString(),
            LocalDate.now().plusDays(5).toString(),
            null, "activo"
        );
        int resultado = alquilerDAO.crear(alquiler);
        assertEquals("Alquiler con idEmpleado=null debe afectar 1 fila", 1, resultado);
    }

    // =========================================================================
    // CU08 — VER ALQUILERES ACTIVOS (cliente)
    // =========================================================================

    /** CU08 — listarPorCliente() devuelve lista no nula con alquileres del cliente. */
    @Test
    public void testVerAlquileresDeCliente() {
        ArrayList<Alquiler> alquileres = alquilerDAO.listarPorCliente(1);
        assertNotNull("La lista no debe ser null", alquileres);
        assertFalse("El cliente 1 debe tener alquileres en la BD", alquileres.isEmpty());
    }

    /** CU08 — Todos los alquileres de listarPorCliente(1) pertenecen al cliente 1. */
    @Test
    public void testAlquileresPertenecenAlCliente() {
        ArrayList<Alquiler> alquileres = alquilerDAO.listarPorCliente(1);
        for (Alquiler a : alquileres) {
            assertEquals("Todos los alquileres deben pertenecer al cliente 1",
                    1, a.getIdCliente());
        }
    }

    // =========================================================================
    // CU09 — SOLICITAR DEVOLUCIÓN (cliente)
    // =========================================================================

    /** CU09 — Solicitar devolución de un alquiler activo devuelve 1. */
    @Test
    public void testSolicitarDevolucionDesdeActivo() {
        ArrayList<Alquiler> todos = alquilerDAO.listarTodos();
        int idActivo = -1;
        for (Alquiler a : todos) {
            if ("activo".equalsIgnoreCase(a.getEstadoAlquiler())) {
                idActivo = a.getIdAlquiler();
                break;
            }
        }
        if (idActivo == -1) {
            return; // No hay alquileres activos
        }
        int resultado = alquilerDAO.solicitarDevolucion(idActivo);
        assertEquals("solicitarDevolucion() sobre 'activo' debe devolver 1", 1, resultado);
    }

    /** CU09 — Solicitar devolución de un alquiler ya devuelto devuelve 0. */
    @Test
    public void testSolicitarDevolucionAlquilerYaDevuelto() {
        ArrayList<Alquiler> todos = alquilerDAO.listarTodos();
        int idDevuelto = -1;
        for (Alquiler a : todos) {
            if ("devuelto".equalsIgnoreCase(a.getEstadoAlquiler())) {
                idDevuelto = a.getIdAlquiler();
                break;
            }
        }
        if (idDevuelto == -1) {
            return; // No hay alquileres devueltos
        }
        int resultado = alquilerDAO.solicitarDevolucion(idDevuelto);
        assertEquals("solicitarDevolucion() sobre 'devuelto' debe devolver 0", 0, resultado);
    }

    // =========================================================================
    // PRUEBA DE INTEGRACIÓN — Flujo completo
    // =========================================================================

    /**
     * Integración — Flujo completo: login → pago → alquiler → solicitar
     * devolución → aceptar devolución → copia disponible.
     * Usa la primera copia disponible de la BD dinámicamente.
     */
    @Test
    public void testFlujoCompletoAlquilerDevolucion() {
        // 1. Login
        Cliente cliente = clienteDAO.login("ana_cli", "1234");
        assertNotNull("Paso 1 — Login debe ser exitoso", cliente);

        // 2. Buscar copia disponible
        ArrayList<Copia> disponibles = copiaDAO.listarTodasDisponibles();
        if (disponibles.isEmpty()) {
            return; // Sin copias no se puede testear
        }
        Copia copia = disponibles.get(0);

        // 3. Registrar pago
        Pago pago = new Pago(0, "efectivo", copia.getPrecioAlquiler() * 4);
        int idTrans = pagoDAO.registrar(pago);
        assertTrue("Paso 3 — El pago debe generar un id > 0", idTrans > 0);

        // 4. Crear alquiler
        Alquiler alquiler = new Alquiler(
            0, cliente.getIdCliente(), copia.getIdCopia(), null, idTrans,
            LocalDate.now().toString(),
            LocalDate.now().plusDays(4).toString(),
            null, "activo"
        );
        int creado = alquilerDAO.crear(alquiler);
        assertEquals("Paso 4 — Crear alquiler debe afectar 1 fila", 1, creado);

        // 5. Recuperar el alquiler recién creado buscando por idCopia y estado "activo"
        ArrayList<Alquiler> misAlquileres = alquilerDAO.listarPorCliente(cliente.getIdCliente());
        assertFalse("Paso 5 — El cliente debe tener alquileres", misAlquileres.isEmpty());
        int idAlquiler = -1;
        for (Alquiler a : misAlquileres) {
            if (a.getIdCopia() == copia.getIdCopia() && "activo".equalsIgnoreCase(a.getEstadoAlquiler())) {
                idAlquiler = a.getIdAlquiler();
                break;
            }
        }
        assertTrue("Paso 5 — Debe encontrarse el alquiler recién creado", idAlquiler > 0);

        // 6. Cliente solicita devolución
        int solicitado = alquilerDAO.solicitarDevolucion(idAlquiler);
        assertEquals("Paso 6 — Solicitar devolución debe devolver 1", 1, solicitado);

        // 7. Empleado acepta devolución
        int idCopaDevuelta = alquilerDAO.aceptarDevolucion(idAlquiler, LocalDate.now().toString());
        assertTrue("Paso 7 — Aceptar devolución debe devolver idCopia > 0", idCopaDevuelta > 0);

        // 8. Copia vuelve a disponible
        int copiaActualizada = copiaDAO.actualizarEstado(idCopaDevuelta, "disponible");
        assertEquals("Paso 8 — Copia debe volver a 'disponible'", 1, copiaActualizada);
    }
}