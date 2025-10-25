package es.uclm.library.negocio.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.persistencia.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class GestorReservas {
	
	@Autowired
	private ReservaDAO reservaDAO;

	@Autowired
	private InquilinoDAO inquilinoDAO;
	
	@GetMapping("/reservar")
	public String mostrarFormularioReserva(Model model, HttpSession session) {
	    Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

	    if (usuario == null) {
	        return "redirect:/login";
	    }
	    
	    if (!(usuario instanceof Inquilino)) {
	        model.addAttribute("error", "Solo los inquilinos pueden hacer reservas");
	        return "redirect:/login";
	    }

	    model.addAttribute("reserva", new Reserva());
	    
	    var reservasExistentes = reservaDAO.findAll();
	    model.addAttribute("reservasExistentes", reservasExistentes);
	    
	    return "reservar";
	}
	
	@PostMapping("/reservar")
	public String procesarReserva(@ModelAttribute("reserva") Reserva reserva, HttpSession session, Model model) {
	    Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

	    if (usuario == null) {
	        return "redirect:/login";
	    }

	    Inquilino inquilino = inquilinoDAO.findByLogin(usuario.getLogin());

	    if (inquilino == null) {
	        model.addAttribute("error", "Solo los inquilinos pueden hacer reservas");
	        return "redirect:/login";
	    }
	    
	    LocalDate hoy = LocalDate.now();
	    if (reserva.getFechaInicio().isBefore(hoy) || reserva.getFechaFin().isBefore(hoy)) {
	        model.addAttribute("error", "No se pueden seleccionar fechas pasadas");
	        model.addAttribute("reserva", new Reserva());
	        return "reservar";
	    }
	    
	    var reservasSolapadas = reservaDAO.findReservasSolapadas(reserva.getFechaInicio(), reserva.getFechaFin());
	    if (!reservasSolapadas.isEmpty()) {
	        model.addAttribute("error", "Esas fechas ya están reservadas. Por favor, elige otro rango.");
	        model.addAttribute("reserva", new Reserva());
	        return "reservar";
	    }

	    reserva.setInquilino(inquilino);
	    reservaDAO.save(reserva);
	    session.setAttribute("reservaActual", reserva);
	    return "redirect:/pago";
	}

}