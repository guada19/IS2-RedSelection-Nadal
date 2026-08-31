package com.nexusai.auth.exception;

/**
 * Excepción de negocio: se lanza desde la capa Service cuando, al intentar
 * loguearse, el correo ingresado NO existe en la base de datos.
 * <p>
 * Es una {@link RuntimeException} (excepción "no chequeada") a propósito:
 * se trata de un caso de flujo de negocio esperado (no un error técnico),
 * por lo que el Controller la captura puntualmente con un bloque try/catch
 * y decide qué vista mostrar, sin obligar a todos los métodos intermedios a
 * declarar "throws" explícitamente.
 * <p>
 * Según el enunciado: "En el ingreso en el sistema si el usuario no está
 * registrado, se le solicita registrarse" — esta excepción es la que
 * dispara ese comportamiento en {@code AutenticacionController}.
 */
public class UsuarioNoRegistradoException extends RuntimeException {

    public UsuarioNoRegistradoException(String mensaje) {
        super(mensaje);
    }
}
