package es.uclm.library.negocio.controladora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.MetodoPago;
import es.uclm.library.negocio.dominio.Pago;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.TipoReserva;
import es.uclm.library.negocio.servicio.LNPagos;
import es.uclm.library.negocio.servicio.LNReservas;
import es.uclm.library.negocio.servicio.LNSolicitud;
import jakarta.servlet.http.HttpSession;

@Controller
public class GestorPagos {

    @Autowired
    private LNPagos lnPagos;
    
	@Autowired
	private LNReservas lnReservas;

    @Autowired
    private LNSolicitud solicitudReserva;

    private static final Logger log = LoggerFactory.getLogger(GestorPagos.class);

    @GetMapping("/pago")
    public String mostrarFormularioPago(HttpSession session, Model model) {
        Reserva reserva = (Reserva) session.getAttribute("reservaActual");
        if (reserva == null) {
            return "redirect:/reserva";
        }

        model.addAttribute("pago", new Pago());
        model.addAttribute("metodos", MetodoPago.values());

        int noches = (int) reserva.getNoches();
        double total = reserva.getNoches() * reserva.getInmueble().getPrecioNoche();
        model.addAttribute("noches", noches);
        model.addAttribute("totalAPagar", total);

        return "pago";
    }

    @PostMapping("/pago")
    public String procesarPago(@ModelAttribute Pago pago, HttpSession session, Model model) {
        
        Reserva reserva = (Reserva) session.getAttribute("reservaActual");

        if (reserva == null) {
            return "redirect:/reserva";
        }

        Pago pagoRegistrado;

        Inmueble inmueble = reserva.getInmueble();

        if (inmueble.getTipoReserva() == TipoReserva.CONFIRMACION) {

            pago.setReserva(reserva);
            session.setAttribute("pagoPendiente", pago);

            solicitudReserva.crearSolicitud(inmueble, reserva.getInquilino(), reserva);

            session.removeAttribute("reservaActual");

            return "redirect:/inicio?solicitudEnviada=true";
        }
        lnReservas.guardarReserva(reserva);
        log.info("Reserva registrada correctamente: {}", reserva);

        pagoRegistrado = lnPagos.registrarPago(pago, reserva);
        log.info("Pago registrado correctamente: {}", pagoRegistrado);
        session.removeAttribute("reservaActual");

        model.addAttribute("reserva", reserva);
        model.addAttribute("pago", pagoRegistrado);
        model.addAttribute("popupPago", true);

        return "pago";
    }

    @GetMapping("/resultadoPago")
    public String mostrarResultadoPago() {
        return "resultadoPago";
    }
}