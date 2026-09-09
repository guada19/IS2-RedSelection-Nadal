package com.club.socios.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Medio de pago EFECTIVO. Subtipo de {@link MedioPago} (herencia).
 * Se registra opcionalmente el número de recibo físico entregado en caja.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("EFECTIVO")
public class PagoEfectivo extends MedioPago {

    @Column(name = "numero_recibo", length = 30)
    private String numeroRecibo;

    @Column(name = "recibido_por", length = 100)
    private String recibidoPor;

    @Override
    public String describir() {
        return "Efectivo" + (numeroRecibo != null ? " - Recibo N° " + numeroRecibo : "");
    }
}
