package es.uclm.library.persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import es.uclm.library.negocio.dominio.EstadoSolicitud;
import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.SolicitudReserva;

@DataJpaTest
class SolicitudReservaDAOTest {

    @Autowired
    private SolicitudReservaDAO solicitudReservaDAO;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByInmueble() {
        Propietario p1 = new Propietario();
        p1.setLogin("prop1"); 
        
        entityManager.persist(p1);

        Inmueble i1 = new Inmueble();
        i1.setPropietario(p1);
        entityManager.persist(i1);

        Inmueble i2 = new Inmueble();
        i2.setPropietario(p1);
        entityManager.persist(i2);

        SolicitudReserva s1 = new SolicitudReserva();
        s1.setInmueble(i1);
        entityManager.persist(s1);

        SolicitudReserva s2 = new SolicitudReserva();
        s2.setInmueble(i1);
        entityManager.persist(s2);

        entityManager.flush();

        Collection<SolicitudReserva> resultadoI1 = solicitudReservaDAO.findByInmueble(i1);
        assertEquals(2, resultadoI1.size());

        Collection<SolicitudReserva> resultadoI2 = solicitudReservaDAO.findByInmueble(i2);
        assertTrue(resultadoI2.isEmpty());
    }

    @Test
    void testFindByPropietario() {
        Propietario pA = new Propietario();
        pA.setLogin("propA"); 
       
        entityManager.persist(pA);

        Propietario pB = new Propietario();
        pB.setLogin("propB"); 
        
        entityManager.persist(pB);

        Inmueble iA = new Inmueble();
        iA.setPropietario(pA);
        entityManager.persist(iA);

        SolicitudReserva s1 = new SolicitudReserva();
        s1.setInmueble(iA);
        entityManager.persist(s1);

        entityManager.flush();

        Collection<SolicitudReserva> resA = solicitudReservaDAO.findByPropietario(pA);
        assertEquals(1, resA.size());

        Collection<SolicitudReserva> resB = solicitudReservaDAO.findByPropietario(pB);
        assertTrue(resB.isEmpty());
    }

    @Test
    void testFindPendientesByPropietario() {
        Propietario p1 = new Propietario();
        p1.setLogin("propPendiente"); 
        
        entityManager.persist(p1);

        Inmueble i1 = new Inmueble();
        i1.setPropietario(p1);
        entityManager.persist(i1);

        SolicitudReserva sPendiente = new SolicitudReserva();
        sPendiente.setInmueble(i1);
        sPendiente.setEstado(EstadoSolicitud.PENDIENTE);
        entityManager.persist(sPendiente);

        SolicitudReserva sAceptada = new SolicitudReserva();
        sAceptada.setInmueble(i1);
        sAceptada.setEstado(EstadoSolicitud.ACEPTADA);
        entityManager.persist(sAceptada);

        entityManager.flush();

        Collection<SolicitudReserva> resultado = solicitudReservaDAO.findPendientesByPropietario(EstadoSolicitud.PENDIENTE, p1);
        
        assertEquals(1, resultado.size());
        assertEquals(EstadoSolicitud.PENDIENTE, resultado.iterator().next().getEstado());
    }

    @Test
    void testFindByPropietarioAndEstado() {
        Propietario p1 = new Propietario();
        p1.setLogin("propEstado"); 
       
        entityManager.persist(p1);

        Inmueble i1 = new Inmueble();
        i1.setPropietario(p1);
        entityManager.persist(i1);

        SolicitudReserva sRechazada = new SolicitudReserva();
        sRechazada.setInmueble(i1);
        sRechazada.setEstado(EstadoSolicitud.RECHAZADA);
        entityManager.persist(sRechazada);

        entityManager.flush();

        Collection<SolicitudReserva> resultado = solicitudReservaDAO.findByPropietarioAndEstado(p1, EstadoSolicitud.RECHAZADA);
        
        assertEquals(1, resultado.size());
    }
}