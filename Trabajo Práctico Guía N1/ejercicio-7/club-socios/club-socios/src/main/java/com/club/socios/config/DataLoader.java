package com.club.socios.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ============================================================================
 * CARGA INICIAL DE DATOS (sólo con fines de demostración/pruebas manuales)
 * ============================================================================
 * Este arranque (CommandLineRunner) NO es transaccional por sí mismo: sólo
 * delega la siembra de datos en {@link DemoDataSeeder}, cuyo método está
 * anotado con @Transactional para que toda la carga (usuarios + socio +
 * grupo familiar + familiar + cuota) ocurra en una única transacción/sesión
 * de Hibernate. Ver el javadoc de {@link DemoDataSeeder} para el detalle.
 * ============================================================================
 */
@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final DemoDataSeeder demoDataSeeder;

    @Bean
    public CommandLineRunner cargarDatosIniciales() {
        return args -> demoDataSeeder.sembrarDatos();
    }
}
