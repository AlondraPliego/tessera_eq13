-- V6: Agrega los flyers promocionales al evento
ALTER TABLE evento
    ADD COLUMN flyer_principal VARCHAR(500) NULL,
    ADD COLUMN flyer_secundario VARCHAR(500) NULL,
    ADD COLUMN flyer_terciario VARCHAR(500) NULL;