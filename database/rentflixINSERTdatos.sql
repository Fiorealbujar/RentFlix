-- =============================================
-- RENTFLIX - Carga inicial de datos
-- =============================================

-- EMPLEADOS
-- El rol se determina por id_jefe:
-- id_jefe = NULL       → Administrador/Jefe (tiene control total del sistema)
-- id_jefe IS NOT NULL  → Empleado normal (reporta a un jefe)
-- El método esJefe() de la clase Empleado implementa esta lógica en Java
INSERT INTO Empleados (nombre_empleado, apellido_empleado, email_empleado, usuario_empleado, contrasenia_empleado, id_jefe) VALUES
('Carlos',  'García',   'carlos@rentflix.com', 'carlos_admin', '1234', NULL),  -- Administrador/Jefe
('Laura',   'Martínez', 'laura@rentflix.com',  'laura_emp',    '1234', 1),     -- Empleado normal
('Pedro',   'López',    'pedro@rentflix.com',  'pedro_emp',    '1234', 1);     -- Empleado normal

-- CLIENTES
-- estado 'activo'    → puede operar con normalidad
-- estado 'bloqueado' → no puede iniciar sesión (RF15)
INSERT INTO Clientes (nombre_cliente, apellido_cliente, email_cliente, nombre_usuario, contrasenia_cliente, estado) VALUES
('Ana',     'Rueda',     'ana@email.com',     'ana_cli',     '1234', 'activo'),
('Gabriel', 'Fernández', 'gabriel@email.com', 'gabriel_cli', '1234', 'activo'),
('María',   'Sánchez',   'maria@email.com',   'maria_cli',   '1234', 'activo'),
('Jorge',   'Pérez',     'jorge@email.com',   'jorge_cli',   '1234', 'activo'),
('Sofía',   'Rodríguez', 'sofia@email.com',   'sofia_cli',   '1234', 'bloqueado');  -- cliente bloqueado para pruebas

-- PELÍCULAS
INSERT INTO Peliculas (nombre_pelicula, director, duracion, genero, sinopsis, clasificacion_edad) VALUES
('El Padrino',                  'Francis Ford Coppola', 175, 'Drama',          'La historia de la familia mafiosa Corleone.',                                    '18'),
('Tiburón',                     'Steven Spielberg',     124, 'Terror',         'Un tiburón asesino aterroriza una pequeña localidad costera.',                    '12'),
('El Imperio Contraataca',      'Irvin Kershner',       124, 'Ciencia Ficción','La segunda entrega de la saga Star Wars.',                                        'TP'),
('Blade Runner',                'Ridley Scott',         117, 'Ciencia Ficción','Un cazador de replicantes persigue a androides en un futuro distópico.',          '16'),
('Alien',                       'Ridley Scott',         117, 'Terror',         'La tripulación de una nave espacial se enfrenta a un alien mortal.',              '16'),
('El Resplandor',               'Stanley Kubrick',      146, 'Terror',         'Un escritor y su familia pasan el invierno en un hotel encantado.',               '18'),
('El Silencio de los Corderos', 'Jonathan Demme',       118, 'Thriller',       'Una agente del FBI colabora con un asesino en serie para cazar a otro.',          '18'),
('Pulp Fiction',                'Quentin Tarantino',    154, 'Thriller',       'Historias entrelazadas del hampa de Los Ángeles.',                                '18'),
('El Rey León',                 'Roger Allers',          88, 'Animación',      'Un joven león debe reclamar su lugar como rey.',                                  'TP'),
('Forrest Gump',                'Robert Zemeckis',      142, 'Drama',          'La vida de un hombre sencillo que vive momentos históricos.',                     '12'),
('Titanic',                     'James Cameron',        195, 'Romance',        'Historia de amor a bordo del famoso transatlántico.',                             '12'),
('Matrix',                      'Lana Wachowski',       136, 'Ciencia Ficción','Un hacker descubre que la realidad es una simulación.',                           '16'),
('Braveheart',                  'Mel Gibson',           178, 'Historia',       'William Wallace lidera la rebelión escocesa contra Inglaterra.',                  '18'),
('La Lista de Schindler',       'Steven Spielberg',     195, 'Drama',          'Un empresario salva la vida de miles de judíos durante el Holocausto.',           '16'),
('Jurassic Park',               'Steven Spielberg',     127, 'Aventura',       'Un parque temático con dinosaurios clonados se convierte en pesadilla.',          '12'),
('Gladiator',                   'Ridley Scott',         155, 'Acción',         'Un general romano busca venganza tras ser traicionado por el príncipe.',          '16'),
('El Señor de los Anillos',     'Peter Jackson',        178, 'Fantasía',       'Un hobbit emprende un viaje para destruir un anillo de poder.',                   '12'),
('Inception',                   'Christopher Nolan',    148, 'Ciencia Ficción','Un ladrón roba secretos entrando en los sueños de sus víctimas.',                 '12'),
('Interstellar',                'Christopher Nolan',    169, 'Ciencia Ficción','Astronautas viajan a través de un agujero de gusano en busca de un nuevo hogar.', 'TP'),
('Avatar',                      'James Cameron',        162, 'Ciencia Ficción','Un marine paralítico viaja a un planeta alienígena.',                             '12'),
('El Caballero Oscuro',         'Christopher Nolan',    152, 'Acción',         'Batman se enfrenta al Joker en una batalla por Gotham City.',                    '12'),
('Avengers: Endgame',           'Anthony Russo',        181, 'Acción',         'Los Vengadores luchan para revertir el chasquido de Thanos.',                    '12'),
('Parasite',                    'Bong Joon-ho',         132, 'Drama',          'Una familia pobre se infiltra en el hogar de una familia adinerada.',             '16'),
('Dune',                        'Denis Villeneuve',     155, 'Ciencia Ficción','Un joven noble lidera a su pueblo en un peligroso planeta desértico.',            '12'),
('Top Gun: Maverick',           'Joseph Kosinski',      131, 'Acción',         'Maverick regresa para entrenar a una nueva generación de pilotos.',               '7'),
('Spider-Man: No Way Home',     'Jon Watts',            148, 'Acción',         'Spider-Man pide ayuda a Doctor Strange para borrar su identidad.',               '12'),
('The Batman',                  'Matt Reeves',          176, 'Acción',         'Batman investiga la corrupción en Gotham mientras persigue al Acertijo.',         '16'),
('Oppenheimer',                 'Christopher Nolan',    180, 'Drama',          'La historia del científico que desarrolló la bomba atómica.',                    '16'),
('Barbie',                      'Greta Gerwig',         114, 'Comedia',        'Barbie emprende un viaje al mundo real tras una crisis existencial.',             '7'),
('Dune: Parte Dos',             'Denis Villeneuve',     166, 'Ciencia Ficción','Paul Atreides lidera la guerra contra los que destruyeron su familia.',           '16');

-- ACTORES (sin duplicados)
INSERT INTO Actores (nombre_actor) VALUES
('Marlon Brando'),       -- 1
('Al Pacino'),           -- 2
('Roy Scheider'),        -- 3
('Mark Hamill'),         -- 4
('Harrison Ford'),       -- 5
('Rutger Hauer'),        -- 6
('Sigourney Weaver'),    -- 7
('Jack Nicholson'),      -- 8
('Jodie Foster'),        -- 9
('Anthony Hopkins'),     -- 10
('John Travolta'),       -- 11
('Uma Thurman'),         -- 12
('Matthew Broderick'),   -- 13
('Tom Hanks'),           -- 14
('Leonardo DiCaprio'),   -- 15
('Keanu Reeves'),        -- 16
('Mel Gibson'),          -- 17
('Liam Neeson'),         -- 18
('Sam Neill'),           -- 19
('Russell Crowe'),       -- 20
('Elijah Wood'),         -- 21
('Viggo Mortensen'),     -- 22
('Matthew McConaughey'), -- 23
('Sam Worthington'),     -- 24
('Christian Bale'),      -- 25
('Heath Ledger'),        -- 26
('Robert Downey Jr.'),   -- 27
('Chris Evans'),         -- 28
('Song Kang-ho'),        -- 29
('Timothée Chalamet'),   -- 30
('Tom Cruise'),          -- 31
('Tom Holland'),         -- 32
('Robert Pattinson'),    -- 33
('Cillian Murphy'),      -- 34
('Margot Robbie'),       -- 35
('Zendaya');             -- 36

-- PELICULA_ACTOR
INSERT INTO Pelicula_Actor (id_pelicula, id_actor) VALUES
(1,  1), (1,  2),
(2,  3),
(3,  4), (3,  5),
(4,  6),
(5,  7),
(6,  8),
(7,  9), (7, 10),
(8, 11), (8, 12),
(9, 13),
(10, 14),
(11, 15),
(12, 16),
(13, 17),
(14, 18),
(15, 19),
(16, 20),
(17, 21), (17, 22),
(18, 15),
(19, 23),
(20, 24),
(21, 25), (21, 26),
(22, 27), (22, 28),
(23, 29),
(24, 30),
(25, 31),
(26, 32),
(27, 33),
(28, 34),
(29, 35),
(30, 30), (30, 36);

-- COPIAS
-- NOTA: Los ids se calculan en orden de inserción para garantizar
-- coherencia con los Alquileres que referencian copias 'alquilada'
-- Precios: DVD=2.50€ | Blu-ray=5.00€ | 4K Ultra HD=7.50€
-- Películas 1-6  (antes 1990): solo DVD
-- Películas 7-15 (años 90):    DVD + Blu-ray
-- Películas 16-30 (2000+):     DVD + Blu-ray + 4K Ultra HD

-- Películas 1-6: solo DVD → ids 1-9
INSERT INTO Copias (id_pelicula, formato, estado, precio_alquiler) VALUES
(1, 'DVD', 'disponible', 2.50),  -- id 1
(1, 'DVD', 'disponible', 2.50),  -- id 2
(2, 'DVD', 'disponible', 2.50),  -- id 3
(2, 'DVD', 'disponible', 2.50),  -- id 4
(3, 'DVD', 'disponible', 2.50),  -- id 5
(4, 'DVD', 'disponible', 2.50),  -- id 6
(5, 'DVD', 'disponible', 2.50),  -- id 7
(5, 'DVD', 'disponible', 2.50),  -- id 8
(6, 'DVD', 'disponible', 2.50);  -- id 9

-- Películas 7-15: DVD + Blu-ray → ids 10-30
INSERT INTO Copias (id_pelicula, formato, estado, precio_alquiler) VALUES
(7,  'DVD',     'disponible', 2.50),  -- id 10
(7,  'Blu-ray', 'disponible', 5.00),  -- id 11 → El Silencio Blu-ray (alquiler devuelto de Ana)
(8,  'DVD',     'disponible', 2.50),  -- id 12
(8,  'Blu-ray', 'disponible', 5.00),  -- id 13
(8,  'Blu-ray', 'disponible', 5.00),  -- id 14
(9,  'DVD',     'disponible', 2.50),  -- id 15
(9,  'Blu-ray', 'disponible', 5.00),  -- id 16
(10, 'DVD',     'disponible', 2.50),  -- id 17
(10, 'DVD',     'disponible', 2.50),  -- id 18
(10, 'Blu-ray', 'disponible', 5.00),  -- id 19
(11, 'DVD',     'disponible', 2.50),  -- id 20
(11, 'Blu-ray', 'alquilada',  5.00),  -- id 21 → Titanic Blu-ray (alquiler pendiente_devolucion de Sofía)
(11, 'Blu-ray', 'disponible', 5.00),  -- id 22
(12, 'DVD',     'disponible', 2.50),  -- id 23
(12, 'Blu-ray', 'disponible', 5.00),  -- id 24
(13, 'DVD',     'disponible', 2.50),  -- id 25
(13, 'Blu-ray', 'disponible', 5.00),  -- id 26
(14, 'DVD',     'disponible', 2.50),  -- id 27
(14, 'Blu-ray', 'disponible', 5.00),  -- id 28
(15, 'DVD',     'disponible', 2.50),  -- id 29
(15, 'Blu-ray', 'disponible', 5.00);  -- id 30

-- Películas 16-30: DVD + Blu-ray + 4K → ids 31-75
INSERT INTO Copias (id_pelicula, formato, estado, precio_alquiler) VALUES
(16, 'DVD',        'disponible', 2.50),  -- id 31
(16, 'Blu-ray',    'disponible', 5.00),  -- id 32
(16, '4K Ultra HD','disponible', 7.50),  -- id 33
(17, 'DVD',        'disponible', 2.50),  -- id 34
(17, 'Blu-ray',    'alquilada',  5.00),  -- id 35 → El Señor Blu-ray (alquiler activo de Gabriel)
(17, '4K Ultra HD','disponible', 7.50),  -- id 36
(18, 'DVD',        'disponible', 2.50),  -- id 37
(18, 'Blu-ray',    'disponible', 5.00),  -- id 38
(18, '4K Ultra HD','disponible', 7.50),  -- id 39
(19, 'DVD',        'disponible', 2.50),  -- id 40
(19, 'Blu-ray',    'disponible', 5.00),  -- id 41
(19, '4K Ultra HD','alquilada',  7.50),  -- id 42 → Interstellar 4K (alquiler vencido de María)
(20, 'DVD',        'disponible', 2.50),  -- id 43
(20, 'Blu-ray',    'disponible', 5.00),  -- id 44
(20, '4K Ultra HD','disponible', 7.50),  -- id 45
(21, 'DVD',        'disponible', 2.50),  -- id 46
(21, 'Blu-ray',    'disponible', 5.00),  -- id 47
(21, '4K Ultra HD','disponible', 7.50),  -- id 48
(22, 'DVD',        'disponible', 2.50),  -- id 49
(22, 'Blu-ray',    'disponible', 5.00),  -- id 50
(22, '4K Ultra HD','disponible', 7.50),  -- id 51
(23, 'DVD',        'disponible', 2.50),  -- id 52
(23, 'Blu-ray',    'disponible', 5.00),  -- id 53
(23, '4K Ultra HD','disponible', 7.50),  -- id 54
(24, 'DVD',        'disponible', 2.50),  -- id 55
(24, 'Blu-ray',    'disponible', 5.00),  -- id 56
(24, '4K Ultra HD','disponible', 7.50),  -- id 57
(25, 'DVD',        'disponible', 2.50),  -- id 58
(25, 'Blu-ray',    'disponible', 5.00),  -- id 59
(25, '4K Ultra HD','alquilada',  7.50),  -- id 60 → Top Gun 4K (alquiler activo de Jorge)
(26, 'DVD',        'disponible', 2.50),  -- id 61
(26, 'Blu-ray',    'disponible', 5.00),  -- id 62
(26, '4K Ultra HD','disponible', 7.50),  -- id 63
(27, 'DVD',        'disponible', 2.50),  -- id 64
(27, 'Blu-ray',    'disponible', 5.00),  -- id 65
(27, '4K Ultra HD','disponible', 7.50),  -- id 66
(28, 'DVD',        'disponible', 2.50),  -- id 67
(28, 'Blu-ray',    'disponible', 5.00),  -- id 68
(28, '4K Ultra HD','disponible', 7.50),  -- id 69
(29, 'DVD',        'disponible', 2.50),  -- id 70
(29, 'Blu-ray',    'disponible', 5.00),  -- id 71
(29, '4K Ultra HD','disponible', 7.50),  -- id 72
(30, 'DVD',        'disponible', 2.50),  -- id 73
(30, 'Blu-ray',    'disponible', 5.00),  -- id 74
(30, '4K Ultra HD','disponible', 7.50);  -- id 75

-- PAGOS
-- Montos coinciden exactamente con precio_alquiler de cada copia
-- id 1 → Ana     | El Silencio Blu-ray  | 5.00€
-- id 2 → Gabriel | El Señor Blu-ray     | 5.00€
-- id 3 → María   | Interstellar 4K      | 7.50€
-- id 4 → Jorge   | Top Gun 4K           | 7.50€
-- id 5 → Sofía   | Titanic Blu-ray      | 5.00€
INSERT INTO Pagos (metodo_pago, monto_cobro) VALUES
('efectivo',      5.00),
('tarjeta',       5.00),
('transferencia', 7.50),
('tarjeta',       7.50),
('efectivo',      5.00);

-- ALQUILERES
-- Los id_copia están calculados en base al orden de inserción de Copias
-- para garantizar coherencia con el estado 'alquilada' de cada copia
-- Variedad de estados para pruebas de todas las funcionalidades:
--   devuelto             → flujo completo normal
--   activo               → alquiler en curso
--   vencido              → plazo superado sin devolver
--   pendiente_devolucion → cliente ha solicitado devolución, pendiente de confirmación del empleado
INSERT INTO Alquileres (id_cliente, id_copia, id_empleado, id_transaccion, fecha_alquiler, fecha_devolucion_prevista, fecha_devolucion_real, estado_alquiler) VALUES
(1, 11, 2, 1, '2026-05-01', '2026-05-08', '2026-05-07', 'devuelto'),            -- Ana,    El Silencio Blu-ray,  devuelto ✅
(2, 35, 1, 2, '2026-05-10', '2026-05-17', NULL,         'activo'),              -- Gabriel, El Señor Blu-ray,   activo ✅
(3, 42, 2, 3, '2026-05-12', '2026-05-19', NULL,         'vencido'),             -- María,   Interstellar 4K,    vencido ✅
(4, 60, 3, 4, '2026-05-15', '2026-05-22', NULL,         'activo'),              -- Jorge,   Top Gun 4K,         activo ✅
(5, 21, 1, 5, '2026-05-16', '2026-05-23', NULL,         'pendiente_devolucion'); -- Sofía (bloqueada), Titanic Blu-ray, pendiente devolución ✅
