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
