package pe.qruta.trazabilidad.repositorio.jpa;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoJpa
        extends JpaRepository<ProductoEntidad, Long> {
}
