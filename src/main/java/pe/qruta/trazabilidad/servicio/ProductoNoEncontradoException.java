package pe.qruta.trazabilidad.servicio;


public class ProductoNoEncontradoException
        extends RuntimeException {


    public ProductoNoEncontradoException(
            String mensaje
    ) {
        super(mensaje);
    }
}
