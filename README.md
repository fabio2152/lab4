# QRuta – Trazabilidad

![CI](https://github.com/fabio2152/lab4/actions/workflows/ci.yml/badge.svg)

## Nombre del proyecto

**QRuta – Trazabilidad** (`qruta-trazabilidad`)

## Integrantes

- Fabio Ezequiel Malpartida Lema
- Piero Anghelo Pittman Tolentino

## Metodología asignada

**TDD – Test-Driven Development**, aplicando el ciclo RED → GREEN → REFACTOR.
El registro de cada ciclo se encuentra en [docs/bitacora-tdd.md](docs/bitacora-tdd.md) y las capturas en [docs/evidencias/](docs/evidencias/).

## Problema seleccionado

Por definir.

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje de programación |
| Spring Boot 4.1 | Framework de la aplicación |
| Spring Web (`spring-boot-starter-web`) | Capa HTTP / API REST |
| JUnit 5 + AssertJ + Mockito (`spring-boot-starter-test`) | Pruebas automatizadas |
| Maven + Maven Wrapper | Construcción y gestión de dependencias |
| JaCoCo | Reporte de cobertura de pruebas |
| GitHub Actions | Integración continua (`./mvnw test` en cada push) |

## Instrucciones de instalación

Requisitos previos:

- JDK 21 (verificar con `java -version`).
- Git.
- No es necesario instalar Maven: el proyecto incluye el Maven Wrapper (`mvnw` / `mvnw.cmd`).

Clonar el repositorio:

```bash
git clone https://github.com/fabio2152/lab4.git
cd lab4
```

## Instrucciones de ejecución

Linux / macOS / Git Bash:

```bash
./mvnw spring-boot:run
```

Windows (CMD / PowerShell):

```bash
mvnw.cmd spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

## Procedimiento para ejecutar las pruebas

Linux / macOS / Git Bash:

```bash
./mvnw test
```

Windows (CMD / PowerShell):

```bash
mvnw.cmd test
```

Al terminar, el reporte de cobertura de JaCoCo se genera en `target/site/jacoco/index.html`.

Las pruebas también se ejecutan automáticamente en GitHub Actions con cada `push` (ver [.github/workflows/ci.yml](.github/workflows/ci.yml)).

## Estructura del proyecto

```text
lab4/
├── .github/workflows/ci.yml   # Integración continua
├── docs/
│   ├── bitacora-tdd.md        # Registro de ciclos RED-GREEN-REFACTOR
│   └── evidencias/            # Capturas de pantalla
├── src/
│   ├── main/java/pe/qruta/trazabilidad/   # Código de producción
│   └── test/java/pe/qruta/trazabilidad/   # Pruebas automatizadas
├── pom.xml
├── mvnw / mvnw.cmd
└── README.md
```

## Convención de commits

El historial de Git se usa como evidencia del proceso TDD:

| Prefijo | Fase | Ejemplo |
|---|---|---|
| `test:` | RED – prueba que falla | `test: crear prueba para calcular total` |
| `feat:` | GREEN – implementación mínima | `feat: implementar calculo de total` |
| `refactor:` | REFACTOR – mejora sin cambiar comportamiento | `refactor: separar calculadora de descuentos` |
| `docs:` | Documentación y evidencias | `docs: registrar ciclo 1 en bitacora` |
| `chore:` | Configuración del entorno | `chore: configurar GitHub Actions` |
