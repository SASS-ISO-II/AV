package es.uclm.library.negocio.servicio;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.persistencia.InmuebleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class LNBusqueda {

    @Autowired
    private InmuebleDAO inmuebleDAO;

    public Collection<Inmueble> buscar(String localizacion,
                                       Double precioMax,
                                       Integer capacidad,
                                       String tipo) {

        Collection<Inmueble> inmuebles = inmuebleDAO.findAll();

        
        if (localizacion != null && !localizacion.isBlank()) {
            inmuebles = inmuebles.stream()
                    .filter(i -> i.getLocalizacion() != null &&
                                 i.getLocalizacion().toLowerCase().contains(localizacion.toLowerCase()))
                    .collect(Collectors.toList());
        }

        
        if (precioMax != null && precioMax > 0) {
            inmuebles = inmuebles.stream()
                    .filter(i -> i.getPrecioNoche() <= precioMax)
                    .collect(Collectors.toList());
        }

        
        if (capacidad != null && capacidad > 0) {
            inmuebles = inmuebles.stream()
                    .filter(i -> i.getCapacidad() >= capacidad)
                    .collect(Collectors.toList());
        }

       
        if (tipo != null && !tipo.equalsIgnoreCase("todos") && !tipo.isBlank()) {
            inmuebles = inmuebles.stream()
                    .filter(i -> i.getTipo() != null &&
                                 i.getTipo().equalsIgnoreCase(tipo))
                    .collect(Collectors.toList());
        }

        return inmuebles;
    }
}
