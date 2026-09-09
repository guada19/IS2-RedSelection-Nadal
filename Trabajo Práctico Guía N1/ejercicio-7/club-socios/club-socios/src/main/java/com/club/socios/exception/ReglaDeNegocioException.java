package com.club.socios.exception;

/** Se lanza cuando una operación viola una regla de negocio (ej: DNI duplicado, cuota ya saldada). */
public class ReglaDeNegocioException extends RuntimeException {
    public ReglaDeNegocioException(String mensaje) {
        super(mensaje);
    }
}
