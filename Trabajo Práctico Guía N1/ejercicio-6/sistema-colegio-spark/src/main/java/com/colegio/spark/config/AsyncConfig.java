package com.colegio.spark.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * ============================================================================
 *  AsyncConfig                   (capa Config)
 * ============================================================================
 * Habilita el procesamiento de metodos anotados con @Async en toda la
 * aplicacion (por ejemplo, EmailServiceImpl.enviarCorreoBienvenida). Sin esta
 * anotacion, @Async se ignoraria y los metodos se ejecutarian de forma
 * sincronica en el mismo hilo que los invoca.
 * ============================================================================
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
