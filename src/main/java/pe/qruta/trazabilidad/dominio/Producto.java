package pe.qruta.trazabilidad.dominio;

import java.time.Clock;
import java.time.LocalDateTime;


public class Producto {


    public Producto(
            String nombre,
            Usuario productor,
            Usuario intermediarioAsignado
    ) {
    }


    public EstadoProducto getEstado() {
        return EstadoProducto.EN_ORIGEN;
    }


    public void cambiarEstado(
            EstadoProducto nuevoEstado,
            Usuario usuario,
            Clock reloj
    ) {
    }


    public LocalDateTime getHoraInicioTransporte() {
        return null;
    }
}
