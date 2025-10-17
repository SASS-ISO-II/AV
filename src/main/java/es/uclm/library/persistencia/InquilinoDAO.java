package es.uclm.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.library.negocio.dominio.Inquilino;

@Repository
public interface InquilinoDAO extends JpaRepository<Inquilino, String> {

	Inquilino findByLogin(String login);

}
