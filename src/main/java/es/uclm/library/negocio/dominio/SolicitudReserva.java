package es.uclm.library.negocio.dominio;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
public class SolicitudReserva {
	
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

	private LocalDate fechaInicio;
	private LocalDate fechaFin;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inquilino_id")
    private Inquilino inquilino;

    @ManyToOne
    @JoinColumn(name = "inmueble_id")
    private Inmueble inmueble;

    @OneToOne
    @JoinColumn(name = "reserva_confirmada_id")
    private Reserva reservaConfirmada;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado = EstadoSolicitud.PENDIENTE;

    private boolean confirmada;
    
    public EstadoSolicitud getEstado() {
		return estado;
	}

	public void setEstado(EstadoSolicitud estado) {
		this.estado = estado;
	}

	public void aceptar(Reserva reserva) {
        this.estado = EstadoSolicitud.ACEPTADA;
        this.reservaConfirmada = reserva;
    }

    public void rechazar() {
        this.estado = EstadoSolicitud.RECHAZADA;
    }
    
    public void confirmarReserva(Reserva reserva) {
        this.reservaConfirmada = reserva;
        this.confirmada = true;
    }

	public SolicitudReserva() {
		super();
	}
	
	public SolicitudReserva(Long id, Inmueble inmueble, EstadoSolicitud estado, Inquilino inquilino, Reserva reservaConfirmada) {
		super();
		this.id = id;
		this.inmueble = inmueble;
		this.estado = estado;
		this.inquilino = inquilino;
		this.reservaConfirmada = reservaConfirmada;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Reserva getReservaConfirmada() {
		return reservaConfirmada;
	}

	public void setReservaConfirmada(Reserva reservaConfirmada) {
		this.reservaConfirmada = reservaConfirmada;
	}

	public boolean isConfirmada() {
		return confirmada;
	}

	public void setConfirmada(boolean confirmada) {
		this.confirmada = confirmada;
	}
}