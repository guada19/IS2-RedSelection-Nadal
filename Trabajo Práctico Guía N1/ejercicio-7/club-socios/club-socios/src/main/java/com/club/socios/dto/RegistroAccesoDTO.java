package com.club.socios.dto;

import com.club.socios.domain.enums.TipoAcceso;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** DTO de un movimiento de entrada/salida, tal como lo pide el enunciado base. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroAccesoDTO {

    private Long id;

    @NotNull(message = "Debe indicar la persona")
    private Long personaId;

    private String personaNombreCompleto;
    private String personaDni;

    private TipoAcceso tipo;
    private LocalDateTime fechaHora;
    private String puntoAcceso;
    private Boolean reconocimientoExitoso;
    private String observaciones;
}
