package pe.qruta.trazabilidad.repositorio;

import java.util.List;
import java.util.Optional;

import pe.qruta.trazabilidad.dominio.Usuario;


public interface UsuarioRepositorio {

    void guardar(Usuario usuario);

    Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario);

    List<Usuario> listarTodos();
}
