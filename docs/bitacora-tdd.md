# Bitácora TDD

Registro de cada ciclo **RED → GREEN → REFACTOR** aplicado en el proyecto.
Cada ciclo debe quedar respaldado por sus commits (`test:` → `feat:` → `refactor:`) y por capturas guardadas en [evidencias/](evidencias/).

## Resumen de ciclos

| # | Requisito / comportamiento | Tipo de caso | Commit RED | Commit GREEN | Commit REFACTOR | Estado |
|---|---|---|---|---|---|---|
| 1 | | Normal / Límite / Error | | | | Pendiente |
| 2 | | | | | | Pendiente |
| 3 | | | | | | Pendiente |

## Control de requisitos mínimos

- [ ] Al menos 5 pruebas automatizadas.
- [ ] Al menos 3 ciclos RED-GREEN-REFACTOR completos.
- [ ] Casos normales.
- [ ] Casos límite.
- [ ] Casos de error.
- [ ] Ejecución completa de todas las pruebas (`./mvnw test`) en verde.
- [ ] Reporte de cobertura JaCoCo.

---

<!--
  Plantilla de ciclo: copiar desde "## Ciclo N" hasta la línea "---" final
  por cada nuevo ciclo y completar todos los campos.
-->

## Ciclo N – <título corto del comportamiento>

**Fecha:** AAAA-MM-DD
**Responsable(s):**
**Requisito que se aborda:**
**Tipo de caso:** Normal / Límite / Error

### Escenario

```text
Dado ...
Cuando ...
Entonces ...
```

### 🔴 RED – Prueba que falla

**Prueba:** `NombreDeLaClaseTest#nombreDelMetodo`

```java
// Código de la prueba escrita ANTES de la implementación
```

**Resultado de la ejecución (`./mvnw test`):**

```text
// Salida mostrando el fallo (FAILURE / error de compilación)
```

**Motivo del fallo esperado:**
**Commit:** `test: ...` (hash: `xxxxxxx`)
**Evidencia:** `evidencias/cicloN-red.png`

### 🟢 GREEN – Implementación mínima

```java
// Código mínimo necesario para que la prueba pase
```

**Resultado de la ejecución (`./mvnw test`):**

```text
// Salida mostrando BUILD SUCCESS / Tests run: X, Failures: 0
```

**¿Por qué es lo mínimo?:**
**Commit:** `feat: ...` (hash: `xxxxxxx`)
**Evidencia:** `evidencias/cicloN-green.png`

### 🔵 REFACTOR – Mejora del diseño

**Qué se mejoró:** (eliminar duplicación, mejorar nombres, separar responsabilidades, etc.)

**Código antes:**

```java
```

**Código después:**

```java
```

**Resultado de la ejecución (`./mvnw test`):**

```text
// Todas las pruebas siguen en verde
```

**Commit:** `refactor: ...` (hash: `xxxxxxx`)
**Evidencia:** `evidencias/cicloN-refactor.png`

### Decisiones de diseño y aprendizajes

- ¿Cómo influyó la prueba en el diseño?
- ¿Qué dificultades aparecieron?

---
