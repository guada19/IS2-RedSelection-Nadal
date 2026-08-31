package com.colegio.spark.dto.response;

import com.colegio.spark.model.enums.Rol;
import com.colegio.spark.model.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ============================================================================
 *  DocenteResponseDTO           (capa DTO - Data Transfer Object)
 * ============================================================================
 * DTO DE SALIDA (Service -> Controller -> View) con los datos de un docente
 * que es seguro exponer en las vistas Thymeleaf.
 *
 * A PROPOSITO NO INCLUYE EL CAMPO "password": esta es la razon principal de
 * ser de los DTO de respuesta: la entidad Docente (capa Model) SI tiene el
 * hash de la contraseña, pero esa informacion nunca debe llegar a la vista
 * ni viajar de vuelta al navegador. El DocenteMapper es el unico responsable
 * de convertir Docente -> DocenteResponseDTO, y al hacerlo simplemente
 * "omite" copiar ese campo.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteResponseDTO {

    private Long id;
    private String nombre;
    private String apellido;
    private Sexo sexo;
    private LocalDate fechaNacimiento;
    private String email;
    private Rol rol;
    private boolean activo;

}
