package es.uclm.library.persistencia;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.uclm.library.negocio.dominio.SolicitudReserva;
import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Propietario;
import es.uclm.library.negocio.dominio.EstadoSolicitud;

@Repository
public interface SolicitudReservaDAO extends JpaRepository<SolicitudReserva, Long> {

    @Query("SELECT s FROM SolicitudReserva s WHERE s.inmueble = :inmueble")
    Collection<SolicitudReserva> findByInmueble(@Param("inmueble") Inmueble inmueble);

    @Query("SELECT s FROM SolicitudReserva s WHERE s.inmueble.propietario = :propietario")
    Collection<SolicitudReserva> findByPropietario(@Param("propietario") Propietario propietario);

    @Query("SELECT s FROM SolicitudReserva s WHERE s.estado = :estado AND s.inmueble.propietario = :propietario")
    Collection<SolicitudReserva> findPendientesByPropietario(
            @Param("estado") EstadoSolicitud estado,
            @Param("propietario") Propietario propietario
    );
    
    @Query("SELECT s FROM SolicitudReserva s WHERE s.estado = :estado AND s.inmueble.propietario = :propietario")
    Collection<SolicitudReserva> findByPropietarioAndEstado(
            @Param("propietario") Propietario propietario,
            @Param("estado") EstadoSolicitud estado
    );


}
