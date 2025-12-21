package es.uclm.library.negocio.controladora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.ListaDeseos;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.servicio.LNListaDeseos;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class GestorListaDeseosTest {

    @Mock
    private LNListaDeseos lnListaDeseos;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private GestorListaDeseos gestorListaDeseos;

    private Inquilino inquilinoPepe;
    private Propietario propietarioJuan;
    private ListaDeseos listaDeseosMock;

    @BeforeEach
    void setUp() {
 
        inquilinoPepe = new Inquilino();
        inquilinoPepe.setLogin("Pepe");

        propietarioJuan = new Propietario();
        propietarioJuan.setLogin("Juan");

        
        listaDeseosMock = new ListaDeseos();
        listaDeseosMock.setInmuebles(new ArrayList<Inmueble>());
    }

    

    @Test
    void testMostrar_UsuarioNull() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(null);

        String vista = gestorListaDeseos.mostrarListaDeseos(session, model);

        assertEquals("listaDeseos", vista);
        verify(model).addAttribute(eq("estaLogueado"), eq(false));
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    void testMostrar_UsuarioPropietario() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(propietarioJuan);

        String vista = gestorListaDeseos.mostrarListaDeseos(session, model);

        assertEquals("listaDeseos", vista);
        verify(model).addAttribute(eq("estaLogueado"), eq(false));
        verify(model).addAttribute(eq("error"), anyString());
    }

    @Test
    void testMostrar_UsuarioInquilino() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(inquilinoPepe);
        
        when(lnListaDeseos.obtenerListaDeseosPorInquilino("Pepe")).thenReturn(listaDeseosMock);

        String vista = gestorListaDeseos.mostrarListaDeseos(session, model);

        assertEquals("listaDeseos", vista);
        verify(model).addAttribute("estaLogueado", true);
        verify(model).addAttribute("listaDeseos", listaDeseosMock);
     
        verify(model).addAttribute("inmuebles", listaDeseosMock.getInmuebles());
    }

  

    @Test
    void testAgregar_UsuarioNull() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(null);

        String vista = gestorListaDeseos.agregarInmueble(1L, session, model);

        assertEquals("redirect:/login", vista);
        verify(lnListaDeseos, never()).agregarInmuebleALista(anyString(), anyLong());
    }

    @Test
    void testAgregar_UsuarioPropietario() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(propietarioJuan);

        String vista = gestorListaDeseos.agregarInmueble(1L, session, model);

        assertEquals("redirect:/login", vista);
        verify(lnListaDeseos, never()).agregarInmuebleALista(anyString(), anyLong());
    }

    @Test
    void testAgregar_UsuarioInquilino() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(inquilinoPepe);

        String vista = gestorListaDeseos.agregarInmueble(1L, session, model);

        assertEquals("redirect:/listaDeseos", vista);
        verify(lnListaDeseos).agregarInmuebleALista("Pepe", 1L);
    }
    
    
    @Test
    void testAgregar_UsuarioInquilino_IdNegativo() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(inquilinoPepe);

        String vista = gestorListaDeseos.agregarInmueble(-3L, session, model);

        assertEquals("redirect:/listaDeseos", vista);
        verify(lnListaDeseos).agregarInmuebleALista("Pepe", -3L);
    }


    @Test
    void testEliminar_UsuarioNull() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(null);

        String vista = gestorListaDeseos.eliminarInmueble(1L, session, model);

        assertEquals("redirect:/login", vista);
        verify(lnListaDeseos, never()).eliminarInmuebleDeLista(anyString(), anyLong());
    }

    @Test
    void testEliminar_UsuarioPropietario() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(propietarioJuan);

        String vista = gestorListaDeseos.eliminarInmueble(1L, session, model);

        assertEquals("redirect:/login", vista);
        verify(lnListaDeseos, never()).eliminarInmuebleDeLista(anyString(), anyLong());
    }

    @Test
    void testEliminar_UsuarioInquilino() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(inquilinoPepe);

        String vista = gestorListaDeseos.eliminarInmueble(1L, session, model);

        assertEquals("redirect:/listaDeseos", vista);
        verify(lnListaDeseos).eliminarInmuebleDeLista("Pepe", 1L);
    }
}