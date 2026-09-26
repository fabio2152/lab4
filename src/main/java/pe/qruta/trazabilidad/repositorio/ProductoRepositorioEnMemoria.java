package pe.qruta.trazabilidad.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pe.qruta.trazabilidad.dominio.Producto;


public class ProductoRepositorioEnMemoria
        implements ProductoRepositorio {

    private final List<Producto> productos = new ArrayList<>();

    private long siguienteId = 1;


    @Override
    public Producto guardar(Producto producto) {

        producto.setId(siguienteId);

        siguienteId++;

        productos.add(producto);

        return producto;
    }


    @Override
    public Optional<Producto> buscarPorId(Long id) {

        return productos
                .stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst();
    }


    @Override
    public List<Producto> listarTodos() {
        return new ArrayList<>(productos);
    }
}
