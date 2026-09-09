package com.club.socios.mapper;

import com.club.socios.domain.Cuota;
import com.club.socios.dto.CuotaDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CuotaMapper {

    private final PagoMapper pagoMapper;

    public CuotaMapper(PagoMapper pagoMapper) {
        this.pagoMapper = pagoMapper;
    }

    public CuotaDTO toDTO(Cuota cuota) {
        if (cuota == null) {
            return null;
        }
        List<com.club.socios.dto.PagoDTO> pagos = cuota.getPagos().stream()
                .map(pagoMapper::toDTO)
                .collect(Collectors.toList());

        return CuotaDTO.builder()
                .id(cuota.getId())
                .grupoFamiliarId(cuota.getGrupoFamiliar().getId())
                .nombreGrupoFamiliar(cuota.getGrupoFamiliar().getNombreGrupo())
                .periodo(cuota.getPeriodo())
                .montoTotal(cuota.getMontoTotal())
                .montoPagado(cuota.getMontoPagado())
                .estado(cuota.getEstado())
                .fechaVencimiento(cuota.getFechaVencimiento())
                .pagos(pagos)
                .build();
    }
}
