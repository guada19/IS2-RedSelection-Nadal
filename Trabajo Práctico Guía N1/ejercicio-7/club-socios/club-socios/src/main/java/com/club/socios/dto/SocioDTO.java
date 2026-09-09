package com.club.socios.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ============================================================================
 * DTO (Data Transfer Object) - SOCIO
 * ============================================================================
 * Objeto plano que viaja entre las capas Controller <-> Service <-> Vista.
 * Nunca se expone la entidad JPA {@code Socio} directamente a Thymeleaf ni
 * a un eventual cliente REST: esto evita fugas de proxies de Hibernate
 * (LazyInitializationException), desacopla el modelo de persistencia del
 * contrato de la capa web, y permite validar (Bean Validation) exactamente
 * lo que el usuario puede enviar por formulario.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80)
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{7,9}", message = "El DNI debe tener entre 7 y 9 dígitos")
    private String dni;

    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @Email(message = "Email inválido")
    private String email;

    private String telefono;

    private String numeroSocio;

    private LocalDate fechaAlta;

    private String categoria;

    private boolean activo;

    /** URL de la foto de rostro ya cargada (sólo lectura, informativa para la vista). */
    private String urlImagenRostro;

    /** Cantidad de integrantes de su grupo familiar (sólo lectura, para el listado). */
    private Integer cantidadFamiliares;
}
