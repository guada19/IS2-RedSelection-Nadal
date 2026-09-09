package com.club.socios.service;

import com.club.socios.domain.GrupoFamiliar;
import com.club.socios.dto.FamiliarSocioDTO;
import com.club.socios.dto.GrupoFamiliarDTO;
import org.springframework.web.multipart.MultipartFile;

public interface GrupoFamiliarService {

    GrupoFamiliarDTO obtenerPorSocio(Long socioId);

    GrupoFamiliarDTO obtenerPorId(Long grupoFamiliarId);

    GrupoFamiliar obtenerEntidadPorSocio(Long socioId);

    /** Agrega un integrante (AGREGACIÓN GrupoFamiliar-FamiliarSocio) al grupo del socio dado. */
    FamiliarSocioDTO agregarFamiliar(Long socioId, FamiliarSocioDTO dto, MultipartFile fotoRostro);

    /** Desvincula (no borra) un familiar del grupo. */
    void quitarFamiliar(Long grupoFamiliarId, Long familiarId);
}
