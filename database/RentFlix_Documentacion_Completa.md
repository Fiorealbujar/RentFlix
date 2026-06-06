# RentFlix — Documentación técnica completa del código

---

## Índice

1. Visión general y arquitectura
2. Arranque de la aplicación: Main.java
3. Capa Model
4. Capa DAO: ConexionDB y patrón de acceso
5. ClienteDAO
6. EmpleadoDAO
7. PeliculaDAO
8. CopiaDAO
9. AlquilerDAO
10. PagoDAO
11. Capa View: estructura general
12. VentanaPrincipal
13. PanelLogin
14. PanelRegistro
15. PanelCatalogo
16. PanelCliente
17. PanelMisAlquileres
18. PanelMiCuenta
19. PanelEmpleado y PanelAdmin
20. PanelGestionAlquileres
21. PanelAnadirPelicula
22. PanelGestionPeliculas
23. PanelGestionEmpleados
24. PanelGestionClientes
25. PanelInformes
26. Capa Controller: Controlador.java
27. Flujos completos paso a paso
28. Decisiones de diseño importantes
29. Preguntas frecuentes de entrega

---

## 1. Visión general y arquitectura

RentFlix es una aplicación de escritorio Java con interfaz gráfica Swing que gestiona el alquiler de películas en un videoclub. Tiene tres tipos de usuario: invitado, cliente, empleado y administrador.

### Patrón MVC + DAO

El proyecto aplica el patrón **MVC (Modelo - Vista - Controlador)** combinado con **DAO (Data Access Object)**:

- **Model**: clases POJO que representan tablas de la BD. No tienen lógica de negocio salvo métodos de utilidad sencillos.
- **DAO**: clases que encapsulan toda la comunicación con la base de datos. El resto del código nunca escribe SQL directamente.
- **View**: paneles Swing que solo muestran datos y capturan eventos. No tienen lógica de negocio.
- **Controller**: única clase que recibe todos los eventos de la vista, decide qué hacer, llama al DAO y actualiza la vista.

### Estructura de paquetes

```
src/
  main/        → Main.java
  model/       → Cliente, Empleado, Pelicula, Copia, Alquiler, Pago
  dao/         → Interfaces, implementaciones, ConexionDB
  view/        → Todos los paneles Swing
  controller/  → Controlador.java
```

### Look and Feel

Se usa **FlatLaf** (`FlatLightLaf`) como librería de Look and Feel para dar un aspecto moderno a los componentes Swing estándar. Se aplica con `FlatLightLaf.setup()` antes de crear cualquier componente.

---

## 2. Arranque de la aplicación: Main.java

`Main` es el punto de entrada. Hace tres cosas en orden:

### 1. Configurar el Look and Feel y los textos de los diálogos

```java
FlatLightLaf.setup();

UIManager.put("OptionPane.yesButtonText",    "Sí");
UIManager.put("OptionPane.noButtonText",     "No");
UIManager.put("OptionPane.okButtonText",     "Aceptar");
UIManager.put("OptionPane.cancelButtonText", "Cancelar");
```

Los `UIManager.put` traducen al español los botones de todos los `JOptionPane` de la app. Sin esto aparecerían "Yes"/"No"/"OK"/"Cancel" en inglés.

### 2. Ejecutar en el hilo EDT con SwingUtilities.invokeLater

```java
SwingUtilities.invokeLater(new Runnable() {
    @Override
    public void run() {
        // toda la creación de la UI aquí
    }
});
```

Swing **no es thread-safe**: todos los componentes deben crearse y modificarse en el hilo de eventos de Swing (EDT, Event Dispatch Thread). `invokeLater` encola el `Runnable` para que se ejecute en ese hilo. Se usa una clase anónima en lugar de una lambda para evitar programación funcional.

### 3. Instanciar paneles, controlador e inyectar

El orden de instanciación es obligatorio:

1. Primero los paneles hijos (los que no contienen a otros).
2. Luego los paneles contenedores (`PanelCliente`, `PanelEmpleado`, `PanelAdmin`) que reciben los hijos por constructor.
3. Luego el `Controlador` con todos los paneles como parámetros.
4. Finalmente `setControlador(controlador)` en cada panel para registrar los listeners.

### Por qué cada rol tiene paneles propios

Empleado y Admin tienen paneles **completamente separados** e independientes. Por ejemplo hay un `gestionAlqEmp` y un `gestionAlqAdm`. La razón es que **Swing no puede mostrar el mismo componente en dos contenedores distintos**. Si se añade un panel a un `JTabbedPane`, Swing lo quita automáticamente del contenedor donde estuviera antes. Si empleado y admin compartieran paneles, al cargar uno el otro quedaría vacío.

Hay además dos catálogos: `catInvitado` (pantalla inicial sin sesión) y `catCliente` (dentro del panel del cliente). El mismo motivo: no pueden ser el mismo objeto.

---

## 3. Capa Model

Los modelos son clases POJO (Plain Old Java Object). Solo tienen:
- Atributos privados.
- Constructor vacío y constructor con todos los parámetros.
- Getters y setters por cada atributo.
- Algún método de utilidad puntual.

### Cliente

Representa la tabla `Clientes`. El campo `estado` puede ser `"activo"` o `"bloqueado"`. Un cliente bloqueado no puede iniciar sesión.

```java
public String getNombreCompleto() {
    return nombreCliente + " " + apellidoCliente;
}
```

### Empleado

Representa la tabla `Empleados`. El campo clave es `idJefe` (Integer, puede ser null).

```java
// Si idJefe es null, es administrador. No hace falta campo de rol en la BD.
public boolean esAdministrador() {
    return this.idJefe == null;
}
```

Esta decisión de diseño evita tener un campo `rol` extra en la BD. La jerarquía se deduce de la estructura: el admin es el empleado que no tiene jefe.

### Pelicula

Campos: `idPelicula`, `nombrePelicula`, `director`, `duracion`, `genero`, `sinopsis`, `clasificacionEdad`.

### Copia

Representa un ejemplar físico de una película. Una película puede tener varias copias en distintos formatos y precios. Campos: `idCopia`, `idPelicula`, `formato` (DVD/Blu-ray/4K Ultra HD), `estado` (disponible/alquilada), `precioAlquiler`.

### Alquiler

Campos de la tabla: `idAlquiler`, `idCliente`, `idCopia`, `idEmpleado` (null si el propio cliente alquiló), `idTransaccion`, `fechaAlquiler`, `fechaDevolucionPrevista`, `fechaDevolucionReal` (null hasta que se devuelve), `estadoAlquiler`.

Estados posibles: `activo`, `pendiente_devolucion`, `devuelto`, `vencido`.

Campos extra que no están en la tabla pero se rellenan desde JOINs en la consulta SQL:

```java
private String nombrePelicula; // JOIN con Peliculas a través de Copias
private String nombreCliente;  // JOIN con Clientes
private double montoCobro;     // JOIN con Pagos
```

Estos campos extra permiten mostrar toda la información del alquiler en la tabla sin hacer consultas adicionales.

### Pago

Campos: `idTransaccion`, `metodoPago` (efectivo/tarjeta/transferencia), `montoCobro`.

---

## 4. Capa DAO: ConexionDB y patrón de acceso

### ConexionDB

Lee la configuración de conexión de un fichero de propiedades externo:

```
DB/ConfiguracionDB.properties:
DRIVER=org.sqlite.JDBC
URL=jdbc:sqlite:DB/rentflix.db
```

```java
public Connection getConexion() throws ClassNotFoundException, SQLException {
    Class.forName(driver);  // registra el driver JDBC
    return DriverManager.getConnection(url);
}
```

`Class.forName(driver)` carga dinámicamente el driver SQLite y lo registra en el `DriverManager`. Es necesario llamarlo antes de `getConnection`. Cada DAO instancia su propio `ConexionDB` y llama a `getConexion()` cuando necesita una conexión, que siempre es nueva. Esto evita problemas de conexiones cerradas o en mal estado.

### Patrón de todos los métodos DAO

Todos los métodos siguen el mismo esquema estricto sin excepción:

```java
public TipoRetorno nombreMetodo(parametros) {
    TipoRetorno resultado = valorPorDefecto;
    String query = "SQL con ? para los parámetros";

    Connection con       = null;  // declarar fuera del try
    PreparedStatement ps = null;
    ResultSet rslt       = null;  // solo si hay SELECT

    try {
        con  = acceso.getConexion();
        ps   = con.prepareStatement(query);
        // ps.setXxx() para cada ?
        rslt = ps.executeQuery(); // o ps.executeUpdate()
        // procesar resultado
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rslt != null) rslt.close(); // orden inverso a la apertura
            if (ps   != null) ps.close();
            if (con  != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return resultado;
}
```

**Por qué declarar fuera del try**: si ocurre una excepción al obtener la conexión, las variables dentro del try no existirían y no se podría cerrar nada en el `finally`. Declarándolas fuera e inicializándolas a `null`, el `finally` siempre puede comprobar si son no-null y cerrarlas.

**Por qué orden inverso en el finally**: los recursos más dependientes deben liberarse primero. `ResultSet` depende del `PreparedStatement`, y este depende de la `Connection`.

**Por qué dos catch separados**: `ClassNotFoundException` es una checked exception que lanza `Class.forName()` si el driver no está en el classpath. `SQLException` es la excepción de JDBC. Son causas distintas y se tratan por separado.

### PreparedStatement vs Statement

`PreparedStatement` se usa cuando la query tiene parámetros variables. Los valores se asignan con `setString()`, `setInt()` etc., y el driver los escapa automáticamente, **protegiendo contra inyección SQL**.

`Statement` solo se usa cuando la query no tiene parámetros (por ejemplo `SELECT * FROM Peliculas`).

### El método mapear()

Cada DAO tiene un método privado `mapear(ResultSet rs)` que convierte una fila del `ResultSet` en un objeto del modelo. Se llama desde todos los métodos de consulta para no repetir código:

```java
private Cliente mapear(ResultSet rs) throws SQLException {
    return new Cliente(
        rs.getInt("id_cliente"),
        rs.getString("nombre_cliente"),
        // ...
    );
}
```

### Los métodos devuelven int, no boolean

`executeUpdate()` de JDBC devuelve el número de filas afectadas. Los DAOs devuelven ese valor directamente. En el controlador se comprueba con `> 0`:

```java
if (clienteDAO.registrar(nuevo) > 0) { // al menos 1 fila afectada = éxito
    // ...
}
```

---

## 5. ClienteDAO

Implementa `IClienteDAO`. Métodos:

**`login(usuario, contrasenia)`**: hace un `SELECT` con `WHERE nombre_usuario = ? AND contrasenia_cliente = ?`. Devuelve el `Cliente` si existe, `null` si no.

**`registrar(cliente)`**: `INSERT` con el estado hardcodeado como `'activo'` directamente en el SQL. Devuelve el número de filas insertadas (1 = éxito, 0 = fallo).

**`obtenerPorId(id)`**: `SELECT` por `id_cliente`. Usado para recargar datos actualizados.

**`listarTodos()`**: `SELECT *` ordenado por `apellido_cliente ASC`. Devuelve `ArrayList<Cliente>`.

**`actualizar(cliente)`**: `UPDATE` que modifica nombre, apellido, email, usuario y estado. No toca la contraseña.

**`eliminar(id)`**: `DELETE` por `id_cliente`. Fallará si el cliente tiene alquileres asociados por restricción de clave foránea.

**`actualizarDatos(cliente, nuevaContrasenia)`**: `UPDATE` que modifica todos los datos del cliente incluyendo la contraseña. Se usa desde "Mi cuenta" cuando el cliente modifica sus propios datos.

---

## 6. EmpleadoDAO

Implementa `IEmpleadoDAO`. Métodos:

**`login(usuario, contrasenia)`**: igual que en `ClienteDAO` pero sobre la tabla `Empleados`.

**`crear(empleado)`**: `INSERT` que incluye `id_jefe`. Cuando el admin crea un empleado, le pasa su propio `idEmpleado` como `id_jefe`, haciendo que el nuevo empleado tenga jefe y por tanto no sea admin.

**`actualizar(empleado)`**: `UPDATE` de nombre, apellido, email y usuario. No toca contraseña ni `id_jefe`.

**`eliminar(id)`**: `DELETE` por `id_empleado`.

**`listarTodos()`**: `SELECT *` de todos los empleados.

---

## 7. PeliculaDAO

Implementa `IPeliculaDAO`. Métodos:

**`listarTodas()`**: `SELECT *` ordenado por `nombre_pelicula ASC`.

**`buscarPorTitulo(titulo)`**: `SELECT` con `WHERE nombre_pelicula LIKE ?` y el parámetro como `"%" + titulo + "%"`. El `%` es el comodín de SQL que significa "cualquier cosa antes/después".

**`agregar(pelicula)`**: `INSERT` con todos los campos.

**`actualizar(pelicula)`**: `UPDATE` por `id_pelicula`.

**`eliminar(id)`**: `DELETE` por `id_pelicula`. Fallará si la película tiene copias asociadas.

---

## 8. CopiaDAO

Implementa `ICopiaDAO`. Métodos:

**`listarTodasDisponibles()`**: `SELECT * FROM Copias WHERE estado = 'disponible'`. Solo devuelve copias que se pueden alquilar.

**`listarDisponiblesPorPelicula(idPelicula)`**: `SELECT` con `WHERE id_pelicula = ? AND estado = 'disponible'`. Se usa para buscar qué copias quedan de una película concreta.

**`listarDisponiblesPorFormato(formato)`**: `SELECT` con `WHERE formato = ? AND estado = 'disponible'`. Se usa para el filtro del catálogo.

**`actualizarEstado(idCopia, nuevoEstado)`**: `UPDATE` del campo `estado`. Se llama con `'alquilada'` al crear un alquiler y con `'disponible'` al aceptar una devolución.

---

## 9. AlquilerDAO

Implementa `IAlquilerDAO`. Es el DAO más complejo por los JOINs.

### Constante SQL_CON_DETALLE

```java
private static final String SQL_CON_DETALLE =
    "SELECT a.*, p.nombre_pelicula, " +
    "c.nombre_cliente || ' ' || c.apellido_cliente AS nombre_cliente, " +
    "COALESCE(pg.monto_cobro, 0) AS monto_cobro " +
    "FROM Alquileres a " +
    "JOIN Copias co     ON co.id_copia       = a.id_copia " +
    "JOIN Peliculas p   ON p.id_pelicula     = co.id_pelicula " +
    "JOIN Clientes c    ON c.id_cliente      = a.id_cliente " +
    "LEFT JOIN Pagos pg ON pg.id_transaccion = a.id_transaccion ";
```

Esta constante contiene la parte común de todas las consultas de listado. Se concatena con el `WHERE` o `ORDER BY` necesario en cada método. El operador `||` en SQLite concatena strings. `COALESCE` devuelve el primer valor no nulo (si el pago es null, devuelve 0). Se usa `LEFT JOIN` con Pagos porque puede haber alquileres sin pago todavía.

### mapear()

```java
private Alquiler mapear(ResultSet rs) throws SQLException {
    Alquiler a = new Alquiler(/* campos de la tabla */);
    try {
        a.setNombrePelicula(rs.getString("nombre_pelicula"));
        a.setNombreCliente(rs.getString("nombre_cliente"));
        a.setMontoCobro(rs.getDouble("monto_cobro"));
    } catch (SQLException ignored) {}
    return a;
}
```

Los campos extra del JOIN se asignan en un try-catch separado porque si la query no incluye esas columnas (posible en consultas sin JOIN), no falla todo el mapeo.

### Métodos

**`crear(alquiler)`**: `INSERT` en `Alquileres`. Usa `ps.setObject()` para `idEmpleado` e `idTransaccion` porque pueden ser `null` (cuando el cliente alquila por sí mismo, `idEmpleado` es null).

**`listarPorCliente(id)`**: usa `SQL_CON_DETALLE + "WHERE a.id_cliente = ?"`. Solo los alquileres del cliente logueado.

**`listarTodos()`**: usa `SQL_CON_DETALLE + "ORDER BY a.fecha_alquiler DESC"`. Todos los alquileres ordenados del más reciente.

**`solicitarDevolucion(id)`**: `UPDATE` que cambia `estado_alquiler` a `'pendiente_devolucion'`. Solo actúa si el estado actual es `'activo'` (para evitar cambios en estados incorrectos):
```sql
UPDATE Alquileres SET estado_alquiler = 'pendiente_devolucion'
WHERE id_alquiler = ? AND estado_alquiler = 'activo'
```

**`aceptarDevolucion(id, fecha)`**: `UPDATE` que cambia el estado a `'devuelto'` y rellena `fecha_devolucion_real`. Solo actúa si el estado es `'pendiente_devolucion'`.

**`marcarVencidos()`**: `UPDATE` masivo que cambia a `'vencido'` todos los alquileres `'activo'` cuya `fecha_devolucion_prevista` es anterior a hoy:
```sql
UPDATE Alquileres SET estado_alquiler = 'vencido'
WHERE estado_alquiler = 'activo'
AND fecha_devolucion_prevista < ?   -- ? = fecha de hoy
```

---

## 10. PagoDAO

Implementa `IPagoDAO`. Solo tiene un método:

**`registrar(pago)`**: `INSERT` en `Pagos`. A diferencia de otros DAOs, usa `Statement.RETURN_GENERATED_KEYS` para recuperar el ID autogenerado:

```java
ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
ps.executeUpdate();
keys = ps.getGeneratedKeys();
if (keys.next()) {
    res = keys.getInt(1); // ID generado por la BD
}
```

Devuelve el `idTransaccion` generado, que luego se pasa al `Alquiler` como referencia. Si falla devuelve `-1`, que el controlador usa para detectar el error.

---

## 11. Capa View: estructura general

Todos los paneles siguen el mismo patrón:

1. **Campos privados**: componentes Swing declarados como atributos de la clase.
2. **Constructor**: configura el layout, el fondo y llama a `initComponents()`.
3. **`initComponents()`**: coordina la construcción llamando a métodos `buildXxx()`.
4. **Métodos `buildXxx()` privados**: cada uno construye una sección del panel y devuelve el componente.
5. **Métodos públicos para el controlador**: getters para leer datos, métodos para cargar/actualizar la vista.
6. **`setControlador(Controlador)`**: registra el controlador como listener de todos los botones y combos del panel.

### Layouts usados

- `BorderLayout`: panel principal de cada pantalla. Divide en NORTH (título/filtros), CENTER (tabla/formulario), SOUTH (botones/acciones).
- `GridBagLayout`: formularios con etiquetas y campos alineados en columnas.
- `BoxLayout Y_AXIS`: formularios verticales como login y registro.
- `FlowLayout`: filas de botones.
- `GridLayout`: rejillas de componentes del mismo tamaño (tarjetas de informes, campos del formulario de alquiler).

### Colores constantes

Todos los paneles definen sus colores como constantes estáticas al principio de la clase:

```java
private static final Color COLOR_DARK   = new Color(0x1a1a2e); // azul oscuro header
private static final Color COLOR_FONDO  = new Color(0xF5F5F5); // gris muy claro fondo
private static final Color COLOR_ACENTO = new Color(0xE50914); // rojo RentFlix
```

### DefaultTableModel con isCellEditable sobreescrito

Todas las tablas del proyecto usan `DefaultTableModel` con `isCellEditable` sobreescrito para devolver siempre `false`, impidiendo que el usuario edite las celdas directamente:

```java
modeloTabla = new DefaultTableModel(columnas, 0) {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
};
```

---

## 12. VentanaPrincipal

`JFrame` principal de la aplicación. Contiene:

- Una **TopBar** (panel `BorderLayout.NORTH`) con el logo y los botones de sesión.
- Un `panelContenido` (`BorderLayout.CENTER`) donde se cargan dinámicamente todos los paneles.

### Navegación entre paneles

```java
public void cargarPanel(JPanel panel) {
    panelContenido.removeAll();               // eliminar panel actual
    panelContenido.add(panel, BorderLayout.CENTER);
    panelContenido.revalidate(); // recalcular layout
    panelContenido.repaint();    // redibujar
}
```

`revalidate()` es necesario para que el `LayoutManager` recalcule las posiciones tras añadir componentes a un contenedor ya visible. `repaint()` fuerza el redibujado visual. Sin estas dos llamadas, el nuevo panel podría no verse o verse mal.

### Control de visibilidad de botones

```java
public void modoInvitado() {
    btnLogin.setVisible(true);
    btnRegistro.setVisible(true);
    btnCerrarSesion.setVisible(false);
}

public void modoSesionActiva() {
    btnLogin.setVisible(false);
    btnRegistro.setVisible(false);
    btnCerrarSesion.setVisible(true);
}
```

El controlador llama a estos métodos al cambiar el estado de sesión.

---

## 13. PanelLogin

Formulario de inicio de sesión con `BoxLayout Y_AXIS` dentro de una tarjeta centrada con `GridBagLayout`.

### BoxLayout y AlignmentX

Con `BoxLayout Y_AXIS` todos los componentes deben tener el mismo `AlignmentX` para que se alineen correctamente. Si se mezclan componentes con `LEFT_ALIGNMENT` y `CENTER_ALIGNMENT`, Swing los descoloca. En este panel todos tienen `CENTER_ALIGNMENT` para que queden centrados en la tarjeta:

```java
lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
txtUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);
// etc.
```

Para que el texto de los labels quede centrado dentro del propio label, se añade también:

```java
lbl.setHorizontalAlignment(SwingConstants.CENTER);
```

### Selector de rol

Un `JComboBox` con "Cliente" y "Empleado / Administrador". El método `esRolEmpleado()` devuelve `true` si el índice seleccionado es 1. El controlador lo usa para decidir a qué tabla hacer login.

### PlaceholderText

```java
campo.putClientProperty("JTextField.placeholderText", "Usuario");
```

Es una propiedad de FlatLaf que muestra texto gris de placeholder en campos vacíos, como en HTML. Solo funciona con FlatLaf.

---

## 14. PanelRegistro

Mismo patrón que `PanelLogin` con `BoxLayout Y_AXIS`. Añade los campos nombre, apellido, email, usuario y contraseña.

**`datosValidos()`**: comprueba que ningún campo obligatorio esté vacío:

```java
public boolean datosValidos() {
    return !txtNombre.getText().trim().isEmpty()
        && !txtApellido.getText().trim().isEmpty()
        && !txtEmail.getText().trim().isEmpty()
        && !txtUsuario.getText().trim().isEmpty()
        && txtContrasenia.getPassword().length > 0;
}
```

Se usa `.trim()` para no considerar como válidos campos con solo espacios. Para la contraseña se usa `.getPassword().length > 0` porque `JPasswordField` no tiene `getText()` accesible directamente (devuelve array de chars por seguridad).

---

## 15. PanelCatalogo

Panel reutilizado con un parámetro constructor:

```java
public PanelCatalogo(boolean mostrarId)
```

Cuando `mostrarId = true` (empleado) muestra una columna extra "ID Copia" al principio. Cuando `mostrarId = false` (cliente e invitado) no la muestra. Esto permite reusar el mismo panel con comportamiento diferente.

### cargarCopias()

Recibe dos listas: `ArrayList<Pelicula>` y `ArrayList<Copia>`. Para cada copia, busca su película correspondiente con un bucle `for`:

```java
for (Copia copia : copias) {
    Pelicula pelicula = null;
    for (Pelicula p : peliculas) {
        if (p.getId() == copia.getIdPelicula()) {
            pelicula = p;
            break; // encontrada, salir del bucle interno
        }
    }
    if (pelicula == null) continue; // copia sin película, saltar
    // añadir fila a la tabla
}
```

El catálogo muestra una fila por **copia**, no por película. Si una película tiene tres copias en distintos formatos, aparecerá tres veces.

### Leer del modelo, no de la vista

El controlador lee los datos de la fila seleccionada del modelo directamente:

```java
String titulo  = (String) catCliente.getModelo().getValueAt(fila, 0);
String formato = (String) catCliente.getModelo().getValueAt(fila, 5);
```

Esto es importante: `getModel().getValueAt()` usa índices del **modelo de datos**, que son siempre estables independientemente de si el usuario ha reordenado columnas visualmente. Si se usara `tabla.getValueAt()`, los índices serían visuales y cambiarían al reordenar.

### getFiltroFormato()

```java
public String getFiltroFormato() {
    String sel = (String) cmbFiltroFormato.getSelectedItem();
    if ("Todos".equals(sel)) return null;
    return sel;
}
```

Devuelve `null` cuando se selecciona "Todos". El controlador usa `null` como señal para listar todas las copias sin filtrar.

---

## 16. PanelCliente

Panel contenedor con `JTabbedPane` que agrupa tres pestañas: Catálogo, Mis alquileres y Mi cuenta.

Recibe los tres paneles hijos por constructor:

```java
public PanelCliente(PanelCatalogo panelCatalogo,
                    PanelMisAlquileres panelMisAlquileres,
                    PanelMiCuenta panelMiCuenta)
```

### Header con contador de alquileres activos

El header tiene un `JLabel` que muestra cuántos alquileres activos tiene el cliente:

```java
public void actualizarContadorActivos(int cantidad) {
    if (cantidad > 0) {
        lblContadorActivos.setText("🎬 " + cantidad +
            (cantidad == 1 ? " alquiler activo" : " alquileres activos"));
        lblContadorActivos.setVisible(true);
    } else {
        lblContadorActivos.setVisible(false);
    }
}
```

El operador ternario `(cantidad == 1 ? ... : ...)` gestiona el singular/plural. El label se oculta completamente cuando hay 0 alquileres activos.

### irAMisAlquileres()

```java
public void irAMisAlquileres() {
    tabbedPane.setSelectedIndex(1);
}
```

El controlador lo llama después de registrar un alquiler para llevar al cliente directamente a ver su nuevo alquiler.

---

## 17. PanelMisAlquileres

Tabla de alquileres del cliente con filtro por estado y botón de solicitar devolución.

### Lista paralela de IDs

El cliente no debe ver el ID del alquiler, pero el controlador lo necesita para operar. La solución es una `ArrayList<Integer>` paralela al modelo de la tabla:

```java
private ArrayList<Integer> listaIds = new ArrayList<Integer>();

public void cargarAlquileres(ArrayList<Alquiler> alquileres) {
    modeloTabla.setRowCount(0);
    listaIds.clear();
    for (Alquiler a : alquileres) {
        listaIds.add(a.getIdAlquiler()); // guardar ID en la lista paralela
        modeloTabla.addRow(new Object[]{
            a.getNombrePelicula(), // sin ID en la tabla visual
            // ...
        });
    }
}

public int getIdAlquilerSeleccionado() {
    int fila = tblAlquileres.getSelectedRow();
    if (fila < 0) return -1;
    return listaIds.get(fila); // recuperar por índice
}
```

Las dos listas (modelo de tabla y `listaIds`) siempre están sincronizadas porque se limpian y rellenan juntas en `cargarAlquileres()`.

### Habilitar botón solo para alquileres activos

```java
public void actualizarBotonDevolucion() {
    int fila = tblAlquileres.getSelectedRow();
    if (fila >= 0) {
        String estado = String.valueOf(modeloTabla.getValueAt(fila, 3));
        btnSolicitarDevolucion.setEnabled("activo".equalsIgnoreCase(estado));
    } else {
        btnSolicitarDevolucion.setEnabled(false);
    }
}
```

El botón solo se activa cuando la fila seleccionada tiene estado `activo`. Se usa `equalsIgnoreCase` para no depender de mayúsculas.

### Filtro por estado

El `JComboBox` de filtro tiene `actionCommand = "FILTRAR_MIS_ALQUILERES"`. El controlador recibe el evento, lee el filtro con `getFiltroEstado()` y recarga la tabla con solo los alquileres del estado seleccionado.

---

## 18. PanelMiCuenta

Panel que muestra los datos del cliente logueado con un diseño de tarjeta centrada.

### GridBagLayout para centrar la tarjeta

```java
setLayout(new GridBagLayout());

GridBagConstraints gbc = new GridBagConstraints();
gbc.fill    = GridBagConstraints.HORIZONTAL;
gbc.weightx = 0.6;
gbc.insets  = new Insets(40, 0, 40, 0);
add(buildTarjeta(), gbc);
```

`GridBagLayout` sin especificar `gridx/gridy` coloca el único componente en el centro. `weightx = 0.6` hace que ocupe el 60% del ancho.

### Filas de datos con buildFila()

Cada dato del cliente (nombre, apellido, email, usuario, contraseña) se muestra como una fila construida por `buildFila()`:

```java
private JPanel buildFila(String etiqueta, JLabel lblValor) {
    JPanel fila = new JPanel(new BorderLayout());
    fila.setBackground(new Color(0xF8F8F8));
    fila.setBorder(/* borde redondeado */);
    // etiqueta a la izquierda, valor en el centro
    fila.add(lbl,      BorderLayout.WEST);
    fila.add(lblValor, BorderLayout.CENTER);
    return fila;
}
```

La contraseña siempre muestra `"••••••••"` fija, nunca el valor real.

### cargarDatos()

```java
public void cargarDatos(Cliente cliente) {
    lblNombre.setText(cliente.getNombreCliente());
    lblApellido.setText(cliente.getApellidoCliente());
    lblEmail.setText(cliente.getEmailCliente());
    lblUsuario.setText(cliente.getNombreUsuario());
    revalidate();
    repaint();
}
```

Se llama `revalidate()` y `repaint()` porque el panel puede ya estar visible cuando se actualizan los datos (tras modificar datos desde el formulario).

---

## 19. PanelEmpleado y PanelAdmin

Son paneles contenedores con `JTabbedPane`. Reciben sus subpaneles exclusivos por constructor.

**PanelEmpleado** tiene pestañas: Alquileres, Añadir película, Gestión películas, Informes, Clientes.

**PanelAdmin** tiene las mismas más: Empleados.

La única diferencia entre empleado y admin es la pestaña de gestión de empleados, que solo tiene el admin. Ambos pueden gestionar clientes, pero con paneles independientes (`gestionClientesEmp` y `gestionClientesAdm`).

El badge del header es amarillo dorado para admin y amarillo para empleado, para distinguirlos visualmente.

---

## 20. PanelGestionAlquileres

Panel con tabla de alquileres, filtro por estado, botón de aceptar devolución y formulario inline para crear nuevos alquileres.

### Cell Renderers con colores por estado

La columna "Estado" (col 6) tiene un renderer personalizado que pinta el fondo y el texto con colores distintos según el valor:

```java
case "activo":             fondo verde claro,  texto verde
case "pendiente_devolucion": fondo rojo claro,  texto rojo oscuro
case "devuelto":           fondo gris claro,  texto gris
case "vencido":            fondo rojo muy claro, texto rojo
```

### Colorear la fila completa

Para que toda la fila se pinte de rojo cuando el estado es `pendiente_devolucion`, se aplica un renderer a cada columna que llama al método `aplicarColorFila()`:

```java
private void aplicarColorFila(JTable table, Component c, int row, boolean isSelected) {
    if (!isSelected) {
        String estado = String.valueOf(table.getModel().getValueAt(row, 6)).toLowerCase();
        if ("pendiente_devolucion".equals(estado)) {
            c.setBackground(new Color(0xFFCDD2));
            c.setForeground(new Color(0xB71C1C));
        } else {
            c.setBackground(table.getBackground());
            c.setForeground(table.getForeground());
        }
    }
}
```

Se lee siempre del `getModel()` (índice del modelo) para que funcione aunque se reordenen columnas visualmente.

### Formulario inline de nuevo alquiler

El formulario de nuevo alquiler (`panelFormAlquiler`) está siempre construido pero empieza oculto con `setVisible(false)`. Al pulsar "Nuevo alquiler" se hace visible con `mostrarFormAlquiler(true)`:

```java
public void mostrarFormAlquiler(boolean visible) {
    panelFormAlquiler.setVisible(visible);
    panelFormAlquiler.revalidate();
    panelFormAlquiler.repaint();
}
```

Los combos de clientes y películas se rellenan dinámicamente desde el controlador con `cargarComboClientes()` y `cargarComboPeliculas()` cada vez que se abre el formulario, para asegurar datos actualizados.

### Leer el cliente/película seleccionada por índice

El formulario de alquiler usa combos para cliente y película. El controlador los rellena con listas cacheadas (`listaClientesCache`, `listaPeliculasCache`). Al confirmar, recupera el objeto por el índice seleccionado del combo:

```java
int idxCliente  = panel.getIndexClienteSeleccionado();  // índice del combo
Cliente cliente = listaClientesCache.get(idxCliente);   // objeto real de la lista
```

---

## 21. PanelAnadirPelicula

Formulario con `GridBagLayout` de dos columnas (etiqueta + campo) para añadir nuevas películas.

### agregarFila()

Método auxiliar que añade una fila de dos campos (etiqueta-campo / etiqueta-campo) al `GridBagLayout`:

```java
private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila,
                          String label1, JComponent comp1,
                          String label2, JComponent comp2) {
    gbc.gridy = fila;
    gbc.weightx = 0; gbc.gridx = 0; panel.add(buildLabel(label1), gbc);
    gbc.weightx = 1; gbc.gridx = 1; panel.add(comp1, gbc);
    gbc.weightx = 0; gbc.gridx = 2; panel.add(buildLabel(label2), gbc);
    gbc.weightx = 1; gbc.gridx = 3; panel.add(comp2, gbc);
}
```

`weightx = 0` en las etiquetas hace que no se expandan. `weightx = 1` en los campos hace que ocupen el espacio disponible.

### Sinopsis ocupa todo el ancho

La sinopsis se añade manualmente con `gridwidth = 3` para que el `JTextArea` ocupe las columnas 1, 2 y 3:

```java
gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 3;
panel.add(scrollSinopsis, gbc);
gbc.gridwidth = 1; // restaurar para siguientes componentes
```

### datosValidos()

Comprueba campos vacíos y que duración y precio sean números válidos:

```java
public boolean datosValidos() {
    if (txtTitulo.getText().trim().isEmpty() || /* otros campos */ ) {
        mostrarMensaje("Rellena todos los campos obligatorios (*).", true);
        return false;
    }
    try {
        Integer.parseInt(txtDuracion.getText().trim());
        Double.parseDouble(txtPrecio.getText().trim());
    } catch (NumberFormatException e) {
        mostrarMensaje("Duración y precio deben ser números.", true);
        return false;
    }
    return true;
}
```

---

## 22. PanelGestionPeliculas

Tabla de películas con botones Editar y Eliminar. Tiene `getPeliculaSeleccionada()` que devuelve un objeto `Pelicula` con los datos de la fila seleccionada, listo para pasarlo al diálogo de edición del controlador.

---

## 23. PanelGestionEmpleados

Tabla de empleados más formulario de creación de empleados en la parte inferior. Los botones Editar y Eliminar están deshabilitados para el administrador (columna "Rol" = "Administrador"), lo que se gestiona en `actualizarBotones()`:

```java
public void actualizarBotones() {
    int fila = tblEmpleados.getSelectedRow();
    if (fila >= 0) {
        String rol = String.valueOf(modeloTabla.getValueAt(fila, 5));
        boolean esAdmin = "Administrador".equals(rol);
        btnEditarEmpleado.setEnabled(!esAdmin);
        btnEliminarEmpleado.setEnabled(!esAdmin);
    }
}
```

`getEmpleadoSeleccionado()` construye un objeto `Empleado` con los datos de la fila, dejando la contraseña vacía (no se muestra en tabla) y `idJefe` como null (no se muestra ni se necesita para editar).

---

## 24. PanelGestionClientes

Igual que `PanelGestionEmpleados` pero para clientes. La columna "Estado" tiene un renderer que pinta el badge verde para `activo` y rojo para `bloqueado`. `getClienteSeleccionado()` construye un `Cliente` con los datos de la fila, dejando la contraseña vacía.

---

## 25. PanelInformes

Panel con cuatro tarjetas de resumen y una tabla de detalle.

### buildTarjeta()

Cada tarjeta es un `JPanel` con `BorderLayout` que muestra un icono a la izquierda y título + valor a la derecha:

```java
private JPanel buildTarjeta(String titulo, JLabel lblValor, String icono,
                              Color colorValor, Color colorFondo) {
    // icono con fondo de color
    // textos: título pequeño gris + valor grande en color
}
```

Las cuatro tarjetas son: Total alquileres, Ingresos totales, Alquileres activos, Pendientes devolución.

### cargarInformes()

Recibe la lista completa y el total de ingresos ya calculado por el controlador. Cuenta activos y pendientes con bucles:

```java
public void cargarInformes(ArrayList<Alquiler> alquileres, double totalIngresos) {
    int activos    = 0;
    int pendientes = 0;
    for (Alquiler a : alquileres) {
        if ("activo".equalsIgnoreCase(a.getEstadoAlquiler()))                activos++;
        if ("pendiente_devolucion".equalsIgnoreCase(a.getEstadoAlquiler())) pendientes++;
    }
    lblTotalAlquileres.setText(String.valueOf(alquileres.size()));
    lblTotalIngresos.setText(String.format("%.2f €", totalIngresos));
    lblAlquileresActivos.setText(String.valueOf(activos));
    lblPendientesDevolucion.setText(String.valueOf(pendientes));
    // rellenar tabla de detalle
}
```

---

## 26. Capa Controller: Controlador.java

El controlador es la clase más grande del proyecto (más de 1000 líneas). Implementa `ActionListener` y es el único punto de recepción de todos los eventos de la aplicación.

### Variables de estado

```java
private Cliente  clienteActivo  = null; // cliente logueado, null si no hay
private Empleado empleadoActivo = null; // empleado logueado, null si no hay
private boolean  esAdmin        = false; // true si el empleado es admin

private ArrayList<Cliente>  listaClientesCache;  // cache para el form de alquiler
private ArrayList<Pelicula> listaPeliculasCache;
```

### actionPerformed: instanceof + switch

```java
@Override
public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();

    if (src instanceof JButton) {
        switch (e.getActionCommand()) {
            case "LOGIN":             procesarLogin();    break;
            case "CERRAR_SESION":     cerrarSesion();     break;
            case "ALQUILAR_PELICULA": alquilarDesdeClienteCatalogo(); break;
            // ...
        }
    } else if (src instanceof JComboBox) {
        switch (e.getActionCommand()) {
            case "FILTRAR_FORMATO_CATALOGO": filtrarFormato(e); break;
            case "FILTRAR_ALQUILERES":       filtrarAlquileres(); break;
            case "FILTRAR_MIS_ALQUILERES":   filtrarMisAlquileres(); break;
        }
    }
}
```

`e.getSource()` devuelve el componente que disparó el evento. `instanceof` separa por tipo. `e.getActionCommand()` devuelve el string asignado con `setActionCommand()` en cada componente de la vista.

### Helpers para obtener el panel activo

```java
private PanelGestionAlquileres getPanelGestionActivo() {
    if (esAdmin) return gestionAlqAdm;
    else         return gestionAlqEmp;
}
```

Estos helpers evitan duplicar código. Muchas operaciones (filtrar, aceptar devolución, abrir form de alquiler) actúan sobre el panel del rol activo sin necesidad de saber cuál es.

### iniciarModoInvitado()

```java
private void iniciarModoInvitado() {
    clienteActivo  = null;
    empleadoActivo = null;
    esAdmin        = false;
    // recargar catálogo invitado
    catInvitado.habilitarAcciones(true); // botón alquilar visible
    ventana.modoInvitado();
    ventana.cargarPanel(catInvitado);
}
```

Se llama al arrancar la app y al cerrar sesión. Resetea toda la sesión.

### procesarLogin()

1. Lee usuario y contraseña del `PanelLogin`.
2. Según el rol seleccionado en el combo, llama a `empleadoDAO.login()` o `clienteDAO.login()`.
3. Si es cliente, comprueba que no esté bloqueado antes de permitir el acceso.
4. Si es empleado, comprueba `esAdministrador()` para decidir qué panel cargar.

### registrarAlquiler()

Método compartido por el alquiler del cliente y del empleado/admin:

1. Registrar el pago con `pagoDAO.registrar()` → obtener `idTransaccion`.
2. Calcular fechas con `java.time.LocalDate.now().plusDays(dias)`.
3. Crear el alquiler con `alquilerDAO.crear()`.
4. Marcar la copia como `'alquilada'` con `copiaDAO.actualizarEstado()`.
5. Recargar las vistas correspondientes según el rol.

```java
java.time.LocalDate hoy        = java.time.LocalDate.now();
java.time.LocalDate devolucion = hoy.plusDays(dias);
```

`java.time.LocalDate` es la API moderna de fechas de Java 8. `.toString()` devuelve el formato `"YYYY-MM-DD"` que SQLite entiende directamente.

### actualizarContadorActivosCliente()

```java
private void actualizarContadorActivosCliente() {
    ArrayList<Alquiler> alquileres = alquilerDAO
        .listarPorCliente(clienteActivo.getIdCliente());
    int activos = 0;
    for (Alquiler a : alquileres) {
        if ("activo".equalsIgnoreCase(a.getEstadoAlquiler())) activos++;
    }
    panelCliente.actualizarContadorActivos(activos);
}
```

Se llama en tres momentos: al cargar el panel del cliente, tras registrar un alquiler y tras solicitar una devolución. Así el contador siempre está actualizado sin reiniciar sesión.

### modificarDatosCliente()

El formulario de modificación de datos incluye campos opcionales de contraseña. La lógica es:

1. Si el cliente deja los tres campos de contraseña vacíos → no se cambia la contraseña.
2. Si rellena alguno → se valida que la contraseña actual sea correcta, que la nueva no esté vacía y que la nueva coincida con la repetición.
3. Se llama a `clienteDAO.actualizarDatos(clienteActivo, contraFinal)` con la contraseña final (la nueva si la cambió, la misma si no).

---

## 27. Flujos completos paso a paso

### Flujo de alquiler por el cliente

1. El cliente selecciona una fila en `catCliente` y pulsa "Alquilar película".
2. `actionPerformed` recibe `ALQUILAR_PELICULA` → llama `alquilarDesdeClienteCatalogo()`.
3. Se verifica que `clienteActivo != null`. Si es null se muestra diálogo para ir al login.
4. Se lee la fila seleccionada con `catCliente.getFilaSeleccionada()`.
5. Se leen título (col 0) y formato (col 5) del modelo con `getModelo().getValueAt()`.
6. Se busca la película por título con `peliculaDAO.buscarPorTitulo()`.
7. Se buscan copias disponibles en ese formato con `copiaDAO.listarDisponiblesPorPelicula()` + filtro por formato.
8. Se muestra `mostrarDialogoAlquilerCliente()` con los datos y campos para elegir días y método de pago.
9. Al confirmar → `registrarAlquiler()`: pago → alquiler → marcar copia → recargar vistas → actualizar contador.

### Flujo de devolución

1. El cliente selecciona un alquiler activo en `PanelMisAlquileres` y pulsa "Solicitar devolución".
2. `SOLICITAR_DEVOLUCION` → `procesarSolicitudDevolucion()`.
3. Se obtiene el ID con `misAlquileres.getIdAlquilerSeleccionado()` (que usa la lista paralela).
4. `alquilerDAO.solicitarDevolucion(id)` → cambia estado a `'pendiente_devolucion'`.
5. Se recarga la tabla y se actualiza el contador.

6. El empleado ve la fila en rojo en `PanelGestionAlquileres`, la selecciona y pulsa "Aceptar devolución".
7. `ACEPTAR_DEVOLUCION` → `procesarAceptarDevolucion()`.
8. `alquilerDAO.aceptarDevolucion(id, fecha)` → cambia estado a `'devuelto'` y registra la fecha real.
9. La copia **no** se marca automáticamente como disponible en este flujo (posible mejora futura).

### Flujo de marcado de vencidos

Al iniciar sesión cualquier usuario, `cargarPanelXxx()` llama primero a `alquilerDAO.marcarVencidos()` antes de cargar los datos. Esto actualiza en una sola query todos los alquileres cuya fecha ha pasado, por lo que cuando se carga la tabla ya están con el estado correcto.

---

## 28. Decisiones de diseño importantes

### Por qué no se usa programación funcional

El proyecto no usa streams, lambdas ni referencias a métodos (salvo el `invokeLater` del Main que es obligatorio). Todos los bucles son `for` tradicionales y todos los listeners son clases anónimas con `@Override`. Esto hace el código más explícito y comprensible para alguien que está aprendiendo.

### Por qué los listeners están en setControlador() de cada panel

Centralizar el registro de listeners en `setControlador()` tiene varias ventajas: el panel sabe exactamente qué componentes tiene, el controlador no necesita conocer los detalles internos de cada panel, y el `Main` solo tiene que llamar a `setControlador()` en cada panel una sola vez.

### Por qué se usa JOptionPane para los formularios de editar

Los formularios de editar película, cliente y empleado se implementan como `JOptionPane.showConfirmDialog()` con un `JPanel` personalizado como contenido. Esto es más simple que crear paneles dedicados y funciona correctamente para operaciones de edición puntuales. Para operaciones frecuentes (como el formulario de alquiler en el panel de gestión) sí se usa un panel inline.

### Por qué el catálogo muestra copias y no películas

El modelo de negocio distingue entre `Pelicula` (la entidad abstracta) y `Copia` (el ejemplar físico). Solo se pueden alquilar copias. Si hay tres copias de "Alien" (dos en DVD y una en Blu-ray), el catálogo muestra tres filas. Cuando se alquila una, esa copia específica pasa a estado `'alquilada'` y desaparece del catálogo, pero las otras dos siguen disponibles.

---

## 29. Preguntas frecuentes de entrega

**¿Qué es el patrón MVC y cómo se aplica aquí?**
MVC separa la aplicación en tres capas: Model (datos), View (interfaz) y Controller (lógica). En RentFlix los modelos son los POJOs, las vistas son los paneles Swing, y el controlador es la clase `Controlador`. La vista nunca habla directamente con el modelo, siempre pasa por el controlador.

**¿Qué es el patrón DAO y por qué se usa?**
DAO (Data Access Object) encapsula todo el acceso a la BD en clases específicas. El controlador nunca escribe SQL directamente, solo llama a métodos como `clienteDAO.login()`. Si se cambia la BD, solo cambian los DAOs.

**¿Para qué sirven las interfaces en los DAOs?**
`IClienteDAO`, `IEmpleadoDAO` etc. definen el contrato de lo que puede hacer cada DAO. El controlador depende de la interfaz, no de la implementación concreta. Esto permite cambiar la implementación sin tocar el controlador.

**¿Por qué se usa PreparedStatement y no Statement?**
`PreparedStatement` protege contra inyección SQL porque los parámetros se escapan automáticamente. `Statement` solo se usa para queries sin parámetros variables.

**¿Por qué se cierran los recursos en finally y en orden inverso?**
En `finally` para garantizar que se cierran aunque haya excepción. En orden inverso (ResultSet → PreparedStatement → Connection) porque los recursos más dependientes se liberan antes.

**¿Cómo se sabe si un empleado es administrador?**
Por el campo `id_jefe` en la BD. Si es `NULL`, es administrador (`esAdministrador()` devuelve `true`). No hace falta un campo de rol extra.

**¿Por qué hay dos catálogos (catInvitado y catCliente)?**
Porque Swing no puede mostrar el mismo componente en dos contenedores a la vez. `catInvitado` está en la ventana principal y `catCliente` está dentro del `JTabbedPane` del `PanelCliente`. Si fuera el mismo objeto, al añadirlo a uno desaparecería del otro.

**¿Cómo funciona la navegación entre paneles?**
`VentanaPrincipal` tiene un `panelContenido` central. El método `cargarPanel()` elimina el panel actual con `removeAll()` y añade el nuevo, seguido de `revalidate()` y `repaint()` para que Swing redibuje la ventana.

**¿Qué hace getValueIsAdjusting() en el ListSelectionListener?**
`getValueIsAdjusting()` devuelve `true` mientras el usuario está arrastrando el ratón por varias filas. Solo se actúa cuando devuelve `false`, es decir, cuando la selección está completa. Sin esta comprobación el código se ejecutaría múltiples veces durante el arrastre.

**¿Cómo funciona el marcado automático de alquileres vencidos?**
Al iniciar sesión, el controlador llama a `alquilerDAO.marcarVencidos()` que ejecuta un `UPDATE` cambiando a `'vencido'` todos los alquileres `'activo'` cuya `fecha_devolucion_prevista` es anterior a la fecha actual de la BD.

**¿Por qué los métodos DAO devuelven int en vez de boolean?**
`executeUpdate()` de JDBC devuelve el número de filas afectadas. Devolver ese int es más informativo. El controlador comprueba `> 0` para saber si la operación tuvo éxito.

**¿Qué es revalidate() y repaint()?**
`revalidate()` pide al `LayoutManager` que recalcule las posiciones de los componentes. `repaint()` fuerza el redibujado visual. Se usan juntos al añadir/quitar componentes de un panel ya visible, como al mostrar/ocultar el formulario de nuevo alquiler.

**¿Por qué las tablas usan getModel().getValueAt() y no getValueAt()?**
`getModel().getValueAt(fila, col)` usa índices del **modelo de datos**, estables siempre. `getValueAt(fila, col)` de la tabla usa índices **visuales**, que cambian si el usuario reordena columnas arrastrando el header. Para leer datos de forma segura siempre se usa el modelo.

**¿Qué es SwingUtilities.invokeLater?**
Swing no es thread-safe. `invokeLater` encola el código para ejecutarse en el EDT (Event Dispatch Thread), el único hilo donde se pueden crear y modificar componentes Swing de forma segura.
