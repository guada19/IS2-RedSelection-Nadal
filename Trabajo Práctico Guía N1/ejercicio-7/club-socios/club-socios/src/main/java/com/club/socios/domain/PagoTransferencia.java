package com.club.socios.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Medio de pago TRANSFERENCIA BANCARIA. Subtipo de {@link MedioPago} (herencia).
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("TRANSFERENCIA")
public class PagoTransferencia extends MedioPago {

    @Column(name = "cbu_cvu", length = 22)
    private String cbuCvu;

    @Column(name = "entidad_bancaria", length = 80)
    private String entidadBancaria;

    @Column(name = "numero_operacion", length = 40)
    private String numeroOperacion;

    @Override
    public String describir() {
        return "Transferencia bancaria (" + entidadBancaria + ") - Op. " + numeroOperacion;
    }
}
