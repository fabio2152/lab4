package pe.qruta.trazabilidad.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pe.qruta.trazabilidad.dominio.Producto;


public class ProductoRepositorioEnMemoria {

    private final List<Producto> productos = new ArrayList<>();

    private long siguienteId = 1;


    public Producto guardar(Producto producto) {

        producto.setId(siguienteId);

        siguienteId++;

        productos.add(producto);

        return producto;
    }


    public Optional<Producto> buscarPorId(Long id) {

        return productos
                .stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst();
    }
}
