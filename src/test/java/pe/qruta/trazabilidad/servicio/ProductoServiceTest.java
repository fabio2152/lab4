package pe.qruta.trazabilidad.servicio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pe.qruta.trazabilidad.dominio.CambioEstado;
import pe.qruta.trazabilidad.dominio.EstadoProducto;
import pe.qruta.trazabilidad.dominio.Producto;
import pe.qruta.trazabilidad.dominio.Rol;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.ProductoRepositorioEnMemoria;


class ProductoServiceTest {

    private static final ZoneId ZONA_LIMA =
        ZoneId.of("America/Lima");

    private static final LocalDateTime OCHO_DE_LA_MANANA =
        LocalDateTime.of(2026, 9, 1, 8, 0);

    private Usuario productor;

    private Usuario intermediario;

    private Usuario otroIntermediario;

    private ProductoService servicio;


    @BeforeEach
    void prepararServicio() {

        productor =
            new Usuario("productor1", Rol.PRODUCTOR);

        intermediario =
            new Usuario("intermediario1", Rol.INTERMEDIARIO);

        otroIntermediario =
            new Usuario("intermediario2", Rol.INTERMEDIARIO);

        Clock relojDelSistema =
            Clock.fixed(
                OCHO_DE_LA_MANANA.atZone(ZONA_LIMA).toInstant(),
                ZONA_LIMA
            );

        servicio =
            new ProductoService(
                new ProductoRepositorioEnMemoria(),
                relojDelSistema
            );
    }


    @Test
    void elProductorCreaUnProductoYQuedaGuardadoConIdEnOrigen() {

        // Dado
        // un productor, un intermediario y el servicio (prepararServicio)

        // Cuando
        Producto producto =
            servicio.crearProducto("Palta Hass", productor, intermediario);

        // Entonces
        assertThat(producto).isNotNull();

        assertThat(producto.getId()).isNotNull();

        Producto guardado =
            servicio.buscarPorId(producto.getId());

        assertThat(guardado).isSameAs(producto);

        assertThat(guardado.getEstado())
            .isEqualTo(EstadoProducto.EN_ORIGEN);
    }


    @Test
    void elReguladorVeTodosLosProductos() {

        // Dado
        Usuario regulador =
            new Usuario("regulador1", Rol.REGULADOR);

        Producto palta =
            servicio.crearProducto("Palta Hass", productor, intermediario);

        Producto mango =
            servicio.crearProducto("Mango Kent", productor, otroIntermediario);

        // Cuando
        List<Producto> productos =
            servicio.listarProductos(regulador);

        // Entonces
        assertThat(productos)
            .containsExactly(palta, mango);
    }


    @Test
    void elProductorVeSoloLosProductosQueCreo() {

        // Dado
        Usuario otroProductor =
            new Usuario("productor2", Rol.PRODUCTOR);

        Producto palta =
            servicio.crearProducto("Palta Hass", productor, intermediario);

        servicio.crearProducto("Mango Kent", otroProductor, intermediario);

        // Cuando
        List<Producto> productos =
            servicio.listarProductos(productor);

        // Entonces
        assertThat(productos)
            .containsExactly(palta);
    }


    @Test
    void elIntermediarioVeSoloSusProductosAsignados() {

        // Dado
        Producto palta =
            servicio.crearProducto("Palta Hass", productor, intermediario);

        servicio.crearProducto("Mango Kent", productor, otroIntermediario);

        // Cuando
        List<Producto> productos =
            servicio.listarProductos(intermediario);

        // Entonces
        assertThat(productos)
            .containsExactly(palta);
    }


    @Test
    void cambiarElEstadoPorIdUsaElRelojDelSistema() {

        // Dado
        Producto palta =
            servicio.crearProducto("Palta Hass", productor, intermediario);

        // Cuando
        servicio.cambiarEstado(
            palta.getId(),
            EstadoProducto.EN_TRANSITO,
            intermediario
        );

        // Entonces
        Producto actualizado =
            servicio.buscarPorId(palta.getId());

        assertThat(actualizado.getEstado())
            .isEqualTo(EstadoProducto.EN_TRANSITO);

        assertThat(actualizado.getHoraInicioTransporte())
            .isEqualTo(OCHO_DE_LA_MANANA);
    }


    @Test
    void cambiarElEstadoDeUnProductoInexistenteSeRechaza() {

        // Dado
        Long idInexistente = 99L;

        // Cuando / Entonces
        assertThatThrownBy(() ->
            servicio.cambiarEstado(
                idInexistente,
                EstadoProducto.EN_TRANSITO,
                intermediario
            )
        )
            .isInstanceOf(ProductoNoEncontradoException.class)
            .hasMessage("No existe un producto con id 99");
    }


    @Test
    void trasDosCambiosElHistorialTieneDosRegistros() {

        // Dado
        Producto palta =
            servicio.crearProducto("Palta Hass", productor, intermediario);

        servicio.cambiarEstado(
            palta.getId(),
            EstadoProducto.EN_TRANSITO,
            intermediario
        );

        servicio.cambiarEstado(
            palta.getId(),
            EstadoProducto.EN_DESTINO,
            intermediario
        );

        // Cuando
        List<CambioEstado> historial =
            servicio.obtenerHistorial(palta.getId());

        // Entonces
        assertThat(historial)
            .containsExactly(
                new CambioEstado(
                    EstadoProducto.EN_ORIGEN,
                    EstadoProducto.EN_TRANSITO,
                    OCHO_DE_LA_MANANA,
                    intermediario
                ),
                new CambioEstado(
                    EstadoProducto.EN_TRANSITO,
                    EstadoProducto.EN_DESTINO,
                    OCHO_DE_LA_MANANA,
                    intermediario
                )
            );
    }
}
