package com.club.socios.dto;

import com.club.socios.domain.enums.Parentesco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** DTO para un integrante del grupo familiar (alta/edición vía formulario). */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamiliarSocioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellido;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{7,9}", message = "El DNI debe tener entre 7 y 9 dígitos")
    private String dni;

    private LocalDate fechaNacimiento;

    @NotNull(message = "Debe indicar el parentesco")
    private Parentesco parentesco;

    private Long grupoFamiliarId;

    private String urlImagenRostro;
}
