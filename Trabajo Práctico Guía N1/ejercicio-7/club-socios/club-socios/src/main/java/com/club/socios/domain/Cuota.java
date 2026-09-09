package com.club.socios.domain;

import com.club.socios.audit.Auditable;
import com.club.socios.domain.enums.EstadoCuota;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * CUOTA ---> RELACIÓN UML: COMPOSICIÓN (con GrupoFamiliar y con Pago)
 * ============================================================================
 * Representa el arancel mensual que debe abonar el GRUPO FAMILIAR completo
 * (no cada persona por separado). Nueva funcionalidad pedida en el
 * enunciado: "registrar el pago de la cuota del club para cada familia".
 *
 * Una Cuota puede saldarse con uno o varios pagos parciales (por eso la
 * colección de {@link Pago}), cada uno realizado con un medio de pago
 * distinto (efectivo, transferencia o Mercado Pago).
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cuota", uniqueConstraints = @UniqueConstraint(columnNames = {"grupo_familiar_id", "periodo"}))
public class Cuota extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Período que cubre la cuota, ej: 2026-09 (setiembre de 2026). */
    @Column(nullable = false, length = 7)
    private String periodo;

    @Column(name = "monto_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoCuota estado = EstadoCuota.PENDIENTE;

    @Column(name = "fecha_vencimiento")
    private java.time.LocalDate fechaVencimiento;

    /** Lado "muchos" de la composición GrupoFamiliar (1) ---- (*) Cuota. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_familiar_id", nullable = false)
    private GrupoFamiliar grupoFamiliar;

    /**
     * ---------------------------------------------------------------------
     * COMPOSICIÓN: Cuota 1 ---- * Pago
     * ---------------------------------------------------------------------
     * Un Pago (una transacción concreta, con su medio de pago) sólo tiene
     * sentido asociado a la cuota que salda: si se elimina la cuota (p. ej.
     * se anuló por un error de carga) se eliminan sus pagos en cascada.
     */
    @OneToMany(mappedBy = "cuota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Pago> pagos = new ArrayList<>();

    public void agregarPago(Pago pago) {
        pagos.add(pago);
        pago.setCuota(this);
    }

    /** Recalcula el estado de la cuota en base a la suma de pagos registrados. */
    @Transient
    public BigDecimal getMontoPagado() {
        return pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public YearMonth getPeriodoComoYearMonth() {
        return YearMonth.parse(periodo);
    }
}
