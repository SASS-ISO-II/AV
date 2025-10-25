package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.servicio.LNInmuebles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/inmuebles")
public class GestorInmuebles {

    @Autowired
    private LNInmuebles lnInmuebles;

    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model,
                                        @RequestParam(value = "login", required = false) String login) {

        model.addAttribute("inmueble", new Inmueble());

        if (login == null || login.isEmpty()) {
            model.addAttribute("estaLogueado", false);
            return "alta-inmueble";
        }

        Propietario propietario = lnInmuebles.obtenerPropietarioPorLogin(login);

        if (propietario == null) {
            model.addAttribute("estaLogueado", false);
        } else {
            model.addAttribute("estaLogueado", true);
            model.addAttribute("propietario", propietario);
        }

        return "alta-inmueble";
    }

    @PostMapping("/guardar")
    public String guardarInmueble(@ModelAttribute("inmueble") Inmueble inmueble,
                                  @RequestParam("login") String login,
                                  Model model) {

        Inmueble inmuebleGuardado = lnInmuebles.registrarInmueble(inmueble, login);

        if (inmuebleGuardado == null) {
            model.addAttribute("error", "Debes iniciar sesión como propietario para registrar un inmueble.");
            model.addAttribute("estaLogueado", false);
            return "alta-inmueble";
        }

        model.addAttribute("mensaje", "Inmueble registrado correctamente.");
        model.addAttribute("estaLogueado", true);
        model.addAttribute("propietario", inmuebleGuardado.getPropietario());

        return "alta-inmueble";
    }
}
