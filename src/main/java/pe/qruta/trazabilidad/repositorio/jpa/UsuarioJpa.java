package pe.qruta.trazabilidad.repositorio.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UsuarioJpa
        extends JpaRepository<UsuarioEntidad, Long> {

    Optional<UsuarioEntidad> findByNombreUsuario(String nombreUsuario);
}
