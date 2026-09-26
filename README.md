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

**Funcionalidad: actualizar el estado de un producto en la cadena de suministro.**

En una cadena de suministro de alimentos es necesario saber en qué etapa está cada producto, quién lo movió y cuánto tardó el transporte. Si esos datos los ingresa el usuario a mano, se pueden alterar, y si cualquiera puede cambiar el estado, se pierde la confianza en la trazabilidad.

Corresponde al caso de uso de negocio **CUN-05 — Registrar evento de trazabilidad**, en el que participan:

- **Productor:** registra el producto y lo asigna a un intermediario. No puede cambiar su estado.
- **Intermediario (operador logístico):** es el único que puede hacer avanzar el producto por las etapas, y solo los productos que tiene asignados.
- **Regulador (entidad sanitaria):** consulta todos los productos y su historial. No cambia estados.

Cada producto recorre estas etapas, siempre en orden:

```text
EN_ORIGEN  →  EN_TRANSITO  →  EN_DESTINO  →  ENTREGADO
```

**Reglas principales:**

- Todo producto nuevo empieza en EN_ORIGEN.
- El estado avanza a la etapa siguiente, sin saltar etapas ni retroceder; un producto ENTREGADO ya no puede cambiar.
- Solo el intermediario asignado puede cambiar el estado del producto.
- Las horas las registra el sistema, no el usuario: al pasar a EN_TRANSITO se guarda la hora de inicio y al pasar a EN_DESTINO la hora de llegada.
- La duración del transporte (llegada − inicio) solo se puede calcular cuando existen ambas horas.

Cada regla incumplida se rechaza con una excepción propia y un mensaje claro (por ejemplo, *"El usuario no está asignado a este producto"*).

Además, cada producto guarda un **historial** de cambios (estado anterior, estado nuevo, hora y usuario), y cada rol ve solo lo que le corresponde: el productor sus productos, el intermediario los asignados y el regulador todos.

**Alcance actual:** el dominio y el servicio están construidos con TDD en 6 ciclos y 17 pruebas (ver [docs/bitacora-tdd.md](docs/bitacora-tdd.md)), y la plataforma se puede usar desde el navegador. Quedan fuera de esta entrega: temperaturas y alertas, código QR, base de datos real (los datos viven en memoria y se pierden al reiniciar) y seguridad real (cifrado de contraseñas, tokens).

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Java 21 | Lenguaje de programación |
| Spring Boot 4.1 | Framework de la aplicación |
| Spring Web (`spring-boot-starter-web`) | API REST y servidor de la página web |
| HTML + CSS + JavaScript | Página web de la plataforma (sin librerías externas) |
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

Abrir **http://localhost:8080** en el navegador.

### Usuarios de prueba

Se cargan al iniciar la aplicación. Todos usan la contraseña `clave123` (solo para la demostración: no se cifra).

| Usuario | Rol | Qué puede hacer |
|---|---|---|
| `productor1` | PRODUCTOR | Registrar productos y asignarlos a un intermediario; ver los suyos |
| `intermediario1` | INTERMEDIARIO | Avanzar por las etapas los productos que tiene asignados |
| `intermediario2` | INTERMEDIARIO | Igual que el anterior (sirve para mostrar el rechazo por no estar asignado) |
| `regulador1` | REGULADOR | Ver todos los productos, los usuarios y el historial |

### Guion de la demostración

1. Ingresar como `productor1` y registrar "Palta Hass" asignada a `intermediario1`: aparece en EN_ORIGEN.
2. Ingresar como `intermediario2` y, en **Cambiar estado**, intentar pasar el producto #1 a EN_TRANSITO: *"El usuario no está asignado a este producto"*.
3. Ingresar como `intermediario1` y pulsar **Pasar a EN_TRANSITO**: se registra la hora de inicio.
4. En **Cambiar estado**, intentar pasar el producto #1 a ENTREGADO: *"No se puede pasar de EN_TRANSITO a ENTREGADO"*.
5. Pulsar **Pasar a EN_DESTINO**: se registra la llegada y se muestra la duración del transporte.
6. Ingresar como `regulador1` y pulsar **Ver historial**.

### API REST

Todas las peticiones, salvo el login, indican quién las hace con la cabecera `X-Usuario: <nombreUsuario>`.

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/login` | Iniciar sesión (`{"nombreUsuario", "contrasena"}`) |
| `POST` | `/api/productos` | Crear un producto (`{"nombre", "intermediario"}`) |
| `GET` | `/api/productos` | Listar los productos según el rol |
| `PUT` | `/api/productos/{id}/estado` | Cambiar la etapa (`{"estado": "EN_TRANSITO"}`) |
| `GET` | `/api/productos/{id}/historial` | Historial de cambios del producto |
| `GET` | `/api/usuarios` | Productores e intermediarios (solo el regulador) |
| `GET` | `/api/usuarios/intermediarios` | Intermediarios disponibles para asignar |

Las reglas incumplidas responden con su mensaje: `403` (sin permiso), `409` (cambio de etapa no permitido o duración no disponible), `404` (producto inexistente) y `401` (credenciales incorrectas).

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
│   └── evidencias/            # Salidas de ./mvnw test de cada fase y capturas
├── src/
│   ├── main/java/pe/qruta/trazabilidad/
│   │   ├── dominio/           # Producto, EstadoProducto, Usuario, Rol, CambioEstado, excepciones
│   │   ├── servicio/          # ProductoService, LoginService
│   │   ├── repositorio/       # Repositorios en memoria
│   │   ├── controlador/       # API REST
│   │   └── configuracion/     # Beans de Spring, reloj del sistema y usuarios de prueba
│   ├── main/resources/static/ # Página web (index.html, app.js, estilos.css)
│   └── test/java/pe/qruta/trazabilidad/
│       ├── dominio/           # ProductoTest (T01–T08)
│       └── servicio/          # ProductoServiceTest (T09–T15), LoginServiceTest (T16–T17)
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
