package com.club.socios.domain;

import com.club.socios.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * GRUPO FAMILIAR (entidad "puente" central del ejercicio)
 * ============================================================================
 * Nueva funcionalidad propuesta por el punto "a" del enunciado para poder
 * exhibir, en un mismo diagrama, las 4 relaciones UML pedidas:
 *
 *   Socio  1 ────────── 1  GrupoFamiliar          => COMPOSICIÓN (dueño: Socio)
 *   GrupoFamiliar 1 ──── * FamiliarSocio           => AGREGACIÓN
 *   GrupoFamiliar 1 ──── * Cuota                   => COMPOSICIÓN
 *   Persona (Socio/Familiar) ── * RegistroAcceso   => ASOCIACIÓN
 *   Persona <|-- Socio, Persona <|-- FamiliarSocio => HERENCIA
 *   MedioPago <|-- Efectivo/Transferencia/MercadoPago => HERENCIA
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "grupo_familiar")
public class GrupoFamiliar extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Apodo visible del grupo, ej: "Familia Fernández". Puramente descriptivo. */
    @Column(name = "nombre_grupo", length = 100)
    private String nombreGrupo;

    /**
     * Lado "uno" de la composición Socio-GrupoFamiliar. La FK real
     * (socio_titular_id) vive en esta tabla porque GrupoFamiliar es la
     * "parte" que no puede existir sin su "todo" (el Socio).
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_titular_id", nullable = false, unique = true)
    private Socio socioTitular;

    /**
     * ---------------------------------------------------------------------
     * AGREGACIÓN: GrupoFamiliar 1 ---- * FamiliarSocio
     * ---------------------------------------------------------------------
     * Ver el comentario simétrico en FamiliarSocio.grupoFamiliar. Se
     * declara sin orphanRemoval: quitar un familiar de esta lista y guardar
     * NO borra al familiar de la base, sólo rompe el vínculo (agregación).
     */
    @OneToMany(mappedBy = "grupoFamiliar", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FamiliarSocio> familiares = new ArrayList<>();

    /**
     * ---------------------------------------------------------------------
     * COMPOSICIÓN: GrupoFamiliar 1 ---- * Cuota
     * ---------------------------------------------------------------------
     * Las cuotas mensuales del club pertenecen exclusivamente a este grupo
     * familiar: no tiene sentido de negocio una Cuota "huérfana", por lo
     * que se usa cascade = ALL + orphanRemoval = true.
     */
    @OneToMany(mappedBy = "grupoFamiliar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cuota> cuotas = new ArrayList<>();

    // -------------------------------------------------------------------
    // Métodos de conveniencia para mantener la integridad bidireccional
    // -------------------------------------------------------------------
    public void agregarFamiliar(FamiliarSocio familiar) {
        familiares.add(familiar);
        familiar.setGrupoFamiliar(this);
    }

    public void quitarFamiliar(FamiliarSocio familiar) {
        familiares.remove(familiar);
        familiar.setGrupoFamiliar(null);
    }

    public void agregarCuota(Cuota cuota) {
        cuotas.add(cuota);
        cuota.setGrupoFamiliar(this);
    }

    /** Cantidad total de personas cubiertas por el grupo (titular + familiares). */
    @Transient
    public int getCantidadIntegrantes() {
        return 1 + (familiares == null ? 0 : familiares.size());
    }
}
