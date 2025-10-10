package es.uclm.library.negocio.dominio;

import java.util.*;

public class Inquilino extends Usuario {

	Collection<Reserva> reservas;
	ListaDeseos listaDeseos;

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

}
