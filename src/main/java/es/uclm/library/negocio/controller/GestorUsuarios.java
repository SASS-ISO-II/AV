package es.uclm.library.negocio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;

@Controller
public class GestorUsuarios {
	
	@GetMapping("/login")
	public String mostrarLogin(Model model) {
	    model.addAttribute("login", new Usuario());
	    return "login";
	}
	
	@PostMapping("/login")
	public String loginUsuario(@RequestParam("tipoUsuario") String tipoUsuario, @ModelAttribute("login") Usuario usuario, Model model) {

	    if ("propietario".equalsIgnoreCase(tipoUsuario)) {
	        Propietario propietario = new Propietario();
	        copiarDatos(usuario, propietario);
	        model.addAttribute("login", propietario);
	        return "propietario";
	    } else { 
	        Inquilino inquilino = new Inquilino();
	        copiarDatos(usuario, inquilino);
	        model.addAttribute("login", inquilino);
	        return "usuario";
	    }
	    
	}
	
	private void copiarDatos(Usuario origen, Usuario destino) {
	    destino.setLogin(origen.getLogin());
	    destino.setPass(origen.getPass());
	    destino.setNombre(origen.getNombre());
	    destino.setApellidos(origen.getApellidos());
	    destino.setDireccion(origen.getDireccion());
	}
	
}