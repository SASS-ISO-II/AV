package es.uclm.library.negocio.servicio;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.library.negocio.dominio.Pago;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.persistencia.PagoDAO;
import es.uclm.library.persistencia.ReservaDAO;

@Service
public class LNPagos {

    @Autowired
    private PagoDAO pagoDAO;

    @Autowired
    private ReservaDAO reservaDAO;

    public Pago registrarPago(Pago pago, Reserva reserva) {
        pago.setReferencia(UUID.randomUUID());
        pago.setReserva(reserva);
        reserva.setPago(pago);
        pagoDAO.save(pago);
        reservaDAO.save(reserva);
        return pago;
    }

}
