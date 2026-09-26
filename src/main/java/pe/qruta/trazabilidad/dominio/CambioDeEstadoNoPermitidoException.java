package pe.qruta.trazabilidad.dominio;


public class CambioDeEstadoNoPermitidoException
        extends RuntimeException {


    public CambioDeEstadoNoPermitidoException(
            String mensaje
    ) {
        super(mensaje);
    }
}
