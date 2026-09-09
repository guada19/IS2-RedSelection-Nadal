package com.club.socios.domain;

import com.club.socios.audit.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * PERSONA (superclase abstracta) ---> RELACIÓN UML: HERENCIA
 * ============================================================================
 * Representa los datos comunes a cualquier persona física conocida por el
 * club: tanto el socio titular como cada integrante de su grupo familiar
 * "son un" (is-a) tipo de Persona. Esto es exactamente lo que pide el
 * enunciado: la posibilidad de registrar el socio Y su grupo familiar con
 * los mismos datos base (nombre, DNI, foto de rostro para reconocimiento
 * en el control de acceso, etc.) sin duplicar código ni columnas.
 *
 * Estrategia de mapeo elegida: JOINED (tabla por clase concreta + tabla
 * padre "persona"). Se prefiere sobre SINGLE_TABLE porque Socio y
 * FamiliarSocio tienen atributos propios bien diferenciados (numeroSocio,
 * fechaAlta vs. parentesco) y así se evitan columnas nulas cruzadas.
 *
 *                     Persona (abstracta)
 *                     /              \
 *                 Socio          FamiliarSocio
 *
 * Relaciones que nacen desde Persona:
 *  - Persona 1 ---- 1 ImagenRostro      => COMPOSICIÓN (ver ImagenRostro)
 *  - Persona 1 ---- * RegistroAcceso    => ASOCIACIÓN   (ver RegistroAcceso)
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_persona")
@EqualsAndHashCode(callSuper = false, of = "id")
public abstract class Persona extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String apellido;

    /** Documento de identidad. Único: no puede haber dos personas con el mismo DNI. */
    @Column(nullable = false, unique = true, length = 15)
    private String dni;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(length = 120)
    private String email;

    @Column(length = 30)
    private String telefono;

    /**
     * ---------------------------------------------------------------------
     * COMPOSICIÓN: Persona 1 ---- 1 ImagenRostro
     * ---------------------------------------------------------------------
     * La imagen de rostro NO tiene sentido ni existencia propia sin la
     * persona a la que pertenece: si se elimina la persona, su foto se
     * elimina con ella (cascade = ALL + orphanRemoval = true). Es el
     * ejemplo más claro de composición del modelo: "todo-parte" con
     * dependencia total del ciclo de vida.
     */
    @OneToOne(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ImagenRostro imagenRostro;

    /**
     * ---------------------------------------------------------------------
     * ASOCIACIÓN: Persona 1 ---- * RegistroAcceso
     * ---------------------------------------------------------------------
     * Cada persona puede tener muchos registros de entrada/salida al club,
     * pero un RegistroAcceso es un simple "hecho histórico" con referencia
     * a la persona: no se borra en cascada (si se diera de baja a la
     * persona, el historial de accesos se conserva por trazabilidad). Por
     * eso NO lleva cascade ni orphanRemoval: es una asociación simple, de
     * acoplamiento débil, y no una composición.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
    private List<RegistroAcceso> registrosAcceso = new ArrayList<>();

    public String getNombreCompleto() {
        return apellido + ", " + nombre;
    }
}
