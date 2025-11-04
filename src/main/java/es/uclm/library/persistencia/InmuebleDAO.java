package es.uclm.library.persistencia;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;

public interface InmuebleDAO extends JpaRepository<Inmueble, Long> {

    Collection<Inmueble> findByPropietario(Propietario propietario);

    
    Collection<Inmueble> findByLocalizacionContainingIgnoreCase(String localizacion);

    
    Collection<Inmueble> findByTipo(String tipo);

   
    Collection<Inmueble> findByCapacidadGreaterThanEqual(int capacidad);

    
    Collection<Inmueble> findByPrecioNocheLessThanEqual(double precio);
}
