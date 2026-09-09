package com.club.socios.mapper;

import com.club.socios.domain.RegistroAcceso;
import com.club.socios.dto.RegistroAccesoDTO;
import org.springframework.stereotype.Component;

@Component
public class RegistroAccesoMapper {

    public RegistroAccesoDTO toDTO(RegistroAcceso registro) {
        if (registro == null) {
            return null;
        }
        return RegistroAccesoDTO.builder()
                .id(registro.getId())
                .personaId(registro.getPersona().getId())
                .personaNombreCompleto(registro.getPersona().getNombreCompleto())
                .personaDni(registro.getPersona().getDni())
                .tipo(registro.getTipo())
                .fechaHora(registro.getFechaHora())
                .puntoAcceso(registro.getPuntoAcceso())
                .reconocimientoExitoso(registro.getReconocimientoExitoso())
                .observaciones(registro.getObservaciones())
                .build();
    }
}
