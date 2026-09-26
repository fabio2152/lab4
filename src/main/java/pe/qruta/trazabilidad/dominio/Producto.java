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

        validarCambioDeEtapa(nuevoEstado);

        estado = nuevoEstado;

        registrarHora(
            nuevoEstado,
            LocalDateTime.now(reloj)
        );
    }


    private void validarCambioDeEtapa(
            EstadoProducto nuevoEstado
    ) {

        if (estado.esFinal()) {
            throw new CambioDeEstadoNoPermitidoException(
                "El producto ya fue entregado y no puede cambiar de estado"
            );
        }

        if (!estado.puedePasarA(nuevoEstado)) {
            throw new CambioDeEstadoNoPermitidoException(
                "No se puede pasar de " + estado + " a " + nuevoEstado
            );
        }
    }


    private void registrarHora(
            EstadoProducto nuevoEstado,
            LocalDateTime ahora
    ) {

        if (nuevoEstado == EstadoProducto.EN_TRANSITO) {
            horaInicioTransporte = ahora;
        }

        if (nuevoEstado == EstadoProducto.EN_DESTINO) {
            horaLlegada = ahora;
        }
    }


    public LocalDateTime getHoraInicioTransporte() {
        return horaInicioTransporte;
    }


    public LocalDateTime getHoraLlegada() {
        return horaLlegada;
    }


    public Duration getDuracionTransporte() {

        if (horaLlegada == null) {
            throw new DuracionNoDisponibleException(
                "La duración del transporte aún no está disponible: falta la hora de llegada"
            );
        }

        return Duration.between(horaInicioTransporte, horaLlegada);
    }
}
