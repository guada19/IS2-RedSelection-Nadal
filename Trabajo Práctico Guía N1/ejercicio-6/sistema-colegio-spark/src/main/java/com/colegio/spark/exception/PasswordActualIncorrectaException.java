package com.colegio.spark.exception;

/**
 * Excepcion de negocio lanzada cuando, al cambiar la contraseña, la
 * "contraseña actual" ingresada por el docente no coincide con el hash
 * almacenado. La captura PerfilController para volver a mostrar el
 * formulario de cambio de contraseña con el error correspondiente.
 */
public class PasswordActualIncorrectaException extends RuntimeException {

    public PasswordActualIncorrectaException(String mensaje) {
        super(mensaje);
    }

}
