package pe.qruta.trazabilidad.dominio;


public class Usuario {

    private final Rol rol;


    public Usuario(
            String nombreUsuario,
            Rol rol
    ) {
        this.rol = rol;
    }


    public Usuario(
            String nombreUsuario,
            String contrasena,
            Rol rol
    ) {
        this(nombreUsuario, rol);
    }


    public Rol getRol() {
        return rol;
    }
}
