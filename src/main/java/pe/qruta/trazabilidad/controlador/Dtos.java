package pe.qruta.trazabilidad.controlador;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import pe.qruta.trazabilidad.dominio.CambioEstado;
import pe.qruta.trazabilidad.dominio.EstadoProducto;
import pe.qruta.trazabilidad.dominio.Producto;
import pe.qruta.trazabilidad.dominio.Usuario;


/**
 * Objetos que entran y salen de la API REST.
 * Separan el formato JSON de las clases del dominio.
 */
final class Dtos {

    private static final DateTimeFormatter FORMATO_HORA =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");


    private Dtos() {
    }


    record LoginSolicitud(
            String nombreUsuario,
            String contrasena
    ) {
    }


    record CrearProductoSolicitud(
            String nombre,
            String intermediario
    ) {
    }


    record CambioEstadoSolicitud(
            String estado
    ) {
    }


    record UsuarioRespuesta(
            String nombreUsuario,
            String rol
    ) {

        static UsuarioRespuesta desde(Usuario usuario) {
            return new UsuarioRespuesta(
                usuario.getNombreUsuario(),
                usuario.getRol().name()
            );
        }
    }


    record ProductoRespuesta(
            Long id,
            String nombre,
            String productor,
            String intermediario,
            String estado,
            String siguienteEstado,
            String horaInicioTransporte,
            String horaLlegada,
            String duracionTransporte
    ) {

        static ProductoRespuesta desde(Producto producto) {

            EstadoProducto siguiente =
                producto.getEstado().siguiente();

            return new ProductoRespuesta(
                producto.getId(),
                producto.getNombre(),
                producto.getProductor().getNombreUsuario(),
                producto.getIntermediarioAsignado().getNombreUsuario(),
                producto.getEstado().name(),
                siguiente == null ? null : siguiente.name(),
                formatear(producto.getHoraInicioTransporte()),
                formatear(producto.getHoraLlegada()),
                producto.getHoraLlegada() == null
                    ? null
                    : formatear(producto.getDuracionTransporte())
            );
        }
    }


    record CambioEstadoRespuesta(
            String estadoAnterior,
            String estadoNuevo,
            String fechaHora,
            String usuario
    ) {

        static CambioEstadoRespuesta desde(CambioEstado cambio) {
            return new CambioEstadoRespuesta(
                cambio.estadoAnterior().name(),
                cambio.estadoNuevo().name(),
                formatear(cambio.fechaHora()),
                cambio.usuario().getNombreUsuario()
            );
        }
    }


    record ErrorRespuesta(
            String mensaje
    ) {
    }


    private static String formatear(LocalDateTime fechaHora) {
        return fechaHora == null ? null : fechaHora.format(FORMATO_HORA);
    }


    private static String formatear(Duration duracion) {
        return duracion.toHours() + " h " + duracion.toMinutesPart() + " min";
    }
}
