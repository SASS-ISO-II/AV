package es.uclm.library.negocio.controladora;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNInmuebles;
import es.uclm.library.negocio.servicio.LNSolicitud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import es.uclm.library.persistencia.PropietarioDAO;
import es.uclm.library.persistencia.UsuarioDAO;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import es.uclm.library.persistencia.InquilinoDAO;

@Controller
@Transactional
public class GestorUsuarios {
	
	private static final String USUARIO_AUTENTICADO = "usuarioAutenticado";

	private static final String USUARIO = "usuario"; 

	private static final String LOGIN = "redirect:/login";  
	
	private static final Logger log = LoggerFactory.getLogger(GestorUsuarios.class);
	@Autowired
	private PropietarioDAO propietarioDAO;
	
	@Autowired
	private InquilinoDAO inquilinoDAO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@Autowired
	private LNInmuebles lninmuebles;

	@Autowired
	private LNSolicitud lnSolicitud;

	
	@GetMapping("/registro")
	public String mostrarRegistro(Model model) {
	    model.addAttribute("registro", new Usuario());
	    return "registro";
	}
	
	@PostMapping("/registro")
	public String loginUsuario(@RequestParam("tipoUsuario") String tipoUsuario, @ModelAttribute("registro") Usuario usuario, Model model,
	        HttpSession session) {
		
		if (!usuario.getLogin().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
	        model.addAttribute("error", "El login debe ser un correo electrónico válido.");
	        return "registro";
	    }
		
	    if ("propietario".equalsIgnoreCase(tipoUsuario)) {
	        Propietario propietario = new Propietario();
	        copiarDatos(usuario, propietario);
	        model.addAttribute("registro", propietario);
	        
	        Propietario savedPropietario = propietarioDAO.save(propietario);
	        log.info("Saved propietario: " + savedPropietario);
	        session.setAttribute(USUARIO_AUTENTICADO, savedPropietario);

	        return "propietario";
	        
	    } else { 
	        Inquilino inquilino = new Inquilino();
	        copiarDatos(usuario, inquilino);
	        model.addAttribute("registro", inquilino);

	        Inquilino savedInquilino = inquilinoDAO.save(inquilino);
	        log.info("Saved inquilino: " + savedInquilino);
	        session.setAttribute(USUARIO_AUTENTICADO, savedInquilino);

	        return USUARIO;
	        
	    }
	    
	}
	
	@GetMapping("/login")
	public String mostrarLogin(Model model) {
	    model.addAttribute("login", new Usuario());
	    return "login";
	}

	@PostMapping("/login")
	public String hacerLogin(@ModelAttribute("login") Usuario usuario, Model model, HttpSession session) {
		
	    Usuario encontrado = usuarioDAO.findByLoginAndPass(usuario.getLogin(), usuario.getPass());
	    
	    if (encontrado == null) {
	        model.addAttribute("error", "Usuario o contraseña incorrectos");
	        return "login";
	    }
	    
	    session.setAttribute(USUARIO_AUTENTICADO, encontrado);
	    
	    Long inmueblePendiente = (Long) session.getAttribute("inmueblePendiente");
	    if (inmueblePendiente != null) {
	        session.removeAttribute("inmueblePendiente");
	        return "redirect:/reserva?inmuebleId=" + inmueblePendiente;
	    }
	    
	    Boolean altaPendiente = (Boolean) session.getAttribute("altaPendiente");
	    if (altaPendiente != null && altaPendiente) {
	        session.removeAttribute("altaPendiente");
	        return "redirect:/alta";
	    }
	    
	    Propietario propietario = propietarioDAO.findByLogin(encontrado.getLogin());
	    if (propietario != null) {
	        model.addAttribute("registro", propietario);
	        return "propietario";
	    }
	    
	    Inquilino inquilino = inquilinoDAO.findByLogin(encontrado.getLogin());
	    if (inquilino != null) {
	        model.addAttribute("registro", inquilino);
	        return USUARIO; 
	    }
	    
	    return "login";
	    
	}
	
	@GetMapping("/usuario")
	public String mostrarInquilino(Model model, HttpSession session) {
		
		Usuario usuario = (Usuario) session.getAttribute(USUARIO_AUTENTICADO);
		
	    if (usuario == null) {
	        return LOGIN;
	    }

	    Inquilino inquilino = lnSolicitud.obtenerInquilinoPorLogin(usuario.getLogin());

        if (inquilino == null) {
            return LOGIN;
        }


        model.addAttribute("registro", inquilino);
        return USUARIO;
        
	}
	
	@GetMapping("/propietario")
	public String mostrarPropietario(Model model, HttpSession session) {

		Usuario usuario = (Usuario) session.getAttribute(USUARIO_AUTENTICADO);
		
	    if (usuario == null) {
	        return LOGIN;
	    }

	    Propietario propietario = lninmuebles.obtenerPropietarioPorLogin(usuario.getLogin());

	    if (propietario == null) {
	        return "redirect:/inicio";
	    }

	    model.addAttribute("registro", propietario);
	    return "propietario";
	    
	}

	private void copiarDatos(Usuario origen, Usuario destino) {
	    destino.setLogin(origen.getLogin());
	    destino.setPass(origen.getPass());
	    destino.setNombre(origen.getNombre());
	    destino.setApellidos(origen.getApellidos());
	    destino.setDireccion(origen.getDireccion());
	}
	
}