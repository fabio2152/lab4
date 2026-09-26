package pe.qruta.trazabilidad.servicio;

import java.time.Clock;
import java.util.List;

import pe.qruta.trazabilidad.dominio.EstadoProducto;
import pe.qruta.trazabilidad.dominio.Producto;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.ProductoRepositorioEnMemoria;


public class ProductoService {

    private final ProductoRepositorioEnMemoria repositorio;

    private final Clock reloj;


    public ProductoService(
            ProductoRepositorioEnMemoria repositorio,
            Clock reloj
    ) {
        this.repositorio = repositorio;
        this.reloj = reloj;
    }


    public Producto crearProducto(
            String nombre,
            Usuario productor,
            Usuario intermediarioAsignado
    ) {

        Producto producto =
            new Producto(nombre, productor, intermediarioAsignado);

        return repositorio.guardar(producto);
    }


    public Producto buscarPorId(Long id) {

        return repositorio
                .buscarPorId(id)
                .orElse(null);
    }


    public List<Producto> listarProductos(Usuario usuario) {

        return repositorio
                .listarTodos()
                .stream()
                .filter(producto -> producto.esVisiblePara(usuario))
                .toList();
    }


    public void cambiarEstado(
            Long idProducto,
            EstadoProducto nuevoEstado,
            Usuario usuario
    ) {

        Producto producto =
            repositorio
                .buscarPorId(idProducto)
                .orElseThrow(
                    () -> new ProductoNoEncontradoException(
                        "No existe un producto con id " + idProducto
                    )
                );

        producto.cambiarEstado(nuevoEstado, usuario, reloj);
    }
}
