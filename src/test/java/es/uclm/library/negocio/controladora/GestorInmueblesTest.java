package es.uclm.library.negocio.controladora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNInmuebles;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class GestorInmueblesTest {

    @Mock
    private LNInmuebles lnInmuebles;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private GestorInmuebles gestorInmuebles;

    private Usuario usuarioPepe; 
    private Usuario usuarioJuan; 
    private Propietario propietarioJuan;

    @BeforeEach
    void setUp() {
        usuarioPepe = new Usuario();
        usuarioPepe.setLogin("pepe");

        usuarioJuan = new Usuario();
        usuarioJuan.setLogin("juan");

        propietarioJuan = new Propietario();
        propietarioJuan.setLogin("juan");
    }

    @Test 
    void testMostrarAlta_SinSesion() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(null);

        String view = gestorInmuebles.mostrarFormularioAlta(model, session);

        assertEquals("alta", view);
        verify(model).addAttribute("estaLogueado", false);
        verify(session).setAttribute("altaPendiente", true);
    }

    @Test 
    void testMostrarAlta_NoPropietario() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioPepe);
        when(lnInmuebles.obtenerPropietarioPorLogin("pepe")).thenReturn(null);

        String view = gestorInmuebles.mostrarFormularioAlta(model, session);

        assertEquals("alta", view);
        verify(model).addAttribute("estaLogueado", false);
        verify(model).addAttribute("error", "Solo los propietarios pueden dar de alta inmuebles.");
    }

    @Test 
    void testMostrarAlta_EsPropietario() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioJuan);
        when(lnInmuebles.obtenerPropietarioPorLogin("juan")).thenReturn(propietarioJuan);

        String view = gestorInmuebles.mostrarFormularioAlta(model, session);

        assertEquals("alta", view);
        verify(model).addAttribute("estaLogueado", true);
    }

    @Test 
    void testGuardar_SinSesion() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(null);

        String view = gestorInmuebles.guardarInmueble(new Inmueble(), model, session);

        assertEquals("alta", view);
        verify(model).addAttribute("error", "Debes iniciar sesión como propietario para registrar un inmueble.");
    }

    @Test 
    void testGuardar_NoPropietario() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioPepe);
        when(lnInmuebles.obtenerPropietarioPorLogin("pepe")).thenReturn(null);

        String view = gestorInmuebles.guardarInmueble(new Inmueble(), model, session);

        assertEquals("alta", view);
        verify(model).addAttribute("error", "Solo los propietarios pueden registrar inmuebles.");
    }

    @Test 
    void testGuardar_FalloDatosInvalidos() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioJuan);
        when(lnInmuebles.obtenerPropietarioPorLogin("juan")).thenReturn(propietarioJuan);

        Inmueble inmInvalido = new Inmueble();
        inmInvalido.setPrecioNoche(-50.0); 

        when(lnInmuebles.registrarInmueble(any(Inmueble.class), anyString())).thenReturn(null);

        String view = gestorInmuebles.guardarInmueble(inmInvalido, model, session);

        assertEquals("alta", view);
        
        verify(model).addAttribute("estaLogueado", true);
        verify(model).addAttribute("propietario", propietarioJuan);

        verify(model).addAttribute("error", "Error al guardar: revise los datos del inmueble.");
    }

    @Test 
    void testGuardar_Exito() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioJuan);
        when(lnInmuebles.obtenerPropietarioPorLogin("juan")).thenReturn(propietarioJuan);

        Inmueble inmV = new Inmueble();
        inmV.setPrecioNoche(100.0);
        inmV.setLocalizacion("Madrid");

        when(lnInmuebles.registrarInmueble(any(Inmueble.class), anyString())).thenReturn(inmV);

        String view = gestorInmuebles.guardarInmueble(inmV, model, session);

        assertEquals("alta", view);
        verify(model).addAttribute("popupExito", true);
    }
}