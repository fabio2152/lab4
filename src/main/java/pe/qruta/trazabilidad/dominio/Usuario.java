package pe.qruta.trazabilidad.dominio;


public class Usuario {

    private final String nombreUsuario;

    private final Rol rol;

    private String contrasena;


    public Usuario(
            String nombreUsuario,
            Rol rol
    ) {
        this.nombreUsuario = nombreUsuario;
        this.rol = rol;
    }


    public Usuario(
            String nombreUsuario,
            String contrasena,
            Rol rol
    ) {
        this(nombreUsuario, rol);
        this.contrasena = contrasena;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }


    public Rol getRol() {
        return rol;
    }


    public boolean tieneContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }
}
