package es.uclm.library.negocio.controladora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

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
import es.uclm.library.negocio.dominio.Pago;
import es.uclm.library.negocio.dominio.Reserva;
import es.uclm.library.negocio.dominio.TipoReserva;
import es.uclm.library.negocio.servicio.LNPagos;
import es.uclm.library.negocio.servicio.LNReservas;
import es.uclm.library.negocio.servicio.LNSolicitud;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class GestorPagosTest {

    @Mock
    private LNPagos lnPagos;

    @Mock
    private LNReservas lnReservas;

    @Mock
    private LNSolicitud solicitudReserva;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private GestorPagos gestorPagos;

    
    private Reserva reservaMock;
    private Inmueble inmuebleMock;
    private Pago pagoInput;

    @BeforeEach
    void setUp() {

        inmuebleMock = new Inmueble();
        
        
        inmuebleMock.setPrecioNoche(100.0);
        
        reservaMock = new Reserva();
        reservaMock.setInmueble(inmuebleMock);
        reservaMock.setInquilino(new Inquilino());

        reservaMock.setFechaInicio(LocalDate.now());
        reservaMock.setFechaFin(LocalDate.now().plusDays(1));

        pagoInput = new Pago();
    }

    @Test
    void testCP1_Mostrar_ReservaNull() {
        when(session.getAttribute("reservaActual")).thenReturn(null);

        String vista = gestorPagos.mostrarFormularioPago(session, model);

        assertEquals("redirect:/reserva", vista);
    }

    
    @Test
    void testCP2_Mostrar_ReservaExiste() {
        when(session.getAttribute("reservaActual")).thenReturn(reservaMock);

        String vista = gestorPagos.mostrarFormularioPago(session, model);

        assertEquals("pago", vista);
        
        
        verify(model).addAttribute("totalAPagar", 100.0);
    }

    
    @Test
    void testCP3_Procesar_ReservaNull() {
        when(session.getAttribute("reservaActual")).thenReturn(null);

        String vista = gestorPagos.procesarPago(pagoInput, session, model);

        assertEquals("redirect:/reserva", vista);
      
        verify(lnPagos, never()).registrarPago(any(), any());
    }

    
    @Test
    void testCP4_Procesar_Confirmacion() {
        
        inmuebleMock.setTipoReserva(TipoReserva.CONFIRMACION);
        reservaMock.setInmueble(inmuebleMock);

        when(session.getAttribute("reservaActual")).thenReturn(reservaMock);

        String vista = gestorPagos.procesarPago(pagoInput, session, model);

        
        verify(solicitudReserva).crearSolicitud(any(), any(), any());
        
        
        verify(lnPagos, never()).registrarPago(any(), any());
        
        
        assertEquals("redirect:/inicio?solicitudEnviada=true", vista);
    }

    @Test
    void testCP5_Procesar_Inmediata() {
        reservaMock.setInmueble(inmuebleMock);

        when(session.getAttribute("reservaActual")).thenReturn(reservaMock);
      
        when(lnPagos.registrarPago(pagoInput, reservaMock)).thenReturn(pagoInput);

        String vista = gestorPagos.procesarPago(pagoInput, session, model);

        
        verify(lnPagos).registrarPago(pagoInput, reservaMock);
        
        
        verify(solicitudReserva, never()).crearSolicitud(any(), any(), any());
        
        
        assertEquals("pago", vista);
    }
}