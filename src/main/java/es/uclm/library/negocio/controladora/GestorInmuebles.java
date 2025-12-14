package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.servicio.LNInmuebles;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestorInmuebles {

    @Autowired
    private LNInmuebles lnInmuebles;

    @GetMapping("/alta")
    public String mostrarFormularioAlta(Model model, HttpSession session) {
    	
    	Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

        model.addAttribute("inmueble", new Inmueble());

        if (usuario == null) {
            model.addAttribute("estaLogueado", false);
            return "alta";
        }

        Propietario propietario = lnInmuebles.obtenerPropietarioPorLogin(usuario.getLogin());
        if (propietario == null) {
            model.addAttribute("estaLogueado", false);
            model.addAttribute("error", "Solo los propietarios pueden dar de alta inmuebles.");
        } else {
            model.addAttribute("estaLogueado", true);
            model.addAttribute("propietario", propietario);
        }

        return "alta";
        
    }

    @PostMapping("/alta")
    public String guardarInmueble(@ModelAttribute("inmueble") Inmueble inmueble,
            Model model, HttpSession session) {

    	Usuario usuario = (Usuario) session.getAttribute("usuarioAutenticado");

    	if (usuario == null) {
    			model.addAttribute("error", "Debes iniciar sesión como propietario para registrar un inmueble.");
    			model.addAttribute("estaLogueado", false);
    			return "alta";
    	}	
    	
    	Propietario propietario = lnInmuebles.obtenerPropietarioPorLogin(usuario.getLogin());
    	
    	if (propietario == null) {
    		model.addAttribute("error", "Solo los propietarios pueden registrar inmuebles.");
    		model.addAttribute("estaLogueado", false);
    		return "alta";
    	}
    	
        Inmueble inmuebleGuardado = lnInmuebles.registrarInmueble(inmueble, propietario.getLogin());
        
        model.addAttribute("estaLogueado", true);
        model.addAttribute("propietario", propietario);
        
        if (inmuebleGuardado == null) {
            model.addAttribute("error", "Debes iniciar sesión como propietario para registrar un inmueble.");
            return "alta";
        }

       
        model.addAttribute("popupExito", true);

        
        model.addAttribute("mensaje", "Inmueble registrado correctamente.");

       
        return "alta";
    }

  

    @GetMapping("/detalle/{id}")
    public String detalleInmueble(@PathVariable Long id, Model model, HttpSession session) {
        Inmueble inmueble = lnInmuebles.obtenerInmueblePorId(id);
        if (inmueble == null) {
            model.addAttribute("error", "El inmueble no existe.");
            return "redirect:/inmueble";
        }

        Object usuario = session.getAttribute("usuarioAutenticado");

        boolean estaLogueado = usuario != null;
        boolean esInquilino = usuario instanceof Inquilino;

        model.addAttribute("estaLogueado", estaLogueado);
        model.addAttribute("esInquilino", esInquilino);
        model.addAttribute("inmueble", inmueble);
        return "detalle";
    }

}
