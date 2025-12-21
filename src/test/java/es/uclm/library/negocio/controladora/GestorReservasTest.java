package es.uclm.library.negocio.controladora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Inquilino;
import es.uclm.library.negocio.dominio.PoliticaCancelacion;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.Usuario;
import es.uclm.library.negocio.servicio.LNReservas;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class GestorReservasTest {

    @Mock
    private LNReservas lnReservas;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private GestorReservas gestorReservas;

    private Usuario usuarioPepe;
    private Inmueble inmuebleMock;

    @BeforeEach
    void setUp() {
        usuarioPepe = new Usuario();
        usuarioPepe.setLogin("Pepe");

        inmuebleMock = new Inmueble();
        inmuebleMock.setId(1L);
    }

   
    @Test
    void test_Procesar_PoliticaReembolsable() {
        configurarMocksProcesarExito();
        
        
        Reserva reserva = new Reserva();
        reserva.setFechaInicio(LocalDate.of(2024, 1, 1));
        reserva.setFechaFin(LocalDate.of(2024, 1, 4)); 

        String vista = gestorReservas.procesarReserva(reserva, session, model, null);

        assertEquals(PoliticaCancelacion.REEMBOLSABLE, reserva.getPoliticaCancelacion());
        assertEquals("redirect:/pago", vista);
    }

   
    @Test
    void test_Procesar_Politica50Porciento() {
        configurarMocksProcesarExito();
        
        
        Reserva reserva = new Reserva();
        reserva.setFechaInicio(LocalDate.of(2024, 1, 1));
        reserva.setFechaFin(LocalDate.of(2024, 1, 6)); // 5 días

        gestorReservas.procesarReserva(reserva, session, model, null);

        assertEquals(PoliticaCancelacion.REEMBOLSABLE_50_PER, reserva.getPoliticaCancelacion());
    }

    
    @Test
    void test_Procesar_PoliticaNoReembolsable() {
        configurarMocksProcesarExito();
        
       
        Reserva reserva = new Reserva();
        reserva.setFechaInicio(LocalDate.of(2024, 1, 1));
        reserva.setFechaFin(LocalDate.of(2024, 1, 11)); // 10 días

        gestorReservas.procesarReserva(reserva, session, model, null);

        assertEquals(PoliticaCancelacion.NO_REEMBOLSABLE, reserva.getPoliticaCancelacion());
    }

   
    @Test
    void test_Procesar_ErrorEnServicio() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioPepe);
        when(lnReservas.obtenerInquilinoPorLogin("Pepe")).thenReturn(new Inquilino());
        when(session.getAttribute("inmuebleActual")).thenReturn(inmuebleMock);

        Reserva reserva = new Reserva();
        reserva.setFechaInicio(LocalDate.now());
        reserva.setFechaFin(LocalDate.now().plusDays(1));

        
        when(lnReservas.procesarReserva(any(), any(), any(), any())).thenReturn("error");

        String vista = gestorReservas.procesarReserva(reserva, session, model, null);

        assertEquals("reserva", vista); // Se queda en la vista
        verify(lnReservas).obtenerReservasPorInmueble(inmuebleMock); // Recarga la lista
    }

   
    private void configurarMocksProcesarExito() {
        when(session.getAttribute("usuarioAutenticado")).thenReturn(usuarioPepe);
        when(lnReservas.obtenerInquilinoPorLogin("Pepe")).thenReturn(new Inquilino());
        when(session.getAttribute("inmuebleActual")).thenReturn(inmuebleMock);
       
        when(lnReservas.procesarReserva(any(), any(), any(), any())).thenReturn("ok");
    }
}