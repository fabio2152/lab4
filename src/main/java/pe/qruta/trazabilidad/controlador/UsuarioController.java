package pe.qruta.trazabilidad.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pe.qruta.trazabilidad.controlador.Dtos.UsuarioRespuesta;
import pe.qruta.trazabilidad.dominio.Rol;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorio;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepositorio usuarios;

    private final SesionUsuario sesion;


    public UsuarioController(
            UsuarioRepositorio usuarios,
            SesionUsuario sesion
    ) {
        this.usuarios = usuarios;
        this.sesion = sesion;
    }


    /** Solo el regulador: lista productores e intermediarios. */
    @GetMapping
    public List<UsuarioRespuesta> listar(
            @RequestHeader(value = SesionUsuario.CABECERA, required = false) String nombreUsuario
    ) {

        Usuario usuario = sesion.obtener(nombreUsuario);

        if (usuario.getRol() != Rol.REGULADOR) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Solo el regulador puede ver los usuarios del sistema"
            );
        }

        return usuarios
                .listarTodos()
                .stream()
                .filter(otro -> otro.getRol() != Rol.REGULADOR)
                .map(UsuarioRespuesta::desde)
                .toList();
    }


    /** Para el formulario del productor: a quién puede asignar el producto. */
    @GetMapping("/intermediarios")
    public List<UsuarioRespuesta> intermediarios(
            @RequestHeader(value = SesionUsuario.CABECERA, required = false) String nombreUsuario
    ) {

        sesion.obtener(nombreUsuario);

        return usuarios
                .listarTodos()
                .stream()
                .filter(otro -> otro.getRol() == Rol.INTERMEDIARIO)
                .map(UsuarioRespuesta::desde)
                .toList();
    }
}
