package pe.qruta.trazabilidad.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ProductoTest {

    private static final ZoneId ZONA_LIMA =
        ZoneId.of("America/Lima");

    private Usuario productor;

    private Usuario intermediario;


    @BeforeEach
    void prepararUsuarios() {

        productor =
            new Usuario("productor1", Rol.PRODUCTOR);

        intermediario =
            new Usuario("intermediario1", Rol.INTERMEDIARIO);
    }


    @Test
    void unProductoRecienCreadoEstaEnOrigen() {

        // Dado
        // un productor y un intermediario (prepararUsuarios)

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
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        LocalDateTime ochoDeLaManana = horaDelDia(8, 0);

        // Cuando
        producto.cambiarEstado(
            EstadoProducto.EN_TRANSITO,
            intermediario,
            relojFijoEn(ochoDeLaManana)
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
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        LocalDateTime ochoDeLaManana = horaDelDia(8, 0);

        LocalDateTime dosYMediaDeLaTarde = horaDelDia(14, 30);

        producto.cambiarEstado(
            EstadoProducto.EN_TRANSITO,
            intermediario,
            relojFijoEn(ochoDeLaManana)
        );

        // Cuando
        producto.cambiarEstado(
            EstadoProducto.EN_DESTINO,
            intermediario,
            relojFijoEn(dosYMediaDeLaTarde)
        );

        // Entonces
        assertThat(producto.getHoraLlegada())
            .isEqualTo(dosYMediaDeLaTarde);

        assertThat(producto.getDuracionTransporte())
            .isEqualTo(Duration.ofHours(6).plusMinutes(30));
    }


    private LocalDateTime horaDelDia(
            int hora,
            int minuto
    ) {
        return LocalDateTime.of(2026, 9, 1, hora, minuto);
    }


    private Clock relojFijoEn(
            LocalDateTime fechaHora
    ) {
        return Clock.fixed(
            fechaHora.atZone(ZONA_LIMA).toInstant(),
            ZONA_LIMA
        );
    }
}
