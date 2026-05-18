DROP DATABASE IF EXISTS Rentflix;
CREATE DATABASE Rentflix;
USE Rentflix;


CREATE TABLE Peliculas (
    id_pelicula        INT AUTO_INCREMENT PRIMARY KEY,
    nombre_pelicula    VARCHAR(100) NOT NULL,
    director           VARCHAR(100) NOT NULL,
    duracion           INT NOT NULL,
    genero             VARCHAR(50) NOT NULL,
    sinopsis           TEXT,
    clasificacion_edad ENUM('TP', '7', '12', '16', '18') NOT NULL
);

CREATE TABLE Actores (
    id_actor     INT AUTO_INCREMENT PRIMARY KEY,
    nombre_actor VARCHAR(100) NOT NULL
);

CREATE TABLE Pelicula_Actor (
    id_pelicula INT NOT NULL,
    id_actor    INT NOT NULL,
    PRIMARY KEY (id_pelicula, id_actor),
    FOREIGN KEY (id_pelicula) REFERENCES Peliculas(id_pelicula),
    FOREIGN KEY (id_actor)    REFERENCES Actores(id_actor)
);

CREATE TABLE Empleados (
    id_empleado          INT AUTO_INCREMENT PRIMARY KEY,
    nombre_empleado      VARCHAR(100) NOT NULL,
    apellido_empleado    VARCHAR(100) NOT NULL,
    email_empleado       VARCHAR(100) NOT NULL,
    usuario_empleado     VARCHAR(50) UNIQUE NOT NULL,
    contrasenia_empleado VARCHAR(255) NOT NULL,
    id_jefe              INT,
    FOREIGN KEY (id_jefe) REFERENCES Empleados(id_empleado)
);


CREATE TABLE Clientes (
    id_cliente          INT AUTO_INCREMENT PRIMARY KEY,
    nombre_cliente      VARCHAR(100) NOT NULL,
    apellido_cliente    VARCHAR(100) NOT NULL,
    email_cliente      VARCHAR(100) NOT NULL,
    nombre_usuario      VARCHAR(50) UNIQUE NOT NULL,
    contrasenia_cliente VARCHAR(255) NOT NULL
);


CREATE TABLE Copias (
    id_copia        INT AUTO_INCREMENT PRIMARY KEY,
    id_pelicula     INT NOT NULL,
    formato 		ENUM('DVD', 'Blu-ray', '4K Ultra HD') NOT NULL,
    estado          ENUM('disponible', 'alquilada', 'dañada') NOT NULL DEFAULT 'disponible',
    precio_alquiler DECIMAL(5,2) NOT NULL,
    FOREIGN KEY (id_pelicula) REFERENCES Peliculas(id_pelicula)
);

CREATE TABLE Pagos (
    id_transaccion  INT AUTO_INCREMENT PRIMARY KEY,
    metodo_pago     ENUM('efectivo', 'tarjeta', 'transferencia') NOT NULL,
    monto_cobro     DECIMAL(5,2) NOT NULL
);


CREATE TABLE Alquileres (
    id_alquiler               INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente                INT NOT NULL,
    id_copia                  INT NOT NULL,
    id_empleado               INT,
    id_transaccion            INT,
    fecha_alquiler            DATE NOT NULL,
    fecha_devolucion_prevista DATE NOT NULL,
    fecha_devolucion_real     DATE,
    estado_alquiler           ENUM('activo', 'devuelto', 'vencido') NOT NULL DEFAULT 'activo',
    FOREIGN KEY (id_cliente)     REFERENCES Clientes(id_cliente),
    FOREIGN KEY (id_copia)       REFERENCES Copias(id_copia),
    FOREIGN KEY (id_empleado)    REFERENCES Empleados(id_empleado),
    FOREIGN KEY (id_transaccion) REFERENCES Pagos(id_transaccion)
);
