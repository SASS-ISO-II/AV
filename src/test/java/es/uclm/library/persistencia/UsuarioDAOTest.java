package es.uclm.library.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import es.uclm.library.negocio.dominio.Usuario;

@DataJpaTest
class UsuarioDAOTest {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByLoginAndPass_Correcto() {
        Usuario u = new Usuario();
        u.setLogin("juan");
        u.setPass("secreta");
        entityManager.persist(u);
        entityManager.flush();

        Usuario resultado = usuarioDAO.findByLoginAndPass("juan", "secreta");

        assertNotNull(resultado);
        assertEquals("juan", resultado.getLogin());
    }

    @Test
    void testFindByLoginAndPass_PassIncorrecta() {
        Usuario u = new Usuario();
        u.setLogin("juan");
        u.setPass("secreta");
        entityManager.persist(u);
        entityManager.flush();

        Usuario resultado = usuarioDAO.findByLoginAndPass("juan", "incorrecta");

        assertNull(resultado);
    }

    @Test
    void testFindByLoginAndPass_UsuarioNoExiste() {
        Usuario u = new Usuario();
        u.setLogin("juan");
        u.setPass("secreta");
        entityManager.persist(u);
        entityManager.flush();

        Usuario resultado = usuarioDAO.findByLoginAndPass("pedro", "secreta");

        assertNull(resultado);
    }
}