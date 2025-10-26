package es.uclm.library.negocio.dominio;

import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Inquilino extends Usuario {

	@OneToMany(mappedBy = "inquilino", cascade = CascadeType.ALL)
	Collection<Reserva> reservas;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "lista_deseos_id")
	ListaDeseos listaDeseos;
	
	public Inquilino() {
		
	}

	public Inquilino(Collection<Reserva> reservas, ListaDeseos listaDeseos) {
		super();
		this.reservas = reservas;
		this.listaDeseos = listaDeseos;
	}
	public Collection<Reserva> getReservas() {
		return reservas;
	}
	public void setReservas(Collection<Reserva> reservas) {
		this.reservas = reservas;
	}
	public ListaDeseos getListaDeseos() {
		return listaDeseos;
	}
	public void setListaDeseos(ListaDeseos listaDeseos) {
		this.listaDeseos = listaDeseos;
	}

	@Override
	public String toString() {
		return "Inquilino [reservas=" + reservas + ", listaDeseos=" + listaDeseos + "]";
	}
	
}