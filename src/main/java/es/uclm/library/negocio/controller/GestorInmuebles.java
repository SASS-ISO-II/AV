package es.uclm.library.negocio.controller;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.persistencia.InmuebleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/inmuebles")
public class GestorInmuebles {

    @Autowired
    private InmuebleDAO inmuebleDAO;

    // Mostrar formulario de alta
    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model) {
        model.addAttribute("inmueble", new Inmueble());
        return "alta-inmueble"; // nombre del HTML
    }

    // Guardar inmueble
    @PostMapping("/guardar")
    public String guardarInmueble(@ModelAttribute Inmueble inmueble) {
        inmuebleDAO.save(inmueble);
        return "exito"; // redirige a una página de éxito o confirmación
    }
}
