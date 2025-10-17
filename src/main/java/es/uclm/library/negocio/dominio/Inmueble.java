package es.uclm.library.negocio.dominio;

import java.util.*;
import jakarta.persistence.*;

@Entity
public class Inmueble {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
    @JoinColumn(name = "propietario_login")
	Propietario propietario;
	
	@OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
	Collection<Reserva> reservas;
	
	@ManyToMany(mappedBy = "inmuebles")
	Collection<ListaDeseos> listaDeseos;
	
	@OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
	Collection<Disponibilidad> disponibilidades;
	
	@OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
	Collection<SolicitudReserva> solicitudesReserva;
	
	private String direccion;
	private double precioNoche;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Propietario getPropietario() {
		return propietario;
	}
	public void setPropietario(Propietario propietario) {
		this.propietario = propietario;
	}
	public Collection<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(Collection<Reserva> reservas) {
		this.reservas = reservas;
	}
	public Collection<ListaDeseos> getListaDeseos() {
		return listaDeseos;
	}
	public void setListaDeseos(Collection<ListaDeseos> listaDeseos) {
		this.listaDeseos = listaDeseos;
	}
	public Collection<Disponibilidad> getDisponibilidades() {
		return disponibilidades;
	}
	public void setDisponibilidades(Collection<Disponibilidad> disponibilidades) {
		this.disponibilidades = disponibilidades;
	}
	public Collection<SolicitudReserva> getSolicitudesReserva() {
		return solicitudesReserva;
	}
	public void setSolicitudesReserva(Collection<SolicitudReserva> solicitudesReserva) {
		this.solicitudesReserva = solicitudesReserva;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public double getPrecioNoche() {
		return precioNoche;
	}
	public void setPrecioNoche(double precioNoche) {
		this.precioNoche = precioNoche;
	}
	
	public Inmueble(Long id, Propietario propietario, Collection<Reserva> reservas, Collection<ListaDeseos> listaDeseos,
			Collection<Disponibilidad> disponibilidades, Collection<SolicitudReserva> solicitudesReserva,
			String direccion, double precioNoche) {
		super();
		this.id = id;
		this.propietario = propietario;
		this.reservas = reservas;
		this.listaDeseos = listaDeseos;
		this.disponibilidades = disponibilidades;
		this.solicitudesReserva = solicitudesReserva;
		this.direccion = direccion;
		this.precioNoche = precioNoche;
	}

	public Inmueble() {
		
	}
	
	@Override
	public String toString() {
		return "Inmueble [id=" + id + ", propietario=" + propietario + ", reservas=" + reservas + ", listaDeseos="
				+ listaDeseos + ", disponibilidades=" + disponibilidades + ", solicitudesReserva=" + solicitudesReserva
				+ ", direccion=" + direccion + ", precioNoche=" + precioNoche + "]";
	}
	
}