package pe.qruta.trazabilidad.servicio;

import java.time.Clock;

import pe.qruta.trazabilidad.dominio.Producto;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.ProductoRepositorioEnMemoria;


public class ProductoService {


    public ProductoService(
            ProductoRepositorioEnMemoria repositorio,
            Clock reloj
    ) {
    }


    public Producto crearProducto(
            String nombre,
            Usuario productor,
            Usuario intermediarioAsignado
    ) {
        return null;
    }


    public Producto buscarPorId(Long id) {
        return null;
    }
}
