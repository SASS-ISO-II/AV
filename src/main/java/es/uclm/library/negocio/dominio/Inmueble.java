package es.uclm.library.negocio.dominio;

import java.util.*;

public class Inmueble {

	Propietario propietario;
	Collection<Reserva> reservas;
	Collection<ListaDeseos> listaDeseos;
	Collection<Disponibilidad> disponibilidades;
	Collection<SolicitudReserva> solicitudesReserva;
	private String direccion;
	private double precioNoche;

}