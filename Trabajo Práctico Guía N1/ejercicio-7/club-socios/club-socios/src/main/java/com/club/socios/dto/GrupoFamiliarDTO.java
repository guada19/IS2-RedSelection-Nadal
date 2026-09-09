package com.club.socios.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/** DTO que agrupa al socio titular junto con su lista de familiares. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrupoFamiliarDTO {

    private Long id;
    private String nombreGrupo;
    private Long socioTitularId;
    private String socioTitularNombreCompleto;
    private List<FamiliarSocioDTO> familiares;
    private int cantidadIntegrantes;
}
