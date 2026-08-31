package com.colegio.spark.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de salida con los datos de un Aula. Trae "aplanados" (desnormalizados)
 * el id y el nombre del Grado al que pertenece, para que la vista Thymeleaf
 * pueda mostrarlos directamente sin necesidad de navegar la relacion JPA
 * (recordar que, en la entidad, Aula.grado es LAZY: acceder a el fuera de una
 * transaccion/sesion abierta lanzaria LazyInitializationException; el Mapper
 * resuelve este problema dentro de la capa Service, que si tiene contexto
 * transaccional).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AulaResponseDTO {

    private Long id;
    private String nombre;
    private Integer capacidad;
    private Long gradoId;
    private String gradoNombre;

}
