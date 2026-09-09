package com.club.socios.domain;

import com.club.socios.domain.enums.TipoMedioPago;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * MEDIO DE PAGO (superclase abstracta) ---> RELACIÓN UML: HERENCIA
 * ============================================================================
 * Segunda jerarquía de herencia del modelo (la primera es Persona). Permite
 * que un {@link Pago} se realice con "distintos medios de pago (Efectivo,
 * Transferencia, Mercado Pago)" tal como pide el enunciado, sin que el
 * resto del sistema (Cuota, Pago, reportes) necesite conocer el subtipo
 * concreto: sólo programa contra la abstracción MedioPago.
 *
 * Estrategia de mapeo: SINGLE_TABLE (una sola tabla "medio_pago" con
 * columna discriminadora "tipo_medio_pago"). Se elige SINGLE_TABLE en
 * lugar de JOINED porque los atributos de cada subtipo son pocos y
 * livianos: prioriza performance de lectura (sin JOINs) sobre
 * normalización estricta.
 *
 *                    MedioPago (abstracta)
 *              /            |              \
 *      PagoEfectivo  PagoTransferencia  PagoMercadoPago
 *
 * Cada subclase, además de heredar, DEBE implementar el método abstracto
 * {@link #describir()} (polimorfismo): cada medio de pago sabe mostrar su
 * propio detalle en los comprobantes y listados.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "medio_pago")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_medio_pago", discriminatorType = DiscriminatorType.STRING)
public abstract class MedioPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Se guarda también como columna explícita (además del discriminador
     * de Hibernate) para poder filtrar/reportar fácilmente por tipo desde
     * consultas nativas o el propio Thymeleaf sin depender del nombre
     * interno de la columna de discriminación.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", insertable = false, updatable = false, length = 20)
    private TipoMedioPago tipo;

    /** Cada subtipo describe su propio comprobante (polimorfismo). */
    public abstract String describir();
}
