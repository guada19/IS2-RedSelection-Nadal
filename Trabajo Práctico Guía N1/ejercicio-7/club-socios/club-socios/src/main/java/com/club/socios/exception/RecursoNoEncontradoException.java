package com.club.socios.exception;

/** Se lanza cuando se busca por id/clave una entidad que no existe. */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
