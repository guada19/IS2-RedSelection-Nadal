package com.colegio.spark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ============================================================================
 *  CLASE DE ARRANQUE DE LA APLICACION (Application / Bootstrap)
 * ============================================================================
 * Punto de entrada del sistema. Al ejecutar el metodo main():
 *
 *   1) Spring Boot escanea el paquete "com.colegio.spark" y todos sus
 *      subpaquetes (config, model, repository, dto, mapper, service,
 *      controller, exception) buscando clases anotadas con los estereotipos
 *      de Spring (@Component, @Service, @Repository, @Controller, @Configuration...).
 *   2) Levanta el contenedor de Inyeccion de Dependencias (ApplicationContext)
 *      y registra todos los "beans" encontrados.
 *   3) Configura automaticamente (auto-configuration) el DataSource hacia
 *      MySQL, Hibernate/JPA, Thymeleaf, Spring Security y el servidor de
 *      correo, en base a las dependencias declaradas en el pom.xml y a las
 *      propiedades de application.properties.
 *   4) Arranca el servidor web embebido (Tomcat) en el puerto configurado.
 *
 * @SpringBootApplication es una anotacion "compuesta" que equivale a:
 *   - @Configuration      -> esta clase puede declarar beans
 *   - @EnableAutoConfiguration -> activa la configuracion automatica de Spring Boot
 *   - @ComponentScan      -> escanea este paquete y sus subpaquetes
 * ============================================================================
 */
@SpringBootApplication
public class SparkColegioApplication {

    public static void main(String[] args) {
        SpringApplication.run(SparkColegioApplication.class, args);
    }

}
