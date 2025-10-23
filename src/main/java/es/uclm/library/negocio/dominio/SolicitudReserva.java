package es.uclm.library.negocio.dominio;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class SolicitudReserva extends Reserva {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//	
//	@ManyToOne
//	Inmueble inmueble;
	
	@OneToOne
	@JsonIgnore
	Reserva reservaConfirmada;
	
	private boolean confirmada;

	public void confirmarReserva() {
		// TODO - implement SolicitudReserva.confirmarReserva
		throw new UnsupportedOperationException();
	}

	public SolicitudReserva(Long id, Inmueble inmueble, Reserva reservaConfirmada, boolean confirmada) {
		super();
		this.id = id;
		this.inmueble = inmueble;
		this.reservaConfirmada = reservaConfirmada;
		this.confirmada = confirmada;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

//	@Override
//	public String toString() {
//		return "SolicitudReserva [id=" + id + ", inmueble=" + inmueble + ", reservaConfirmada=" + reservaConfirmada
//				+ ", confirmada=" + confirmada + "]";
//	}

}