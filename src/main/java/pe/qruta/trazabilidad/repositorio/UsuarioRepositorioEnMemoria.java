package pe.qruta.trazabilidad.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pe.qruta.trazabilidad.dominio.Usuario;


public class UsuarioRepositorioEnMemoria {

    private final List<Usuario> usuarios = new ArrayList<>();


    public void guardar(Usuario usuario) {
        usuarios.add(usuario);
    }


    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {

        return usuarios
                .stream()
                .filter(usuario -> usuario.getNombreUsuario().equals(nombreUsuario))
                .findFirst();
    }
}
