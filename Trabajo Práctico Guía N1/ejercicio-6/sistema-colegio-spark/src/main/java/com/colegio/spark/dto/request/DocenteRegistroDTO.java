package com.colegio.spark.dto.request;

import com.colegio.spark.model.enums.Sexo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ============================================================================
 *  DocenteRegistroDTO           (capa DTO - Data Transfer Object)
 * ============================================================================
 * DTO DE ENTRADA (Controller -> Service) para el AUTO-REGISTRO PUBLICO de un
 * docente nuevo (formulario "/registro").
 *
 * POR QUE SE USA UN DTO Y NO LA ENTIDAD Docente DIRECTAMENTE:
 *  - Desacopla la capa View/Controller de la capa Model: la vista Thymeleaf
 *    (auth/registro.html) solo conoce estos campos, no toda la entidad JPA
 *    (que ademas trae relaciones, campos de auditoria, el flag "activo", etc.)
 *  - Permite aplicar reglas de VALIDACION propias del caso de uso "registro"
 *    (ej: aca la contraseña es obligatoria con una longitud minima; en un
 *    DTO de edicion de perfil, en cambio, la contraseña ni se pediria).
 *  - Evita "mass assignment": el usuario que completa el formulario NUNCA
 *    podria, por ejemplo, mandar directamente un campo "rol=ADMIN" u
 *    "activo=true/false", porque este DTO ni siquiera declara esos atributos.
 *    Es el Service (DocenteServiceImpl) quien decide, en el codigo del
 *    servidor, que todo auto-registro recibe el rol DOCENTE y activo=true.
 *
 * ANOTACIONES DE VALIDACION (Bean Validation / Jakarta Validation):
 *  Se procesan automaticamente cuando el Controller recibe el DTO anotado
 *  con @Valid; si alguna regla falla, Spring llena un BindingResult con los
 *  errores, que el Controller usa para volver a mostrar el formulario con
 *  los mensajes correspondientes (ver AuthController.registrar()).
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteRegistroDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80, message = "El apellido no puede superar los 80 caracteres")
    private String apellido;

    @NotNull(message = "Debe seleccionar el sexo")
    private Sexo sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "Debe ingresar un correo electronico valido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String password;

    @NotBlank(message = "Debe confirmar la contraseña")
    private String confirmarPassword;

}
