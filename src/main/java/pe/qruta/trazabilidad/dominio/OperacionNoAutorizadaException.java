package pe.qruta.trazabilidad.dominio;


public class OperacionNoAutorizadaException
        extends RuntimeException {


    public OperacionNoAutorizadaException(
            String mensaje
    ) {
        super(mensaje);
    }
}
