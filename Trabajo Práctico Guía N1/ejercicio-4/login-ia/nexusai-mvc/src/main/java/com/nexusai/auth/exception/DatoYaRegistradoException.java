package com.nexusai.auth.exception;

/**
 * Excepción de negocio: se lanza desde la capa Service al intentar registrar
 * un usuario cuyo correo personal (usado como nombre de usuario) o cuyo
 * número de documento ya existen en la base de datos. Se aprovecha la
 * restricción {@code unique = true} definida en la entidad {@code Usuario}
 * y se valida también de forma explícita en el Service para poder devolver
 * un mensaje claro en la VISTA en lugar de una excepción SQL genérica.
 */
public class DatoYaRegistradoException extends RuntimeException {

    public DatoYaRegistradoException(String mensaje) {
        super(mensaje);
    }
}
