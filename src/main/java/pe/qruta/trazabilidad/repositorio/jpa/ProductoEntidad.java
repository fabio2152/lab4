package pe.qruta.trazabilidad.repositorio.jpa;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import pe.qruta.trazabilidad.dominio.EstadoProducto;
import pe.qruta.trazabilidad.dominio.Producto;


@Entity
@Table(name = "productos")
public class ProductoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(optional = false)
    private UsuarioEntidad productor;

    @ManyToOne(optional = false)
    private UsuarioEntidad intermediarioAsignado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoProducto estado;

    private LocalDateTime horaInicioTransporte;

    private LocalDateTime horaLlegada;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    @OrderBy("id")
    private List<CambioEstadoEntidad> historial = new ArrayList<>();


    protected ProductoEntidad() {
    }


    public ProductoEntidad(
            String nombre,
            UsuarioEntidad productor,
            UsuarioEntidad intermediarioAsignado
    ) {
        this.nombre = nombre;
        this.productor = productor;
        this.intermediarioAsignado = intermediarioAsignado;
    }


    public Long getId() {
        return id;
    }


    public List<CambioEstadoEntidad> getHistorial() {
        return historial;
    }


    /** Copia el estado actual del producto de dominio en la entidad. */
    public void actualizarDesde(Producto producto) {
        this.estado = producto.getEstado();
        this.horaInicioTransporte = producto.getHoraInicioTransporte();
        this.horaLlegada = producto.getHoraLlegada();
    }


    public Producto aDominio() {
        return Producto.reconstruir(
            id,
            nombre,
            productor.aDominio(),
            intermediarioAsignado.aDominio(),
            estado,
            horaInicioTransporte,
            horaLlegada,
            historial.stream().map(CambioEstadoEntidad::aDominio).toList()
        );
    }
}
