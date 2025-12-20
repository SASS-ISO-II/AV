package es.uclm.library.negocio.servicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.library.negocio.controladora.GestorPagos;
import es.uclm.library.negocio.dominio.EstadoSolicitud;
import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.SolicitudReserva;
import es.uclm.library.persistencia.InmuebleDAO;
import es.uclm.library.persistencia.InquilinoDAO;
import es.uclm.library.persistencia.ReservaDAO;
import es.uclm.library.persistencia.SolicitudReservaDAO;

@Service
public class LNSolicitud {

    @Autowired
    private SolicitudReservaDAO solicitudDAO;

    @Autowired
    private InmuebleDAO inmuebleDAO;

    @Autowired
    private InquilinoDAO inquilinoDAO;

    @Autowired
    private ReservaDAO reservaDAO;

    private static final Logger log = LoggerFactory.getLogger(GestorPagos.class);

    public Inquilino obtenerInquilinoPorLogin(String login) {
        return inquilinoDAO.findByLogin(login);
    }
    
    public SolicitudReserva crearSolicitud(Inmueble inmueble, Inquilino inquilino, Reserva reservaTemp) {

        inquilino = inquilinoDAO.findByLogin(inquilino.getLogin());
        inmueble = inmuebleDAO.findById(inmueble.getId()).orElseThrow(() -> new RuntimeException("Inmueble no encontrado."));

        SolicitudReserva s = new SolicitudReserva();
        s.setInquilino(inquilino);
        s.setInmueble(inmueble);
        s.setReservaConfirmada(null);
        
        s.setFechaInicio(reservaTemp.getFechaInicio());
        s.setFechaFin(reservaTemp.getFechaFin());


        solicitudDAO.save(s);
        log.info("Solicitud registrada correctamente: {}", s);
        
        return s;
    }
    
    public void aceptarSolicitud(Long idSolicitud) {

        SolicitudReserva solicitud = solicitudDAO.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));

        Reserva reserva = new Reserva();
        reserva.setInquilino(solicitud.getInquilino());
        reserva.setInmueble(solicitud.getInmueble());
        reserva.setFechaInicio(solicitud.getFechaInicio());
        reserva.setFechaFin(solicitud.getFechaFin());

        reservaDAO.save(reserva);
        log.info("Reserva registrada correctamente: {}", reserva);

        solicitud.setReservaConfirmada(reserva);
        solicitud.setEstado(EstadoSolicitud.ACEPTADA);
        
        log.info("Solicitud registrada correctamente: {}", solicitud);
        
        solicitudDAO.save(solicitud);
    }

    public void rechazarSolicitud(Long idSolicitud) {

        SolicitudReserva solicitud = solicitudDAO.findById(idSolicitud).orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
        
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Esta solicitud ya fue procesada.");
        }
        
        solicitud.rechazar();

        solicitudDAO.save(solicitud);
        log.info("Solicitud registrada correctamente: {}", solicitud);
        System.out.println("Solicitud rechazada: " + idSolicitud);
    }
    

    public int contarSolicitudesPendientes(Propietario propietario) {
        return solicitudDAO.findPendientesByPropietario(EstadoSolicitud.PENDIENTE, propietario).size();
    }
    
}

