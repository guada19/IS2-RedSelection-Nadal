package com.club.socios.mapper;

import com.club.socios.domain.MedioPago;
import com.club.socios.domain.Pago;
import com.club.socios.domain.PagoEfectivo;
import com.club.socios.domain.PagoMercadoPago;
import com.club.socios.domain.PagoTransferencia;
import com.club.socios.domain.enums.TipoMedioPago;
import com.club.socios.dto.PagoDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper de Pago. Es el más particular de todos porque debe "aplanar" la
 * jerarquía polimórfica MedioPago (Efectivo/Transferencia/MercadoPago)
 * dentro de un único DTO plano, y viceversa: a partir del DTO reconstruye
 * el subtipo concreto de entidad correspondiente.
 */
@Component
public class PagoMapper {

    public PagoDTO toDTO(Pago pago) {
        if (pago == null) {
            return null;
        }
        PagoDTO.PagoDTOBuilder builder = PagoDTO.builder()
                .id(pago.getId())
                .cuotaId(pago.getCuota().getId())
                .monto(pago.getMonto())
                .fechaPago(pago.getFechaPago())
                .observaciones(pago.getObservaciones())
                .tipoMedioPago(pago.getMedioPago().getTipo())
                .descripcionMedioPago(pago.getMedioPago().describir());

        // "Downcast" seguro según el tipo real (polimorfismo) para exponer
        // en el DTO sólo los campos propios del subtipo concreto. Se usa
        // "pattern matching for instanceof" (estándar desde Java 16), y NO
        // "pattern matching for switch" a propósito, ya que este último
        // recién se estabilizó en Java 21 y el proyecto compila con Java 17.
        MedioPago medioPago = pago.getMedioPago();
        if (medioPago instanceof PagoEfectivo efectivo) {
            builder.numeroRecibo(efectivo.getNumeroRecibo())
                    .recibidoPor(efectivo.getRecibidoPor());
        } else if (medioPago instanceof PagoTransferencia transferencia) {
            builder.cbuCvu(transferencia.getCbuCvu())
                    .entidadBancaria(transferencia.getEntidadBancaria())
                    .numeroOperacion(transferencia.getNumeroOperacion());
        } else if (medioPago instanceof PagoMercadoPago mercadoPago) {
            builder.idTransaccionMp(mercadoPago.getIdTransaccionMp())
                    .emailComprador(mercadoPago.getEmailComprador())
                    .estadoOperacion(mercadoPago.getEstadoOperacion());
        }
        return builder.build();
    }

    /** Fábrica del subtipo concreto de MedioPago a partir del tipo elegido en el DTO. */
    public MedioPago crearMedioPago(PagoDTO dto) {
        TipoMedioPago tipo = dto.getTipoMedioPago();
        return switch (tipo) {
            case EFECTIVO -> {
                PagoEfectivo p = new PagoEfectivo();
                p.setNumeroRecibo(dto.getNumeroRecibo());
                p.setRecibidoPor(dto.getRecibidoPor());
                yield p;
            }
            case TRANSFERENCIA -> {
                PagoTransferencia p = new PagoTransferencia();
                p.setCbuCvu(dto.getCbuCvu());
                p.setEntidadBancaria(dto.getEntidadBancaria());
                p.setNumeroOperacion(dto.getNumeroOperacion());
                yield p;
            }
            case MERCADO_PAGO -> {
                PagoMercadoPago p = new PagoMercadoPago();
                p.setIdTransaccionMp(dto.getIdTransaccionMp());
                p.setEmailComprador(dto.getEmailComprador());
                p.setEstadoOperacion(dto.getEstadoOperacion() != null ? dto.getEstadoOperacion() : "APROBADO");
                yield p;
            }
        };
    }
}
