package pe.qruta.trazabilidad.repositorio;

import java.util.List;
import java.util.Optional;

import pe.qruta.trazabilidad.dominio.Producto;


public interface ProductoRepositorio {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(Long id);

    List<Producto> listarTodos();
}
