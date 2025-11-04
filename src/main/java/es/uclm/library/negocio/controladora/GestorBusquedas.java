package es.uclm.library.negocio.controladora;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.servicio.LNBusqueda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class GestorBusquedas {

    @Autowired
    private LNBusqueda lnBusqueda;

    @GetMapping("/busqueda")
    public String mostrarFormularioBusqueda() {
        return "busqueda";
    }

    @PostMapping("/busqueda")
    public String procesarBusqueda(
            @RequestParam(required = false) String localizacion,
            @RequestParam(required = false) Integer capacidad,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) String fechaInicio,
            @RequestParam(required = false) String fechaFin,
            Model model) {

        LocalDate inicio = (fechaInicio == null || fechaInicio.isEmpty()) 
                ? null : LocalDate.parse(fechaInicio);
        LocalDate fin = (fechaFin == null || fechaFin.isEmpty()) 
                ? null : LocalDate.parse(fechaFin);

        List<Inmueble> resultados =
                lnBusqueda.buscarInmuebles(localizacion, capacidad, tipo, precioMax, inicio, fin);

        model.addAttribute("resultados", resultados);

        return "resultadosBusqueda";
    }
}
