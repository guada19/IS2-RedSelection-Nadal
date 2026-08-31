package com.nexusai.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * ============================================================================
 * CLASE DE CONFIGURACIÓN (capa transversal, no forma parte del "triángulo"
 * Modelo-Vista-Controlador pero es necesaria para que la CAPA SERVICE pueda
 * trabajar con contraseñas de forma segura).
 * ============================================================================
 * La anotación @Configuration le indica al contenedor IoC de Spring que esta
 * clase declara uno o más @Bean, es decir, objetos cuya creación y ciclo de
 * vida quedan delegados al framework (en vez de instanciarlos con "new" a
 * mano en cada lugar donde se necesiten).
 *
 * Nota de diseño: el enunciado pide un sistema de "usuario y clave" simple
 * (no pide login social, roles ni URLs protegidas por Spring Security), por
 * lo que NO se agrega la dependencia completa de Spring Security. Sin
 * embargo, sí se aplica una buena práctica ineludible: las contraseñas NUNCA
 * se guardan en texto plano en la base de datos. Para eso se usa el
 * algoritmo BCrypt (hash con "salt" incorporado y factor de costo
 * configurable), disponible en el módulo spring-security-crypto que llega
 * transitivamente con spring-boot-starter-web/validation.
 * ============================================================================
 */
@Configuration
public class SeguridadConfig {

    /**
     * Expone un {@link PasswordEncoder} como Bean para que pueda ser
     * inyectado (mediante @Autowired / inyección por constructor) en la capa
     * de servicio (UsuarioServiceImpl), que es quien lo utiliza para:
     *   - Encriptar la clave al registrar un usuario nuevo.
     *   - Comparar la clave ingresada en el login contra el hash guardado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
