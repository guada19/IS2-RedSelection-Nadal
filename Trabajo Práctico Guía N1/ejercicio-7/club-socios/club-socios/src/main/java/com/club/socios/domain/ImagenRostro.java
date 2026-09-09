package com.club.socios.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ============================================================================
 * IMAGEN DE ROSTRO ---> RELACIÓN UML: COMPOSICIÓN (parte de Persona)
 * ============================================================================
 * Guarda la referencia a la foto de rostro usada por el sistema de control
 * de acceso (reconocimiento facial en el molinete de entrada/salida).
 *
 * Notar que NO se guarda el binario de la imagen dentro de la fila (no es
 * buena práctica en una base relacional): se persiste sólo la ruta/URL del
 * archivo físico (o de un bucket de almacenamiento), junto con metadatos.
 *
 * La clave primaria de esta tabla ES TAMBIÉN clave foránea hacia Persona
 * (@MapsId): esto refuerza a nivel de modelo que una ImagenRostro no puede
 * existir sin su Persona dueña -> composición 1 a 1 real.
 * ============================================================================
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "imagen_rostro")
public class ImagenRostro {

    @Id
    private Long id;

    @Column(name = "url_archivo", nullable = false, length = 255)
    private String urlArchivo;

    @Column(name = "fecha_captura")
    private LocalDateTime fechaCaptura;

    /** Hash/vector de referencia que usaría el motor de reconocimiento facial (simulado). */
    @Column(name = "hash_biometrico", length = 255)
    private String hashBiometrico;

    /**
     * Dueño de la composición. @MapsId hace que el id de esta entidad sea
     * el mismo que el id de la Persona (comparten clave primaria), modelando
     * la dependencia de existencia propia de una composición.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "persona_id")
    private Persona persona;
}
