package es.uclm.library.negocio.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import es.uclm.library.negocio.dominio.Disponibilidad;
import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.persistencia.InmuebleDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LNBusqueda {

    @Autowired
    private InmuebleDAO inmuebleDAO;

    public List<Inmueble> buscarInmuebles(
            String localizacion,
            Integer capacidad,
            String tipo,
            Double precioMax,
            LocalDate inicio,
            LocalDate fin) {

        List<Inmueble> inmuebles = inmuebleDAO.findAll();

        return inmuebles.stream()
                .filter(i -> localizacion == null || localizacion.isEmpty() ||
                        i.getLocalizacion().toLowerCase().contains(localizacion.toLowerCase()))
                .filter(i -> capacidad == null || i.getCapacidad() >= capacidad)
                .filter(i -> tipo == null || tipo.isEmpty() || 
                        i.getTipo().equalsIgnoreCase(tipo))
                .filter(i -> precioMax == null || i.getPrecioNoche() <= precioMax)
                .filter(i -> estaDisponible(i, inicio, fin))
                .collect(Collectors.toList());
    }

    private boolean estaDisponible(Inmueble inmueble, LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) return true;

        for (Disponibilidad d : inmueble.getDisponibilidades()) {
            if ( !inicio.isBefore(d.getFechaInicio().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()) &&
                 !fin.isAfter(d.getFechaFin().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())) {
                return true;
            }
        }
        return false;
    }
}
