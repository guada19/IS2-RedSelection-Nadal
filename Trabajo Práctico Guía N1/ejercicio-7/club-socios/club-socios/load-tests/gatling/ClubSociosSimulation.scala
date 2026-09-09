package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

/**
 * ============================================================================
 * SIMULACIÓN GATLING - Club Socios
 * ============================================================================
 * Alternativa "as code" al plan de JMeter (load-tests/jmeter/plan-club-socios.jmx).
 * Se incluye como referencia/documentación de la estrategia de testing; para
 * ejecutarla se necesita el proyecto Gatling (https://gatling.io/open-source)
 * con este archivo copiado a su carpeta user-files/simulations.
 *
 * Ejecución:
 *   PRUEBA DE CARGA  -> gatling.sh -s simulations.ClubSociosSimulation -DperfilCarga=carga
 *   PRUEBA DE ESTRÉS -> gatling.sh -s simulations.ClubSociosSimulation -DperfilCarga=estres
 *
 * Criterios de aceptación (umbrales típicos para este tipo de sistema):
 *   - Percentil 95 de tiempo de respuesta < 800 ms en la prueba de carga.
 *   - Tasa de error < 1% en la prueba de carga.
 *   - En la prueba de estrés se documenta el punto de quiebre (a partir de
 *     cuántos usuarios concurrentes el error supera el 5% o el p95 supera
 *     los 3 segundos), no se exige que "pase".
 * ============================================================================
 */
class ClubSociosSimulation extends Simulation {

  val baseUrl = "http://localhost:8080"

  val httpProtocol = http
    .baseUrl(baseUrl)
    .acceptHeader("text/html,application/xhtml+xml")
    .userAgentHeader("Gatling-ClubSocios-LoadTest")

  // ---- Escenario 1: Recepción hace login y consulta el listado de socios ----
  val escenarioRecepcion = scenario("Recepcion - Login y consulta de socios")
    .exec(
      http("Login")
        .post("/login")
        .formParam("username", "recepcion")
        .formParam("password", "recepcion123")
        .check(status.in(200, 302))
    )
    .pause(1)
    .exec(
      http("Listado de socios")
        .get("/socios")
        .check(status.is(200))
    )

  // ---- Escenario 2: Registro de acceso (molinete) - operación de mayor frecuencia ----
  val escenarioControlAcceso = scenario("Molinete - Registro de acceso")
    .exec(
      http("Login")
        .post("/login")
        .formParam("username", "recepcion")
        .formParam("password", "recepcion123")
        .check(status.in(200, 302))
    )
    .pause(500.milliseconds)
    .repeat(5) {
      exec(
        http("Registrar acceso socio de prueba")
          .post("/socios/1/acceso")
          .check(status.in(200, 302, 403))
      ).pause(1)
    }

  // Perfil configurable por línea de comandos: -DperfilCarga=carga|estres
  val perfil = System.getProperty("perfilCarga", "carga")

  val inyeccionRecepcion = perfil match {
    case "estres" => rampUsers(500).during(20.seconds) // PRUEBA DE ESTRÉS
    case _        => rampUsers(50).during(30.seconds)  // PRUEBA DE CARGA
  }
  val inyeccionAcceso = perfil match {
    case "estres" => rampUsers(300).during(20.seconds)
    case _        => rampUsers(30).during(30.seconds)
  }

  setUp(
    escenarioRecepcion.inject(inyeccionRecepcion).protocols(httpProtocol),
    escenarioControlAcceso.inject(inyeccionAcceso).protocols(httpProtocol)
  ).assertions(
    global.responseTime.percentile(95).lt(3000),
    global.successfulRequests.percent.gt(90)
  )
}
