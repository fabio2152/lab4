package pe.qruta.trazabilidad.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import pe.qruta.trazabilidad.controlador.Dtos.ErrorRespuesta;
import pe.qruta.trazabilidad.dominio.CambioDeEstadoNoPermitidoException;
import pe.qruta.trazabilidad.dominio.DuracionNoDisponibleException;
import pe.qruta.trazabilidad.dominio.OperacionNoAutorizadaException;
import pe.qruta.trazabilidad.servicio.CredencialesInvalidasException;
import pe.qruta.trazabilidad.servicio.ProductoNoEncontradoException;


/**
 * Traduce las excepciones del dominio y del servicio a respuestas HTTP
 * con el mismo mensaje claro que verifican las pruebas.
 */
@RestControllerAdvice
public class ManejadorDeErrores {


    @ExceptionHandler({
        CambioDeEstadoNoPermitidoException.class,
        DuracionNoDisponibleException.class
    })
    public ResponseEntity<ErrorRespuesta> reglaDeNegocio(RuntimeException error) {
        return responder(HttpStatus.CONFLICT, error.getMessage());
    }


    @ExceptionHandler(OperacionNoAutorizadaException.class)
    public ResponseEntity<ErrorRespuesta> sinPermiso(RuntimeException error) {
        return responder(HttpStatus.FORBIDDEN, error.getMessage());
    }


    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(RuntimeException error) {
        return responder(HttpStatus.NOT_FOUND, error.getMessage());
    }


    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorRespuesta> credenciales(RuntimeException error) {
        return responder(HttpStatus.UNAUTHORIZED, error.getMessage());
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuesta> datoInvalido(RuntimeException error) {
        return responder(HttpStatus.BAD_REQUEST, "Dato inválido: " + error.getMessage());
    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorRespuesta> estadoHttp(ResponseStatusException error) {
        return ResponseEntity
                .status(error.getStatusCode())
                .body(new ErrorRespuesta(error.getReason()));
    }


    private ResponseEntity<ErrorRespuesta> responder(
            HttpStatus estado,
            String mensaje
    ) {
        return ResponseEntity
                .status(estado)
                .body(new ErrorRespuesta(mensaje));
    }
}
