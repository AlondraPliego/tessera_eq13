-- V3: Eventos y tipos de boleto (precio por zona/evento)
-- Tabla principal del evento (información general)
CREATE TABLE evento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    empresa_id BIGINT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'PROGRAMADO',
    flyer_principal VARCHAR(500) NULL,
    flyer_secundario VARCHAR(500) NULL,
    flyer_terciario VARCHAR(500) NULL,
    CONSTRAINT fk_evento_empresa FOREIGN KEY (empresa_id) REFERENCES usuario(id)
) ENGINE=InnoDB;

-- Tabla de funciones / fechas del evento
CREATE TABLE fechas_eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    ciudad VARCHAR(150) NOT NULL,
    recinto_id BIGINT NOT NULL,
    CONSTRAINT fk_fechaevento_evento FOREIGN KEY (evento_id) REFERENCES evento(id) ON DELETE CASCADE,
    CONSTRAINT fk_fechaevento_recinto FOREIGN KEY (recinto_id) REFERENCES recinto(id)
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
