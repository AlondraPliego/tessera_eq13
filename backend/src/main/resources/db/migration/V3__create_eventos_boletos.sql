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
