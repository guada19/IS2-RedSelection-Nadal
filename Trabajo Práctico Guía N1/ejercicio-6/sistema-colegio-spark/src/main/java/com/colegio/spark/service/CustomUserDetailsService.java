package com.colegio.spark.service;

import com.colegio.spark.model.Docente;
import com.colegio.spark.repository.DocenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ============================================================================
 *  CustomUserDetailsService      (capa Service - SEGURIDAD)
 * ============================================================================
 * Implementacion del contrato UserDetailsService de Spring Security. Es la
 * pieza que conecta el mecanismo de login de Spring Security con los datos
 * reales de la aplicacion (la tabla "docentes").
 *
 * Cuando un docente envia el formulario de login, Spring Security invoca
 * automaticamente loadUserByUsername(email) para obtener:
 *   - el hash de la contraseña a comparar (DaoAuthenticationProvider hace la
 *     comparacion usando el PasswordEncoder configurado en SecurityConfig),
 *   - los "authorities" (roles) del usuario,
 *   - si la cuenta esta habilitada (enabled) -> se usa el flag Docente.activo,
 *     de forma que un docente dado de baja logicamente NO pueda iniciar sesion
 *     aunque conozca su contraseña.
 *
 * IMPORTANTE (separacion de responsabilidades): la entidad Docente (capa
 * Model) NO implementa UserDetails directamente. Se prefiere construir aqui
 * un org.springframework.security.core.userdetails.User "puro" a partir de
 * los datos del Docente, para no acoplar el modelo de dominio a una interfaz
 * especifica de Spring Security.
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final DocenteRepository docenteRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Docente docente = docenteRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "No existe ningun docente registrado con el correo: " + email));

        List<GrantedAuthority> authorities = List.of(
                // Spring Security exige el prefijo "ROLE_" para que hasRole("ADMIN")
                // (usado en SecurityConfig) funcione correctamente.
                new SimpleGrantedAuthority("ROLE_" + docente.getRol().name())
        );

        return org.springframework.security.core.userdetails.User.builder()
                .username(docente.getEmail())
                .password(docente.getPassword()) // ya viene hasheado con BCrypt
                .authorities(authorities)
                .disabled(!docente.isActivo())
                .build();
    }

}
