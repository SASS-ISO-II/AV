package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.ListaDeseos;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNListaDeseos;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/listaDeseos")
public class GestorListaDeseos {

    @Autowired
    private LNListaDeseos lnListaDeseos;

    @GetMapping
    public String mostrarListaDeseos(HttpSession session, Model model) {
        Object usuario = session.getAttribute("usuarioAutenticado");

        if (usuario == null || !(usuario instanceof Inquilino)) {
            model.addAttribute("error", "Debes iniciar sesión como inquilino para ver tu lista de deseos.");
            model.addAttribute("estaLogueado", false);
            return "listaDeseos";
        }

        String login = ((Usuario) usuario).getLogin();
        ListaDeseos lista = lnListaDeseos.obtenerListaDeseosPorInquilino(login);

        model.addAttribute("estaLogueado", true);
        model.addAttribute("listaDeseos", lista);
        model.addAttribute("inmuebles", lista.getInmuebles());

        return "listaDeseos";
    }

    @GetMapping("/agregar/{idInmueble}")
    public String agregarInmueble(@PathVariable Long idInmueble,
                                  HttpSession session, Model model) {

        Object usuario = session.getAttribute("usuarioAutenticado");

        if (usuario == null || !(usuario instanceof Inquilino)) {
            return "redirect:/login";
        }

        String login = ((Usuario) usuario).getLogin();
        lnListaDeseos.agregarInmuebleALista(login, idInmueble);

        return "redirect:/listaDeseos";
    }

    @GetMapping("/eliminar/{idInmueble}")
    public String eliminarInmueble(@PathVariable Long idInmueble,
                                   HttpSession session, Model model) {

        Object usuario = session.getAttribute("usuarioAutenticado");

        if (usuario == null || !(usuario instanceof Inquilino)) {
            return "redirect:/login";
        }

        String login = ((Usuario) usuario).getLogin();
        lnListaDeseos.eliminarInmuebleDeLista(login, idInmueble);

        return "redirect:/listaDeseos";
    }
}
