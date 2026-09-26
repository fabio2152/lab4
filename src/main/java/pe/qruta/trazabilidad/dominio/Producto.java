package pe.qruta.trazabilidad.dominio;


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
}
