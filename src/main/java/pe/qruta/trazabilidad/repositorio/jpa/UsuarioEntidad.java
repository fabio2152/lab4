package pe.qruta.trazabilidad.repositorio.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import pe.qruta.trazabilidad.dominio.Rol;
import pe.qruta.trazabilidad.dominio.Usuario;


@Entity
@Table(name = "usuarios")
public class UsuarioEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(nullable = false, length = 100)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;


    protected UsuarioEntidad() {
    }


    public UsuarioEntidad(Usuario usuario) {
        this.nombreUsuario = usuario.getNombreUsuario();
        this.contrasena = usuario.getContrasena();
        this.rol = usuario.getRol();
    }


    public Usuario aDominio() {
        return new Usuario(nombreUsuario, contrasena, rol);
    }
}
