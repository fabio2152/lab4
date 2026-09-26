package pe.qruta.trazabilidad.dominio;


public class DuracionNoDisponibleException
        extends RuntimeException {


    public DuracionNoDisponibleException(
            String mensaje
    ) {
        super(mensaje);
    }
}
