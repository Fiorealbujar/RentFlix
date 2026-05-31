-- =============================================
-- RENTFLIX - Script de creación de tablas
-- Base de datos: SQLite
-- =============================================

CREATE TABLE Peliculas (
    id_pelicula        INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_pelicula    TEXT NOT NULL,
    director           TEXT NOT NULL,
    duracion           INTEGER NOT NULL,
    genero             TEXT NOT NULL,
    sinopsis           TEXT,
    clasificacion_edad TEXT NOT NULL,
    CONSTRAINT ck_clas_edad CHECK (clasificacion_edad IN ('TP', '7', '12', '16', '18'))
);

CREATE TABLE Actores (
    id_actor     INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_actor TEXT NOT NULL
);

CREATE TABLE Pelicula_Actor (
    id_pelicula INTEGER NOT NULL,
    id_actor    INTEGER NOT NULL,
    CONSTRAINT pk_pel_act PRIMARY KEY (id_pelicula, id_actor),
    CONSTRAINT fk_id_pel  FOREIGN KEY (id_pelicula) REFERENCES Peliculas(id_pelicula),
    CONSTRAINT fk_id_act  FOREIGN KEY (id_actor)    REFERENCES Actores(id_actor)
);

CREATE TABLE Empleados (
    -- El rol se determina por id_jefe:
    -- id_jefe = NULL       → Administrador/Jefe (tiene control total del sistema)
    -- id_jefe IS NOT NULL  → Empleado normal (reporta a un jefe)
    -- El método esJefe() de la clase Empleado implementa esta lógica en Java
    id_empleado          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_empleado      TEXT NOT NULL,
    apellido_empleado    TEXT NOT NULL,
    email_empleado       TEXT NOT NULL,
    usuario_empleado     TEXT UNIQUE NOT NULL,
    contrasenia_empleado TEXT NOT NULL,
    id_jefe              INTEGER,
    CONSTRAINT fk_id_jefe FOREIGN KEY (id_jefe) REFERENCES Empleados(id_empleado)
);

CREATE TABLE Clientes (
    id_cliente          INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_cliente      TEXT NOT NULL,
    apellido_cliente    TEXT NOT NULL,
    email_cliente       TEXT NOT NULL,
    nombre_usuario      TEXT UNIQUE NOT NULL,
    contrasenia_cliente TEXT NOT NULL,
    estado              TEXT NOT NULL DEFAULT 'activo',
    -- 'activo'    → cliente puede operar con normalidad
    -- 'bloqueado' → cliente no puede iniciar sesión
    CONSTRAINT ck_estado_cli CHECK (estado IN ('activo', 'bloqueado'))
);

CREATE TABLE Copias (
    id_copia        INTEGER PRIMARY KEY AUTOINCREMENT,
    id_pelicula     INTEGER NOT NULL,
    formato         TEXT NOT NULL,
    estado          TEXT NOT NULL DEFAULT 'disponible',
    precio_alquiler REAL NOT NULL,
    CONSTRAINT ck_formato CHECK (formato IN ('DVD', 'Blu-ray', '4K Ultra HD')),
    CONSTRAINT ck_estado   CHECK (estado IN ('disponible', 'alquilada', 'dañada')),
    CONSTRAINT fk_id_pel   FOREIGN KEY (id_pelicula) REFERENCES Peliculas(id_pelicula)
);

CREATE TABLE Pagos (
    id_transaccion INTEGER PRIMARY KEY AUTOINCREMENT,
    metodo_pago    TEXT NOT NULL,
    monto_cobro    REAL NOT NULL,
    CONSTRAINT ck_met_pago CHECK (metodo_pago IN ('efectivo', 'tarjeta', 'transferencia'))
);

CREATE TABLE Alquileres (
    id_alquiler               INTEGER PRIMARY KEY AUTOINCREMENT,
    id_cliente                INTEGER NOT NULL,
    id_copia                  INTEGER NOT NULL,
    id_empleado               INTEGER,
    id_transaccion            INTEGER,
    fecha_alquiler            DATE NOT NULL,
    fecha_devolucion_prevista DATE NOT NULL,
    fecha_devolucion_real     DATE,
    estado_alquiler           TEXT NOT NULL DEFAULT 'activo',
    -- 'activo'               → alquiler en curso
    -- 'devuelto'             → devuelto correctamente
    -- 'vencido'              → plazo superado sin devolver
    -- 'pendiente_devolucion' → cliente ha solicitado devolución, pendiente de confirmación del empleado
    CONSTRAINT ck_est_alq  CHECK (estado_alquiler IN ('activo', 'devuelto', 'vencido', 'pendiente_devolucion')),
    CONSTRAINT fk_id_cli   FOREIGN KEY (id_cliente)     REFERENCES Clientes(id_cliente),
    CONSTRAINT fk_id_cop   FOREIGN KEY (id_copia)       REFERENCES Copias(id_copia),
    CONSTRAINT fk_id_emp   FOREIGN KEY (id_empleado)    REFERENCES Empleados(id_empleado),
    CONSTRAINT fk_id_trans FOREIGN KEY (id_transaccion) REFERENCES Pagos(id_transaccion)
);
