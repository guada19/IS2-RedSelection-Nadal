package com.nexusai.auth.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * ============================================================================
 * ENTIDAD "Usuario" — CAPA MODELO (la "M" de MVC) / ORM
 * ============================================================================
 * Esta clase representa, al mismo tiempo:
 *   - Un objeto del dominio del negocio (un usuario del sistema).
 *   - Una tabla de la base de datos MySQL, gracias al mapeo objeto-relacional
 *     (ORM) que realiza Hibernate/JPA a partir de las anotaciones.
 *
 * ANOTACIONES JPA UTILIZADAS:
 *
 *  @Entity              Marca la clase como una entidad persistente: Hibernate
 *                        generará (o validará) una tabla para ella y se
 *                        encargará de traducir cada instancia Java en una fila
 *                        SQL (INSERT/UPDATE/DELETE/SELECT), evitando escribir
 *                        SQL a mano.
 *
 *  @Table(name=...)     Indica el nombre físico de la tabla en MySQL. Si se
 *                        omite, Hibernate usa el nombre de la clase.
 *
 *  @Id                  Marca el atributo que es clave primaria (Primary Key).
 *
 *  @GeneratedValue       Delega en la base de datos la generación del valor de
 *  (strategy=IDENTITY)   la PK. En MySQL esto se traduce en una columna
 *                        AUTO_INCREMENT.
 *
 *  @Column               Permite configurar el nombre físico de la columna,
 *                        si admite nulos (nullable), si debe ser único
 *                        (unique) y su longitud máxima.
 *
 *  @Enumerated / etc.    No se usan acá, pero son parte del mismo mecanismo de
 *                        mapeo ORM para otros tipos de datos.
 * ============================================================================
 */
@Entity
@Table(name = "usuarios")
public class Usuario {

    /** Clave primaria autogenerada por MySQL (AUTO_INCREMENT). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Datos personales solicitados por el enunciado. */
    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 80)
    private String apellido;

    /** Documento de identidad. Se marca como único: no puede haber dos altas con el mismo documento. */
    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    /**
     * El enunciado indica: "Se utiliza como usuario del sistema el correo
     * personal". Por eso esta columna es, a la vez, el dato de contacto y el
     * NOMBRE DE USUARIO con el que se inicia sesión. Debe ser único.
     */
    @Column(name = "correo_personal", nullable = false, unique = true, length = 150)
    private String correoPersonal;

    /**
     * Clave de acceso. Jamás se guarda en texto plano: la capa Service la
     * encripta con BCrypt (ver UsuarioServiceImpl) antes de persistirla.
     */
    @Column(name = "clave", nullable = false, length = 255)
    private String clave;

    /**
     * Contador de intentos de login fallidos consecutivos. Se reinicia a 0
     * cuando el usuario ingresa la clave correcta. Cuando llega al máximo
     * configurado (por defecto 3, ver application.properties), la cuenta se
     * bloquea automáticamente.
     */
    @Column(name = "intentos_fallidos", nullable = false)
    private Integer intentosFallidos = 0;

    /**
     * Indica si la cuenta está bloqueada por exceso de intentos fallidos.
     * Mientras esté en true, el login se rechaza aunque la clave sea correcta.
     */
    @Column(name = "bloqueado", nullable = false)
    private Boolean bloqueado = false;

    /** Fecha/hora de alta del usuario, informativa. */
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Constructor vacío EXIGIDO por la especificación JPA: Hibernate necesita
     * poder instanciar la entidad por reflexión (sin argumentos) para luego
     * completarla con los datos leídos de la base.
     */
    public Usuario() {
    }

    /**
     * Callback del ciclo de vida JPA: se ejecuta automáticamente ANTES del
     * primer INSERT de la entidad, para completar la fecha de registro sin
     * tener que hacerlo manualmente en cada punto donde se cree un Usuario.
     */
    @PrePersist
    protected void alPersistir() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.intentosFallidos == null) {
            this.intentosFallidos = 0;
        }
        if (this.bloqueado == null) {
            this.bloqueado = false;
        }
    }

    // ============================ GETTERS Y SETTERS ============================
    // Hibernate (y Thymeleaf al leer atributos en la vista) accede al estado
    // del objeto a través de estos métodos siguiendo la convención JavaBean.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreoPersonal() {
        return correoPersonal;
    }

    public void setCorreoPersonal(String correoPersonal) {
        this.correoPersonal = correoPersonal;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Integer getIntentosFallidos() {
        return intentosFallidos;
    }

    public void setIntentosFallidos(Integer intentosFallidos) {
        this.intentosFallidos = intentosFallidos;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /** Método de conveniencia usado por la vista (Thymeleaf) para el saludo de bienvenida. */
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}
