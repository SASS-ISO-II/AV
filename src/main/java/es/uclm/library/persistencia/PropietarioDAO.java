package es.uclm.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.library.negocio.dominio.Propietario;

@Repository
public interface PropietarioDAO extends JpaRepository<Propietario, String> {

	Propietario findByLogin(String login);

}
