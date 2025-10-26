package es.uclm.library.negocio.dominio;

import java.util.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class ListaDeseos {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@OneToOne(mappedBy = "listaDeseos")
	Inquilino usuario;
	
	@ManyToMany
	Collection<Inmueble> inmuebles;
	
	public ListaDeseos() {
		
	}
	
	public ListaDeseos(Inquilino usuario, Collection<Inmueble> inmuebles) {
		super();
		this.usuario = usuario;
		this.inmuebles = inmuebles;
	}

	@Override
	public String toString() {
		return "ListaDeseos [usuario=" + usuario + ", inmuebles=" + inmuebles + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Inquilino getUsuario() {
		return usuario;
	}

	public void setUsuario(Inquilino usuario) {
		this.usuario = usuario;
	}

	public Collection<Inmueble> getInmuebles() {
		return inmuebles;
	}

	public void setInmuebles(Collection<Inmueble> inmuebles) {
		this.inmuebles = inmuebles;
	}
	
}