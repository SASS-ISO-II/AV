package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.servicio.LNBusqueda;
import es.uclm.library.negocio.servicio.LNInmuebles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class GestorBusquedas {

    @Autowired
    private LNBusqueda lnBusqueda;

    @Autowired
    private LNInmuebles lnInmuebles;


    
    @GetMapping("/inmueble")
    public String mostrarInmuebles(Model model) {

        
        Collection<Inmueble> inmuebles = lnInmuebles.obtenerTodos();

        model.addAttribute("inmuebles", inmuebles);

        
        model.addAttribute("localizacion", "");
        model.addAttribute("precioMax", "");
        model.addAttribute("capacidad", "");
        model.addAttribute("tipo", "todos");

        return "inmueble";

    }


   
    @GetMapping("/inmueble/buscar")
    public String buscar(
            @RequestParam(required = false) String localizacion,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Integer capacidad,
            @RequestParam(required = false) String tipo,
            Model model) {

        
        Collection<Inmueble> filtrados = lnBusqueda.buscar(localizacion, precioMax, capacidad, tipo);

        model.addAttribute("inmuebles", filtrados);

       
        model.addAttribute("localizacion", localizacion != null ? localizacion : "");
        model.addAttribute("precioMax", precioMax != null ? precioMax : "");
        model.addAttribute("capacidad", capacidad != null ? capacidad : "");
        model.addAttribute("tipo", tipo != null ? tipo : "todos");

        return "inmueble";

    }
}
