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
