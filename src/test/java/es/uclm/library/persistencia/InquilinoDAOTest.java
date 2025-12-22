package es.uclm.library.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import es.uclm.library.negocio.dominio.Inquilino;

@DataJpaTest
class InquilinoDAOTest {

    @Autowired
    private InquilinoDAO inquilinoDAO;

    @Autowired
    private TestEntityManager entityManager;

    
    @Test
    void testFindByLogin_Existente() {
        
        Inquilino inquilino = new Inquilino();
        
        inquilino.setLogin("pepe@email.com"); 
        
       
        entityManager.persist(inquilino);
        entityManager.flush();

        
        Inquilino resultado = inquilinoDAO.findByLogin("pepe@email.com");

       
        assertNotNull(resultado, "El inquilino debería ser encontrado");
        assertEquals("pepe@email.com", resultado.getLogin(), "El login recuperado debe coincidir");
    }

   
    @Test
    void testFindByLogin_NoExistente() {
        
        Inquilino otroInquilino = new Inquilino();
        otroInquilino.setLogin("juan@email.com");
        entityManager.persist(otroInquilino);
        entityManager.flush();

       
        Inquilino resultado = inquilinoDAO.findByLogin("noexiste@email.com");

        
        assertNull(resultado, "El resultado debería ser null para un usuario inexistente");
    }
}