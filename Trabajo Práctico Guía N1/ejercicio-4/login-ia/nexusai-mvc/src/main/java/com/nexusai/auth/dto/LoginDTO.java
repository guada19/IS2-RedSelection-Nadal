package com.nexusai.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * ============================================================================
 * DTO — LoginDTO
 * ============================================================================
 * Objeto simple que representa el formulario de inicio de sesión: sólo
 * transporta el correo (usuario) y la clave ingresados. Igual que en el caso
 * de {@link RegistroUsuarioDTO}, mantenerlo separado de la entidad Usuario
 * evita exponer o depender de columnas internas (id, bloqueado, etc.) en la
 * capa de VISTA.
 * ============================================================================
 */
public class LoginDTO {

    @NotBlank(message = "Ingrese su correo")
    private String correoPersonal;

    @NotBlank(message = "Ingrese su clave")
    private String clave;

    public LoginDTO() {
    }

    public String getCorreoPersonal() {
        return correoPersonal;
    }

    public void setCorreoPersonal(String correoPersonal) {
        this.correoPersonal = correoPersonal;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
