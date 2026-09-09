# Club Socios

Sistema de gestión de socios, grupo familiar, control de acceso (entrada/salida
con reconocimiento facial simulado) y cobro de cuotas para un club deportivo.

Trabajo práctico realizado con asistencia de IA, aplicando: arquitectura
**MVC**, vista con **Thymeleaf**, persistencia con **ORM (Spring Data JPA /
Hibernate) sobre MySQL**, transporte de datos entre capas con **DTO**,
**auditoría de entidades** y **seguridad (Spring Security)**.

📄 Ver también:
- [`docs/01-diagrama-clases-y-funcionalidades.md`](docs/01-diagrama-clases-y-funcionalidades.md)
  — punto "a": nuevas funcionalidades y diagrama de clases UML.
- [`load-tests/README-testing.md`](load-tests/README-testing.md)
  — estrategia de testing (unitarias, carga y estrés).

## 1. Arquitectura

```
Thymeleaf (Vista)
       │  DTO
Controller  (MVC - recibe HTTP, delega, elige vista)
       │  DTO
Service     (lógica de negocio, transacciones, @Transactional)
       │  Entidad JPA
Repository  (Spring Data JPA / ORM)
       │
MySQL
```

- **Controller**: `com.club.socios.controller` — nunca accede a
  `Repository` directamente, ni contiene lógica de negocio.
- **Service**: `com.club.socios.service` (+ `impl`) — toda la lógica de
  negocio y las reglas de validación viven acá.
- **Repository**: `com.club.socios.repository` — interfaces de Spring
  Data JPA (ORM).
- **domain**: entidades JPA (con las relaciones UML pedidas: Herencia,
  Composición, Agregación y Asociación — ver `docs/01-...md`).
- **dto** + **mapper**: objetos planos que viajan entre capas, y su
  traducción manual hacia/desde las entidades.
- **audit**: auditoría de entidades (fecha/usuario de creación y
  modificación) vía Spring Data JPA Auditing.
- **security**: Spring Security (login por formulario, roles ADMIN /
  RECEPCIONISTA).

## 2. Cómo ejecutar el proyecto

### Requisitos
- JDK 17+
- Maven 3.9+
- MySQL 8+ corriendo en `localhost:3306`

### Pasos
```bash
# 1) Crear la base de datos
mysql -u root -p -e "CREATE DATABASE club_socios CHARACTER SET utf8mb4;"

# 2) Ajustar usuario/contraseña de MySQL si hace falta
#    (src/main/resources/application.properties)

# 3) Compilar y correr
mvn spring-boot:run
```

La aplicación queda disponible en **http://localhost:8080**.
Al arrancar por primera vez, `DataLoader` precarga automáticamente:

| Usuario     | Contraseña     | Rol             |
|-------------|----------------|-----------------|
| `admin`     | `admin123`     | ROLE_ADMIN      |
| `recepcion` | `recepcion123` | ROLE_RECEPCIONISTA |

... y un socio de ejemplo ("Guadalupe Fernández") con un familiar y una
cuota pendiente, para poder probar el sistema de punta a punta sin cargar
datos a mano.

### Correr los tests
```bash
mvn test
```
Los tests usan el perfil `test` (base **H2 en memoria**), por lo que NO
requieren tener MySQL levantado.

### Pruebas de carga/estrés
Ver [`load-tests/README-testing.md`](load-tests/README-testing.md).

## 3. Funcionalidades implementadas

- Alta / edición / baja lógica de socios, con carga de foto de rostro.
- Alta de integrantes del grupo familiar (con su propia foto de rostro).
- Registro de entrada/salida del club (para el socio y para cada
  familiar), alternando automáticamente ENTRADA/SALIDA según el último
  movimiento.
- Historial de accesos por persona.
- Generación de la cuota mensual del club **por grupo familiar**.
- Registro de pagos (totales o parciales) de la cuota, con 3 medios de
  pago: **Efectivo**, **Transferencia** y **Mercado Pago** (simulado).
- Recalculo automático del estado de la cuota: `PENDIENTE` / `PARCIAL` /
  `PAGADA` / `VENCIDA`.
- Login y autorización por rol (ADMIN puede gestionar cuotas/pagos y
  altas/bajas; RECEPCIONISTA sólo puede consultar y registrar accesos).
- Auditoría automática de quién y cuándo creó/modificó cada registro.

## 4. Estructura de carpetas

```
club-socios/
├── pom.xml
├── docs/
│   └── 01-diagrama-clases-y-funcionalidades.md
├── load-tests/
│   ├── README-testing.md
│   ├── jmeter/plan-club-socios.jmx
│   └── gatling/ClubSociosSimulation.scala
└── src/
    ├── main/
    │   ├── java/com/club/socios/
    │   │   ├── audit/          (auditoría de entidades)
    │   │   ├── config/         (seguridad, carga de datos demo)
    │   │   ├── controller/     (capa MVC)
    │   │   ├── domain/         (entidades JPA + enums)
    │   │   ├── dto/            (objetos de transferencia)
    │   │   ├── exception/      (excepciones de negocio)
    │   │   ├── mapper/         (Entidad <-> DTO)
    │   │   ├── repository/     (Spring Data JPA)
    │   │   ├── security/       (Spring Security)
    │   │   └── service/        (lógica de negocio) + impl/
    │   └── resources/
    │       ├── application.properties
    │       ├── static/         (assets de la plantilla Spark Admin)
    │       └── templates/      (vistas Thymeleaf)
    └── test/
        ├── java/com/club/socios/
        │   ├── service/        (pruebas unitarias)
        │   └── integration/    (prueba de integración con MockMvc)
        └── resources/application-test.properties
```
