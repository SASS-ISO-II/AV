package es.uclm.library.negocio.controladora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNInmuebles;
import es.uclm.library.negocio.servicio.LNSolicitud;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class GestorInicioTest {

    @Mock
    private LNInmuebles lninmuebles;

    @Mock
    private LNSolicitud lnSolicitud;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private GestorInicio gestorInicio;

   
 
    private Usuario usuarioJuan;
    private Usuario usuarioAna;
    private Usuario usuarioLuis;

    @BeforeEach
    void setUp() {
       
 
        
        usuarioJuan = new Usuario();
        usuarioJuan.setLogin("Juan");

        usuarioAna = new Usuario();
        usuarioAna.setLogin("Ana");

        usuarioLuis = new Usuario();
        usuarioLuis.setLogin("Luis");
    }

    
    @Test
    void testCP1_UsuarioNoLogueado() {
        
        when(session.getAttribute("usuarioAutenticado")).thenReturn(null);

      
        String vista = gestorInicio.mostrarInicio(session, model);

       
        verify(model).addAttribute("estaLogueado", false);
        assertEquals("inicio", vista);
    }

   
    @Test
    void testCP2_UsuarioEsPropietario() {
        
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioJuan);
        
      
        Propietario mockPropietario = new Propietario();
        when(lninmuebles.obtenerPropietarioPorLogin("Juan")).thenReturn(mockPropietario);
        
        
        when(lnSolicitud.contarSolicitudesPendientes(mockPropietario)).thenReturn(0);

   
        gestorInicio.mostrarInicio(session, model);

    
        verify(model).addAttribute("estaLogueado", true);
        verify(model).addAttribute("esPropietario", true);
        
        
        verify(model, never()).addAttribute(eq("esInquilino"), any());
    }

    
    @Test
    void testCP3_UsuarioEsInquilino() {
        
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioAna);
        
      
        when(lninmuebles.obtenerPropietarioPorLogin("Ana")).thenReturn(null);
        
        
        when(lnSolicitud.obtenerInquilinoPorLogin("Ana")).thenReturn(new Inquilino());

        
        gestorInicio.mostrarInicio(session, model);

        
        verify(model).addAttribute("estaLogueado", true);
        verify(model).addAttribute("esInquilino", true);
        
      
        verify(model, never()).addAttribute(eq("esPropietario"), eq(true));
    }

   
    @Test
    void testCP4_UsuarioSinRol() {
        
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioLuis);
        
       
        when(lninmuebles.obtenerPropietarioPorLogin("Luis")).thenReturn(null);
        when(lnSolicitud.obtenerInquilinoPorLogin("Luis")).thenReturn(null);

        gestorInicio.mostrarInicio(session, model);

        
        verify(model).addAttribute("estaLogueado", true);
        verify(model).addAttribute("esPropietario", false);
        verify(model).addAttribute("esInquilino", false);
    }
}
