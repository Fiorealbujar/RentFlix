-- =============================================
-- RENTFLIX - Carga inicial de datos
-- =============================================

-- =============================================
-- EMPLEADOS (primero porque Clientes no depende de ellos)
-- =============================================
INSERT INTO Empleados (nombre_empleado, apellido_empleado, email_empleado, usuario_empleado, contrasenia_empleado, id_jefe) VALUES
('Carlos',  'García',    'carlos@rentflix.com',  'carlos_admin', '1234', NULL),
('Laura',   'Martínez',  'laura@rentflix.com',   'laura_emp',    '1234', 1),
('Pedro',   'López',     'pedro@rentflix.com',   'pedro_emp',    '1234', 1);

-- =============================================
-- CLIENTES
-- =============================================
INSERT INTO Clientes (nombre_cliente, apellido_cliente, email_cliente, nombre_usuario, contrasenia_cliente) VALUES
('Ana',     'Rueda',      'ana@email.com',     'ana_cli',     '1234'),
('Gabriel', 'Fernández',  'gabriel@email.com', 'gabriel_cli', '1234'),
('María',   'Sánchez',    'maria@email.com',   'maria_cli',   '1234'),
('Jorge',   'Pérez',      'jorge@email.com',   'jorge_cli',   '1234'),
('Sofía',   'Rodríguez',  'sofia@email.com',   'sofia_cli',   '1234');

-- =============================================
-- PELÍCULAS (30 películas reales)
-- Antiguas (antes 1990): solo DVD
-- Años 90: DVD + Blu-ray
-- 2000 en adelante: DVD + Blu-ray + 4K
-- =============================================
INSERT INTO Peliculas (nombre_pelicula, director, duracion, genero, sinopsis, clasificacion_edad) VALUES
-- Antiguas (antes 1990) → solo DVD
('El Padrino',                  'Francis Ford Coppola', 175, 'Drama',          'La historia de la familia mafiosa Corleone.',                          '18'),
('Tiburón',                     'Steven Spielberg',     124, 'Terror',          'Un tiburón asesino aterroriza una pequeña localidad costera.',         '12'),
('El Imperio Contraataca',      'Irvin Kershner',       124, 'Ciencia Ficción', 'La segunda entrega de la saga Star Wars.',                             'TP'),
('Blade Runner',                'Ridley Scott',         117, 'Ciencia Ficción', 'Un cazador de replicantes persigue a androides en un futuro distópico.','16'),
('Alien',                       'Ridley Scott',         117, 'Terror',          'La tripulación de una nave espacial se enfrenta a un alien mortal.',    '16'),
('El Resplandor',               'Stanley Kubrick',      146, 'Terror',          'Un escritor y su familia pasan el invierno en un hotel encantado.',     '18'),
-- Años 90 → DVD + Blu-ray
('El Silencio de los Corderos', 'Jonathan Demme',       118, 'Thriller',        'Una agente del FBI colabora con un asesino en serie para cazar a otro.','18'),
('Pulp Fiction',                'Quentin Tarantino',    154, 'Thriller',        'Historias entrelazadas del hampa de Los Ángeles.',                     '18'),
('El Rey León',                 'Roger Allers',          88, 'Animación',       'Un joven león debe reclamar su lugar como rey.',                       'TP'),
('Forrest Gump',                'Robert Zemeckis',      142, 'Drama',           'La vida de un hombre sencillo que vive momentos históricos.',           '12'),
('Titanic',                     'James Cameron',        195, 'Romance',         'Historia de amor a bordo del famoso transatlántico.',                  '12'),
('Matrix',                      'Lana Wachowski',       136, 'Ciencia Ficción', 'Un hacker descubre que la realidad es una simulación.',                '16'),
('Braveheart',                  'Mel Gibson',           178, 'Historia',        'William Wallace lidera la rebelión escocesa contra Inglaterra.',        '18'),
('La Lista de Schindler',       'Steven Spielberg',     195, 'Drama',           'Un empresario salva la vida de miles de judíos durante el Holocausto.', '16'),
('Jurassic Park',               'Steven Spielberg',     127, 'Aventura',        'Un parque temático con dinosaurios clonados se convierte en pesadilla.','12'),
-- 2000 en adelante → DVD + Blu-ray + 4K
('Gladiator',                   'Ridley Scott',         155, 'Acción',          'Un general romano busca venganza tras ser traicionado por el príncipe.', '16'),
('El Señor de los Anillos',     'Peter Jackson',        178, 'Fantasía',        'Un hobbit emprende un viaje para destruir un anillo de poder.',          '12'),
('Inception',                   'Christopher Nolan',    148, 'Ciencia Ficción', 'Un ladrón roba secretos entrando en los sueños de sus víctimas.',       '12'),
('Interstellar',                'Christopher Nolan',    169, 'Ciencia Ficción', 'Astronautas viajan a través de un agujero de gusano en busca de un nuevo hogar.','TP'),
('Avatar',                      'James Cameron',        162, 'Ciencia Ficción', 'Un marine paralítico viaja a un planeta alienígena.',                   '12'),
('El Caballero Oscuro',         'Christopher Nolan',    152, 'Acción',          'Batman se enfrenta al Joker en una batalla por Gotham City.',           '12'),
('Avengers: Endgame',           'Anthony Russo',        181, 'Acción',          'Los Vengadores luchan para revertir el chasquido de Thanos.',           '12'),
('Parasite',                    'Bong Joon-ho',         132, 'Drama',           'Una familia pobre se infiltra en el hogar de una familia adinerada.',   '16'),
('Dune',                        'Denis Villeneuve',     155, 'Ciencia Ficción', 'Un joven noble lidera a su pueblo en un peligroso planeta desértico.',  '12'),
('Top Gun: Maverick',           'Joseph Kosinski',      131, 'Acción',          'Maverick regresa para entrenar a una nueva generación de pilotos.',     '7'),
('Spider-Man: No Way Home',     'Jon Watts',            148, 'Acción',          'Spider-Man pide ayuda a Doctor Strange para borrar su identidad.',      '12'),
('The Batman',                  'Matt Reeves',          176, 'Acción',          'Batman investiga la corrupción en Gotham mientras persigue al Acertijo.','16'),
('Oppenheimer',                 'Christopher Nolan',    180, 'Drama',           'La historia del científico que desarrolló la bomba atómica.',           '16'),
('Barbie',                      'Greta Gerwig',         114, 'Comedia',         'Barbie emprende un viaje al mundo real tras una crisis existencial.',   '7'),
('Dune: Parte Dos',             'Denis Villeneuve',     166, 'Ciencia Ficción', 'Paul Atreides lidera la guerra contra los que destruyeron su familia.', '16');

-- =============================================
-- ACTORES
-- =============================================
INSERT INTO Actores (nombre_actor) VALUES
('Marlon Brando'),       -- 1
('Al Pacino'),           -- 2
('Roy Scheider'),        -- 3
('Mark Hamill'),         -- 4
('Harrison Ford'),       -- 5
('Harrison Ford'),       -- ya existe, usaremos el mismo id
('Harrison Ford'),       -- ya existe, usaremos el mismo id
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
('Leonardo DiCaprio'),   -- ya existe
('Matthew McConaughey'),-- 23
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

-- =============================================
-- ACTORES (version limpia sin duplicados)
-- =============================================
DELETE FROM Actores;
INSERT INTO Actores (nombre_actor) VALUES
('Marlon Brando'),        -- 1
('Al Pacino'),            -- 2
('Roy Scheider'),         -- 3
('Mark Hamill'),          -- 4
('Harrison Ford'),        -- 5
('Rutger Hauer'),         -- 6
('Sigourney Weaver'),     -- 7
('Jack Nicholson'),       -- 8
('Jodie Foster'),         -- 9
('Anthony Hopkins'),      -- 10
('John Travolta'),        -- 11
('Uma Thurman'),          -- 12
('Matthew Broderick'),    -- 13
('Tom Hanks'),            -- 14
('Leonardo DiCaprio'),    -- 15
('Keanu Reeves'),         -- 16
('Mel Gibson'),           -- 17
('Liam Neeson'),          -- 18
('Sam Neill'),            -- 19
('Russell Crowe'),        -- 20
('Elijah Wood'),          -- 21
('Viggo Mortensen'),      -- 22
('Matthew McConaughey'),  -- 23
('Sam Worthington'),      -- 24
('Christian Bale'),       -- 25
('Heath Ledger'),         -- 26
('Robert Downey Jr.'),    -- 27
('Chris Evans'),          -- 28
('Song Kang-ho'),         -- 29
('Timothée Chalamet'),    -- 30
('Tom Cruise'),           -- 31
('Tom Holland'),          -- 32
('Robert Pattinson'),     -- 33
('Cillian Murphy'),       -- 34
('Margot Robbie'),        -- 35
('Zendaya');              -- 36

-- =============================================
-- PELICULA_ACTOR
-- =============================================
INSERT INTO Pelicula_Actor (id_pelicula, id_actor) VALUES
(1,  1), (1,  2),   -- El Padrino
(2,  3),            -- Tiburón
(3,  4), (3,  5),   -- El Imperio Contraataca
(4,  6),            -- Blade Runner
(5,  7),            -- Alien
(6,  8),            -- El Resplandor
(7,  9), (7, 10),   -- El Silencio de los Corderos
(8, 11), (8, 12),   -- Pulp Fiction
(9, 13),            -- El Rey León
(10, 14),           -- Forrest Gump
(11, 15),           -- Titanic
(12, 16),           -- Matrix
(13, 17),           -- Braveheart
(14, 18),           -- La Lista de Schindler
(15, 19),           -- Jurassic Park
(16, 20),           -- Gladiator
(17, 21), (17, 22), -- El Señor de los Anillos
(18, 15),           -- Inception
(19, 23),           -- Interstellar
(20, 24),           -- Avatar
(21, 25), (21, 26), -- El Caballero Oscuro
(22, 27), (22, 28), -- Avengers: Endgame
(23, 29),           -- Parasite
(24, 30),           -- Dune
(25, 31),           -- Top Gun: Maverick
(26, 32),           -- Spider-Man
(27, 33),           -- The Batman
(28, 34),           -- Oppenheimer
(29, 35),           -- Barbie
(30, 30), (30, 36); -- Dune: Parte Dos

-- =============================================
-- COPIAS
-- Precios: DVD=2.50€, Blu-ray=5.00€, 4K=7.50€
-- Películas 1-6  (antes 1990): solo DVD
-- Películas 7-15 (años 90):    DVD + Blu-ray
-- Películas 16-30 (2000+):     DVD + Blu-ray + 4K
-- =============================================

-- Películas antiguas (1-6): solo DVD
INSERT INTO Copias (id_pelicula, formato, estado, precio_alquiler) VALUES
(1,  'DVD', 'disponible', 2.50),
(1,  'DVD', 'disponible', 2.50),
(2,  'DVD', 'disponible', 2.50),
(2,  'DVD', 'disponible', 2.50),
(3,  'DVD', 'disponible', 2.50),
(4,  'DVD', 'disponible', 2.50),
(5,  'DVD', 'disponible', 2.50),
(5,  'DVD', 'disponible', 2.50),
(6,  'DVD', 'disponible', 2.50);

-- Películas años 90 (7-15): DVD + Blu-ray
INSERT INTO Copias (id_pelicula, formato, estado, precio_alquiler) VALUES
(7,  'DVD',     'disponible', 2.50),
(7,  'Blu-ray', 'disponible', 5.00),
(8,  'DVD',     'disponible', 2.50),
(8,  'Blu-ray', 'disponible', 5.00),
(8,  'Blu-ray', 'disponible', 5.00),
(9,  'DVD',     'disponible', 2.50),
(9,  'Blu-ray', 'disponible', 5.00),
(10, 'DVD',     'disponible', 2.50),
(10, 'DVD',     'disponible', 2.50),
(10, 'Blu-ray', 'disponible', 5.00),
(11, 'DVD',     'disponible', 2.50),
(11, 'Blu-ray', 'disponible', 5.00),
(11, 'Blu-ray', 'alquilada',  5.00),
(12, 'DVD',     'disponible', 2.50),
(12, 'Blu-ray', 'disponible', 5.00),
(13, 'DVD',     'disponible', 2.50),
(13, 'Blu-ray', 'disponible', 5.00),
(14, 'DVD',     'disponible', 2.50),
(14, 'Blu-ray', 'disponible', 5.00),
(15, 'DVD',     'disponible', 2.50),
(15, 'Blu-ray', 'disponible', 5.00);

-- Películas 2000+ (16-30): DVD + Blu-ray + 4K
INSERT INTO Copias (id_pelicula, formato, estado, precio_alquiler) VALUES
(16, 'DVD',        'disponible', 2.50),
(16, 'Blu-ray',    'disponible', 5.00),
(16, '4K Ultra HD','disponible', 7.50),
(17, 'DVD',        'disponible', 2.50),
(17, 'Blu-ray',    'alquilada',  5.00),
(17, '4K Ultra HD','disponible', 7.50),
(18, 'DVD',        'disponible', 2.50),
(18, 'Blu-ray',    'disponible', 5.00),
(18, '4K Ultra HD','disponible', 7.50),
(19, 'DVD',        'disponible', 2.50),
(19, 'Blu-ray',    'disponible', 5.00),
(19, '4K Ultra HD','alquilada',  7.50),
(20, 'DVD',        'disponible', 2.50),
(20, 'Blu-ray',    'disponible', 5.00),
(20, '4K Ultra HD','disponible', 7.50),
(21, 'DVD',        'disponible', 2.50),
(21, 'Blu-ray',    'disponible', 5.00),
(21, '4K Ultra HD','disponible', 7.50),
(22, 'DVD',        'disponible', 2.50),
(22, 'Blu-ray',    'disponible', 5.00),
(22, '4K Ultra HD','disponible', 7.50),
(23, 'DVD',        'disponible', 2.50),
(23, 'Blu-ray',    'disponible', 5.00),
(23, '4K Ultra HD','disponible', 7.50),
(24, 'DVD',        'disponible', 2.50),
(24, 'Blu-ray',    'disponible', 5.00),
(24, '4K Ultra HD','disponible', 7.50),
(25, 'DVD',        'disponible', 2.50),
(25, 'Blu-ray',    'disponible', 5.00),
(25, '4K Ultra HD','alquilada',  7.50),
(26, 'DVD',        'disponible', 2.50),
(26, 'Blu-ray',    'disponible', 5.00),
(26, '4K Ultra HD','disponible', 7.50),
(27, 'DVD',        'disponible', 2.50),
(27, 'Blu-ray',    'disponible', 5.00),
(27, '4K Ultra HD','disponible', 7.50),
(28, 'DVD',        'disponible', 2.50),
(28, 'Blu-ray',    'disponible', 5.00),
(28, '4K Ultra HD','disponible', 7.50),
(29, 'DVD',        'disponible', 2.50),
(29, 'Blu-ray',    'disponible', 5.00),
(29, '4K Ultra HD','disponible', 7.50),
(30, 'DVD',        'disponible', 2.50),
(30, 'Blu-ray',    'disponible', 5.00),
(30, '4K Ultra HD','disponible', 7.50);

-- =============================================
-- PAGOS
-- =============================================
INSERT INTO Pagos (metodo_pago, monto_cobro) VALUES
('efectivo',      5.00),
('tarjeta',       7.50),
('transferencia', 2.50),
('tarjeta',      10.00),
('efectivo',      5.00);

-- =============================================
-- ALQUILERES
-- (copias alquiladas: id 13=Titanic Blu-ray, 
--  34=El Señor Blu-ray, 46=Interstellar 4K, 
--  73=Top Gun 4K)
-- =============================================
INSERT INTO Alquileres (id_cliente, id_copia, id_empleado, id_transaccion, fecha_alquiler, fecha_devolucion_prevista, fecha_devolucion_real, estado_alquiler) VALUES
(1, 13, 2, 1, '2026-05-01', '2026-05-08', '2026-05-07', 'devuelto'),
(2, 34, 1, 2, '2026-05-10', '2026-05-17', NULL,         'activo'),
(3, 46, 2, 3, '2026-05-12', '2026-05-19', NULL,         'vencido'),
(4, 73, 3, 4, '2026-05-15', '2026-05-22', NULL,         'activo'),
(5, 11, 1, 5, '2026-05-16', '2026-05-23', NULL,         'activo');
