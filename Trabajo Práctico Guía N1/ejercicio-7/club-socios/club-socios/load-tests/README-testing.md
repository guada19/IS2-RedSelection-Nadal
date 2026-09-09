# Estrategia de Testing - Club Socios

Este proyecto cubre los 3 niveles de testing pedidos por la consigna:
**pruebas unitarias**, **pruebas de carga** y **pruebas de estrés**.

## 1) Pruebas unitarias (`src/test/java/.../service`)

Herramientas: **JUnit 5** + **Mockito** + **AssertJ** (incluidas por
`spring-boot-starter-test`).

Se aíslan los `Service` de sus dependencias (`Repository`, `Mapper`) con
mocks, para probar la lógica de negocio pura, rápido y sin base de datos:

| Clase de test              | Qué valida |
|-----------------------------|------------|
| `SocioServiceTest`           | Rechazo de DNI duplicado; creación automática del `GrupoFamiliar` (composición) al dar de alta un socio; baja lógica (`activo=false`, nunca `delete`). |
| `CuotaServiceTest`           | Rechazo de cuota duplicada (mismo grupo + período); recálculo de estado `PENDIENTE`/`PARCIAL`/`PAGADA`/`VENCIDA`. |
| `PagoServiceTest`            | Rechazo de pago sobre cuota ya saldada; rechazo de pago que supera el saldo pendiente; correcta delegación al recálculo de estado de la cuota. |
| `RegistroAccesoServiceTest`  | Alternancia automática ENTRADA/SALIDA según el último movimiento registrado. |

Ejecución:
```bash
mvn test
```

## 2) Prueba de integración (`src/test/java/.../integration`)

`SocioControllerIntegrationTest` levanta el contexto completo de Spring
(Controller + Service + Repository + Security) contra una base **H2 en
memoria** (perfil `test`, ver `application-test.properties`), usando
`MockMvc`. Verifica:
- que un usuario no autenticado es redirigido a `/login`,
- que `ROLE_ADMIN` puede listar y dar de alta socios,
- que `ROLE_RECEPCIONISTA` NO puede dar de alta socios (403),
- que el alta de un socio válido persiste y redirige correctamente.

## 3) Pruebas de carga y de estrés (`load-tests/`)

Se proveen **dos herramientas equivalentes** (usar la que tenga disponible
la cátedra/el alumno), apuntando a los 3 endpoints más exigidos en el uso
real de un club (login, listado de socios, y sobre todo el registro de
entrada/salida en el molinete, que es la operación de mayor frecuencia):

### a) Apache JMeter — `load-tests/jmeter/plan-club-socios.jmx`
```bash
# Prueba de CARGA (50 usuarios concurrentes, ramp-up 30s)
jmeter -n -t load-tests/jmeter/plan-club-socios.jmx \
       -l resultados-carga.jtl -e -o reporte-carga

# Prueba de ESTRÉS (subir USUARIOS_CONCURRENTES a 500+ vía -J)
jmeter -n -t load-tests/jmeter/plan-club-socios.jmx \
       -JUSUARIOS_CONCURRENTES=500 -JRAMP_UP=20 \
       -l resultados-estres.jtl -e -o reporte-estres
```

### b) Gatling — `load-tests/gatling/ClubSociosSimulation.scala`
```bash
gatling.sh -s simulations.ClubSociosSimulation -DperfilCarga=carga
gatling.sh -s simulations.ClubSociosSimulation -DperfilCarga=estres
```

### Diferencia entre carga y estrés

- **Prueba de CARGA**: simula el volumen de uso **esperado en un día
  normal** del club (ej. 50 usuarios concurrentes entre recepción y
  socios consultando su cuenta) para verificar que el sistema responde
  dentro de los tiempos aceptables (objetivo: p95 < 800 ms, error < 1%).
- **Prueba de ESTRÉS**: aumenta la carga **por encima de lo esperado**
  (300-500+ usuarios concurrentes) de forma progresiva hasta encontrar el
  **punto de quiebre** del sistema (dónde empieza a degradarse el tiempo
  de respuesta o a devolver errores), para conocer el límite real de
  capacidad y dimensionar la infraestructura (pool de conexiones JDBC,
  memoria de la JVM, recursos de MySQL) en consecuencia.

### Recomendaciones de configuración para la prueba de estrés

Antes de una prueba de estrés real, ajustar en `application.properties`:
```properties
spring.datasource.hikari.maximum-pool-size=20
server.tomcat.threads.max=200
```
y monitorear la base de datos (conexiones activas, locks) durante la
ejecución, ya que suele ser el primer cuello de botella del sistema.
