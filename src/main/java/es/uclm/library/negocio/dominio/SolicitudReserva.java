package es.uclm.library.negocio.dominio;

import jakarta.persistence.*;

@Entity
public class SolicitudReserva {

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

    private boolean confirmada;

    public void confirmarReserva(Reserva reserva) {
        this.reservaConfirmada = reserva;
        this.confirmada = true;
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
}