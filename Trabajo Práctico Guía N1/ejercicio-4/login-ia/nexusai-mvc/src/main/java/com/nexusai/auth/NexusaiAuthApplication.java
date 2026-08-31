package com.nexusai.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 * CLASE PRINCIPAL (ENTRY POINT) DE LA APLICACIÓN
 * ============================================================================
 * La anotación {@link SpringBootApplication} es una anotación "compuesta" que
 * combina tres anotaciones fundamentales del framework:
 *
 *   1) @Configuration   -> Indica que esta clase puede definir Beans de Spring.
 *   2) @EnableAutoConfiguration -> Le dice a Spring Boot que configure
 *      automáticamente el contenedor (IoC) en base a las dependencias del
 *      classpath (por ejemplo: al detectar spring-boot-starter-web configura
 *      el DispatcherServlet; al detectar spring-boot-starter-data-jpa
 *      configura el DataSource, el EntityManagerFactory, etc.)
 *   3) @ComponentScan   -> Escanea el paquete actual (com.nexusai.auth) y sus
 *      subpaquetes en busca de clases anotadas con @Controller, @Service,
 *      @Repository, @Component, etc., para registrarlas como Beans
 *      administrados por el contenedor de Inversión de Control (IoC).
 *
 * Al ejecutar main(), Spring Boot:
 *   - Levanta el contenedor de aplicaciones (ApplicationContext).
 *   - Inicia un servidor Tomcat embebido (gracias a spring-boot-starter-web).
 *   - Publica el DispatcherServlet, que es el "Front Controller" del patrón
 *     MVC de Spring: toda petición HTTP entra por acá y es enrutada hacia el
 *     @Controller correspondiente según el @RequestMapping.
 * ============================================================================
 */
@SpringBootApplication
public class NexusaiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusaiAuthApplication.class, args);
    }
}
