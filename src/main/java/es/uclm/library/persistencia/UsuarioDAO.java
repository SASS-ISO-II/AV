package es.uclm.library.persistencia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uclm.library.negocio.dominio.Usuario;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, String> {

	Usuario findByLoginAndPass(String login, String pass);

}
