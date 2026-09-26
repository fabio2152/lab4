# Bitácora TDD

Registro de cada ciclo **RED → GREEN → REFACTOR** aplicado en el proyecto.
Funcionalidad: **Actualizar el estado de un producto en la cadena** (CUN-05 — Registrar evento de trazabilidad).

Forma de trabajo: dentro de cada ciclo las pruebas se escriben **de a una** (RED → GREEN) y, cuando todas las pruebas del ciclo están en verde, se hace **un solo REFACTOR**. Cada paso queda respaldado por su commit y por la salida de `./mvnw test` guardada en [evidencias/](evidencias/).

Repositorio: https://github.com/fabio2152/lab4

## Resumen de pruebas

| Prueba | Ciclo | Tipo | Regla | Comportamiento | RED | GREEN | Estado |
|---|---|---|---|---|---|---|---|
| T01 | 1 | Normal | RE-01 | Un producto recién creado está en EN_ORIGEN | [`50337de`](https://github.com/fabio2152/lab4/commit/50337de) | [`f833eb0`](https://github.com/fabio2152/lab4/commit/f833eb0) | ✅ |
| T02 | 1 | Normal | RE-07 | Al pasar a EN_TRANSITO se guarda la hora de inicio | [`e276c29`](https://github.com/fabio2152/lab4/commit/e276c29) | [`61ef540`](https://github.com/fabio2152/lab4/commit/61ef540) | ✅ |
| T03 | 1 | Normal | RE-07, RE-08 | Al pasar a EN_DESTINO se guarda la llegada y la duración es 6 h 30 min | [`e40e9cd`](https://github.com/fabio2152/lab4/commit/e40e9cd) | [`a2bdd9f`](https://github.com/fabio2152/lab4/commit/a2bdd9f) | ✅ |
| T04 | 2 | Error | RE-08 | Pedir la duración estando EN_TRANSITO se rechaza | [`6d5bdf6`](https://github.com/fabio2152/lab4/commit/6d5bdf6) | [`dda7055`](https://github.com/fabio2152/lab4/commit/dda7055) | ✅ |
| T05 | 2 | Límite | RE-06 | Un producto ENTREGADO no puede cambiar de estado | [`0452480`](https://github.com/fabio2152/lab4/commit/0452480) | [`dd8ccc4`](https://github.com/fabio2152/lab4/commit/dd8ccc4) | ✅ |
| T06 | 2 | Error | RE-05 | Pasar de EN_ORIGEN a EN_DESTINO se rechaza | [`f76fa76`](https://github.com/fabio2152/lab4/commit/f76fa76) | [`dc8fa59`](https://github.com/fabio2152/lab4/commit/dc8fa59) | ✅ |
| T07 | 3 | Error | RE-04 | El productor no puede cambiar el estado | | | Pendiente |
| T08 | 3 | Error | RE-04 | Un intermediario no asignado no puede cambiar el estado | | | Pendiente |

| Ciclo | Tema | REFACTOR | Estado |
|---|---|---|---|
| 1 | Recorrido normal del producto | [`33b4d0a`](https://github.com/fabio2152/lab4/commit/33b4d0a) | ✅ Completo |
| 2 | Orden de las etapas | [`15062f8`](https://github.com/fabio2152/lab4/commit/15062f8) | ✅ Completo |
| 3 | Permisos | | Pendiente |

## Control de requisitos mínimos

- [x] Al menos 5 pruebas automatizadas (6 de 8).
- [ ] Al menos 3 ciclos RED-GREEN-REFACTOR completos (2 de 3).
- [x] Casos normales (T01, T02, T03).
- [x] Casos límite (T05).
- [x] Casos de error (T04, T06; faltan T07, T08).
- [x] Ejecución completa de todas las pruebas (`./mvnw test`) en verde al cerrar cada ciclo.
- [x] Reporte de cobertura JaCoCo (`target/site/jacoco/index.html`).

---

## Ciclo 1 – Recorrido normal del producto

**Fecha:** 2026-09-26
**Responsables:** Fabio Ezequiel Malpartida Lema, Piero Anghelo Pittman Tolentino
**Reglas que se abordan:** RE-01, RE-07, RE-08
**Tipo de casos:** Normales

### T01 – Un producto recién creado está en EN_ORIGEN (RE-01)

```text
Dado un productor y un intermediario
Cuando el productor crea el producto "Palta Hass" asignado al intermediario
Entonces el producto está en EN_ORIGEN
```

**🔴 RED** – `ProductoTest#unProductoRecienCreadoEstaEnOrigen`

- Se escribió la prueba primero. Para que compile se crearon solo esqueletos: los enums `Rol` (PRODUCTOR, INTERMEDIARIO) y `EstadoProducto` (EN_ORIGEN), `Usuario` con constructor vacío y `Producto` con constructor vacío y `getEstado()` que devuelve `null`.
- **Qué falló:** la verificación, no la compilación.

```text
Tests run: 1, Failures: 1, Errors: 0
expected: EN_ORIGEN
 but was: null
```

- Commit: `test(ciclo-1): T01 producto recien creado esta en EN_ORIGEN [RED]` ([`50337de`](https://github.com/fabio2152/lab4/commit/50337de))
- Evidencia: [ciclo-1-T01-red.txt](evidencias/ciclo-1-T01-red.txt)

**🟢 GREEN**

- **Código mínimo:** `getEstado()` devuelve la constante `EstadoProducto.EN_ORIGEN` (técnica *fake it*). No se creó un campo `estado` porque ninguna prueba necesitaba todavía que el estado cambiara.

```java
public EstadoProducto getEstado() {
    return EstadoProducto.EN_ORIGEN;
}
```

- Resultado: `Tests run: 1, Failures: 0, Errors: 0` – BUILD SUCCESS.
- Commit: `feat(ciclo-1): T01 producto recien creado esta en EN_ORIGEN [GREEN]` ([`f833eb0`](https://github.com/fabio2152/lab4/commit/f833eb0))
- Evidencia: [ciclo-1-T01-green.txt](evidencias/ciclo-1-T01-green.txt)

### T02 – Al pasar a EN_TRANSITO se guarda la hora de inicio (RE-07)

```text
Dado un producto en EN_ORIGEN y un reloj fijo a las 08:00 del 01/09/2026
Cuando el intermediario asignado lo cambia a EN_TRANSITO
Entonces el producto está en EN_TRANSITO y su hora de inicio del transporte es las 08:00
```

**🔴 RED** – `ProductoTest#alPasarAEnTransitoSeGuardaLaHoraDeInicioDelTransporte`

- Esqueletos agregados: valor `EN_TRANSITO`, método `cambiarEstado(nuevoEstado, usuario, reloj)` vacío y `getHoraInicioTransporte()` que devuelve `null`.
- **Qué falló:** el *fake it* de T01 ya no alcanza; el estado seguía siendo la constante.

```text
Tests run: 2, Failures: 1, Errors: 0
expected: EN_TRANSITO
 but was: EN_ORIGEN
```

- Commit: `test(ciclo-1): T02 al pasar a EN_TRANSITO se guarda la hora de inicio del transporte [RED]` ([`e276c29`](https://github.com/fabio2152/lab4/commit/e276c29))
- Evidencia: [ciclo-1-T02-red.txt](evidencias/ciclo-1-T02-red.txt)

**🟢 GREEN**

- **Código mínimo:** campo `estado` inicializado en EN_ORIGEN, campo `horaInicioTransporte`, y `cambiarEstado` que asigna el estado y guarda la hora del reloj. No se distingue a qué etapa se pasa ni se valida al usuario: ninguna prueba lo exigía aún.

```java
public void cambiarEstado(EstadoProducto nuevoEstado, Usuario usuario, Clock reloj) {
    estado = nuevoEstado;
    horaInicioTransporte = LocalDateTime.now(reloj);
}
```

- Resultado: `Tests run: 2, Failures: 0, Errors: 0` – BUILD SUCCESS.
- Commit: `feat(ciclo-1): T02 al pasar a EN_TRANSITO se guarda la hora de inicio del transporte [GREEN]` ([`61ef540`](https://github.com/fabio2152/lab4/commit/61ef540))
- Evidencia: [ciclo-1-T02-green.txt](evidencias/ciclo-1-T02-green.txt)

### T03 – Al pasar a EN_DESTINO se guarda la llegada y se calcula la duración (RE-07, RE-08)

```text
Dado un producto que pasó a EN_TRANSITO a las 08:00
Cuando el intermediario lo cambia a EN_DESTINO a las 14:30
Entonces la hora de llegada es las 14:30 y la duración del transporte es 6 h 30 min
```

**🔴 RED** – `ProductoTest#alPasarAEnDestinoSeGuardaLaHoraDeLlegadaYSeCalculaLaDuracionDelTransporte`

- Esqueletos agregados: valor `EN_DESTINO`, `getHoraLlegada()` y `getDuracionTransporte()` que devuelven `null`.
- **Qué falló:** la hora de llegada no se guardaba (y el atajo de T02 habría pisado la hora de inicio con las 14:30).

```text
Tests run: 3, Failures: 1, Errors: 0
expected: 2026-09-01T14:30 (java.time.LocalDateTime)
 but was: null
```

- Commit: `test(ciclo-1): T03 al pasar a EN_DESTINO se guarda la hora de llegada y la duracion del transporte [RED]` ([`e40e9cd`](https://github.com/fabio2152/lab4/commit/e40e9cd))
- Evidencia: [ciclo-1-T03-red.txt](evidencias/ciclo-1-T03-red.txt)

**🟢 GREEN**

- **Código mínimo:** campo `horaLlegada`; `cambiarEstado` guarda la hora de inicio solo con EN_TRANSITO y la de llegada solo con EN_DESTINO; la duración se calcula al pedirla con `Duration.between`, sin guardarla en un campo (así nunca queda desfasada de las horas).

```java
if (nuevoEstado == EstadoProducto.EN_TRANSITO) {
    horaInicioTransporte = LocalDateTime.now(reloj);
}

if (nuevoEstado == EstadoProducto.EN_DESTINO) {
    horaLlegada = LocalDateTime.now(reloj);
}

public Duration getDuracionTransporte() {
    return Duration.between(horaInicioTransporte, horaLlegada);
}
```

- Resultado: `Tests run: 3, Failures: 0, Errors: 0` – BUILD SUCCESS.
- Commit: `feat(ciclo-1): T03 al pasar a EN_DESTINO se guarda la hora de llegada y la duracion del transporte [GREEN]` ([`a2bdd9f`](https://github.com/fabio2152/lab4/commit/a2bdd9f))
- Evidencia: [ciclo-1-T03-green.txt](evidencias/ciclo-1-T03-green.txt)

### 🔵 REFACTOR del ciclo 1

**Sugerencia del documento:** separar el cálculo de la duración en un método propio usando `Duration`. Esto ya quedó resuelto en el GREEN de T03 (`getDuracionTransporte()` con `Duration.between`), así que el refactor se centró en la duplicación que quedó.

**Qué se mejoró y por qué:**

1. **Producción – `Producto`:** `LocalDateTime.now(reloj)` se repetía en cada condición. Ahora la hora se toma **una sola vez** y el registro de horas pasa a un método privado `registrarHora(...)`. `cambiarEstado` queda más corto y con una sola responsabilidad visible: cambiar el estado y delegar el registro de la hora.
2. **Pruebas – `ProductoTest`:** la creación de usuarios y de relojes se repetía en las tres pruebas. Se agregó `@BeforeEach prepararUsuarios()` y los métodos auxiliares `horaDelDia(hora, minuto)` y `relojFijoEn(fechaHora)`. Las verificaciones (`assertThat`) de cada prueba **no cambiaron**.

**Código antes:**

```java
public void cambiarEstado(EstadoProducto nuevoEstado, Usuario usuario, Clock reloj) {

    estado = nuevoEstado;

    if (nuevoEstado == EstadoProducto.EN_TRANSITO) {
        horaInicioTransporte = LocalDateTime.now(reloj);
    }

    if (nuevoEstado == EstadoProducto.EN_DESTINO) {
        horaLlegada = LocalDateTime.now(reloj);
    }
}
```

**Código después:**

```java
public void cambiarEstado(EstadoProducto nuevoEstado, Usuario usuario, Clock reloj) {

    estado = nuevoEstado;

    registrarHora(nuevoEstado, LocalDateTime.now(reloj));
}


private void registrarHora(EstadoProducto nuevoEstado, LocalDateTime ahora) {

    if (nuevoEstado == EstadoProducto.EN_TRANSITO) {
        horaInicioTransporte = ahora;
    }

    if (nuevoEstado == EstadoProducto.EN_DESTINO) {
        horaLlegada = ahora;
    }
}
```

- Resultado: `Tests run: 3, Failures: 0, Errors: 0` – BUILD SUCCESS (el comportamiento no cambió).
- Commit: `refactor(ciclo-1): extraer registro de horas y reloj de prueba reutilizable [REFACTOR]` ([`33b4d0a`](https://github.com/fabio2152/lab4/commit/33b4d0a))
- Evidencia: [ciclo-1-refactor.txt](evidencias/ciclo-1-refactor.txt)

### Decisiones de diseño y aprendizajes del ciclo 1

- **El reloj se recibe en `cambiarEstado`** (inyección por parámetro) en lugar de en el constructor. Así cada cambio de estado puede usar su propio reloj fijo (08:00 y 14:30 en T03) sin necesitar un reloj de prueba modificable, y las horas las pone el sistema, no el usuario.
- **El constructor pide productor e intermediario desde T01**, porque así se crea un producto según RE-03. Esto evitó tener que modificar T01 en las pruebas siguientes.
- **Las pruebas guiaron el crecimiento del código:** T01 se resolvió con una constante, T02 obligó a crear el campo `estado`, y T03 obligó a distinguir la etapa para no pisar la hora de inicio. Cada línea de `Producto` apareció porque una prueba la pidió.
- **Pendiente para el ciclo 2:** hoy `getDuracionTransporte()` fallaría con un `NullPointerException` si falta la hora de llegada, y `cambiarEstado` acepta cualquier etapa. Esas reglas (RE-05, RE-06, RE-08) las exigirán T04, T05 y T06.

---

## Ciclo 2 – Orden de las etapas

**Fecha:** 2026-09-26
**Responsables:** Fabio Ezequiel Malpartida Lema, Piero Anghelo Pittman Tolentino
**Reglas que se abordan:** RE-05, RE-06, RE-08
**Tipo de casos:** Error (T04, T06) y límite (T05)

### T04 – Pedir la duración estando EN_TRANSITO se rechaza (RE-08)

```text
Dado un producto que pasó a EN_TRANSITO a las 08:00
Cuando se pide la duración del transporte
Entonces se rechaza con DuracionNoDisponibleException:
  "La duración del transporte aún no está disponible: falta la hora de llegada"
```

**🔴 RED** – `ProductoTest#pedirLaDuracionDeUnProductoEnTransitoSeRechazaPorqueAunNoTieneHoraDeLlegada`

- Esqueleto agregado: clase vacía `DuracionNoDisponibleException extends RuntimeException`. `Producto` no cambió.
- **Qué falló:** se lanzaba un `NullPointerException` genérico desde `Duration.between`, no la excepción del dominio. La prueba destapó un hueco del código de T03.

```text
Tests run: 4, Failures: 1, Errors: 0
Expecting actual throwable to be an instance of:
  pe.qruta.trazabilidad.dominio.DuracionNoDisponibleException
but was:
  java.lang.NullPointerException: temporal
```

- Commit: `test(ciclo-2): T04 pedir la duracion de un producto EN_TRANSITO se rechaza [RED]` ([`6d5bdf6`](https://github.com/fabio2152/lab4/commit/6d5bdf6))
- Evidencia: [ciclo-2-T04-red.txt](evidencias/ciclo-2-T04-red.txt)

**🟢 GREEN**

- **Código mínimo:** constructor con mensaje en la excepción y una guarda en `getDuracionTransporte()` que solo revisa la hora de llegada (no la de inicio: ninguna prueba lo exige y, con RE-05, no se puede llegar a EN_DESTINO sin pasar por EN_TRANSITO).

```java
public Duration getDuracionTransporte() {

    if (horaLlegada == null) {
        throw new DuracionNoDisponibleException(
            "La duración del transporte aún no está disponible: falta la hora de llegada"
        );
    }

    return Duration.between(horaInicioTransporte, horaLlegada);
}
```

- Resultado: `Tests run: 4, Failures: 0, Errors: 0` – BUILD SUCCESS.
- Commit: `feat(ciclo-2): T04 pedir la duracion de un producto EN_TRANSITO se rechaza [GREEN]` ([`dda7055`](https://github.com/fabio2152/lab4/commit/dda7055))
- Evidencia: [ciclo-2-T04-green.txt](evidencias/ciclo-2-T04-green.txt)

### T05 – Un producto ENTREGADO no puede cambiar de estado (RE-06)

```text
Dado un producto que recorrió EN_TRANSITO (08:00), EN_DESTINO (14:30) y ENTREGADO (16:00)
Cuando se intenta pasarlo a EN_DESTINO
Entonces se rechaza con CambioDeEstadoNoPermitidoException:
  "El producto ya fue entregado y no puede cambiar de estado"
```

**🔴 RED** – `ProductoTest#unProductoEntregadoNoPuedeCambiarDeEstado`

- Esqueletos agregados: valor `ENTREGADO` en `EstadoProducto` y clase vacía `CambioDeEstadoNoPermitidoException`. `Producto` no cambió.
- **Qué falló:** no se lanzaba ninguna excepción; el producto entregado cambiaba de estado sin control.

```text
Tests run: 5, Failures: 1, Errors: 0
Expecting code to raise a throwable.
```

- Commit: `test(ciclo-2): T05 un producto ENTREGADO no puede cambiar de estado [RED]` ([`0452480`](https://github.com/fabio2152/lab4/commit/0452480))
- Evidencia: [ciclo-2-T05-red.txt](evidencias/ciclo-2-T05-red.txt)

**🟢 GREEN**

- **Código mínimo:** constructor con mensaje en la excepción y una guarda al inicio de `cambiarEstado` solo para ENTREGADO. Al estar al inicio, un intento rechazado no deja el producto a medio cambiar. Los saltos de etapa se seguían aceptando.

```java
if (estado == EstadoProducto.ENTREGADO) {
    throw new CambioDeEstadoNoPermitidoException(
        "El producto ya fue entregado y no puede cambiar de estado"
    );
}
```

- Resultado: `Tests run: 5, Failures: 0, Errors: 0` – BUILD SUCCESS.
- Commit: `feat(ciclo-2): T05 un producto ENTREGADO no puede cambiar de estado [GREEN]` ([`dd8ccc4`](https://github.com/fabio2152/lab4/commit/dd8ccc4))
- Evidencia: [ciclo-2-T05-green.txt](evidencias/ciclo-2-T05-green.txt)

### T06 – Pasar de EN_ORIGEN directamente a EN_DESTINO se rechaza (RE-05)

```text
Dado un producto recién creado (EN_ORIGEN)
Cuando se intenta pasarlo directamente a EN_DESTINO
Entonces se rechaza con CambioDeEstadoNoPermitidoException:
  "No se puede pasar de EN_ORIGEN a EN_DESTINO"
```

**🔴 RED** – `ProductoTest#pasarDeEnOrigenDirectamenteAEnDestinoSeRechaza`

- No hizo falta ningún esqueleto: la prueba reutiliza la excepción de T05.
- **Qué falló:** el salto de etapa se aceptaba sin control.

```text
Tests run: 6, Failures: 1, Errors: 0
Expecting code to raise a throwable.
```

- Commit: `test(ciclo-2): T06 pasar de EN_ORIGEN directamente a EN_DESTINO se rechaza [RED]` ([`f76fa76`](https://github.com/fabio2152/lab4/commit/f76fa76))
- Evidencia: [ciclo-2-T06-red.txt](evidencias/ciclo-2-T06-red.txt)

**🟢 GREEN**

- **Código mínimo:** una condición que enumera a mano los tres pasos válidos. Se descartó rechazar solo el caso literal EN_ORIGEN → EN_DESTINO: con eso, el refactor sugerido (que cada etapa sepa su siguiente) habría empezado a rechazar otros saltos, y **un refactor no puede cambiar el comportamiento**. Por eso GREEN implementa RE-05 completa, pero con condiciones repetidas.

```java
boolean esLaSiguienteEtapa =
    (estado == EstadoProducto.EN_ORIGEN
        && nuevoEstado == EstadoProducto.EN_TRANSITO)
    || (estado == EstadoProducto.EN_TRANSITO
        && nuevoEstado == EstadoProducto.EN_DESTINO)
    || (estado == EstadoProducto.EN_DESTINO
        && nuevoEstado == EstadoProducto.ENTREGADO);

if (!esLaSiguienteEtapa) {
    throw new CambioDeEstadoNoPermitidoException(
        "No se puede pasar de " + estado + " a " + nuevoEstado
    );
}
```

- Resultado: `Tests run: 6, Failures: 0, Errors: 0` – BUILD SUCCESS.
- Commit: `feat(ciclo-2): T06 pasar de EN_ORIGEN directamente a EN_DESTINO se rechaza [GREEN]` ([`dc8fa59`](https://github.com/fabio2152/lab4/commit/dc8fa59))
- Evidencia: [ciclo-2-T06-green.txt](evidencias/ciclo-2-T06-green.txt)

### 🔵 REFACTOR del ciclo 2

**Sugerencia del documento:** que cada etapa del enum `EstadoProducto` sepa cuál es la siguiente, en lugar de repetir condiciones.

**Qué se mejoró y por qué:**

1. **`EstadoProducto`** ahora conoce el orden de la cadena: `siguiente()`, `esFinal()` y `puedePasarA(nuevoEstado)`. El conocimiento sobre las etapas vive en el enum de las etapas (alta cohesión), y si mañana se agrega una etapa se cambia en un solo lugar.
2. **`Producto`** ya no enumera combinaciones de estados: delega en el enum dentro de un método privado `validarCambioDeEtapa(...)`, y `cambiarEstado` queda en tres pasos legibles: validar, cambiar, registrar la hora.
3. Los mensajes de T05 y T06 no cambiaron; las 6 pruebas siguen en verde sin tocarlas.

**Código antes (`Producto.cambiarEstado`):**

```java
if (estado == EstadoProducto.ENTREGADO) {
    throw new CambioDeEstadoNoPermitidoException(
        "El producto ya fue entregado y no puede cambiar de estado"
    );
}

boolean esLaSiguienteEtapa =
    (estado == EstadoProducto.EN_ORIGEN
        && nuevoEstado == EstadoProducto.EN_TRANSITO)
    || (estado == EstadoProducto.EN_TRANSITO
        && nuevoEstado == EstadoProducto.EN_DESTINO)
    || (estado == EstadoProducto.EN_DESTINO
        && nuevoEstado == EstadoProducto.ENTREGADO);

if (!esLaSiguienteEtapa) {
    throw new CambioDeEstadoNoPermitidoException(
        "No se puede pasar de " + estado + " a " + nuevoEstado
    );
}

estado = nuevoEstado;
```

**Código después:**

```java
// EstadoProducto
public EstadoProducto siguiente() {

    return switch (this) {
        case EN_ORIGEN -> EN_TRANSITO;
        case EN_TRANSITO -> EN_DESTINO;
        case EN_DESTINO -> ENTREGADO;
        case ENTREGADO -> null;
    };
}

public boolean esFinal() {
    return siguiente() == null;
}

public boolean puedePasarA(EstadoProducto nuevoEstado) {
    return siguiente() == nuevoEstado;
}


// Producto
public void cambiarEstado(EstadoProducto nuevoEstado, Usuario usuario, Clock reloj) {

    validarCambioDeEtapa(nuevoEstado);

    estado = nuevoEstado;

    registrarHora(nuevoEstado, LocalDateTime.now(reloj));
}

private void validarCambioDeEtapa(EstadoProducto nuevoEstado) {

    if (estado.esFinal()) {
        throw new CambioDeEstadoNoPermitidoException(
            "El producto ya fue entregado y no puede cambiar de estado"
        );
    }

    if (!estado.puedePasarA(nuevoEstado)) {
        throw new CambioDeEstadoNoPermitidoException(
            "No se puede pasar de " + estado + " a " + nuevoEstado
        );
    }
}
```

- Resultado: `Tests run: 6, Failures: 0, Errors: 0` – BUILD SUCCESS (el comportamiento no cambió).
- Commit: `refactor(ciclo-2): cada etapa de EstadoProducto conoce su siguiente [REFACTOR]` ([`15062f8`](https://github.com/fabio2152/lab4/commit/15062f8))
- Evidencia: [ciclo-2-refactor.txt](evidencias/ciclo-2-refactor.txt)

### Decisiones de diseño y aprendizajes del ciclo 2

- **El orden de las pruebas importa:** T05 (ENTREGADO) fue antes que T06 (saltos). Si la regla general de "solo la siguiente etapa" hubiera existido primero, T05 habría pasado sin fallar y no habría RED que mostrar.
- **Las pruebas de error destaparon huecos reales:** T04 mostró que el código de T03 reventaba con un `NullPointerException` sin explicación; ahora hay una excepción del dominio con un mensaje claro.
- **Guardas al inicio de `cambiarEstado`:** una regla incumplida se detecta antes de modificar datos, así el producto nunca queda en un estado intermedio.
- **Límite de "lo mínimo":** en T06 el mínimo no podía ser un caso literal, porque eso habría obligado a que el refactor cambiara comportamiento. Se eligió el mínimo que el refactor pudiera reorganizar sin alterar resultados.
- **Pendiente para el ciclo 3:** el parámetro `usuario` de `cambiarEstado` todavía no se usa; cualquiera puede cambiar el estado. T07 y T08 (RE-04) lo exigirán.

---

<!--
  Plantilla de ciclo: copiar desde "## Ciclo N" hasta la línea "---" final
  por cada nuevo ciclo. Repetir el bloque "### TXX" por cada prueba del ciclo.
-->

## Ciclo N – <tema del ciclo>

**Fecha:** AAAA-MM-DD
**Responsables:**
**Reglas que se abordan:**
**Tipo de casos:** Normal / Límite / Error

### TXX – <comportamiento> (RE-XX)

```text
Dado ...
Cuando ...
Entonces ...
```

**🔴 RED** – `ClaseTest#metodo`

- Esqueletos agregados:
- **Qué falló:**

```text
// Salida de ./mvnw test
```

- Commit: `test(ciclo-N): TXX ... [RED]` (`xxxxxxx`)
- Evidencia: `evidencias/ciclo-N-TXX-red.txt`

**🟢 GREEN**

- **Código mínimo:**

```java
```

- Resultado:
- Commit: `feat(ciclo-N): TXX ... [GREEN]` (`xxxxxxx`)
- Evidencia: `evidencias/ciclo-N-TXX-green.txt`

### 🔵 REFACTOR del ciclo N

**Qué se mejoró y por qué:**

**Código antes:**

```java
```

**Código después:**

```java
```

- Resultado:
- Commit: `refactor(ciclo-N): ... [REFACTOR]` (`xxxxxxx`)

### Decisiones de diseño y aprendizajes del ciclo N

-

---
