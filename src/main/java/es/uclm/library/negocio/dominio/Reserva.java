package es.uclm.library.negocio.dominio;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
public class Reserva {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pago_id")
    private Pago pago;

    @OneToOne(mappedBy = "reservaConfirmada")
    private SolicitudReserva solicitud; 

    @ManyToOne
    @JoinColumn(name = "inquilino_id")
    private Inquilino inquilino;

    @ManyToOne
    @JoinColumn(name = "inmueble_id")
    private Inmueble inmueble;

    @Enumerated(EnumType.STRING)
    private PoliticaCancelacion politicaCancelacion;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

	public Reserva() {
		super();
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
	
    public boolean isPagado() {
        return pago != null;
    }

    public boolean isActiva() {
        LocalDate hoy = LocalDate.now();
        return (fechaInicio != null && fechaFin != null &&
                (hoy.isEqual(fechaInicio) || (hoy.isAfter(fechaInicio) && hoy.isBefore(fechaFin))));
    }

	public double getNoches() {
		
		if (fechaInicio != null && fechaFin != null) {
	        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
	    }
		
		return 0;
	}

}