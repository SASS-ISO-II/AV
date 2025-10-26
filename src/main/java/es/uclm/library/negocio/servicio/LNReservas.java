package es.uclm.library.negocio.servicio;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.persistencia.InquilinoDAO;
import es.uclm.library.persistencia.ReservaDAO;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

@Service
public class LNReservas {

    @Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private InquilinoDAO inquilinoDAO;

    @Autowired
    private LNInmuebles lnInmuebles;

    public boolean validarFechas(Reserva reserva) {
        LocalDate hoy = LocalDate.now();
        return !reserva.getFechaInicio().isBefore(hoy) && !reserva.getFechaFin().isBefore(hoy);
    }

    public boolean haySolapamiento(Inmueble inmueble, LocalDate inicio, LocalDate fin) {
        Collection<Reserva> reservas = reservaDAO.findReservasSolapadas(inmueble, inicio, fin);
        return reservas.stream()
                .anyMatch(r -> !(fin.isBefore(r.getFechaInicio()) || inicio.isAfter(r.getFechaFin())));
    }

    @Transactional
    public void guardarReserva(Reserva reserva) {
        reservaDAO.save(reserva);
    }

    public Inquilino obtenerInquilinoPorLogin(String login) {
        return inquilinoDAO.findByLogin(login);
    }

    public Collection<Reserva> obtenerReservasPorInmueble(Inmueble inmueble) {
        return reservaDAO.findReservasPorInmueble(inmueble);
    }

    public Inmueble obtenerInmuebleParaReserva(Long inmuebleId, HttpSession session, Model model) {
        Inmueble inmueble = null;

        if (inmuebleId != null) {
            inmueble = lnInmuebles.obtenerInmueblePorId(inmuebleId);
            if (inmueble != null) {
                session.setAttribute("inmuebleActual", inmueble);
            }
        } else {
            inmueble = (Inmueble) session.getAttribute("inmuebleActual");
        }

        return inmueble;
    }

    public String procesarReserva(Reserva reserva, Usuario usuario, HttpSession session, Model model) {
        Inquilino inquilino = obtenerInquilinoPorLogin(usuario.getLogin());
        if (inquilino == null) {
            model.addAttribute("error", "Solo los inquilinos pueden hacer reservas");
            return "error";
        }

        Inmueble inmueble = (Inmueble) session.getAttribute("inmuebleActual");
        if (inmueble == null) {
            model.addAttribute("error", "No se ha seleccionado ningún inmueble.");
            return "error";
        }

        if (!validarFechas(reserva)) {
            model.addAttribute("error", "No se pueden seleccionar fechas pasadas");
            return "error";
        }

        if (haySolapamiento(inmueble, reserva.getFechaInicio(), reserva.getFechaFin())) {
            model.addAttribute("error", "Esas fechas ya están reservadas. Por favor, elige otro rango.");
            return "error";
        }

        reserva.setInquilino(inquilino);
        reserva.setInmueble(inmueble);
        guardarReserva(reserva);
        session.setAttribute("reservaActual", reserva);

        return "ok";
    }
}
