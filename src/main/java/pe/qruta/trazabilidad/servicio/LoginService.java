package pe.qruta.trazabilidad.servicio;

import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorio;


public class LoginService {

    private final UsuarioRepositorio usuarios;


    public LoginService(
            UsuarioRepositorio usuarios
    ) {
        this.usuarios = usuarios;
    }


    public Usuario iniciarSesion(
            String nombreUsuario,
            String contrasena
    ) {

        return usuarios
                .buscarPorNombreUsuario(nombreUsuario)
                .filter(usuario -> usuario.tieneContrasena(contrasena))
                .orElseThrow(
                    () -> new CredencialesInvalidasException(
                        "Usuario o contraseña incorrectos"
                    )
                );
    }
}
