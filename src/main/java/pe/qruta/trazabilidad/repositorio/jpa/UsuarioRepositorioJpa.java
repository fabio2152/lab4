package pe.qruta.trazabilidad.repositorio.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorio;


/** Implementación de UsuarioRepositorio sobre PostgreSQL con Spring Data JPA. */
@Repository
@Transactional
public class UsuarioRepositorioJpa
        implements UsuarioRepositorio {

    private final UsuarioJpa jpa;


    public UsuarioRepositorioJpa(UsuarioJpa jpa) {
        this.jpa = jpa;
    }


    @Override
    public void guardar(Usuario usuario) {
        jpa.save(new UsuarioEntidad(usuario));
    }


    @Override
    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        return jpa
                .findByNombreUsuario(nombreUsuario)
                .map(UsuarioEntidad::aDominio);
    }


    @Override
    public List<Usuario> listarTodos() {
        return jpa
                .findAll()
                .stream()
                .map(UsuarioEntidad::aDominio)
                .toList();
    }
}
