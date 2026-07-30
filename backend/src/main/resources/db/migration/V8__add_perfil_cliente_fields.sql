-- V8: Campos adicionales de perfil para clientes (apellidos y fecha de nacimiento)
-- Se agregan como nullable porque los clientes ya existentes no tienen estos datos todavía.

ALTER TABLE clientes
    ADD COLUMN apellidos VARCHAR(150) NULL AFTER nombre_usuario,
    ADD COLUMN fecha_nacimiento DATE NULL AFTER telefono;