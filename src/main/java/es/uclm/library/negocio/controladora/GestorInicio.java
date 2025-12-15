package es.uclm.library.negocio.controladora;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNInmuebles;
import es.uclm.library.negocio.servicio.LNSolicitud;
import es.uclm.library.persistencia.PropietarioDAO;
import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class GestorInicio {

	@GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/inicio";
    }
	
	@Autowired
	private LNInmuebles lninmuebles;

	@Autowired
	private LNSolicitud lnSolicitud;

	@GetMapping("/inicio")
	public String mostrarInicio(HttpSession session, Model model) {

	    Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

	    if (usuario == null) {
	        model.addAttribute("estaLogueado", false);
	        return "inicio";
	    }

	    model.addAttribute("estaLogueado", true);
	    
	    Propietario propietario;
	    propietario = lninmuebles.obtenerPropietarioPorLogin(usuario.getLogin());
	    Inquilino inquilino;
	    inquilino = lnSolicitud.obtenerInquilinoPorLogin(usuario.getLogin());

	    if (propietario != null) {
	        int pendientes = lnSolicitud.contarSolicitudesPendientes(propietario);
	        model.addAttribute("solicitudesPendientes", pendientes);
	        model.addAttribute("esPropietario", true);
	    } else if (inquilino != null) {
	    	model.addAttribute("esInquilino", true);
	    }else {
	        model.addAttribute("esPropietario", false);
	        model.addAttribute("esInquilino", false);
	    }

	    return "inicio";
	}

	
}