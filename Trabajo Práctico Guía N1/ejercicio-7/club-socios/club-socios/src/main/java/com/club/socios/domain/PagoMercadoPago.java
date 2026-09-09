package com.club.socios.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Medio de pago MERCADO PAGO (billetera / checkout online). Subtipo de
 * {@link MedioPago} (herencia). En un caso real, idTransaccionMp e
 * estadoOperacion se completarían con la respuesta del webhook/API de
 * Mercado Pago (ver {@code com.club.socios.service.impl.PagoServiceImpl}
 * para la simulación de esa integración).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("MERCADO_PAGO")
public class PagoMercadoPago extends MedioPago {

    @Column(name = "id_transaccion_mp", length = 60)
    private String idTransaccionMp;

    @Column(name = "email_comprador", length = 120)
    private String emailComprador;

    @Column(name = "estado_operacion", length = 30)
    private String estadoOperacion;

    @Override
    public String describir() {
        return "Mercado Pago - Transacción " + idTransaccionMp + " (" + estadoOperacion + ")";
    }
}
