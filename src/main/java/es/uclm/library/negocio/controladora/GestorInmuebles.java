package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNInmuebles;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inmuebles")
public class GestorInmuebles {

    @Autowired
    private LNInmuebles lnInmuebles;

    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model, HttpSession session) {
    	
    	Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

        model.addAttribute("inmueble", new Inmueble());

        if (usuario == null) {
            model.addAttribute("estaLogueado", false);
            return "alta-inmueble";
        }

        Propietario propietario = lnInmuebles.obtenerPropietarioPorLogin(usuario.getLogin());
        if (propietario == null) {
            model.addAttribute("estaLogueado", false);
            model.addAttribute("error", "Solo los propietarios pueden dar de alta inmuebles.");
        } else {
            model.addAttribute("estaLogueado", true);
            model.addAttribute("propietario", propietario);
        }

        return "alta-inmueble";
        
    }

    @PostMapping("/guardar")
    public String guardarInmueble(@ModelAttribute("inmueble") Inmueble inmueble,
            Model model, HttpSession session) {

    	Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

    	if (usuario == null) {
    			model.addAttribute("error", "Debes iniciar sesión como propietario para registrar un inmueble.");
    			model.addAttribute("estaLogueado", false);
    			return "alta-inmueble";
    	}	
    	
    	Propietario propietario = lnInmuebles.obtenerPropietarioPorLogin(usuario.getLogin());
    	
    	if (propietario == null) {
    		model.addAttribute("error", "Solo los propietarios pueden registrar inmuebles.");
    		model.addAttribute("estaLogueado", false);
    		return "alta-inmueble";
    	}
    	
        Inmueble inmuebleGuardado = lnInmuebles.registrarInmueble(inmueble, propietario.getLogin());
        
        model.addAttribute("estaLogueado", true);
        model.addAttribute("propietario", propietario);
        
        if (inmuebleGuardado == null) {
            model.addAttribute("error", "Debes iniciar sesión como propietario para registrar un inmueble.");
            return "alta-inmueble";
        }

        model.addAttribute("mensaje", "Inmueble registrado correctamente.");

        return "redirect:/inmuebles/exito";
    }
    
    @GetMapping("/exito")
    public String mostrarConfirmacion() {
        return "exito";
    }
    
    @GetMapping("/lista")
    public String listarInmuebles(Model model) {
        model.addAttribute("inmuebles", lnInmuebles.obtenerTodos());
        return "mostrarInmuebles";
    }

    @GetMapping("/detalle/{id}")
    public String detalleInmueble(@PathVariable Long id, Model model) {
        Inmueble inmueble = lnInmuebles.obtenerInmueblePorId(id);
        if (inmueble == null) {
            model.addAttribute("error", "El inmueble no existe.");
            return "redirect:/inmuebles/lista";
        }
        model.addAttribute("inmueble", inmueble);
        return "inmuebleDetalle";
    }

}
