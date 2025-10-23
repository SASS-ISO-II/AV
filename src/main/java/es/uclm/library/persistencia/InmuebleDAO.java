package es.uclm.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import es.uclm.library.negocio.dominio.Inmueble;

public interface InmuebleDAO extends JpaRepository<Inmueble, Long> {
}
