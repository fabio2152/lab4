package pe.qruta.trazabilidad.servicio;

import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorioEnMemoria;


public class LoginService {


    public LoginService(
            UsuarioRepositorioEnMemoria usuarios
    ) {
    }


    public Usuario iniciarSesion(
            String nombreUsuario,
            String contrasena
    ) {
        return null;
    }
}
