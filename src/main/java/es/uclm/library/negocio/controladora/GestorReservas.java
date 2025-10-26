package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNReservas;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GestorReservas {

    @Autowired
    private LNReservas lnReservas;

    @GetMapping("/reservar")
    public String mostrarFormularioReserva(@RequestParam(name="inmuebleId", required=false) Long inmuebleId,
                                           Model model,
                                           HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
        if (usuario == null) return "redirect:/login";

        Inmueble inmueble = lnReservas.obtenerInmuebleParaReserva(inmuebleId, session, model);
        if (inmueble == null) {
            model.addAttribute("error", "Debes seleccionar un inmueble antes de reservar.");
            return "redirect:/inmuebles/lista";
        }

        model.addAttribute("reserva", new Reserva());
        model.addAttribute("inmueble", inmueble);
        model.addAttribute("reservasExistentes", lnReservas.obtenerReservasPorInmueble(inmueble));

        return "reservar";
    }

    @PostMapping("/reservar")
    public String procesarReserva(@ModelAttribute("reserva") Reserva reserva,
                                  HttpSession session,
                                  Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
        if (usuario == null) return "redirect:/login";

        String resultado = lnReservas.procesarReserva(reserva, usuario, session, model);

        if (resultado.equals("error")) {
            model.addAttribute("reserva", reserva);
            Inmueble inmueble = (Inmueble) session.getAttribute("inmuebleActual");
            model.addAttribute("inmueble", inmueble);
            model.addAttribute("reservasExistentes", lnReservas.obtenerReservasPorInmueble(inmueble));
            return "reservar";
        }

        return "redirect:/pago";
    }
}
