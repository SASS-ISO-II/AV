package es.uclm.library.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Reserva;

@DataJpaTest
class ReservaDAOTest {

    @Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindReservasSolapadas() {
        Propietario propietario = new Propietario();
        entityManager.persist(propietario);

        Inmueble inmueble = new Inmueble();
        inmueble.setPropietario(propietario);
        entityManager.persist(inmueble);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setInmueble(inmueble);
        reservaExistente.setFechaInicio(LocalDate.of(2024, 1, 1));
        reservaExistente.setFechaFin(LocalDate.of(2024, 1, 10));
        entityManager.persist(reservaExistente);

        entityManager.flush();

        Collection<Reserva> solapadas = reservaDAO.findReservasSolapadas(
            inmueble, 
            LocalDate.of(2024, 1, 5), 
            LocalDate.of(2024, 1, 15)
        );
        assertEquals(1, solapadas.size());

        Collection<Reserva> noSolapadas = reservaDAO.findReservasSolapadas(
            inmueble, 
            LocalDate.of(2024, 1, 20), 
            LocalDate.of(2024, 1, 25)
        );
        assertTrue(noSolapadas.isEmpty());
    }

    @Test
    void testFindReservasSolapadas_DiferenteInmueble() {
        Propietario propietario = new Propietario();
        entityManager.persist(propietario);

        Inmueble inmuebleA = new Inmueble();
        inmuebleA.setPropietario(propietario);
        entityManager.persist(inmuebleA);

        Inmueble inmuebleB = new Inmueble();
        inmuebleB.setPropietario(propietario);
        entityManager.persist(inmuebleB);

        Reserva reservaA = new Reserva();
        reservaA.setInmueble(inmuebleA);
        reservaA.setFechaInicio(LocalDate.of(2024, 1, 1));
        reservaA.setFechaFin(LocalDate.of(2024, 1, 10));
        entityManager.persist(reservaA);

        entityManager.flush();

        Collection<Reserva> resultado = reservaDAO.findReservasSolapadas(
            inmuebleB, 
            LocalDate.of(2024, 1, 5), 
            LocalDate.of(2024, 1, 8)
        );

        assertTrue(resultado.isEmpty());
    }

    @Test
    void testFindReservasPorInmueble() {
        Propietario propietario = new Propietario();
        entityManager.persist(propietario);

        Inmueble inmueble = new Inmueble();
        inmueble.setPropietario(propietario);
        entityManager.persist(inmueble);

        Reserva r1 = new Reserva();
        r1.setInmueble(inmueble);
        r1.setFechaInicio(LocalDate.of(2024, 2, 1));
        r1.setFechaFin(LocalDate.of(2024, 2, 5));
        entityManager.persist(r1);

        Reserva r2 = new Reserva();
        r2.setInmueble(inmueble);
        r2.setFechaInicio(LocalDate.of(2024, 3, 1));
        r2.setFechaFin(LocalDate.of(2024, 3, 5));
        entityManager.persist(r2);

        entityManager.flush();

        Collection<Reserva> resultado = reservaDAO.findReservasPorInmueble(inmueble);

        assertEquals(2, resultado.size());
    }
}