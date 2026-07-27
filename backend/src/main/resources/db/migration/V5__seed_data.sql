-- V5: Datos de prueba (seed)
-- Contraseña para TODOS los usuarios de prueba de este archivo: Tessera123!
-- (ya viene encriptada con BCrypt, así puedes usarla directo para hacer login)

-- Roles
INSERT INTO rol (id, nombre) VALUES
(1, 'ADMIN'),
(2, 'EMPRESA'),
(3, 'CLIENTE');

-- Usuarios (1 admin, 4 empresas, 10 clientes = 15)
INSERT INTO usuario (id, nombre, email, password, rol_id) VALUES
(1, 'Alondra Pliego Mendez', 'admin@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 1),
(2, 'Vega Ruiz', 'empresa1@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 2),
(3, 'Rivas Mendez', 'empresa2@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 2),
(4, 'Cortes Castro', 'empresa3@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 2),
(5, 'Fernandez Rojas', 'empresa4@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 2),
(6, 'Ana Garcia Ruiz', 'cliente1@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(7, 'Luis Hernandez Mendez', 'cliente2@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(8, 'Marta Lopez Castro', 'cliente3@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(9, 'Jorge Martinez Rojas', 'cliente4@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(10, 'Sofia Gonzalez Vega', 'cliente5@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(11, 'Carlos Perez Nunez', 'cliente6@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(12, 'Elena Sanchez Soto', 'cliente7@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(13, 'Diego Ramirez Aguilar', 'cliente8@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(14, 'Paula Torres Campos', 'cliente9@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3),
(15, 'Ivan Flores Silva', 'cliente10@tessera.com', '$2b$10$DfEfLy1eBaCfM9FhX8DUye/fTKPHPinOqWvowou9iI1Ohd19..dYW', 3);

-- Datos extra del administrador
INSERT INTO administradores (usuario_id, nivel_acceso) VALUES
(1, 'SUPER_ADMIN');

-- Datos extra de las empresas
INSERT INTO empresas (usuario_id, nombre_empresa, rfc, telefono, sitio_web) VALUES
(2, 'Ticket Entertainment', 'TEN900101AAA', '5551001000', 'https://ticket.com'),
(3, 'Pro Events', 'PEV900101BBB', '5551001001', 'https://proevents.com'),
(4, 'Live Nation Local', 'LNL900101CCC', '5551001002', 'https://livelocal.com'),
(5, 'Max Show', 'MSH900101DDD', '5551001003', 'https://maxshow.com');

-- Datos extra de los clientes
INSERT INTO clientes (usuario_id, nombre_usuario, telefono) VALUES
(6, 'ana.garcia', '5552001000'),
(7, 'luis.hernandez', '5552001001'),
(8, 'marta.lopez', '5552001002'),
(9, 'jorge.martinez', '5552001003'),
(10, 'sofia.gonzalez', '5552001004'),
(11, 'carlos.perez', '5552001005'),
(12, 'elena.sanchez', '5552001006'),
(13, 'diego.ramirez', '5552001007'),
(14, 'paula.torres', '5552001008'),
(15, 'ivan.flores', '5552001009');

-- Recintos (12)
INSERT INTO recinto (id, nombre, direccion, mapa_svg, empresa_id) VALUES
(1, 'Arena Central', 'Av. Principal #100, Ciudad', NULL, 2),
(2, 'Teatro Metropolitano', 'Av. Principal #101, Ciudad', NULL, 3),
(3, 'Estadio Norte', 'Av. Principal #102, Ciudad', NULL, 4),
(4, 'Auditorio Aurora', 'Av. Principal #103, Ciudad', NULL, 5),
(5, 'Foro Sol Chico', 'Av. Principal #104, Ciudad', NULL, 2),
(6, 'Palacio de Cultura', 'Av. Principal #105, Ciudad', NULL, 3),
(7, 'Domo Sur', 'Av. Principal #106, Ciudad', NULL, 4),
(8, 'Coliseo del Valle', 'Av. Principal #107, Ciudad', NULL, 5),
(9, 'Sala Armonia', 'Av. Principal #108, Ciudad', NULL, 2),
(10, 'Centro de Convenciones Delta', 'Av. Principal #109, Ciudad', NULL, 3),
(11, 'Parque de Espectaculos', 'Av. Principal #110, Ciudad', NULL, 4),
(12, 'Anfiteatro Luna', 'Av. Principal #111, Ciudad', NULL, 5);

-- Zonas (30)
INSERT INTO zona (id, recinto_id, nombre, capacidad, color, coordenadas) VALUES
(1, 1, 'General', 107, '#33A1FF', '[10,15]'),
(2, 1, 'Preferente', 114, '#75FF33', '[20,30]'),
(3, 1, 'VIP', 121, '#F333FF', '[30,45]'),
(4, 2, 'General', 128, '#FFD433', '[40,60]'),
(5, 2, 'Preferente', 135, '#FF5733', '[50,75]'),
(6, 3, 'General', 142, '#33A1FF', '[60,90]'),
(7, 3, 'Preferente', 149, '#75FF33', '[70,105]'),
(8, 3, 'VIP', 156, '#F333FF', '[80,120]'),
(9, 4, 'General', 163, '#FFD433', '[90,135]'),
(10, 4, 'Preferente', 170, '#FF5733', '[100,150]'),
(11, 5, 'General', 177, '#33A1FF', '[110,165]'),
(12, 5, 'Preferente', 184, '#75FF33', '[120,180]'),
(13, 5, 'VIP', 191, '#F333FF', '[130,195]'),
(14, 6, 'General', 198, '#FFD433', '[140,210]'),
(15, 6, 'Preferente', 205, '#FF5733', '[150,225]'),
(16, 7, 'General', 212, '#33A1FF', '[160,240]'),
(17, 7, 'Preferente', 219, '#75FF33', '[170,255]'),
(18, 7, 'VIP', 226, '#F333FF', '[180,270]'),
(19, 8, 'General', 233, '#FFD433', '[190,285]'),
(20, 8, 'Preferente', 240, '#FF5733', '[200,300]'),
(21, 9, 'General', 247, '#33A1FF', '[210,315]'),
(22, 9, 'Preferente', 254, '#75FF33', '[220,330]'),
(23, 9, 'VIP', 261, '#F333FF', '[230,345]'),
(24, 10, 'General', 268, '#FFD433', '[240,360]'),
(25, 10, 'Preferente', 275, '#FF5733', '[250,375]'),
(26, 11, 'General', 282, '#33A1FF', '[260,390]'),
(27, 11, 'Preferente', 289, '#75FF33', '[270,405]'),
(28, 11, 'VIP', 296, '#F333FF', '[280,420]'),
(29, 12, 'General', 303, '#FFD433', '[290,435]'),
(30, 12, 'Preferente', 310, '#FF5733', '[300,450]');

-- Eventos (información general, SIN fecha/recinto: eso vive en fechas_eventos)
INSERT INTO evento (id, nombre, descripcion, empresa_id, estado) VALUES
(1, 'Festival de Verano', 'Descripcion del evento Festival de Verano', 2, 'AGOTADO'),
(2, 'Concierto Sinfonico', 'Descripcion del evento Concierto Sinfonico', 3, 'PROGRAMADO'),
(3, 'Noche de Rock', 'Descripcion del evento Noche de Rock', 4, 'PROGRAMADO'),
(4, 'Gala de Danza', 'Descripcion del evento Gala de Danza', 5, 'PROGRAMADO'),
(5, 'Torneo de Comedia', 'Descripcion del evento Torneo de Comedia', 2, 'PROGRAMADO'),
(6, 'Feria Cultural', 'Descripcion del evento Feria Cultural', 3, 'AGOTADO'),
(7, 'Recital Acustico', 'Descripcion del evento Recital Acustico', 4, 'PROGRAMADO'),
(8, 'Festival Electronico', 'Descripcion del evento Festival Electronico', 5, 'PROGRAMADO'),
(9, 'Obra de Teatro Clasico', 'Descripcion del evento Obra de Teatro Clasico', 2, 'PROGRAMADO'),
(10, 'Show de Magia', 'Descripcion del evento Show de Magia', 3, 'PROGRAMADO'),
(11, 'Concierto Pop', 'Descripcion del evento Concierto Pop', 4, 'AGOTADO'),
(12, 'Encuentro de Jazz', 'Descripcion del evento Encuentro de Jazz', 5, 'PROGRAMADO'),
(13, 'Batalla de Bandas', 'Descripcion del evento Batalla de Bandas', 2, 'PROGRAMADO'),
(14, 'Musical Infantil', 'Descripcion del evento Musical Infantil', 3, 'PROGRAMADO'),
(15, 'Cierre de Temporada', 'Descripcion del evento Cierre de Temporada', 4, 'PROGRAMADO');

-- Fechas de los eventos (fecha, hora, ciudad y recinto de cada función)
INSERT INTO fechas_eventos (id, evento_id, fecha, hora, ciudad, recinto_id) VALUES
(1, 1, '2026-09-01', '19:00:00', 'Ciudad Central', 1),
(2, 2, '2026-09-04', '19:00:00', 'Ciudad Central', 2),
(3, 3, '2026-09-07', '19:00:00', 'Ciudad Central', 3),
(4, 4, '2026-09-10', '19:00:00', 'Ciudad Central', 4),
(5, 5, '2026-09-13', '19:00:00', 'Ciudad Central', 5),
(6, 6, '2026-09-16', '19:00:00', 'Ciudad Central', 6),
(7, 7, '2026-09-19', '19:00:00', 'Ciudad Central', 7),
(8, 8, '2026-09-22', '19:00:00', 'Ciudad Central', 8),
(9, 9, '2026-09-25', '19:00:00', 'Ciudad Central', 9),
(10, 10, '2026-10-01', '19:00:00', 'Ciudad Central', 10),
(11, 11, '2026-10-04', '19:00:00', 'Ciudad Central', 11),
(12, 12, '2026-10-07', '19:00:00', 'Ciudad Central', 12),
(13, 13, '2026-10-10', '19:00:00', 'Ciudad Central', 1),
(14, 14, '2026-10-13', '19:00:00', 'Ciudad Central', 2),
(15, 15, '2026-10-16', '19:00:00', 'Ciudad Central', 3);

-- Boletos por evento/zona (30)
INSERT INTO boleto_evento (id, evento_id, zona_id, precio, cantidad_disponible) VALUES
(1, 1, 1, 173.00, 61),
(2, 1, 2, 196.00, 72),
(3, 2, 4, 219.00, 83),
(4, 2, 5, 242.00, 94),
(5, 3, 6, 265.00, 105),
(6, 3, 7, 288.00, 116),
(7, 4, 9, 311.00, 127),
(8, 4, 10, 334.00, 138),
(9, 5, 11, 357.00, 149),
(10, 5, 12, 380.00, 160),
(11, 6, 14, 403.00, 171),
(12, 6, 15, 426.00, 182),
(13, 7, 16, 449.00, 193),
(14, 7, 17, 472.00, 204),
(15, 8, 19, 495.00, 215),
(16, 8, 20, 518.00, 226),
(17, 9, 21, 541.00, 237),
(18, 9, 22, 564.00, 248),
(19, 10, 24, 587.00, 59),
(20, 10, 25, 610.00, 70),
(21, 11, 26, 633.00, 81),
(22, 11, 27, 656.00, 92),
(23, 12, 29, 679.00, 103),
(24, 12, 30, 702.00, 114),
(25, 13, 1, 725.00, 125),
(26, 13, 2, 748.00, 136),
(27, 14, 4, 771.00, 147),
(28, 14, 5, 794.00, 158),
(29, 15, 6, 817.00, 169),
(30, 15, 7, 840.00, 180);

-- Compras (15)
INSERT INTO compra (id, cliente_id, fecha, total, estado) VALUES
(1, 6, '2026-07-01 10:00:00', 1279.00, 'PENDIENTE'),
(2, 7, '2026-07-03 11:07:00', 933.00, 'PAGADA'),
(3, 8, '2026-07-05 12:14:00', 1923.00, 'PAGADA'),
(4, 9, '2026-07-07 13:21:00', 449.00, 'PAGADA'),
(5, 10, '2026-07-09 14:28:00', 2659.00, 'PENDIENTE'),
(6, 11, '2026-07-11 15:35:00', 1761.00, 'PAGADA'),
(7, 12, '2026-07-13 16:42:00', 3303.00, 'PAGADA'),
(8, 13, '2026-07-15 17:49:00', 725.00, 'PAGADA'),
(9, 14, '2026-07-17 18:56:00', 4039.00, 'PENDIENTE'),
(10, 15, '2026-07-19 10:03:00', 519.00, 'PAGADA'),
(11, 6, '2026-07-21 11:10:00', 1233.00, 'PAGADA'),
(12, 7, '2026-07-23 12:17:00', 311.00, 'PAGADA'),
(13, 8, '2026-07-25 13:24:00', 1969.00, 'PENDIENTE'),
(14, 9, '2026-07-26 14:31:00', 1347.00, 'PAGADA'),
(15, 10, '2026-07-27 15:38:00', 2613.00, 'PAGADA');

-- Detalle de compra (23) -- relacion N:M compra <-> boleto_evento
INSERT INTO detalle_compra (id, compra_id, boleto_evento_id, cantidad, subtotal) VALUES
(1, 1, 4, 2, 484.0),
(2, 1, 5, 3, 795.0),
(3, 2, 7, 3, 933.0),
(4, 3, 10, 4, 1520.0),
(5, 3, 11, 1, 403.0),
(6, 4, 13, 1, 449.0),
(7, 5, 16, 2, 1036.0),
(8, 5, 17, 3, 1623.0),
(9, 6, 19, 3, 1761.0),
(10, 7, 22, 4, 2624.0),
(11, 7, 23, 1, 679.0),
(12, 8, 25, 1, 725.0),
(13, 9, 28, 2, 1588.0),
(14, 9, 29, 3, 2451.0),
(15, 10, 1, 3, 519.0),
(16, 11, 4, 4, 968.0),
(17, 11, 5, 1, 265.0),
(18, 12, 7, 1, 311.0),
(19, 13, 10, 2, 760.0),
(20, 13, 11, 3, 1209.0),
(21, 14, 13, 3, 1347.0),
(22, 15, 16, 4, 2072.0),
(23, 15, 17, 1, 541.0);
