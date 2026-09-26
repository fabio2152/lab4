package pe.qruta.trazabilidad.controlador;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorio;


/**
 * Identifica al usuario que hace cada petición a partir de la cabecera X-Usuario.
 * Es un login solo para la demostración: no hay tokens ni sesiones reales.
 */
@Component
public class SesionUsuario {

    public static final String CABECERA = "X-Usuario";

    private final UsuarioRepositorio usuarios;


    public SesionUsuario(
            UsuarioRepositorio usuarios
    ) {
        this.usuarios = usuarios;
    }


    public Usuario obtener(String nombreUsuario) {

        return buscar(nombreUsuario)
                .orElseThrow(
                    () -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Debe iniciar sesión"
                    )
                );
    }


    public Usuario obtenerPorNombre(String nombreUsuario) {

        return buscar(nombreUsuario)
                .orElseThrow(
                    () -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "No existe el usuario " + nombreUsuario
                    )
                );
    }


    private Optional<Usuario> buscar(String nombreUsuario) {

        if (nombreUsuario == null) {
            return Optional.empty();
        }

        return usuarios.buscarPorNombreUsuario(nombreUsuario);
    }
}
