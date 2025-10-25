package es.uclm.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.library.negocio.dominio.Pago;

@Repository
public interface PagoDAO extends JpaRepository<Pago, Long>  {

}
