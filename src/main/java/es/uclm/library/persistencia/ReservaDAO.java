package es.uclm.library.persistencia;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.uclm.library.negocio.dominio.Inmueble;
import es.uclm.library.negocio.dominio.Reserva;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Long> {
	
	@Query("SELECT r FROM Reserva r WHERE r.inmueble = :inmueble AND r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio")
	Collection<Reserva> findReservasSolapadas(
	        @Param("inmueble") Inmueble inmueble,
	        @Param("fechaInicio") LocalDate fechaInicio,
	        @Param("fechaFin") LocalDate fechaFin);
	
	@Query("SELECT r FROM Reserva r WHERE r.inmueble = :inmueble")
	Collection<Reserva> findReservasPorInmueble(@Param("inmueble") Inmueble inmueble);

}