package pe.qruta.trazabilidad.servicio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pe.qruta.trazabilidad.dominio.Rol;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorioEnMemoria;


class LoginServiceTest {

    private Usuario productor;

    private LoginService login;


    @BeforeEach
    void prepararUsuarios() {

        UsuarioRepositorioEnMemoria usuarios =
            new UsuarioRepositorioEnMemoria();

        productor =
            new Usuario("productor1", "clave123", Rol.PRODUCTOR);

        usuarios.guardar(productor);

        usuarios.guardar(
            new Usuario("intermediario1", "clave456", Rol.INTERMEDIARIO)
        );

        login = new LoginService(usuarios);
    }


    @Test
    void usuarioYContrasenaCorrectosDevuelvenElUsuario() {

        // Dado
        // productor1 registrado con la contraseña clave123 (prepararUsuarios)

        // Cuando
        Usuario usuario =
            login.iniciarSesion("productor1", "clave123");

        // Entonces
        assertThat(usuario).isSameAs(productor);
    }


    @Test
    void unaContrasenaIncorrectaSeRechaza() {

        // Dado
        // productor1 registrado con la contraseña clave123 (prepararUsuarios)

        // Cuando / Entonces
        assertThatThrownBy(() ->
            login.iniciarSesion("productor1", "otraClave")
        )
            .isInstanceOf(CredencialesInvalidasException.class)
            .hasMessage("Usuario o contraseña incorrectos");
    }
}
