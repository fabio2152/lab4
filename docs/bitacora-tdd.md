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
| T04 | 2 | Error | RE-08 | Pedir la duración estando EN_TRANSITO se rechaza | | | Pendiente |
| T05 | 2 | Límite | RE-06 | Un producto ENTREGADO no puede cambiar de estado | | | Pendiente |
| T06 | 2 | Error | RE-05 | Pasar de EN_ORIGEN a EN_DESTINO se rechaza | | | Pendiente |
| T07 | 3 | Error | RE-04 | El productor no puede cambiar el estado | | | Pendiente |
| T08 | 3 | Error | RE-04 | Un intermediario no asignado no puede cambiar el estado | | | Pendiente |

| Ciclo | Tema | REFACTOR | Estado |
|---|---|---|---|
| 1 | Recorrido normal del producto | [`33b4d0a`](https://github.com/fabio2152/lab4/commit/33b4d0a) | ✅ Completo |
| 2 | Orden de las etapas | | Pendiente |
| 3 | Permisos | | Pendiente |

## Control de requisitos mínimos

- [ ] Al menos 5 pruebas automatizadas (3 de 8).
- [ ] Al menos 3 ciclos RED-GREEN-REFACTOR completos (1 de 3).
- [x] Casos normales (T01, T02, T03).
- [ ] Casos límite (T05).
- [ ] Casos de error (T04, T06, T07, T08).
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
