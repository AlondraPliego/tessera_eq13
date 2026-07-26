-- ==============================================
-- Backup / dump completo de la base tessera_db
-- Generado a partir de las migraciones Flyway V1-V5
-- Uso: mysql -u root -p tessera_db < backup.sql
-- ==============================================

CREATE DATABASE IF NOT EXISTS tessera_db;
USE tessera_db;

-- V1: Tablas base de roles y usuarios
CREATE TABLE rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    rol_id BIGINT NOT NULL,
    empresa_id BIGINT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES rol(id),
    CONSTRAINT fk_usuario_empresa FOREIGN KEY (empresa_id) REFERENCES usuario(id)
) ENGINE=InnoDB;

-- V2: Recintos (propiedad de una empresa) y sus zonas
CREATE TABLE recinto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(255) NOT NULL,
    mapa_svg TEXT,
    empresa_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_recinto_empresa FOREIGN KEY (empresa_id) REFERENCES usuario(id)
) ENGINE=InnoDB;

CREATE TABLE zona (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recinto_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    capacidad INT NOT NULL,
    color VARCHAR(20),
    coordenadas VARCHAR(255),
    CONSTRAINT fk_zona_recinto FOREIGN KEY (recinto_id) REFERENCES recinto(id)
) ENGINE=InnoDB;

-- V3: Eventos y tipos de boleto (precio por zona/evento)
CREATE TABLE evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha DATETIME NOT NULL,
    recinto_id BIGINT NOT NULL,
    empresa_id BIGINT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PROGRAMADO',
    CONSTRAINT fk_evento_recinto FOREIGN KEY (recinto_id) REFERENCES recinto(id),
    CONSTRAINT fk_evento_empresa FOREIGN KEY (empresa_id) REFERENCES usuario(id)
) ENGINE=InnoDB;

-- "Tipo de boleto" por zona/evento
CREATE TABLE boleto_evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    zona_id BIGINT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad_disponible INT NOT NULL,
    CONSTRAINT fk_boleto_evento FOREIGN KEY (evento_id) REFERENCES evento(id),
    CONSTRAINT fk_boleto_zona FOREIGN KEY (zona_id) REFERENCES zona(id)
) ENGINE=InnoDB;

-- V4: Compras y su detalle.
-- detalle_compra es la tabla asociativa de la relación N:M entre
-- compra y boleto_evento (una compra puede tener varios tipos de boleto,
-- y un tipo de boleto puede aparecer en muchas compras), con atributos
-- propios de la relación: cantidad y subtotal.
CREATE TABLE compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT fk_compra_cliente FOREIGN KEY (cliente_id) REFERENCES usuario(id)
) ENGINE=InnoDB;

CREATE TABLE detalle_compra (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    compra_id BIGINT NOT NULL,
    boleto_evento_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id) REFERENCES compra(id),
    CONSTRAINT fk_detalle_boleto FOREIGN KEY (boleto_evento_id) REFERENCES boleto_evento(id)
) ENGINE=InnoDB;

-- V5: Datos de prueba (seed)

-- Roles
INSERT INTO rol (id, nombre) VALUES
(1, 'ADMIN'),
(2, 'EMPRESA'),
(3, 'CLIENTE');

-- Usuarios (1 admin, 4 empresas, 10 clientes = 15)
INSERT INTO usuario (id, nombre, apellido_paterno, apellido_materno, email, password, telefono, rol_id, empresa_id) VALUES
(1, 'Admin', 'Root', 'Sistema', 'admin@tessera.com', '$2a$10$hashPlaceholder1', '5551000001', 1, NULL),
(2, 'Ticket', 'Vega', 'Ruiz', 'empresa1@tessera.com', '$2a$10$hashPlaceholder2', '5551001000', 2, NULL),
(3, 'Pro', 'Rivas', 'Mendez', 'empresa2@tessera.com', '$2a$10$hashPlaceholder3', '5551001001', 2, NULL),
(4, 'Live', 'Cortes', 'Castro', 'empresa3@tessera.com', '$2a$10$hashPlaceholder4', '5551001002', 2, NULL),
(5, 'Max', 'Fernandez', 'Rojas', 'empresa4@tessera.com', '$2a$10$hashPlaceholder5', '5551001003', 2, NULL),
(6, 'Ana', 'Garcia', 'Ruiz', 'cliente1@tessera.com', '$2a$10$hashPlaceholder6', '5552001000', 3, NULL),
(7, 'Luis', 'Hernandez', 'Mendez', 'cliente2@tessera.com', '$2a$10$hashPlaceholder7', '5552001001', 3, NULL),
(8, 'Marta', 'Lopez', 'Castro', 'cliente3@tessera.com', '$2a$10$hashPlaceholder8', '5552001002', 3, NULL),
(9, 'Jorge', 'Martinez', 'Rojas', 'cliente4@tessera.com', '$2a$10$hashPlaceholder9', '5552001003', 3, NULL),
(10, 'Sofia', 'Gonzalez', 'Vega', 'cliente5@tessera.com', '$2a$10$hashPlaceholder10', '5552001004', 3, NULL),
(11, 'Carlos', 'Perez', 'Nunez', 'cliente6@tessera.com', '$2a$10$hashPlaceholder11', '5552001005', 3, NULL),
(12, 'Elena', 'Sanchez', 'Soto', 'cliente7@tessera.com', '$2a$10$hashPlaceholder12', '5552001006', 3, NULL),
(13, 'Diego', 'Ramirez', 'Aguilar', 'cliente8@tessera.com', '$2a$10$hashPlaceholder13', '5552001007', 3, NULL),
(14, 'Paula', 'Torres', 'Campos', 'cliente9@tessera.com', '$2a$10$hashPlaceholder14', '5552001008', 3, NULL),
(15, 'Ivan', 'Flores', 'Silva', 'cliente10@tessera.com', '$2a$10$hashPlaceholder15', '5552001009', 3, NULL);

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

-- Eventos (15)
INSERT INTO evento (id, nombre, descripcion, fecha, recinto_id, empresa_id, estado) VALUES
(1, 'Festival de Verano', 'Descripcion del evento Festival de Verano', '2026-01-01 19:00:00', 1, 2, 'AGOTADO'),
(2, 'Concierto Sinfonico', 'Descripcion del evento Concierto Sinfonico', '2026-02-04 19:00:00', 2, 3, 'PROGRAMADO'),
(3, 'Noche de Rock', 'Descripcion del evento Noche de Rock', '2026-03-07 19:00:00', 3, 4, 'PROGRAMADO'),
(4, 'Gala de Danza', 'Descripcion del evento Gala de Danza', '2026-04-10 19:00:00', 4, 5, 'PROGRAMADO'),
(5, 'Torneo de Comedia', 'Descripcion del evento Torneo de Comedia', '2026-05-13 19:00:00', 5, 2, 'PROGRAMADO'),
(6, 'Feria Cultural', 'Descripcion del evento Feria Cultural', '2026-06-16 19:00:00', 6, 3, 'AGOTADO'),
(7, 'Recital Acustico', 'Descripcion del evento Recital Acustico', '2026-07-19 19:00:00', 7, 4, 'PROGRAMADO'),
(8, 'Festival Electronico', 'Descripcion del evento Festival Electronico', '2026-08-22 19:00:00', 8, 5, 'PROGRAMADO'),
(9, 'Obra de Teatro Clasico', 'Descripcion del evento Obra de Teatro Clasico', '2026-09-25 19:00:00', 9, 2, 'PROGRAMADO'),
(10, 'Show de Magia', 'Descripcion del evento Show de Magia', '2026-10-01 19:00:00', 10, 3, 'PROGRAMADO'),
(11, 'Concierto Pop', 'Descripcion del evento Concierto Pop', '2026-11-04 19:00:00', 11, 4, 'AGOTADO'),
(12, 'Encuentro de Jazz', 'Descripcion del evento Encuentro de Jazz', '2026-12-07 19:00:00', 12, 5, 'PROGRAMADO'),
(13, 'Batalla de Bandas', 'Descripcion del evento Batalla de Bandas', '2026-01-10 19:00:00', 1, 2, 'PROGRAMADO'),
(14, 'Musical Infantil', 'Descripcion del evento Musical Infantil', '2026-02-13 19:00:00', 2, 3, 'PROGRAMADO'),
(15, 'Cierre de Temporada', 'Descripcion del evento Cierre de Temporada', '2026-03-16 19:00:00', 3, 4, 'PROGRAMADO');

-- Boletos por evento/zona (30)
INSERT INTO boleto_evento (id, evento_id, zona_id, precio, cantidad_disponible) VALUES
(1, 1, 1, 173, 61),
(2, 1, 2, 196, 72),
(3, 2, 4, 219, 83),
(4, 2, 5, 242, 94),
(5, 3, 6, 265, 105),
(6, 3, 7, 288, 116),
(7, 4, 9, 311, 127),
(8, 4, 10, 334, 138),
(9, 5, 11, 357, 149),
(10, 5, 12, 380, 160),
(11, 6, 14, 403, 171),
(12, 6, 15, 426, 182),
(13, 7, 16, 449, 193),
(14, 7, 17, 472, 204),
(15, 8, 19, 495, 215),
(16, 8, 20, 518, 226),
(17, 9, 21, 541, 237),
(18, 9, 22, 564, 248),
(19, 10, 24, 587, 59),
(20, 10, 25, 610, 70),
(21, 11, 26, 633, 81),
(22, 11, 27, 656, 92),
(23, 12, 29, 679, 103),
(24, 12, 30, 702, 114),
(25, 13, 1, 725, 125),
(26, 13, 2, 748, 136),
(27, 14, 4, 771, 147),
(28, 14, 5, 794, 158),
(29, 15, 6, 817, 169),
(30, 15, 7, 840, 180);

-- Compras (15)
INSERT INTO compra (id, cliente_id, fecha, total, estado) VALUES
(1, 6, '2026-01-01 10:00:00', 1279.0, 'PENDIENTE'),
(2, 7, '2026-02-03 11:07:00', 933.0, 'PAGADA'),
(3, 8, '2026-03-05 12:14:00', 1923.0, 'PAGADA'),
(4, 9, '2026-04-07 13:21:00', 449.0, 'PAGADA'),
(5, 10, '2026-05-09 14:28:00', 2659.0, 'PENDIENTE'),
(6, 11, '2026-06-11 15:35:00', 1761.0, 'PAGADA'),
(7, 12, '2026-07-13 16:42:00', 3303.0, 'PAGADA'),
(8, 13, '2026-08-15 17:49:00', 725.0, 'PAGADA'),
(9, 14, '2026-09-17 18:56:00', 4039.0, 'PENDIENTE'),
(10, 15, '2026-01-19 10:03:00', 519.0, 'PAGADA'),
(11, 6, '2026-02-21 11:10:00', 1233.0, 'PAGADA'),
(12, 7, '2026-03-23 12:17:00', 311.0, 'PAGADA'),
(13, 8, '2026-04-25 13:24:00', 1969.0, 'PENDIENTE'),
(14, 9, '2026-05-27 14:31:00', 1347.0, 'PAGADA'),
(15, 10, '2026-06-02 15:38:00', 2613.0, 'PAGADA');

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

