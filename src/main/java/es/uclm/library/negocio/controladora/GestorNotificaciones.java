package es.uclm.library.negocio.controladora;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uclm.library.negocio.dominio.EstadoSolicitud;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNSolicitud;
import es.uclm.library.persistencia.PropietarioDAO;
import es.uclm.library.persistencia.SolicitudReservaDAO;
import jakarta.servlet.http.HttpSession;

@Controller
public class GestorNotificaciones {

	private static final String SOLICITUDES = "solicitudes";  // Compliant
	
    @Autowired
    private LNSolicitud lnSolicitudReserva;

    @Autowired
    private PropietarioDAO propietarioDAO;

    @Autowired
    private SolicitudReservaDAO solicitudReservaDAO;

    @GetMapping("/solicitudes")
    public String listarSolicitudes(HttpSession session, Model model, @RequestParam(required = false, defaultValue = "TODAS") String filtro) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
        if (usuario == null) return "redirect:/login";
        
        Propietario propietario = propietarioDAO.findByLogin(usuario.getLogin());
        
        model.addAttribute(SOLICITUDES, solicitudReservaDAO.findByPropietario(propietario));
        
        if ("TODAS".equalsIgnoreCase(filtro)) {
            model.addAttribute(SOLICITUDES, solicitudReservaDAO.findByPropietario(propietario));
        } else {
            model.addAttribute(SOLICITUDES, solicitudReservaDAO.findByPropietarioAndEstado(
                    propietario, EstadoSolicitud.valueOf(filtro)));
        }

        model.addAttribute("filtroActual", filtro);

        return SOLICITUDES;
    }
    
    @PostMapping("/solicitudes")
    public String procesarSolicitud(@RequestParam Long id, @RequestParam String accion) {

        if ("ACEPTAR".equalsIgnoreCase(accion)) {
            lnSolicitudReserva.aceptarSolicitud(id);
        } else if ("RECHAZAR".equalsIgnoreCase(accion)) {
            lnSolicitudReserva.rechazarSolicitud(id);
        }

        return "redirect:/solicitudes";
    }
    
}