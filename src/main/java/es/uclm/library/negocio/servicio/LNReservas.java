package es.uclm.library.negocio.servicio;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.persistencia.InquilinoDAO;
import es.uclm.library.persistencia.ReservaDAO;

@Service
public class LNReservas {

	@Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private InquilinoDAO inquilinoDAO;

    public boolean validarFechas(Reserva reserva) {
        LocalDate hoy = LocalDate.now();
        return !reserva.getFechaInicio().isBefore(hoy) && !reserva.getFechaFin().isBefore(hoy);
    }

    public boolean haySolapamiento(LocalDate inicio, LocalDate fin) {
        return !reservaDAO.findReservasSolapadas(inicio, fin).isEmpty();
    }

    public void guardarReserva(Reserva reserva) {
        reservaDAO.save(reserva);
    }

    public Inquilino obtenerInquilinoPorLogin(String login) {
        return inquilinoDAO.findByLogin(login);
    }
}
