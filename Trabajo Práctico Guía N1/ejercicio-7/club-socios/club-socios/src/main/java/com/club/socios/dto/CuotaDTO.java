package com.club.socios.dto;

import com.club.socios.domain.enums.EstadoCuota;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** DTO de la cuota mensual de un grupo familiar, con el detalle de sus pagos. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuotaDTO {

    private Long id;

    @NotNull(message = "Debe indicar el grupo familiar")
    private Long grupoFamiliarId;

    private String nombreGrupoFamiliar;

    @NotBlank(message = "Debe indicar el período (AAAA-MM)")
    private String periodo;

    @NotNull(message = "Debe indicar el monto total")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a cero")
    private BigDecimal montoTotal;

    private BigDecimal montoPagado;

    private EstadoCuota estado;

    private LocalDate fechaVencimiento;

    private List<PagoDTO> pagos;
}
