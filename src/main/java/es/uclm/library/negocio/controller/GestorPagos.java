package es.uclm.library.negocio.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.uclm.library.negocio.dominio.MetodoPago;
import es.uclm.library.negocio.dominio.Pago;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.persistencia.PagoDAO;
import es.uclm.library.persistencia.ReservaDAO;
import jakarta.servlet.http.HttpSession;

@Controller
public class GestorPagos {
	
	@Autowired
	private PagoDAO pagoDAO;

    @Autowired
    private ReservaDAO reservaDAO;

	private static final Logger log = LoggerFactory.getLogger(GestorUsuarios.class);
	
	@GetMapping("/pago")
    public String mostrarFormularioPago(HttpSession session, Model model) {
		
		Reserva reserva = (Reserva) session.getAttribute("reservaActual");
	    if (reserva == null) return "redirect:/reservar";
	    
		model.addAttribute("pago", new Pago());
        model.addAttribute("metodos", MetodoPago.values());
	    log.info(pagoDAO.findAll().toString());
        return "pago";
        
    }

    @PostMapping("/pago")
    public String procesarPago(@ModelAttribute Pago pago, HttpSession session, Model model) {
    	Reserva reserva = (Reserva) session.getAttribute("reservaActual");
        if (reserva == null) return "redirect:/reservar";
        
        pago.setReferencia(UUID.randomUUID());
        
        pago.setReserva(reserva);
        reserva.setPago(pago);
        
        pagoDAO.save(pago);
        reservaDAO.save(reserva);

        log.info("Pago registrado correctamente: " + pago);

        model.addAttribute("reserva", reserva);
        model.addAttribute("pago", pago);
        
        return "resultadoPago";
    }

	@GetMapping("/resultadoPago")
    public String mostrarResultadoPago() {
        return "resultadoPago";
    }
	
}