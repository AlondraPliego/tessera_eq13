-- V7: para reservas temporales de boletos
CREATE TABLE reserva (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    boleto_evento_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    creada_en DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expira_en DATETIME NOT NULL,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (cliente_id) REFERENCES usuario(id),
    CONSTRAINT fk_reserva_boleto FOREIGN KEY (boleto_evento_id) REFERENCES boleto_evento(id)
) ENGINE=InnoDB;

CREATE INDEX idx_reserva_expira_en ON reserva(expira_en);
