package es.uclm.library.negocio.servicio;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.persistencia.InmuebleDAO;
import es.uclm.library.persistencia.PropietarioDAO;

@Service
public class LNInmuebles {

    @Autowired
    private InmuebleDAO inmuebleDAO;

    @Autowired
    private PropietarioDAO propietarioDAO;

    /**
     * Obtiene el propietario asociado a un login.
     */
    public Propietario obtenerPropietarioPorLogin(String login) {
        return propietarioDAO.findByLogin(login);
    }

    /**
     * Registra un nuevo inmueble para un propietario.
     * 
     * @return el inmueble guardado, o null si no existe el propietario.
     */
    public Inmueble registrarInmueble(Inmueble inmueble, String login) {
        Propietario propietario = propietarioDAO.findByLogin(login);
        if (propietario == null) {
            return null; // No se puede registrar si no existe propietario
        }

        inmueble.setPropietario(propietario);
        return inmuebleDAO.save(inmueble);
    }
    
    public Collection<Inmueble> obtenerTodos() {
        return inmuebleDAO.findAll(); // devuelve todos los inmuebles de la BD
    }

    public Inmueble obtenerInmueblePorId(Long id) {
        return inmuebleDAO.findById(id).orElse(null);
    }

}
