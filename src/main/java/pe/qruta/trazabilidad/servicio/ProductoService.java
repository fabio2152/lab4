package pe.qruta.trazabilidad.servicio;

import java.time.Clock;
import java.util.List;

import pe.qruta.trazabilidad.dominio.Producto;
import pe.qruta.trazabilidad.dominio.Rol;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.ProductoRepositorioEnMemoria;


public class ProductoService {

    private final ProductoRepositorioEnMemoria repositorio;


    public ProductoService(
            ProductoRepositorioEnMemoria repositorio,
            Clock reloj
    ) {
        this.repositorio = repositorio;
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

        List<Producto> todos = repositorio.listarTodos();

        if (usuario.getRol() == Rol.PRODUCTOR) {
            return todos
                    .stream()
                    .filter(producto -> producto.getProductor() == usuario)
                    .toList();
        }

        return todos;
    }
}
