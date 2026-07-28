-- V6: Integración con seatmap.pro (selección visual de asientos/zonas)

-- El ID del mapa (schema) que se diseñó en el editor de seatmap.pro para este recinto
ALTER TABLE recinto ADD COLUMN seatmap_schema_id BIGINT NULL;

-- El ID de la sección/zona DENTRO de ese mapa (para poder pintarla con su precio)
ALTER TABLE zona ADD COLUMN seatmap_object_id BIGINT NULL;

-- El ID del "evento" que seatmap.pro genera para cada función/fecha
ALTER TABLE fechas_eventos ADD COLUMN seatmap_event_id VARCHAR(100) NULL;
