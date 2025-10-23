package es.uclm.library.negocio.dominio;

import jakarta.persistence.*;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Reserva {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pago_id")
	Pago pago;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JsonIgnore
    @JoinColumn(name = "solicitud_id")
	SolicitudReserva solicitud;
	
	@ManyToOne
    @JoinColumn(name = "inquilino_id")
	Inquilino inquilino;
	
	@ManyToOne
    @JoinColumn(name = "inmueble_id")
	Inmueble inmueble;
	
	@Enumerated(EnumType.STRING)
	PoliticaCancelacion politicaCancelacion;
	
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	
	public Reserva() {
		
	}

	public Reserva(Long id, Pago pago, SolicitudReserva solicitud, Inquilino inquilino, Inmueble inmueble,
			PoliticaCancelacion politicaCancelacion, LocalDate fechaInicio, LocalDate fechaFin) {
		super();
		this.id = id;
		this.pago = pago;
		this.solicitud = solicitud;
		this.inquilino = inquilino;
		this.inmueble = inmueble;
		this.politicaCancelacion = politicaCancelacion;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
	}

	public void isPagado() {
		// TODO - implement Reserva.isPagado
		throw new UnsupportedOperationException();
	}

	public void isActiva() {
		// TODO - implement Reserva.isActiva
		throw new UnsupportedOperationException();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	public SolicitudReserva getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(SolicitudReserva solicitud) {
		this.solicitud = solicitud;
	}

	public Inquilino getInquilino() {
		return inquilino;
	}

	public void setInquilino(Inquilino inquilino) {
		this.inquilino = inquilino;
	}

	public Inmueble getInmueble() {
		return inmueble;
	}

	public void setInmueble(Inmueble inmueble) {
		this.inmueble = inmueble;
	}

	public PoliticaCancelacion getPoliticaCancelacion() {
		return politicaCancelacion;
	}

	public void setPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) {
		this.politicaCancelacion = politicaCancelacion;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

//	@Override
//	public String toString() {
//		return "Reserva [id=" + id + ", pago=" + pago + ", solicitud=" + solicitud + ", inquilino=" + inquilino
//				+ ", inmueble=" + inmueble + ", politicaCancelacion=" + politicaCancelacion + ", fechaInicio="
//				+ fechaInicio + ", fechaFin=" + fechaFin + "]";
//	}

}