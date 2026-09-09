package com.club.socios.domain;

import com.club.socios.domain.enums.TipoAcceso;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * REGISTRO DE ACCESO ---> RELACIÓN UML: ASOCIACIÓN (con Persona)
 * ============================================================================
 * Funcionalidad base del enunciado: "al ingresar al club el sistema
 * registra el horario de entrada, lo mismo sucede en caso de salida".
 *
 * Se modela como una ASOCIACIÓN simple (no composición/agregación) porque
 * un registro histórico de acceso tiene entidad e interés propio (se
 * conserva para reportes/estadísticas de asistencia, auditorías de
 * seguridad, etc.) incluso si la persona luego se da de baja del club; es
 * una relación de "uso"/referencia, de bajo acoplamiento, sin que el
 * "todo" (Persona) sea dueño exclusivo del ciclo de vida del "detalle".
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "registro_acceso")
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoAcceso tipo;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    /** Punto físico de control (ej: "Molinete Principal", "Acceso Pileta"). */
    @Column(name = "punto_acceso", length = 60)
    private String puntoAcceso;

    /** Resultado de la verificación biométrica simulada al momento del paso. */
    @Column(name = "reconocimiento_exitoso")
    private Boolean reconocimientoExitoso;

    @Column(length = 255)
    private String observaciones;
}
