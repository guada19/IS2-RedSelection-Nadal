package com.colegio.spark.config;

import com.colegio.spark.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ============================================================================
 *  SecurityConfig                (capa Config - SEGURIDAD)
 * ============================================================================
 * Configuracion central de Spring Security. Define:
 *
 *   1) Como se AUTENTICA un docente (DaoAuthenticationProvider: compara el
 *      email/password ingresados contra los datos devueltos por
 *      CustomUserDetailsService, usando BCrypt para verificar el hash).
 *   2) Como se AUTORIZA el acceso a cada URL segun el rol del docente
 *      logueado (ADMIN vs DOCENTE), mediante la cadena de filtros
 *      SecurityFilterChain.
 *   3) La pagina de login personalizada (Thymeleaf) y el comportamiento del
 *      logout.
 *
 * ANOTACIONES:
 *  @Configuration        -> clase de configuracion de Spring (declara beans).
 *  @EnableWebSecurity     -> activa la seguridad web de Spring Security e
 *                            integra la cadena de filtros con el contenedor
 *                            de Servlets.
 *  @EnableMethodSecurity  -> permite, ademas de las reglas por URL definidas
 *                            aqui, usar anotaciones de seguridad a nivel de
 *                            metodo en los Controllers/Services si hiciera
 *                            falta mayor granularidad (ej: @PreAuthorize).
 *  @RequiredArgsConstructor (Lombok) -> genera un constructor con los campos
 *                            "final", lo que permite la Inyeccion de
 *                            Dependencias por constructor (buena practica,
 *                            en lugar de @Autowired en el campo).
 * ============================================================================
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Codificador de contraseñas. BCrypt aplica un algoritmo de hashing
     * adaptativo con "salt" aleatorio incorporado: dos docentes con la misma
     * contraseña tendran hashes distintos en la base de datos, y el proceso
     * es deliberadamente lento para dificultar ataques de fuerza bruta.
     * Se usa tanto al registrar/cambiar una contraseña (DocenteServiceImpl)
     * como, internamente, al validar el login (DaoAuthenticationProvider).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Proveedor de autenticacion basado en el patron DAO: delega en
     * CustomUserDetailsService la busqueda del Docente por email, y usa el
     * PasswordEncoder para comparar la contraseña ingresada contra el hash
     * almacenado. Al registrarse como @Bean, Spring Security lo detecta y
     * lo incorpora automaticamente al AuthenticationManager global sin
     * necesidad de configuracion adicional.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Cadena de filtros de seguridad: define, URL por URL, quien puede
     * acceder a que recurso.
     *
     *  - Recursos publicos (login, auto-registro de docentes, assets
     *    estaticos de la plantilla) -> permitAll().
     *  - Rutas de administracion academica (alumnos, grados, aulas,
     *    materias, gestion de docentes, asignaciones) -> solo ROLE_ADMIN.
     *  - Carga de notas -> ROLE_ADMIN o ROLE_DOCENTE (cada docente solo ve/
     *    edita, a nivel de logica de negocio en NotaServiceImpl, las notas
     *    de SUS propias asignaciones; la autorizacion por URL solo garantiza
     *    que el rol pueda entrar a la seccion).
     *  - El resto de rutas autenticadas -> cualquier usuario logueado
     *    (ADMIN o DOCENTE), por ejemplo /panel y /perfil/**.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF se mantiene HABILITADO (comportamiento por defecto) porque la
                // aplicacion es 100% MVC con formularios HTML renderizados por el
                // servidor (Thymeleaf incluye automaticamente el token csrf oculto
                // en cada <form> gracias a la integracion thymeleaf-extras-springsecurity6).
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/login", "/registro", "/registro/**",
                                "/assets/**", "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/error", "/error/**"
                        ).permitAll()
                        .requestMatchers("/docentes/**", "/alumnos/**", "/grados/**",
                                "/aulas/**", "/materias/**", "/asignaciones/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/notas/**").hasAnyRole("ADMIN", "DOCENTE")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")               // pagina de login propia (Thymeleaf)
                        .loginProcessingUrl("/login")       // URL a la que se envia el POST del form
                        .usernameParameter("email")         // el "username" logico es el email
                        .passwordParameter("password")
                        .defaultSuccessUrl("/panel", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/error/403"));

        return http.build();
    }

}
