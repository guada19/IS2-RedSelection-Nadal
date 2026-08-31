package com.empresatech.app.config;

import com.empresatech.app.model.Rol;
import com.empresatech.app.model.Usuario;
import com.empresatech.app.repository.RolRepository;
import com.empresatech.app.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolRepository rolRepository,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (rolRepository.count() == 0) {
            Rol adminRole = rolRepository.save(Rol.builder().nombre("ROLE_ADMIN").build());
            Rol userRole = rolRepository.save(Rol.builder().nombre("ROLE_USER").build());

            Set<Rol> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);
        }

        usuarioRepository.findByUsername("admin").ifPresentOrElse(
                user -> {
                    // El usuario admin ya existe; no se duplica.
                },
                () -> {
                    Rol adminRole = rolRepository.findByNombre("ROLE_ADMIN")
                            .orElseThrow(() -> new IllegalStateException("El rol ROLE_ADMIN no existe."));

                    Usuario admin = Usuario.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("admin123"))
                            .enabled(true)
                            .roles(new HashSet<>(Set.of(adminRole)))
                            .build();

                    usuarioRepository.save(admin);
                }
        );
    }
}
