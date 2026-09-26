package pe.qruta.trazabilidad.configuracion;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pe.qruta.trazabilidad.repositorio.ProductoRepositorio;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorio;
import pe.qruta.trazabilidad.servicio.LoginService;
import pe.qruta.trazabilidad.servicio.ProductoService;


/**
 * Conecta las clases de dominio y servicio (Java puro, sin Spring)
 * con el contenedor de Spring. Aquí se decide qué reloj usa la aplicación real;
 * los repositorios son los de PostgreSQL (paquete repositorio.jpa).
 */
@Configuration
public class ConfiguracionAplicacion {


    @Bean
    public Clock relojDelSistema() {
        return Clock.system(ZoneId.of("America/Lima"));
    }


    @Bean
    public ProductoService productoService(
            ProductoRepositorio productoRepositorio,
            Clock relojDelSistema
    ) {
        return new ProductoService(
            productoRepositorio,
            relojDelSistema
        );
    }


    @Bean
    public LoginService loginService(
            UsuarioRepositorio usuarioRepositorio
    ) {
        return new LoginService(usuarioRepositorio);
    }
}
