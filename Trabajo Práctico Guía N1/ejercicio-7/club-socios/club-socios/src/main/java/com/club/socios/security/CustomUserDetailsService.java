package com.club.socios.security;

import com.club.socios.domain.Usuario;
import com.club.socios.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * ============================================================================
 * SEGURIDAD: carga del usuario para Spring Security
 * ============================================================================
 * Puente entre el modelo de dominio propio ({@link Usuario}, persistido vía
 * JPA/MySQL) y el contrato {@link UserDetails} que Spring Security necesita
 * para autenticar. Se apoya en {@link UsuarioRepository} (ORM).
 * ============================================================================
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .disabled(!usuario.isActivo())
                .authorities(new SimpleGrantedAuthority(usuario.getRol().name()))
                .build();
    }
}
