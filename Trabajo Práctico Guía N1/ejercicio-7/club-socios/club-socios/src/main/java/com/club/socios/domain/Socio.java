package com.club.socios.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * ============================================================================
 * SOCIO ---> RELACIÓN UML: HERENCIA (extends Persona)
 * ============================================================================
 * Es el socio TITULAR: la persona que se asocia al club y, opcionalmente,
 * da de alta a su grupo familiar. Al extender Persona hereda nombre,
 * apellido, dni, imagen de rostro y el historial de accesos, y sólo agrega
 * los atributos propios de la membresía.
 *
 * Nueva funcionalidad propuesta en el punto "a" (para justificar la
 * composición con GrupoFamiliar): el socio titular es el único responsable
 * administrativo y económico del grupo familiar completo.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "socio")
@DiscriminatorValue("SOCIO")
public class Socio extends Persona {

    /** Número de socio, visible en la credencial/carnet físico o digital. */
    @Column(name = "numero_socio", unique = true, length = 20)
    private String numeroSocio;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDate fechaAlta;

    /** Categoría social (ej: individual, familiar, vitalicio, cadete) que define el arancel base. */
    @Column(name = "categoria", length = 40)
    private String categoria;

    /**
     * ---------------------------------------------------------------------
     * COMPOSICIÓN: Socio 1 ---- 1 GrupoFamiliar
     * ---------------------------------------------------------------------
     * El grupo familiar NO existe sin su socio titular: se crea junto con
     * el alta del socio y se elimina si el socio se da de baja
     * definitivamente (cascade = ALL, orphanRemoval = true). Es la relación
     * de composición pedida por la consigna para permitir registrar
     * "el socio y su grupo familiar".
     */
    @OneToOne(mappedBy = "socioTitular", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private GrupoFamiliar grupoFamiliar;
}
