package es.uclm.library.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import es.uclm.library.negocio.dominio.Propietario;

@DataJpaTest
class PropietarioDAOTest {

    @Autowired
    private PropietarioDAO propietarioDAO;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByLogin_Existente() {
        Propietario propietario = new Propietario();
        propietario.setLogin("propietario@email.com");
        
        entityManager.persist(propietario);
        entityManager.flush();

        Propietario resultado = propietarioDAO.findByLogin("propietario@email.com");

        assertNotNull(resultado);
        assertEquals("propietario@email.com", resultado.getLogin());
    }

    @Test
    void testFindByLogin_NoExistente() {
        Propietario otro = new Propietario();
        otro.setLogin("otro@email.com");
        entityManager.persist(otro);
        entityManager.flush();

        Propietario resultado = propietarioDAO.findByLogin("nadie@email.com");

        assertNull(resultado);
    }
}