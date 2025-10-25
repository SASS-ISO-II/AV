package es.uclm.library.negocio.servicio;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uclm.library.negocio.dominio.Inmueble;
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

    public boolean haySolapamiento(Inmueble inmueble, LocalDate inicio, LocalDate fin) {
    	Collection<Reserva> reservas = reservaDAO.findReservasSolapadas(inmueble, inicio, fin);
        Reserva[] reservasArray = reservas.toArray(new Reserva[0]);
        
        for (int i = 0; i < reservasArray.length; i++) {
            Reserva r = reservasArray[i];
            if (!(fin.isBefore(r.getFechaInicio()) || inicio.isAfter(r.getFechaFin()))) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void guardarReserva(Reserva reserva) {
        reservaDAO.save(reserva);
    }

    public Inquilino obtenerInquilinoPorLogin(String login) {
        return inquilinoDAO.findByLogin(login);
    }
}
