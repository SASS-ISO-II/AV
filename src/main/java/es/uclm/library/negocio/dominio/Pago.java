package es.uclm.library.negocio.dominio;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Pago {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Enumerated(EnumType.STRING)
	MetodoPago metodo;
	
	@OneToOne(mappedBy = "pago")
	Reserva reserva;
	
	private UUID referencia;
	
	public Pago(Long id, MetodoPago metodo, Reserva reserva, UUID referencia) {
		super();
		this.id = id;
		this.metodo = metodo;
		this.reserva = reserva;
		this.referencia = referencia;
	}
	
	public Pago() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MetodoPago getMetodo() {
		return metodo;
	}

	public void setMetodo(MetodoPago metodo) {
		this.metodo = metodo;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public UUID getReferencia() {
		return referencia;
	}

	public void setReferencia(UUID referencia) {
		this.referencia = referencia;
	}

	@Override
	public String toString() {
		return "Pago [id=" + id + ", metodo=" + metodo + ", reserva=" + reserva + ", referencia=" + referencia + "]";
	}
	
}