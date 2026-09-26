package pe.qruta.trazabilidad.dominio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

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


    @Test
    void pedirLaDuracionDeUnProductoEnTransitoSeRechazaPorqueAunNoTieneHoraDeLlegada() {

        // Dado
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        producto.cambiarEstado(
            EstadoProducto.EN_TRANSITO,
            intermediario,
            relojFijoEn(horaDelDia(8, 0))
        );

        // Cuando / Entonces
        assertThatThrownBy(() -> producto.getDuracionTransporte())
            .isInstanceOf(DuracionNoDisponibleException.class)
            .hasMessage(
                "La duración del transporte aún no está disponible: falta la hora de llegada"
            );
    }


    @Test
    void unProductoEntregadoNoPuedeCambiarDeEstado() {

        // Dado
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        producto.cambiarEstado(
            EstadoProducto.EN_TRANSITO,
            intermediario,
            relojFijoEn(horaDelDia(8, 0))
        );

        producto.cambiarEstado(
            EstadoProducto.EN_DESTINO,
            intermediario,
            relojFijoEn(horaDelDia(14, 30))
        );

        producto.cambiarEstado(
            EstadoProducto.ENTREGADO,
            intermediario,
            relojFijoEn(horaDelDia(16, 0))
        );

        // Cuando / Entonces
        assertThatThrownBy(() ->
            producto.cambiarEstado(
                EstadoProducto.EN_DESTINO,
                intermediario,
                relojFijoEn(horaDelDia(17, 0))
            )
        )
            .isInstanceOf(CambioDeEstadoNoPermitidoException.class)
            .hasMessage(
                "El producto ya fue entregado y no puede cambiar de estado"
            );
    }


    @Test
    void pasarDeEnOrigenDirectamenteAEnDestinoSeRechaza() {

        // Dado
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        // Cuando / Entonces
        assertThatThrownBy(() ->
            producto.cambiarEstado(
                EstadoProducto.EN_DESTINO,
                intermediario,
                relojFijoEn(horaDelDia(8, 0))
            )
        )
            .isInstanceOf(CambioDeEstadoNoPermitidoException.class)
            .hasMessage(
                "No se puede pasar de EN_ORIGEN a EN_DESTINO"
            );
    }


    @Test
    void elProductorNoPuedeCambiarElEstadoDeSuProducto() {

        // Dado
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        // Cuando / Entonces
        assertThatThrownBy(() ->
            producto.cambiarEstado(
                EstadoProducto.EN_TRANSITO,
                productor,
                relojFijoEn(horaDelDia(8, 0))
            )
        )
            .isInstanceOf(OperacionNoAutorizadaException.class)
            .hasMessage(
                "Solo un intermediario puede cambiar el estado de un producto"
            );
    }


    @Test
    void unIntermediarioNoAsignadoNoPuedeCambiarElEstado() {

        // Dado
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        Usuario otroIntermediario =
            new Usuario("intermediario2", Rol.INTERMEDIARIO);

        // Cuando / Entonces
        assertThatThrownBy(() ->
            producto.cambiarEstado(
                EstadoProducto.EN_TRANSITO,
                otroIntermediario,
                relojFijoEn(horaDelDia(8, 0))
            )
        )
            .isInstanceOf(OperacionNoAutorizadaException.class)
            .hasMessage(
                "El usuario no está asignado a este producto"
            );
    }


    @Test
    void elIntermediarioAsignadoLeidoDeNuevoPuedeCambiarElEstado() {

        // Dado
        Producto producto =
            new Producto("Palta Hass", productor, intermediario);

        Usuario mismoIntermediarioLeidoDeNuevo =
            new Usuario("intermediario1", Rol.INTERMEDIARIO);

        // Cuando / Entonces
        assertThatCode(() ->
            producto.cambiarEstado(
                EstadoProducto.EN_TRANSITO,
                mismoIntermediarioLeidoDeNuevo,
                relojFijoEn(horaDelDia(8, 0))
            )
        )
            .doesNotThrowAnyException();
    }


    @Test
    void unProductoReconstruidoEnTransitoCalculaLaDuracionAlLlegar() {

        // Dado
        LocalDateTime ochoDeLaManana = horaDelDia(8, 0);

        Producto reconstruido =
            Producto.reconstruir(
                7L,
                "Palta Hass",
                productor,
                intermediario,
                EstadoProducto.EN_TRANSITO,
                ochoDeLaManana,
                null,
                List.of(
                    new CambioEstado(
                        EstadoProducto.EN_ORIGEN,
                        EstadoProducto.EN_TRANSITO,
                        ochoDeLaManana,
                        intermediario
                    )
                )
            );

        // Cuando
        assertThat(reconstruido.getEstado())
            .isEqualTo(EstadoProducto.EN_TRANSITO);

        reconstruido.cambiarEstado(
            EstadoProducto.EN_DESTINO,
            intermediario,
            relojFijoEn(horaDelDia(14, 30))
        );

        // Entonces
        assertThat(reconstruido.getId()).isEqualTo(7L);

        assertThat(reconstruido.getDuracionTransporte())
            .isEqualTo(Duration.ofHours(6).plusMinutes(30));

        assertThat(reconstruido.getHistorial()).hasSize(2);
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
