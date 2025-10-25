package es.uclm.library.negocio.controladora;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNInmuebles;
import es.uclm.library.negocio.servicio.LNReservas;
import es.uclm.library.persistencia.ReservaDAO;
import jakarta.servlet.http.HttpSession;

@Controller
public class GestorReservas {
	
	@Autowired
	private ReservaDAO reservaDAO;
	
	@Autowired
	private LNInmuebles lnInmuebles;

	@Autowired
	private LNReservas lnReservas;
	
	@GetMapping("/reservar")
	public String mostrarFormularioReserva(@RequestParam(name="inmuebleId", required=false) Long inmuebleId, Model model, HttpSession session) {
	    
		Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
	    
		if (usuario == null) return "redirect:/login";

		Inmueble inmueble;
	
	    if (inmuebleId != null) {
	        inmueble = lnInmuebles.obtenerInmueblePorId(inmuebleId);
	        if (inmueble == null) {
	            model.addAttribute("error", "El inmueble seleccionado no existe.");
	            return "redirect:/inmuebles/lista";
	        }
	        session.setAttribute("inmuebleActual", inmueble);
	    } else {
	    	inmueble = (Inmueble) session.getAttribute("inmuebleActual");
	        if (inmueble == null) {
	            model.addAttribute("error", "Debes seleccionar un inmueble antes de reservar.");
	            return "redirect:/inmuebles/lista";
	        }
	    }

	    model.addAttribute("reserva", new Reserva());
	    model.addAttribute("inmueble", inmueble);
	    
	    var reservasExistentes = reservaDAO.findReservasPorInmueble(inmueble);
	    model.addAttribute("reservasExistentes", reservasExistentes);

	    return "reservar";
	}
	
	@PostMapping("/reservar")
	public String procesarReserva(@ModelAttribute("reserva") Reserva reserva, HttpSession session, Model model) {
	    Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
	    if (usuario == null) return "redirect:/login";

	    Inquilino inquilino = lnReservas.obtenerInquilinoPorLogin(usuario.getLogin());
	    if (inquilino == null) {
	        model.addAttribute("error", "Solo los inquilinos pueden hacer reservas");
	        return "redirect:/login";
	    }

	    Inmueble inmueble = (Inmueble) session.getAttribute("inmuebleActual");
	    
	    if (!lnReservas.validarFechas(reserva)) {
	        model.addAttribute("error", "No se pueden seleccionar fechas pasadas");
	        model.addAttribute("reserva", reserva);
	        model.addAttribute("inmueble", inmueble);
	        var reservasExistentes = reservaDAO.findReservasPorInmueble(inmueble);
	        model.addAttribute("reservasExistentes", reservasExistentes);
	        return "reservar";
	    }

	    if (lnReservas.haySolapamiento(inmueble, reserva.getFechaInicio(), reserva.getFechaFin())) {
	        model.addAttribute("error", "Esas fechas ya están reservadas. Por favor, elige otro rango.");
	        model.addAttribute("reserva", reserva);
	        model.addAttribute("inmueble", inmueble);
	        var reservasExistentes = reservaDAO.findReservasPorInmueble(inmueble);
	        model.addAttribute("reservasExistentes", reservasExistentes);
	        return "reservar";
	    }
	    
	    if (inmueble == null) {
	        model.addAttribute("error", "No se ha seleccionado ningún inmueble.");
	        return "redirect:/inmuebles/lista";
	    }

	    reserva.setInquilino(inquilino);
	    reserva.setInmueble(inmueble);
	    
	    session.setAttribute("reservaActual", reserva);
	    
	    return "redirect:/pago";
	}
}
