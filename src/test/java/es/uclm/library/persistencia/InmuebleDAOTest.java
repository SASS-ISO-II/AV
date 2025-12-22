package es.uclm.library.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;

@DataJpaTest
class InmuebleDAOTest {

    @Autowired
    private InmuebleDAO inmuebleDAO;

    @Autowired
    private TestEntityManager entityManager;

  
    @Test
    void testFindByCapacidadGreaterThanEqual_MiniMax() {

        // Arrange
        Propietario propietario = new Propietario();
        entityManager.persist(propietario);

        Inmueble minimo = new Inmueble();
        minimo.setCapacidad(1);
        minimo.setPropietario(propietario);
        entityManager.persist(minimo);

        Inmueble intermedio = new Inmueble();
        intermedio.setCapacidad(3);
        intermedio.setPropietario(propietario);
        entityManager.persist(intermedio);

        Inmueble maximo = new Inmueble();
        maximo.setCapacidad(6);
        maximo.setPropietario(propietario);
        entityManager.persist(maximo);

        entityManager.flush();

        // Act
        Collection<Inmueble> resultado = inmuebleDAO.findByCapacidadGreaterThanEqual(3);

        // Assert
        assertEquals(2, resultado.size());
    }


    @Test
    void testFindByPrecioNocheLessThanEqual_MiniMax() {

        // Arrange
        Inmueble barato = new Inmueble();
        barato.setPrecioNoche(50.0);
        entityManager.persist(barato);

        Inmueble medio = new Inmueble();
        medio.setPrecioNoche(100.0);
        entityManager.persist(medio);

        Inmueble caro = new Inmueble();
        caro.setPrecioNoche(150.0);
        entityManager.persist(caro);

        entityManager.flush();

        // Act
        Collection<Inmueble> resultado = inmuebleDAO.findByPrecioNocheLessThanEqual(100.0);

        // Assert
        assertEquals(2, resultado.size());
    }

 
    @Test
    void testFindByTipo() {

        // Arrange
        Inmueble piso = new Inmueble();
        piso.setTipo("Piso");
        entityManager.persist(piso);

        Inmueble chalet = new Inmueble();
        chalet.setTipo("Chalet");
        entityManager.persist(chalet);

        entityManager.flush();

        // Act
        Collection<Inmueble> resultado = inmuebleDAO.findByTipo("Piso");

        // Assert
        assertEquals(1, resultado.size());
    }

   
    @Test
    void testFindByLocalizacionContainingIgnoreCase() {

        // Arrange
        Inmueble i1 = new Inmueble();
        i1.setLocalizacion("Madrid Centro");
        entityManager.persist(i1);

        Inmueble i2 = new Inmueble();
        i2.setLocalizacion("Toledo");
        entityManager.persist(i2);

        entityManager.flush();

        // Act
        Collection<Inmueble> resultado =
                inmuebleDAO.findByLocalizacionContainingIgnoreCase("madrid");

        // Assert
        assertEquals(1, resultado.size());
    }

   
    @Test
    void testFindByPropietario() {

        Propietario p1 = new Propietario();
        entityManager.persist(p1);

        Propietario p2 = new Propietario();
        entityManager.persist(p2);

        Inmueble i1 = new Inmueble();
        i1.setPropietario(p1);
        entityManager.persist(i1);

        Inmueble i2 = new Inmueble();
        i2.setPropietario(p2);
        entityManager.persist(i2);

        entityManager.flush();

        Collection<Inmueble> resultado = inmuebleDAO.findByPropietario(p1);
   
        assertEquals(1, resultado.size());
    }
}
