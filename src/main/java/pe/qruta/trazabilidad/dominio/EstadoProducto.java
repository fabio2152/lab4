package pe.qruta.trazabilidad.dominio;


public enum EstadoProducto {
    EN_ORIGEN,
    EN_TRANSITO,
    EN_DESTINO,
    ENTREGADO;


    public EstadoProducto siguiente() {

        return switch (this) {
            case EN_ORIGEN -> EN_TRANSITO;
            case EN_TRANSITO -> EN_DESTINO;
            case EN_DESTINO -> ENTREGADO;
            case ENTREGADO -> null;
        };
    }


    public boolean esFinal() {
        return siguiente() == null;
    }


    public boolean puedePasarA(
            EstadoProducto nuevoEstado
    ) {
        return siguiente() == nuevoEstado;
    }
}
