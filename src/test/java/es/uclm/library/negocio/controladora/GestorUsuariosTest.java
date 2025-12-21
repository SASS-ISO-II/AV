package es.uclm.library.negocio.controladora;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import es.uclm.library.persistencia.InquilinoDAO;
import es.uclm.library.persistencia.PropietarioDAO;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
public class GestorUsuariosTest {

    // Mocks necesarios para el método de registro
    @Mock
    private PropietarioDAO propietarioDAO;

    @Mock
    private InquilinoDAO inquilinoDAO;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private GestorUsuarios gestorUsuarios;

    // Objetos auxiliares
    private Usuario usuarioJuan;
    private Usuario usuarioPepe;
    private Usuario usuarioInvalido;

    @BeforeEach
    void setUp() {
        
        usuarioJuan = new Usuario();
        usuarioJuan.setLogin("juan@gmail.com"); 
        usuarioJuan.setPass("1234");
        usuarioJuan.setNombre("Juan");

        
        usuarioPepe = new Usuario();
        usuarioPepe.setLogin("pepe@gmail.com"); 
        usuarioPepe.setPass("1234");
        usuarioPepe.setNombre("Pepe");

        
        usuarioInvalido = new Usuario();
        usuarioInvalido.setLogin("hola"); 
        usuarioInvalido.setPass("1234");
    }

   
    @Test
    void testCP1_Registro_LoginInvalido() {
      
        String vista = gestorUsuarios.loginUsuario("propietario", usuarioInvalido, model, session);

        assertEquals("registro", vista);
        
   
        verify(model).addAttribute(eq("error"), eq("El login debe ser un correo electrónico válido."));
        
        
        verify(propietarioDAO, never()).save(any());
        verify(inquilinoDAO, never()).save(any());
    }

   
    @Test
    void testCP2_Registro_Juan_Propietario() {
        
        when(propietarioDAO.save(any(Propietario.class))).thenReturn(new Propietario());

      
        String vista = gestorUsuarios.loginUsuario("propietario", usuarioJuan, model, session);

        
        assertEquals("propietario", vista);
        
        
        verify(propietarioDAO).save(any(Propietario.class));
        
       
        verify(inquilinoDAO, never()).save(any());
    }

    
    @Test
    void testCP3_Registro_Pepe_Inquilino() {
       
        when(inquilinoDAO.save(any(Inquilino.class))).thenReturn(new Inquilino());

        
        String vista = gestorUsuarios.loginUsuario("inquilino", usuarioPepe, model, session);

       
        assertEquals("usuario", vista);
        
    
        verify(inquilinoDAO).save(any(Inquilino.class));
        
       
        verify(propietarioDAO, never()).save(any());
    }
}