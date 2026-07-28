package com.TESSERA.Eq13Tessera.common.exception;

/**
 * Se lanza cuando un usuario intenta modificar/eliminar algo que no le pertenece
 * (ej. una empresa tratando de editar el recinto de otra empresa).
 */
public class OperacionNoPermitidaException extends RuntimeException {
    public OperacionNoPermitidaException(String message) {
        super(message);
    }
}
