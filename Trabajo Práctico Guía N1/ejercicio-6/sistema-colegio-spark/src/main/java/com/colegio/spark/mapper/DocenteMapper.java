package com.colegio.spark.mapper;

import com.colegio.spark.dto.response.DocenteResponseDTO;
import com.colegio.spark.model.Docente;
import org.springframework.stereotype.Component;

/**
 * ============================================================================
 *  DocenteMapper                (capa Mapper - conversion Entidad <-> DTO)
 * ============================================================================
 * Responsable exclusivo de traducir entre la ENTIDAD JPA (Docente, capa
 * Model) y los DTO de la capa de transferencia (DocenteResponseDTO).
 *
 * POR QUE UNA CAPA DE MAPPERS SEPARADA:
 *  - Mantiene a los Services enfocados en la LOGICA DE NEGOCIO, sin mezclar
 *    codigo de "aplanado"/conversion de objetos.
 *  - Centraliza en un unico lugar la regla "que campos de la entidad se
 *    exponen hacia afuera": por ejemplo, aca es donde se decide, en un solo
 *    punto del sistema, que el hash de la contraseña JAMAS se copia al DTO.
 *  - Facilita el testing unitario del mapeo de forma aislada.
 *
 * @Component -> se registra como bean de Spring para poder inyectarse (via
 *  constructor) en los Services que lo necesiten.
 * ============================================================================
 */
@Component
public class DocenteMapper {

    /** Convierte la entidad Docente en su DTO de salida (sin exponer el password). */
    public DocenteResponseDTO toResponseDTO(Docente docente) {
        if (docente == null) {
            return null;
        }
        return DocenteResponseDTO.builder()
                .id(docente.getId())
                .nombre(docente.getNombre())
                .apellido(docente.getApellido())
                .sexo(docente.getSexo())
                .fechaNacimiento(docente.getFechaNacimiento())
                .email(docente.getEmail())
                .rol(docente.getRol())
                .activo(docente.isActivo())
                .build();
    }

}
