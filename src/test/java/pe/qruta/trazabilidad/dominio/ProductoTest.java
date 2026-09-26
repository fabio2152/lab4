package pe.qruta.trazabilidad.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.Test;


class ProductoTest {

    private static final ZoneId ZONA_LIMA =
        ZoneId.of("America/Lima");


    @Test
    void unProductoRecienCreadoEstaEnOrigen() {

        // Dado
        Usuario productor =
            new Usuario("productor1", Rol.PRODUCTOR);

        Usuario intermediario =
            new Usuario("intermediario1", Rol.INTERMEDIARIO);

        // Cuando
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        // Entonces
        assertThat(producto.getEstado())
            .isEqualTo(EstadoProducto.EN_ORIGEN);
    }


    @Test
    void alPasarAEnTransitoSeGuardaLaHoraDeInicioDelTransporte() {

        // Dado
        Usuario productor =
            new Usuario("productor1", Rol.PRODUCTOR);

        Usuario intermediario =
            new Usuario("intermediario1", Rol.INTERMEDIARIO);

        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        LocalDateTime ochoDeLaManana =
            LocalDateTime.of(2026, 9, 1, 8, 0);

        Clock relojALasOcho =
            Clock.fixed(
                ochoDeLaManana.atZone(ZONA_LIMA).toInstant(),
                ZONA_LIMA
            );

        // Cuando
        producto.cambiarEstado(
            EstadoProducto.EN_TRANSITO,
            intermediario,
            relojALasOcho
        );

        // Entonces
        assertThat(producto.getEstado())
            .isEqualTo(EstadoProducto.EN_TRANSITO);

        assertThat(producto.getHoraInicioTransporte())
            .isEqualTo(ochoDeLaManana);
    }


    @Test
    void alPasarAEnDestinoSeGuardaLaHoraDeLlegadaYSeCalculaLaDuracionDelTransporte() {

        // Dado
        Usuario productor =
            new Usuario("productor1", Rol.PRODUCTOR);

        Usuario intermediario =
            new Usuario("intermediario1", Rol.INTERMEDIARIO);

        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        LocalDateTime ochoDeLaManana =
            LocalDateTime.of(2026, 9, 1, 8, 0);

        Clock relojALasOcho =
            Clock.fixed(
                ochoDeLaManana.atZone(ZONA_LIMA).toInstant(),
                ZONA_LIMA
            );

        LocalDateTime dosYMediaDeLaTarde =
            LocalDateTime.of(2026, 9, 1, 14, 30);

        Clock relojALasDosYMedia =
            Clock.fixed(
                dosYMediaDeLaTarde.atZone(ZONA_LIMA).toInstant(),
                ZONA_LIMA
            );

        producto.cambiarEstado(
            EstadoProducto.EN_TRANSITO,
            intermediario,
            relojALasOcho
        );

        // Cuando
        producto.cambiarEstado(
            EstadoProducto.EN_DESTINO,
            intermediario,
            relojALasDosYMedia
        );

        // Entonces
        assertThat(producto.getHoraLlegada())
            .isEqualTo(dosYMediaDeLaTarde);

        assertThat(producto.getDuracionTransporte())
            .isEqualTo(Duration.ofHours(6).plusMinutes(30));
    }
}
