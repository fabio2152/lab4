package pe.qruta.trazabilidad.servicio;


public class CredencialesInvalidasException
        extends RuntimeException {


    public CredencialesInvalidasException(
            String mensaje
    ) {
        super(mensaje);
    }
}
