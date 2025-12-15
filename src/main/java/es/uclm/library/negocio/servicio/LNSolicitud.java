package es.uclm.library.negocio.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.library.negocio.dominio.EstadoSolicitud;
import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.SolicitudReserva;
import es.uclm.library.persistencia.InmuebleDAO;
import es.uclm.library.persistencia.InquilinoDAO;
import es.uclm.library.persistencia.PropietarioDAO;
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

        solicitud.setReservaConfirmada(reserva);
        solicitud.setEstado(EstadoSolicitud.ACEPTADA);

        solicitudDAO.save(solicitud);
    }

//    public void aceptarSolicitud(Long idSolicitud) {
//
//        SolicitudReserva solicitud = solicitudDAO.findById(idSolicitud).orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
//
//        Reserva reserva = solicitud.getReservaConfirmada();
//        
//        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
//            throw new RuntimeException("Esta solicitud ya fue procesada.");
//        }
//        
//        if (reserva == null) {
//            throw new RuntimeException("Esta solicitud no tiene reserva asociada.");
//        }
//
//        reservaDAO.save(reserva);
//
//        solicitud.aceptar(reserva);
//        solicitudDAO.save(solicitud);
//        reservaDAO.save(reserva);
//        System.out.println("Solicitud aceptada: " + idSolicitud);
//    }

    public void rechazarSolicitud(Long idSolicitud) {

        SolicitudReserva solicitud = solicitudDAO.findById(idSolicitud).orElseThrow(() -> new RuntimeException("Solicitud no encontrada."));
        
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new RuntimeException("Esta solicitud ya fue procesada.");
        }
        
        solicitud.rechazar();

        solicitudDAO.save(solicitud);

        System.out.println("Solicitud rechazada: " + idSolicitud);
    }
    

    public int contarSolicitudesPendientes(Propietario propietario) {
        return solicitudDAO.findPendientesByPropietario(EstadoSolicitud.PENDIENTE, propietario).size();
    }
    
}

