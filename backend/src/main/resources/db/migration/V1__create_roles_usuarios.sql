-- V1: Tablas base de roles, usuarios y los datos propios de cada rol
-- (cliente, empresa, administrador)

CREATE TABLE rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabla "usuario": datos que TODOS los usuarios tienen sin importar su rol
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES rol(id)
) ENGINE=InnoDB;

-- Tabla "clientes": datos extra SOLO de los usuarios con rol CLIENTE
-- usuario_id es al mismo tiempo la llave primaria y la llave foránea hacia "usuario"
CREATE TABLE clientes (
    usuario_id BIGINT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla "empresas": datos extra SOLO de los usuarios con rol EMPRESA
CREATE TABLE empresas (
    usuario_id BIGINT PRIMARY KEY,
    nombre_empresa VARCHAR(150) NOT NULL,
    rfc VARCHAR(50) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    sitio_web VARCHAR(255),
    CONSTRAINT fk_empresa_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Tabla "administradores": datos extra SOLO de los usuarios con rol ADMIN
CREATE TABLE administradores (
    usuario_id BIGINT PRIMARY KEY,
    nivel_acceso VARCHAR(50) NOT NULL DEFAULT 'GENERAL',
    CONSTRAINT fk_admin_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;
