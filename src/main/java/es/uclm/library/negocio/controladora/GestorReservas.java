package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNReservas;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import es.uclm.library.negocio.dominio.PoliticaCancelacion;

import java.time.temporal.ChronoUnit;


@Controller
public class GestorReservas {

    @Autowired
    private LNReservas lnReservas;

    @GetMapping("/reserva")
    public String mostrarFormularioReserva(@RequestParam(name="inmuebleId", required=false) Long inmuebleId,
                                           Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
        if (usuario == null) {
            session.setAttribute("inmueblePendiente", inmuebleId);
            return "redirect:/login";
        }
        
        Inmueble inmueble = lnReservas.obtenerInmuebleParaReserva(inmuebleId, session, model);
        if (inmueble == null) {
            model.addAttribute("error", "Debes seleccionar un inmueble antes de reservar.");
            return "redirect:/inmueble";
        }
        
        Collection<Reserva> reservasOriginales = lnReservas.obtenerReservasPorInmueble(inmueble);

        List<Reserva> reservasFiltradas = new ArrayList<>();

        LocalDate hoy = LocalDate.now();

        for (Reserva r : reservasOriginales) {
            if (!r.getFechaFin().isBefore(hoy)) { 
                reservasFiltradas.add(r);
            }
        }
        
        model.addAttribute("reservasExistentes", reservasFiltradas);
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("inmueble", inmueble);

        return "reserva";
    }

    @PostMapping("/reserva")
    public String procesarReserva(@ModelAttribute("reserva") Reserva reserva, HttpSession session,
                                  Model model, @RequestParam(value = "politicaCancelacion", required = false) String politicaHidden) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");
        if (usuario == null) return "redirect:/login";

        LocalDate inicio = reserva.getFechaInicio();
        LocalDate fin = reserva.getFechaFin();
        
        reserva.setInquilino(lnReservas.obtenerInquilinoPorLogin(usuario.getLogin()));
        
        Inmueble inmueble = (Inmueble) session.getAttribute("inmuebleActual");

        reserva.setInmueble(inmueble);

        if (politicaHidden != null && !politicaHidden.isEmpty()) {
            try {
                reserva.setPoliticaCancelacion(PoliticaCancelacion.valueOf(politicaHidden));
            } catch (Exception ignored) {}
        }

        
        if (inicio != null && fin != null) {

            long dias = ChronoUnit.DAYS.between(inicio, fin);

            String politica = "No definida";

            if (dias <= 3) {
                politica = "REEMBOLSABLE";
            } else if (dias <= 8) {
                politica = "REEMBOLSABLE_50_PER";
            } else {			
                politica = "NO_REEMBOLSABLE";
            }

            
            model.addAttribute("politicaCancelacion", politica);

            
            reserva.setPoliticaCancelacion(PoliticaCancelacion.valueOf(politica));
        }
        

        String resultado = lnReservas.procesarReserva(reserva, usuario, session, model);

        if (resultado.equals("error")) {
            model.addAttribute("reserva", reserva);
            model.addAttribute("inmueble", inmueble);
            model.addAttribute("reservasExistentes", lnReservas.obtenerReservasPorInmueble(inmueble));
            return "reserva";
        }

        return "redirect:/pago";
    }
}
