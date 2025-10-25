package es.uclm.library.negocio.controladora;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestorInicio {

	@GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/inicio";
    }
	
	@GetMapping("/inicio")
    public String mostrarInicio() {
        return "inicio";
    }
	
}
