CREATE TABLE fechas_eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    ciudad VARCHAR(150) NOT NULL,
    CONSTRAINT fk_fechaevento_evento FOREIGN KEY (evento_id) REFERENCES evento(id)
) ENGINE=InnoDB;
