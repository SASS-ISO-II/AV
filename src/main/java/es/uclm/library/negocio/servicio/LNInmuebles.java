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

    public Propietario obtenerPropietarioPorLogin(String login) {
        return propietarioDAO.findByLogin(login);
    }

    public Inmueble registrarInmueble(Inmueble inmueble, String login) {
        Propietario propietario = propietarioDAO.findByLogin(login);
        if (propietario == null) {
            return null;
        }

        inmueble.setPropietario(propietario);
        return inmuebleDAO.save(inmueble);
        
    }
    
    public Collection<Inmueble> obtenerTodos() {
        return inmuebleDAO.findAll();
    }

    public Inmueble obtenerInmueblePorId(Long id) {
        return inmuebleDAO.findById(id).orElse(null);
    }

}
