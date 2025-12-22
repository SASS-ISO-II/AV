package es.uclm.library.negocio.servicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.persistencia.InmuebleDAO;

@ExtendWith(MockitoExtension.class)
public class LNBusquedaTest {

    @Mock
    private InmuebleDAO inmuebleDAO; // Mockeamos el DAO para aislar la lógica

    @InjectMocks
    private LNBusqueda lnBusqueda; // La clase que estamos probando

    private Inmueble inmuebleA;
    private Inmueble inmuebleB;

    @BeforeEach
    void setUp() {
        
        inmuebleA = new Inmueble();
        inmuebleA.setLocalizacion("Madrid Centro");
        inmuebleA.setPrecioNoche(100.0);
        inmuebleA.setCapacidad(4);
        inmuebleA.setTipo("Apartamento Completo");

       
        inmuebleB = new Inmueble();
        inmuebleB.setLocalizacion("Cuenca");
        inmuebleB.setPrecioNoche(50.0);
        inmuebleB.setCapacidad(2);
        inmuebleB.setTipo("Casa");

        
        when(inmuebleDAO.findAll()).thenReturn(Arrays.asList(inmuebleA, inmuebleB));
    }

    
    @Test
    void testCP1_BuscarTodosNulos() {
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, null, null, "todos");
        
        assertEquals(2, resultado.size(), "Debe devolver los 2 inmuebles");
    }

    
    @Test
    void testCP2_BuscarLocalizacionMadrid() {
        Collection<Inmueble> resultado = lnBusqueda.buscar("Madrid", null, null, "todos");
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(inmuebleA), "Debe contener el inmueble de Madrid");
    }

   
    @Test
    void testCP3_BuscarLocalizacionTokio() {
        Collection<Inmueble> resultado = lnBusqueda.buscar("Tokio", null, null, "todos");
        
        assertEquals(0, resultado.size(), "Debe devolver lista vacía");
    }

    
    @Test
    void testCP4_BuscarPrecioNegativo() {
        
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, -1.0, null, "todos");
        
        assertEquals(2, resultado.size(), "Debe ignorar el filtro negativo y devolver todo");
    }

   
    @Test
    void testCP5_BuscarPrecioLimite100() {
        
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, 100.0, null, "todos");
        
        assertEquals(2, resultado.size(), "Debe incluir 100 (límite) y 50 (inferior)");
    }

    
    @Test
    void testCP6_BuscarPrecioBajo49() {
        
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, 49.0, null, "todos");
        
        assertEquals(0, resultado.size());
    }

  
    @Test
    void testCP7_BuscarCapacidadCero() {
       
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, null, 0, "todos");
        
        assertEquals(2, resultado.size(), "Debe ignorar capacidad 0 y devolver todo");
    }

   
    @Test
    void testCP8_BuscarCapacidadLimite4() {
        
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, null, 4, "todos");
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(inmuebleA));
    }

  
    @Test
    void testCP9_BuscarTipoExacto() {
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, null, null, "Apartamento Completo");
        
        assertEquals(1, resultado.size());
        assertTrue(resultado.contains(inmuebleA));
    }

   
    @Test
    void testCP10_BuscarTipoInexistente() {
        Collection<Inmueble> resultado = lnBusqueda.buscar(null, null, null, "Iglu");
        
        assertEquals(0, resultado.size());
    }
}