package es.uclm.library.negocio.controladora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import es.uclm.library.negocio.dominio.EstadoSolicitud;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNSolicitud;
import es.uclm.library.persistencia.PropietarioDAO;
import es.uclm.library.persistencia.SolicitudReservaDAO;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class GestorNotificacionesTest {

    @Mock
    private LNSolicitud lnSolicitud;

    @Mock
    private PropietarioDAO propietarioDAO;

    @Mock
    private SolicitudReservaDAO solicitudReservaDAO;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private GestorNotificaciones gestorNotificaciones;

   
    private Usuario usuarioPepe;
    private Propietario propietarioPepe;

    @BeforeEach
    void setUp() {
       
        usuarioPepe = new Usuario();
        usuarioPepe.setLogin("Pepe");

        propietarioPepe = new Propietario();
    }

   
    @Test
    void testCP1_Listar_SessionNull() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(null);

        String vista = gestorNotificaciones.listarSolicitudes(session, model, "TODAS");

        assertEquals("redirect:/login", vista);
      
        verify(solicitudReservaDAO, never()).findByPropietario(any());
    }

   
    @Test
    void testCP2_Listar_Pepe_Todas() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioPepe);
        when(propietarioDAO.findByLogin("Pepe")).thenReturn(propietarioPepe);
        
        
        when(solicitudReservaDAO.findByPropietario(propietarioPepe)).thenReturn(new ArrayList<>());

        String vista = gestorNotificaciones.listarSolicitudes(session, model, "TODAS");

        assertEquals("solicitudes", vista);
        
        verify(solicitudReservaDAO).findByPropietario(propietarioPepe);
    }

    
    @Test
    void testCP3_Listar_Pepe_Pendiente() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioPepe);
        when(propietarioDAO.findByLogin("Pepe")).thenReturn(propietarioPepe);
        when(solicitudReservaDAO.findByPropietarioAndEstado(eq(propietarioPepe), any())).thenReturn(new ArrayList<>());

        String vista = gestorNotificaciones.listarSolicitudes(session, model, "PENDIENTE");

        assertEquals("solicitudes", vista);
     
        verify(solicitudReservaDAO).findByPropietarioAndEstado(propietarioPepe, EstadoSolicitud.PENDIENTE);
    }

   
    @Test
    void testCP4_Listar_FiltroInventado() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioPepe);
        when(propietarioDAO.findByLogin("Pepe")).thenReturn(propietarioPepe);

       
        assertThrows(IllegalArgumentException.class, () -> {
            gestorNotificaciones.listarSolicitudes(session, model, "INVENTADO");
        });
    }

   
    @Test
    void testCP5_Procesar_Aceptar() {
        String vista = gestorNotificaciones.procesarSolicitud(1L, "ACEPTAR");

        assertEquals("redirect:/solicitudes", vista);
     
        verify(lnSolicitud).aceptarSolicitud(1L);
    }

    
    @Test
    void testCP6_Procesar_Rechazar() {
        String vista = gestorNotificaciones.procesarSolicitud(1L, "RECHAZAR");

        assertEquals("redirect:/solicitudes", vista);
      
        verify(lnSolicitud).rechazarSolicitud(1L);
    }

    
    @Test
    void testCP7_Procesar_Inventado() {
        String vista = gestorNotificaciones.procesarSolicitud(1L, "INVENTADO");

        assertEquals("redirect:/solicitudes", vista);
        
        verify(lnSolicitud, never()).aceptarSolicitud(any());
        verify(lnSolicitud, never()).rechazarSolicitud(any());
    }
}