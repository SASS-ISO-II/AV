package es.uclm.library.negocio.dominio;

import java.util.Date;
import jakarta.persistence.*;

@Entity
public class Disponibilidad {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@ManyToOne
    @JoinColumn(name = "inmueble_id")
	Inmueble inmueble;
	
	@Enumerated(EnumType.STRING)
	PoliticaCancelacion politicaCancelación;
	
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	
	@Temporal(TemporalType.DATE)
	private Date fechaFin;
	
	private double precio;
	private boolean directa;
	
	public Disponibilidad() {
		super();
	}
	
	public Disponibilidad(Long id, Inmueble inmueble, PoliticaCancelacion politicaCancelación, Date fechaInicio,
			Date fechaFin, double precio, boolean directa) {
		super();
		this.id = id;
		this.inmueble = inmueble;
		this.politicaCancelación = politicaCancelación;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.precio = precio;
		this.directa = directa;
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
	public PoliticaCancelacion getPoliticaCancelación() {
		return politicaCancelación;
	}
	public void setPoliticaCancelación(PoliticaCancelacion politicaCancelación) {
		this.politicaCancelación = politicaCancelación;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public boolean isDirecta() {
		return directa;
	}
	public void setDirecta(boolean directa) {
		this.directa = directa;
	}
	
	@Override
	public String toString() {
		return "Disponibilidad [id=" + id + ", inmueble=" + inmueble + ", politicaCancelación=" + politicaCancelación
				+ ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + ", precio=" + precio + ", directa="
				+ directa + "]";
	}

}