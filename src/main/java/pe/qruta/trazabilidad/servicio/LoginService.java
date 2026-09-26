package pe.qruta.trazabilidad.servicio;

import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorioEnMemoria;


public class LoginService {

    private final UsuarioRepositorioEnMemoria usuarios;


    public LoginService(
            UsuarioRepositorioEnMemoria usuarios
    ) {
        this.usuarios = usuarios;
    }


    public Usuario iniciarSesion(
            String nombreUsuario,
            String contrasena
    ) {

        return usuarios
                .buscarPorNombreUsuario(nombreUsuario)
                .orElse(null);
    }
}
