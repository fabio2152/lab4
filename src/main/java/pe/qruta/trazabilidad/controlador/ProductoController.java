package pe.qruta.trazabilidad.controlador;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import pe.qruta.trazabilidad.controlador.Dtos.CambioEstadoRespuesta;
import pe.qruta.trazabilidad.controlador.Dtos.CambioEstadoSolicitud;
import pe.qruta.trazabilidad.controlador.Dtos.CrearProductoSolicitud;
import pe.qruta.trazabilidad.controlador.Dtos.ProductoRespuesta;
import pe.qruta.trazabilidad.dominio.EstadoProducto;
import pe.qruta.trazabilidad.dominio.Usuario;
import pe.qruta.trazabilidad.servicio.ProductoService;


@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    private final SesionUsuario sesion;


    public ProductoController(
            ProductoService service,
            SesionUsuario sesion
    ) {
        this.service = service;
        this.sesion = sesion;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoRespuesta crear(
            @RequestHeader(value = SesionUsuario.CABECERA, required = false) String nombreUsuario,
            @RequestBody CrearProductoSolicitud solicitud
    ) {

        Usuario productor = sesion.obtener(nombreUsuario);

        Usuario intermediario =
            sesion.obtenerPorNombre(solicitud.intermediario());

        return ProductoRespuesta.desde(
            service.crearProducto(
                solicitud.nombre(),
                productor,
                intermediario
            )
        );
    }


    @GetMapping
    public List<ProductoRespuesta> listar(
            @RequestHeader(value = SesionUsuario.CABECERA, required = false) String nombreUsuario
    ) {

        Usuario usuario = sesion.obtener(nombreUsuario);

        return service
                .listarProductos(usuario)
                .stream()
                .map(ProductoRespuesta::desde)
                .toList();
    }


    @PutMapping("/{id}/estado")
    public ProductoRespuesta cambiarEstado(
            @RequestHeader(value = SesionUsuario.CABECERA, required = false) String nombreUsuario,
            @PathVariable Long id,
            @RequestBody CambioEstadoSolicitud solicitud
    ) {

        Usuario usuario = sesion.obtener(nombreUsuario);

        service.cambiarEstado(
            id,
            EstadoProducto.valueOf(solicitud.estado()),
            usuario
        );

        return ProductoRespuesta.desde(
            service.buscarPorId(id)
        );
    }


    @GetMapping("/{id}/historial")
    public List<CambioEstadoRespuesta> historial(
            @RequestHeader(value = SesionUsuario.CABECERA, required = false) String nombreUsuario,
            @PathVariable Long id
    ) {

        sesion.obtener(nombreUsuario);

        return service
                .obtenerHistorial(id)
                .stream()
                .map(CambioEstadoRespuesta::desde)
                .toList();
    }
}
