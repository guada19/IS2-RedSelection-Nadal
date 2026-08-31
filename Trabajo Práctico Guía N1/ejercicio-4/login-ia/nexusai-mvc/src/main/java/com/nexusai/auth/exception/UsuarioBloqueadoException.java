package com.nexusai.auth.exception;

/**
 * Excepción de negocio: se lanza desde la capa Service cuando la cuenta ya
 * está marcada como {@code bloqueado = true} (ya sea porque el usuario
 * agotó los 3 intentos permitidos en este mismo login, o porque ya venía
 * bloqueada de intentos anteriores).
 * <p>
 * Según el enunciado: "Si el usuario está registrado y equivoca la clave 3
 * veces el mismo se bloquea".
 */
public class UsuarioBloqueadoException extends RuntimeException {

    public UsuarioBloqueadoException(String mensaje) {
        super(mensaje);
    }
}
