package com.TESSERA.Eq13Tessera.auth.exception;

/**
 * Se lanza cuando faltan datos obligatorios según el rol elegido
 * (por ejemplo, el RFC cuando alguien se registra como EMPRESA).
 */
public class DatosRegistroInvalidosException extends RuntimeException {
    public DatosRegistroInvalidosException(String message) {
        super(message);
    }
}
