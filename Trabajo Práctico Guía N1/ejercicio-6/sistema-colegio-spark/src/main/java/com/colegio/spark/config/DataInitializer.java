package com.colegio.spark.config;

import com.colegio.spark.model.Docente;
import com.colegio.spark.model.enums.Rol;
import com.colegio.spark.model.enums.Sexo;
import com.colegio.spark.repository.DocenteRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * ============================================================================
 *  DataInitializer               (capa Config - datos iniciales)
 * ============================================================================
 * Como el auto-registro publico (/registro) SIEMPRE crea docentes con rol
 * DOCENTE (ver DocenteServiceImpl.registrarDocente), el sistema necesita al
 * menos UN usuario con rol ADMIN para poder empezar a operar (cargar grados,
 * aulas, materias, etc.) - de lo contrario nadie podria otorgarse ese rol.
 *
 * Esta clase implementa CommandLineRunner: Spring Boot ejecuta su metodo
 * run() automaticamente una sola vez, justo despues de levantar el
 * ApplicationContext. Si todavia no existe ningun docente con rol ADMIN en
 * la base de datos, se crea uno con credenciales de arranque (documentadas
 * en el README.md), pensadas para cambiarse en el primer ingreso desde
 * "Mi perfil > Cambiar contraseña".
 * ============================================================================
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private static final String EMAIL_ADMIN_INICIAL = "admin@colegiospark.edu.ar";
    private static final String PASSWORD_ADMIN_INICIAL = "Admin1234";

    private final DocenteRepository docenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        boolean existeAdmin = docenteRepository.findAll().stream()
                .anyMatch(d -> d.getRol() == Rol.ADMIN);

        if (existeAdmin) {
            return;
        }

        Docente admin = Docente.builder()
                .nombre("Administrador")
                .apellido("Sistema")
                .sexo(Sexo.MASCULINO)
                .fechaNacimiento(LocalDate.of(1990, 1, 1))
                .email(EMAIL_ADMIN_INICIAL)
                .password(passwordEncoder.encode(PASSWORD_ADMIN_INICIAL))
                .rol(Rol.ADMIN)
                .activo(true)
                .build();

        docenteRepository.save(admin);

        log.warn("=========================================================================");
        log.warn(" Se creo un usuario ADMIN inicial:");
        log.warn("   email:    {}", EMAIL_ADMIN_INICIAL);
        log.warn("   password: {}", PASSWORD_ADMIN_INICIAL);
        log.warn(" Por seguridad, cambia esta contraseña desde 'Mi perfil' apenas ingreses.");
        log.warn("=========================================================================");
    }

}
