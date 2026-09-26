package pe.qruta.trazabilidad.dominio;

import java.time.LocalDateTime;


public record CambioEstado(
        EstadoProducto estadoAnterior,
        EstadoProducto estadoNuevo,
        LocalDateTime fechaHora,
        Usuario usuario
) {
}
