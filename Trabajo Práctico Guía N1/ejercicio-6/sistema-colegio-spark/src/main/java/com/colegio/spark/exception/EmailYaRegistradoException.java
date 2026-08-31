package com.colegio.spark.exception;

/**
 * Excepcion de negocio lanzada al intentar registrar un docente con un
 * email que ya existe en la base de datos (el email es el "username" de
 * login y debe ser unico). La captura AuthController para volver a mostrar
 * el formulario de registro con el error correspondiente.
 */
public class EmailYaRegistradoException extends RuntimeException {

    public EmailYaRegistradoException(String mensaje) {
        super(mensaje);
    }

}
