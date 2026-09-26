package pe.qruta.trazabilidad.configuracion;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import pe.qruta.trazabilidad.dominio.Rol;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorio;


/**
 * Carga los usuarios de prueba al iniciar la aplicación.
 * Solo para la demostración: las contraseñas no se cifran.
 */
@Component
public class CargaUsuariosDePrueba
        implements CommandLineRunner {

    private final UsuarioRepositorio usuarios;


    public CargaUsuariosDePrueba(
            UsuarioRepositorio usuarios
    ) {
        this.usuarios = usuarios;
    }


    @Override
    public void run(String... args) {

        registrarSiNoExiste(new Usuario("productor1", "clave123", Rol.PRODUCTOR));
        registrarSiNoExiste(new Usuario("intermediario1", "clave123", Rol.INTERMEDIARIO));
        registrarSiNoExiste(new Usuario("intermediario2", "clave123", Rol.INTERMEDIARIO));
        registrarSiNoExiste(new Usuario("regulador1", "clave123", Rol.REGULADOR));
    }


    private void registrarSiNoExiste(Usuario usuario) {

        if (usuarios.buscarPorNombreUsuario(usuario.getNombreUsuario()).isEmpty()) {
            usuarios.guardar(usuario);
        }
    }
}
