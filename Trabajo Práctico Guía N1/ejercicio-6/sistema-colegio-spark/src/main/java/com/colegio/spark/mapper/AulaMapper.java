package com.colegio.spark.mapper;

import com.colegio.spark.dto.response.AulaResponseDTO;
import com.colegio.spark.model.Aula;
import org.springframework.stereotype.Component;

/**
 * Mapper Entidad <-> DTO para Aula.
 * NOTA: toEntity()/actualizarEntity() para Aula viven en AulaServiceImpl en
 * lugar de aca, porque construir la entidad requiere primero BUSCAR el Grado
 * relacionado en su repositorio (GradoRepository) -- una operacion de acceso
 * a datos que le corresponde a la capa Service, no a un Mapper "puro".
 */
@Component
public class AulaMapper {

    public AulaResponseDTO toResponseDTO(Aula aula) {
        if (aula == null) {
            return null;
        }
        return AulaResponseDTO.builder()
                .id(aula.getId())
                .nombre(aula.getNombre())
                .capacidad(aula.getCapacidad())
                .gradoId(aula.getGrado() != null ? aula.getGrado().getId() : null)
                .gradoNombre(aula.getGrado() != null ? aula.getGrado().getNombre() : null)
                .build();
    }

}
