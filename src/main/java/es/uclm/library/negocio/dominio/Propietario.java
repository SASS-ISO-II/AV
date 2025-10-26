package es.uclm.library.negocio.dominio;

import java.util.*;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Propietario extends Usuario {

	@OneToMany(mappedBy = "propietario")
	Collection<Inmueble> propiedades;
	
	public Propietario() {
		
	}

	public Propietario(Collection<Inmueble> propiedades) {
		super();
		this.propiedades = propiedades;
	}

	public Collection<Inmueble> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Collection<Inmueble> propiedades) {
		this.propiedades = propiedades;
	}

	@Override
	public String toString() {
		return "Propietario [propiedades=" + propiedades + "]";
	}
	
	

}