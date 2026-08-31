package com.empresatech.app.exception;

/**
 * Excepción de dominio para indicar que un registro solicitado no existe
 * en la base de datos.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
