package pe.qruta.trazabilidad.servicio;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    private ProductoService servicio;


    @BeforeEach
    void prepararServicio() {

        productor =
            new Usuario("productor1", Rol.PRODUCTOR);

        intermediario =
            new Usuario("intermediario1", Rol.INTERMEDIARIO);

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
}
