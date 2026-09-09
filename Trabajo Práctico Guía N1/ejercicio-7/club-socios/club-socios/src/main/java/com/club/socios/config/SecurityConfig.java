package com.club.socios.config;

import com.club.socios.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * ============================================================================
 * CONFIGURACIÓN DE SEGURIDAD (Spring Security)
 * ============================================================================
 * - Login por formulario propio (Thymeleaf) en /login.
 * - Autorización por rol: sólo ROLE_ADMIN puede gestionar cuotas/pagos y
 *   dar de alta/baja socios; ROLE_RECEPCIONISTA puede operar el control de
 *   acceso (registrar entradas/salidas) y consultar socios.
 * - Contraseñas hasheadas con BCrypt (nunca texto plano).
 * - CSRF habilitado por defecto (Thymeleaf agrega el token automáticamente
 *   en los formularios vía el tag "th:action").
 * ============================================================================
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos de la plantilla Spark Admin (css/js/imágenes) y login: públicos
                .requestMatchers("/css/**", "/js/**", "/libs/**", "/images/**", "/login", "/webjars/**").permitAll()
                // Sólo administración puede gestionar cuotas/pagos, altas/bajas de
                // socios y la desvinculación de un familiar del grupo
                .requestMatchers("/cuotas/**", "/pagos/**", "/socios/nuevo",
                        "/socios/*/eliminar", "/socios/*/familiares/*/eliminar").hasAuthority("ROLE_ADMIN")
                // El resto de las pantallas requieren estar autenticado (admin o recepcionista)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            // El módulo de reconocimiento facial (webhook simulado) es llamado por un
            // dispositivo externo (molinete), por lo que se exceptúa de CSRF.
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/acceso/webhook"))
            .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
