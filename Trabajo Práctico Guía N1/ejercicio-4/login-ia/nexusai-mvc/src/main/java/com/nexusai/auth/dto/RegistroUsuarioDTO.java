package com.nexusai.auth.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * ============================================================================
 * DTO (Data Transfer Object) — RegistroUsuarioDTO
 * ============================================================================
 * ¿Por qué no usar directamente la entidad {@code Usuario} como objeto de
 * formulario (th:object) en la vista de registro?
 *
 *   1) Separación de responsabilidades (principio propio de MVC estricto):
 *      la ENTIDAD representa el modelo de persistencia (ORM, tabla de BD);
 *      el DTO representa el "contrato" de datos que la VISTA envía al
 *      CONTROLADOR. Mezclarlos acopla la base de datos a la UI.
 *   2) Seguridad: la entidad tiene campos que jamás deben venir del usuario
 *      final por formulario (id, intentosFallidos, bloqueado, fechaRegistro).
 *      Si se usara la entidad directamente, un usuario malicioso podría
 *      enviar esos campos en el POST y alterarlos (mass assignment).
 *   3) Permite agregar campos que sólo existen en el formulario y no en la
 *      tabla, como "claveConfirmacion" (repetir clave).
 *
 * Las anotaciones de Bean Validation (jakarta.validation) declaran las
 * reglas de validación de forma DECLARATIVA. El CONTROLLER las dispara con
 * @Valid y Spring MVC completa automáticamente un objeto BindingResult con
 * los errores encontrados, que luego la VISTA muestra con th:errors.
 * ============================================================================
 */
public class RegistroUsuarioDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre es demasiado largo")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 80, message = "El apellido es demasiado largo")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    @Pattern(regexp = "\\d{6,10}", message = "El documento debe tener entre 6 y 10 dígitos numéricos")
    private String documento;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a hoy")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El correo personal es obligatorio")
    @Email(message = "Ingrese un correo electrónico válido")
    private String correoPersonal;

    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 6, message = "La clave debe tener al menos 6 caracteres")
    private String clave;

    @NotBlank(message = "Debe confirmar la clave")
    private String claveConfirmacion;

    public RegistroUsuarioDTO() {
    }

    /**
     * Validación "cruzada" simple entre dos campos (clave / confirmación).
     * No se puede expresar con una única anotación estándar de campo, por
     * eso se resuelve con un método de negocio auxiliar invocado desde el
     * controlador antes de persistir.
     */
    public boolean lasClavesCoinciden() {
        return clave != null && clave.equals(claveConfirmacion);
    }

    // ============================ GETTERS Y SETTERS ============================
    // Requeridos por el mecanismo de "data binding" de Spring MVC: al recibir
    // el POST, Spring instancia este DTO y usa los setters para volcar cada
    // parámetro del formulario (matching por el atributo "name" del <input>).

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getClaveConfirmacion() {
        return claveConfirmacion;
    }

    public void setClaveConfirmacion(String claveConfirmacion) {
        this.claveConfirmacion = claveConfirmacion;
    }
}
