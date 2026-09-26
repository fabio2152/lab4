package pe.qruta.trazabilidad.dominio;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;


public class Producto {

    private EstadoProducto estado = EstadoProducto.EN_ORIGEN;

    private LocalDateTime horaInicioTransporte;

    private LocalDateTime horaLlegada;


    public Producto(
            String nombre,
            Usuario productor,
            Usuario intermediarioAsignado
    ) {
    }


    public EstadoProducto getEstado() {
        return estado;
    }


    public void cambiarEstado(
            EstadoProducto nuevoEstado,
            Usuario usuario,
            Clock reloj
    ) {

        estado = nuevoEstado;

        if (nuevoEstado == EstadoProducto.EN_TRANSITO) {
            horaInicioTransporte = LocalDateTime.now(reloj);
        }

        if (nuevoEstado == EstadoProducto.EN_DESTINO) {
            horaLlegada = LocalDateTime.now(reloj);
        }
    }


    public LocalDateTime getHoraInicioTransporte() {
        return horaInicioTransporte;
    }


    public LocalDateTime getHoraLlegada() {
        return horaLlegada;
    }


    public Duration getDuracionTransporte() {
        return Duration.between(horaInicioTransporte, horaLlegada);
    }
}
