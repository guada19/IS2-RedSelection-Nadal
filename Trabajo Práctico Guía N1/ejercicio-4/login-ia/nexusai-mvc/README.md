# NexusAI Auth MVC

Sistema de **registro e inicio de sesión** desarrollado como trabajo práctico
académico, aplicando **arquitectura MVC estricta** con **Spring MVC +
Thymeleaf** (vista), **Spring Data JPA / Hibernate** (ORM) y **MySQL** como
motor de base de datos.

La vista reutiliza el diseño del template `nexusai-1.0.0` provisto (colores,
tipografía "Space Grotesk", tarjeta de login/registro, iconos Font Awesome),
adaptado de panel lateral (offcanvas / JS) a páginas Thymeleaf servidas por
el backend, ya que en una app MVC real cada formulario implica una petición
GET/POST genuina contra el servidor (no simulada con JavaScript/localStorage
como en el template original).

## Requisito funcional cubierto

- Alta de usuarios con **Nombre, Apellido, Documento, Fecha de Nacimiento y
  Correo Personal** (+ clave).
- El **correo personal** se usa como **usuario del sistema**.
- Si en el login el correo no está registrado, se **redirige al registro**.
- Si el usuario existe y **equivoca la clave 3 veces, la cuenta se bloquea**
  automáticamente (configurable en `application.properties`).

## Arquitectura (MVC estricto) y capas

```
Vista (Thymeleaf)  <──>  Controller (Spring MVC)  <──>  Service (negocio)  <──>  Repository (Spring Data JPA)  <──>  MySQL
   templates/               controller/                  service/                 repository/                     (ORM/Hibernate)
```

| Capa | Paquete | Responsabilidad |
|---|---|---|
| **Modelo (M)** | `model` | Entidad JPA `Usuario`, mapeada 1:1 a la tabla `usuarios` (ORM). |
| **Vista (V)** | `templates/*.html` | Plantillas Thymeleaf; sólo presentación, sin lógica de negocio. |
| **Controlador (C)** | `controller` | Recibe HTTP, delega en el Service, elige la vista/redirect. |
| **Servicio** | `service` / `service.impl` | Reglas de negocio: alta, login, conteo de intentos, bloqueo. |
| **Repositorio (ORM)** | `repository` | Interfaz `JpaRepository`; Spring Data genera el SQL/JPQL. |
| **DTO** | `dto` | Objetos de formulario (`LoginDTO`, `RegistroUsuarioDTO`), desacoplados de la entidad. |
| **Excepciones de negocio** | `exception` | `UsuarioNoRegistradoException`, `UsuarioBloqueadoException`, `CredencialesInvalidasException`, `DatoYaRegistradoException`. |
| **Configuración** | `config` | Bean `PasswordEncoder` (BCrypt) para hashear claves. |

Cada clase incluye comentarios extensos explicando **qué hace cada anotación**
(`@Entity`, `@Controller`, `@Service`, `@Repository`, `@Valid`, `@Transactional`,
etc.) y por qué se tomó cada decisión de diseño.

## Puesta en marcha

### 1. Requisitos
- JDK 17+
- Maven 3.9+ (o usar el wrapper si se agrega)
- MySQL 8.x en ejecución local (`localhost:3306`)

### 2. Base de datos
No hace falta crear tablas a mano: `spring.jpa.hibernate.ddl-auto=update`
más `createDatabaseIfNotExist=true` en la URL JDBC generan automáticamente
la base `nexusai_auth` y la tabla `usuarios` la primera vez que se levanta
la app. Sólo hay que ajustar usuario/clave de MySQL en
`src/main/resources/application.properties` si no son `root`/`root`:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Ejecutar
```bash
cd nexusai-auth-mvc
mvn spring-boot:run
```

La app queda disponible en **http://localhost:8080** (redirige a `/login`).

### 4. Flujo de prueba
1. Ir a `/login` e intentar ingresar con un correo inexistente → se
   redirige a `/registro` con un aviso.
2. Completar el formulario de registro → vuelve a `/login` con mensaje de
   éxito.
3. Iniciar sesión con el correo y la clave recién creados → se llega a
   `/home`.
4. Volver a `/login`, escribir el correo correcto y una clave incorrecta
   3 veces seguidas → la cuenta queda `bloqueado = true` en la tabla
   `usuarios` y el sistema rechaza el login aunque luego se tipee la clave
   correcta.

## Notas de diseño

- Las claves se almacenan **hasheadas con BCrypt** (`spring-security-crypto`),
  nunca en texto plano.
- No se agregó el starter completo de Spring Security a propósito: el
  enunciado pide una autenticación simple y propia (usuario + clave +
  bloqueo por intentos), controlada explícitamente por el `Service`/
  `Controller`, sin la cadena de filtros que activaría el starter completo.
- El estado de sesión se maneja con `HttpSession` estándar de Servlet API.
- Se aplica el patrón **Post/Redirect/Get** en login y registro exitoso
  para evitar el reenvío del formulario al refrescar el navegador.
