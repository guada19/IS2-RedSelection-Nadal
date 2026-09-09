package com.club.socios.config;

import com.club.socios.domain.*;
import com.club.socios.domain.enums.EstadoCuota;
import com.club.socios.domain.enums.Parentesco;
import com.club.socios.domain.enums.RolUsuario;
import com.club.socios.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * ============================================================================
 * SIEMBRA DE DATOS DE DEMOSTRACIÓN (usuarios + socio + familia + cuota)
 * ============================================================================
 * IMPORTANTE (bug corregido): todo el método {@link #sembrarDatos()} está
 * anotado con @Transactional a propósito. Un {@code CommandLineRunner} NO
 * abre transacción propia; si cada {@code repository.save(...)} se llamara
 * suelto (sin una transacción común), Spring Data abriría y confirmaría una
 * transacción distinta por cada save(), y las entidades guardadas en un
 * save anterior quedarían "detached" (desconectadas de la sesión de
 * Hibernate) para el siguiente save(). Al relacionarlas en cascada
 * (ej. FamiliarSocio -> GrupoFamiliar, que tiene cascade = PERSIST) eso
 * provoca:
 *   org.springframework.dao.InvalidDataAccessApiUsageException:
 *   detached entity passed to persist: com.club.socios.domain.GrupoFamiliar
 *
 * Al envolver toda la siembra en una única transacción, todas las
 * entidades permanecen "managed" (adjuntas a la misma sesión) durante
 * toda la carga, evitando el problema. El resto de la aplicación no sufre
 * este inconveniente porque cada método de {@code Service} ya está
 * anotado con @Transactional (ver com.club.socios.service.impl.*).
 * ============================================================================
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoDataSeeder {

    private final UsuarioRepository usuarioRepository;
    private final SocioRepository socioRepository;
    private final GrupoFamiliarRepository grupoFamiliarRepository;
    private final FamiliarSocioRepository familiarSocioRepository;
    private final CuotaRepository cuotaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sembrarDatos() {
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNombreCompleto("Administrador del Club");
            admin.setRol(RolUsuario.ROLE_ADMIN);
            usuarioRepository.save(admin);

            Usuario recepcion = new Usuario();
            recepcion.setUsername("recepcion");
            recepcion.setPassword(passwordEncoder.encode("recepcion123"));
            recepcion.setNombreCompleto("Recepción");
            recepcion.setRol(RolUsuario.ROLE_RECEPCIONISTA);
            usuarioRepository.save(recepcion);

            log.info("Usuarios de demostración creados: admin/admin123, recepcion/recepcion123");
        }

        if (socioRepository.count() == 0) {
            Socio socio = new Socio();
            socio.setNombre("Guadalupe");
            socio.setApellido("Fernández");
            socio.setDni("30111222");
            socio.setFechaNacimiento(LocalDate.of(1998, 5, 14));
            socio.setEmail("guada@example.com");
            socio.setTelefono("2610000000");
            socio.setNumeroSocio("S-0001");
            socio.setFechaAlta(LocalDate.now());
            socio.setCategoria("Familiar");
            socio.setActivo(true);
            socioRepository.save(socio);

            GrupoFamiliar grupo = new GrupoFamiliar();
            grupo.setSocioTitular(socio);
            grupo.setNombreGrupo("Familia Fernández");
            grupoFamiliarRepository.save(grupo);

            FamiliarSocio hijo = new FamiliarSocio();
            hijo.setNombre("Tomás");
            hijo.setApellido("Fernández");
            hijo.setDni("50999888");
            hijo.setFechaNacimiento(LocalDate.of(2015, 3, 2));
            hijo.setParentesco(Parentesco.HIJO_A);
            hijo.setActivo(true);
            grupo.agregarFamiliar(hijo);
            familiarSocioRepository.save(hijo);

            Cuota cuota = new Cuota();
            cuota.setPeriodo(YearMonth.now().toString());
            cuota.setMontoTotal(new BigDecimal("25000.00"));
            cuota.setEstado(EstadoCuota.PENDIENTE);
            cuota.setFechaVencimiento(LocalDate.now().plusDays(10));
            grupo.agregarCuota(cuota);
            cuotaRepository.save(cuota);

            log.info("Datos de demostración cargados: socio {} con 1 familiar y 1 cuota pendiente",
                    socio.getNombreCompleto());
        }
    }
}
