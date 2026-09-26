package pe.qruta.trazabilidad.dominio;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class ProductoTest {


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
}
