package es.uclm.library.negocio.servicio;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uclm.library.negocio.controladora.GestorPagos;
import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.ListaDeseos;
import es.uclm.library.persistencia.InmuebleDAO;
import es.uclm.library.persistencia.InquilinoDAO;
import es.uclm.library.persistencia.ListaDeseosDAO;

@Service
public class LNListaDeseos {

    @Autowired
    private ListaDeseosDAO listaDeseosDAO;

    @Autowired
    private InquilinoDAO inquilinoDAO;

    @Autowired
    private InmuebleDAO inmuebleDAO;

    private static final Logger log = LoggerFactory.getLogger(GestorPagos.class);

    public ListaDeseos obtenerListaDeseosPorInquilino(String loginInquilino) {
        Inquilino inquilino = inquilinoDAO.findByLogin(loginInquilino);
        if (inquilino == null) {
            return null;
        }

        ListaDeseos lista = inquilino.getListaDeseos();

        if (lista == null) {
            lista = new ListaDeseos();
            lista.setUsuario(inquilino);
            listaDeseosDAO.save(lista);
            inquilino.setListaDeseos(lista);
            inquilinoDAO.save(inquilino);
        }

        return lista;
    }


    public boolean agregarInmuebleALista(String loginInquilino, Long idInmueble) {
        Inquilino inquilino = inquilinoDAO.findByLogin(loginInquilino);
        Optional<Inmueble> inmuebleOpt = inmuebleDAO.findById(idInmueble);

        if (inquilino == null || inmuebleOpt.isEmpty()) {
            return false;
        }

        ListaDeseos lista = obtenerListaDeseosPorInquilino(loginInquilino);
        Inmueble inmueble = inmuebleOpt.get();

        if (lista.getInmuebles().contains(inmueble)) {
            return false;
        }

        lista.getInmuebles().add(inmueble);
        listaDeseosDAO.save(lista);

        log.info("Inmueble registrado correctamente: {}", inmueble);
        
        return true;
    }

  
    public boolean eliminarInmuebleDeLista(String loginInquilino, Long idInmueble) {
        Inquilino inquilino = inquilinoDAO.findByLogin(loginInquilino);
        Optional<Inmueble> inmuebleOpt = inmuebleDAO.findById(idInmueble);

        if (inquilino == null || inmuebleOpt.isEmpty()) {
            return false;
        }

        ListaDeseos lista = obtenerListaDeseosPorInquilino(loginInquilino);
        Inmueble inmueble = inmuebleOpt.get();

        if (!lista.getInmuebles().contains(inmueble)) {
            return false;
        }

        lista.getInmuebles().remove(inmueble);
        listaDeseosDAO.save(lista);

        log.info("Inmueble eliminado correctamente: {}", inmueble);
        
        return true;
    }

   
    public boolean vaciarListaDeseos(String loginInquilino) {
        Inquilino inquilino = inquilinoDAO.findByLogin(loginInquilino);
        if (inquilino == null || inquilino.getListaDeseos() == null) {
            return false;
        }

        ListaDeseos lista = inquilino.getListaDeseos();
        lista.getInmuebles().clear();
        listaDeseosDAO.save(lista);

        log.info("Lista vaciada correctamente: {}", lista);
        
        return true;
    }
}
