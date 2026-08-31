package com.empresatech.app.repository;

import com.empresatech.app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Utilizado típicamente por la implementación de UserDetailsService
     * de Spring Security para cargar un usuario por su username durante
     * el proceso de login.
     */
    Optional<Usuario> findByUsername(String username);
}
