package es.uclm.library.negocio.dominio;

import java.util.*;

public class Propietario extends Usuario {

	Collection<Inmueble> propiedades;

	public Collection<Inmueble> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Collection<Inmueble> propiedades) {
		this.propiedades = propiedades;
	}

}