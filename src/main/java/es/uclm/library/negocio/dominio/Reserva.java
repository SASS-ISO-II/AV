package es.uclm.library.negocio.dominio;

import java.util.Date;

public class Reserva {

	Pago pago;
	SolicitudReserva solicitud;
	Inquilino inquilino;
	Inmueble inmueble;
	PoliticaCancelacion politicaCancelacion;
	private Date fechaInicio;
	private Date fechaFin;

	public void isPagado() {
		// TODO - implement Reserva.isPagado
		throw new UnsupportedOperationException();
	}

	public void isActiva() {
		// TODO - implement Reserva.isActiva
		throw new UnsupportedOperationException();
	}

}