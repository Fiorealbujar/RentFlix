DROP DATABASE IF EXISTS Rentflix;
CREATE DATABASE Rentflix;
USE Rentflix;


CREATE TABLE Peliculas (
    id_pelicula        INT AUTO_INCREMENT,
    nombre_pelicula    VARCHAR(100) NOT NULL,
    director           VARCHAR(100) NOT NULL,
    duracion           INT NOT NULL,
    genero             VARCHAR(50) NOT NULL,
    sinopsis           TEXT,
    clasificacion_edad TEXT NOT NULL,
    CONSTRAINT ck_clas_edad CHECK (clasificacion_edad IN ('TP', '7', '12', '16', '18')),
    CONSTRAINT pk_id_pel PRIMARY KEY (id_pelicula)
);

CREATE TABLE Actores (
    id_actor     INT AUTO_INCREMENT,
    nombre_actor VARCHAR(100) NOT NULL,
    CONSTRAINT pk_id_act PRIMARY KEY (id_actor)
);

CREATE TABLE Pelicula_Actor (
    id_pelicula INT NOT NULL,
    id_actor    INT NOT NULL,
    CONSTRAINT pk_pel_act PRIMARY KEY (id_pelicula, id_actor),
    CONSTRAINT fk_id_pel  FOREIGN KEY (id_pelicula) REFERENCES Peliculas(id_pelicula),
    CONSTRAINT fk_id_act  FOREIGN KEY (id_actor)    REFERENCES Actores(id_actor)
);

CREATE TABLE Empleados (
    id_empleado          INT AUTO_INCREMENT,
    nombre_empleado      VARCHAR(100) NOT NULL,
    apellido_empleado    VARCHAR(100) NOT NULL,
    email_empleado       VARCHAR(100) NOT NULL,
    usuario_empleado     VARCHAR(50) UNIQUE NOT NULL,
    contrasenia_empleado VARCHAR(255) NOT NULL,
    id_jefe              INT,
    CONSTRAINT fk_id_jefe FOREIGN KEY (id_jefe) REFERENCES Empleados(id_empleado),
    CONSTRAINT pk_id_emp  PRIMARY KEY (id_empleado)
);

CREATE TABLE Clientes (
    id_cliente          INT AUTO_INCREMENT,
    nombre_cliente      VARCHAR(100) NOT NULL,
    apellido_cliente    VARCHAR(100) NOT NULL,
    email_cliente       VARCHAR(100) NOT NULL,
    nombre_usuario      VARCHAR(50) UNIQUE NOT NULL,
    contrasenia_cliente VARCHAR(255) NOT NULL,
    CONSTRAINT pk_id_cli PRIMARY KEY (id_cliente)
);

CREATE TABLE Copias (
    id_copia        INT AUTO_INCREMENT,
    id_pelicula     INT NOT NULL,
    formato         TEXT NOT NULL,
    estado          TEXT NOT NULL DEFAULT 'disponible',
    precio_alquiler DECIMAL(5,2) NOT NULL,
    CONSTRAINT ck_formato CHECK (formato IN ('DVD', 'Blu-ray', '4K Ultra HD')),
    CONSTRAINT ck_estado CHECK (estado IN ('disponible', 'alquilada', 'dañada')),
    CONSTRAINT pk_id_cop  PRIMARY KEY (id_copia),
    CONSTRAINT fk_id_pel  FOREIGN KEY (id_pelicula) REFERENCES Peliculas(id_pelicula)
);

CREATE TABLE Pagos (
    id_transaccion INT AUTO_INCREMENT,
    metodo_pago    TEXT NOT NULL,
    monto_cobro    DECIMAL(5,2) NOT NULL,
    CONSTRAINT ck_met_pago CHECK (metodo_pago IN ('efectivo', 'tarjeta', 'transferencia')),
    CONSTRAINT pk_id_trans PRIMARY KEY (id_transaccion)
);

CREATE TABLE Alquileres (
    id_alquiler               INT AUTO_INCREMENT,
    id_cliente                INT NOT NULL,
    id_copia                  INT NOT NULL,
    id_empleado               INT,
    id_transaccion            INT,
    fecha_alquiler            DATE NOT NULL,
    fecha_devolucion_prevista DATE NOT NULL,
    fecha_devolucion_real     DATE,
    estado_alquiler           TEXT NOT NULL DEFAULT 'activo',
    CONSTRAINT ck_est_alq  CHECK (estado_alquiler IN ('activo', 'devuelto', 'vencido')),
    CONSTRAINT pk_id_alq   PRIMARY KEY (id_alquiler),
    CONSTRAINT fk_id_cli   FOREIGN KEY (id_cliente)      REFERENCES Clientes(id_cliente),
    CONSTRAINT fk_id_cop   FOREIGN KEY (id_copia)        REFERENCES Copias(id_copia),
    CONSTRAINT fk_id_emp   FOREIGN KEY (id_empleado)     REFERENCES Empleados(id_empleado),
    CONSTRAINT fk_id_trans FOREIGN KEY (id_transaccion)  REFERENCES Pagos(id_transaccion)
);

