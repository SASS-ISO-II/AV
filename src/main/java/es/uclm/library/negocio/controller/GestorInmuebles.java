package es.uclm.library.negocio.controller;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.persistencia.InmuebleDAO;
import es.uclm.library.persistencia.PropietarioDAO;
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
    private InmuebleDAO inmuebleDAO;

    @Autowired
    private PropietarioDAO propietarioDAO;

    // ============================================
    // GET: mostrar formulario de alta de inmueble
    // ============================================
    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model,
                                        @RequestParam(value = "login", required = false) String login) {

        model.addAttribute("inmueble", new Inmueble());

        // Si no hay login → formulario bloqueado
        if (login == null || login.isEmpty()) {
            model.addAttribute("estaLogueado", false);
            return "alta-inmueble";
        }

        // Buscar si ese login pertenece a un propietario
        Propietario propietario = propietarioDAO.findByLogin(login);

        if (propietario == null) {
            // El login no pertenece a un propietario válido
            model.addAttribute("estaLogueado", false);
        } else {
            // Es un propietario válido → habilitar formulario
            model.addAttribute("estaLogueado", true);
            model.addAttribute("propietario", propietario);
        }

        return "alta-inmueble";
    }

    // ============================================
    // POST: guardar inmueble
    // ============================================
    @PostMapping("/guardar")
    public String guardarInmueble(@ModelAttribute("inmueble") Inmueble inmueble,
                                  @RequestParam("login") String login,
                                  Model model) {

        Propietario propietario = propietarioDAO.findByLogin(login);

        if (propietario == null) {
            model.addAttribute("error", "Debes iniciar sesión como propietario para registrar un inmueble.");
            model.addAttribute("estaLogueado", false);
            return "alta-inmueble";
        }

        // Asociar inmueble con propietario
        inmueble.setPropietario(propietario);
        inmuebleDAO.save(inmueble);

        // Confirmación visual
        model.addAttribute("mensaje", "Inmueble registrado correctamente.");
        model.addAttribute("estaLogueado", true);
        model.addAttribute("propietario", propietario);

        return "alta-inmueble";
    }
}
