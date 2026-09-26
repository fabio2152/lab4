package pe.qruta.trazabilidad.configuracion;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pe.qruta.trazabilidad.repositorio.ProductoRepositorioEnMemoria;
import pe.qruta.trazabilidad.repositorio.UsuarioRepositorioEnMemoria;
import pe.qruta.trazabilidad.servicio.LoginService;
import pe.qruta.trazabilidad.servicio.ProductoService;


/**
 * Conecta las clases de dominio y servicio (Java puro, sin Spring)
 * con el contenedor de Spring. Aquí se decide qué reloj usa la aplicación real.
 */
@Configuration
public class ConfiguracionAplicacion {


    @Bean
    public Clock relojDelSistema() {
        return Clock.system(ZoneId.of("America/Lima"));
    }


    @Bean
    public ProductoRepositorioEnMemoria productoRepositorio() {
        return new ProductoRepositorioEnMemoria();
    }


    @Bean
    public UsuarioRepositorioEnMemoria usuarioRepositorio() {
        return new UsuarioRepositorioEnMemoria();
    }


    @Bean
    public ProductoService productoService(
            ProductoRepositorioEnMemoria productoRepositorio,
            Clock relojDelSistema
    ) {
        return new ProductoService(
            productoRepositorio,
            relojDelSistema
        );
    }


    @Bean
    public LoginService loginService(
            UsuarioRepositorioEnMemoria usuarioRepositorio
    ) {
        return new LoginService(usuarioRepositorio);
    }
}
