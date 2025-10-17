package es.uclm.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.library.negocio.dominio.ListaDeseos;

@Repository
public interface ListaDeseosDAO extends JpaRepository<ListaDeseos, String> {

}
