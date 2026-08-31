package com.nexusai.auth.exception;

/**
 * Excepción de negocio: se lanza desde la capa Service cuando el correo
 * existe, la cuenta NO está bloqueada, pero la clave ingresada no coincide
 * con el hash almacenado.
 * <p>
 * Transporta además la cantidad de intentos restantes antes del bloqueo,
 * para que el Controller pueda informarle ese dato al usuario a través de
 * la VISTA (por ejemplo: "Clave incorrecta. Le quedan 2 intentos.").
 */
public class CredencialesInvalidasException extends RuntimeException {

    /** Intentos restantes antes de que la cuenta quede bloqueada. */
    private final int intentosRestantes;

    public CredencialesInvalidasException(String mensaje, int intentosRestantes) {
        super(mensaje);
        this.intentosRestantes = intentosRestantes;
    }

    public int getIntentosRestantes() {
        return intentosRestantes;
    }
}
