package pe.qruta.trazabilidad.dominio;


public class Usuario {

    private Rol rol;


    public Usuario(
            String nombreUsuario,
            Rol rol
    ) {
        this.rol = rol;
    }


    public Rol getRol() {
        return rol;
    }
}
