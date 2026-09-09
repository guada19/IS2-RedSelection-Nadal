package com.club.socios.domain;

import com.club.socios.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * PAGO ---> RELACIONES UML: COMPOSICIÓN (con Cuota y con MedioPago)
 * ============================================================================
 * Representa una transacción concreta de dinero contra una {@link Cuota}.
 * Cada Pago se realiza con exactamente un {@link MedioPago} (Efectivo,
 * Transferencia o Mercado Pago -> polimorfismo/herencia).
 *
 *   Cuota 1 ──────── * Pago            COMPOSICIÓN (ver Cuota.pagos)
 *   Pago  1 ──────── 1 MedioPago       COMPOSICIÓN (el detalle del medio de
 *                                      pago no existe fuera del pago que lo usa)
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pago")
public class Pago extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(length = 255)
    private String observaciones;

    /** Lado "muchos" de la composición Cuota (1) ---- (*) Pago. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuota_id", nullable = false)
    private Cuota cuota;

    /**
     * COMPOSICIÓN 1 a 1 con el detalle polimórfico del medio de pago.
     * cascade = ALL + orphanRemoval = true: el detalle (n° de recibo, CBU,
     * id de transacción de Mercado Pago, etc.) se guarda/borra siempre
     * junto con el Pago dueño.
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "medio_pago_id", nullable = false)
    private MedioPago medioPago;
}
