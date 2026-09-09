package com.club.socios.dto;

import com.club.socios.domain.enums.TipoMedioPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de un pago concreto. Contiene TODOS los campos posibles de los 3
 * medios de pago soportados; el service, según {@code tipoMedioPago},
 * arma el subtipo de entidad {@code MedioPago} correspondiente
 * (equivalente a un patrón "Factory" simplificado). Sólo se completan los
 * campos relevantes al medio elegido (validados en la capa de servicio).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDTO {

    private Long id;

    private Long cuotaId;

    @NotNull(message = "Debe indicar el monto del pago")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    private LocalDateTime fechaPago;

    private String observaciones;

    @NotNull(message = "Debe indicar el medio de pago")
    private TipoMedioPago tipoMedioPago;

    // ---- campos específicos de EFECTIVO ----
    private String numeroRecibo;
    private String recibidoPor;

    // ---- campos específicos de TRANSFERENCIA ----
    private String cbuCvu;
    private String entidadBancaria;
    private String numeroOperacion;

    // ---- campos específicos de MERCADO PAGO ----
    private String idTransaccionMp;
    private String emailComprador;
    private String estadoOperacion;

    /** Texto ya resuelto (polimorfismo) para mostrar en listados/comprobantes. */
    private String descripcionMedioPago;
}
