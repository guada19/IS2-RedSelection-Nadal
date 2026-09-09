package com.club.socios.domain;

import com.club.socios.domain.enums.Parentesco;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * FAMILIAR DEL SOCIO ---> RELACIÓN UML: HERENCIA (extends Persona)
 * ============================================================================
 * Integrante del grupo familiar de un socio (cónyuge, hijo/a, etc.). Al
 * heredar de Persona reutiliza automáticamente el registro de rostro y el
 * historial de accesos, cumpliendo el requisito de que TODO el grupo
 * familiar (no sólo el titular) quede registrado con sus datos y su foto
 * al ingresar/egresar del predio.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "familiar_socio")
@DiscriminatorValue("FAMILIAR")
public class FamiliarSocio extends Persona {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Parentesco parentesco;

    /**
     * ---------------------------------------------------------------------
     * AGREGACIÓN: GrupoFamiliar 1 ---- * FamiliarSocio
     * ---------------------------------------------------------------------
     * El familiar "pertenece a" un grupo familiar (es dueño de la relación
     * mediante esta FK), pero a diferencia de la composición Socio-GrupoFamiliar,
     * aquí el "todo" (GrupoFamiliar) NO es responsable exclusivo del ciclo de
     * vida de la "parte" (FamiliarSocio): un familiar sigue existiendo como
     * Persona en el sistema (con su propio historial de accesos) aunque se
     * lo desvincule del grupo, y de hecho puede reasignarse a otro grupo
     * familiar (p. ej. tras un cambio de titularidad). Por eso NO se usa
     * orphanRemoval y el cascade se limita a PERSIST/MERGE.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "grupo_familiar_id")
    private GrupoFamiliar grupoFamiliar;
}
