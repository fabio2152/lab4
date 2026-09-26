package pe.qruta.trazabilidad.controlador;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.qruta.trazabilidad.controlador.Dtos.LoginSolicitud;
import pe.qruta.trazabilidad.controlador.Dtos.UsuarioRespuesta;
import pe.qruta.trazabilidad.servicio.LoginService;


@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;


    public LoginController(
            LoginService loginService
    ) {
        this.loginService = loginService;
    }


    @PostMapping
    public UsuarioRespuesta iniciarSesion(
            @RequestBody LoginSolicitud solicitud
    ) {

        return UsuarioRespuesta.desde(
            loginService.iniciarSesion(
                solicitud.nombreUsuario(),
                solicitud.contrasena()
            )
        );
    }
}
