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
    private Propietario propietario;
    
    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
    private Collection<Reserva> reservas;

    @ManyToMany(mappedBy = "inmuebles")
    private Collection<ListaDeseos> listaDeseos;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
    private Collection<Disponibilidad> disponibilidades;

    @OneToMany(mappedBy = "inmueble", cascade = CascadeType.ALL)
    private Collection<SolicitudReserva> solicitudesReserva;

    private String calle;
    private String numero;
    private String localizacion;
    private double precioNoche;
    private int capacidad;
    private String tipo;

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Propietario getPropietario() { return propietario; }
    public void setPropietario(Propietario propietario) { this.propietario = propietario; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getLocalizacion() { return localizacion; }
    public void setLocalizacion(String localizacion) { this.localizacion = localizacion; }

    public double getPrecioNoche() { return precioNoche; }
    public void setPrecioNoche(double precioNoche) { this.precioNoche = precioNoche; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Collection<Reserva> getReservas() { return reservas; }
    public void setReservas(Collection<Reserva> reservas) { this.reservas = reservas; }

    public Collection<ListaDeseos> getListaDeseos() { return listaDeseos; }
    public void setListaDeseos(Collection<ListaDeseos> listaDeseos) { this.listaDeseos = listaDeseos; }

    public Collection<Disponibilidad> getDisponibilidades() { return disponibilidades; }
    public void setDisponibilidades(Collection<Disponibilidad> disponibilidades) { this.disponibilidades = disponibilidades; }

    public Collection<SolicitudReserva> getSolicitudesReserva() { return solicitudesReserva; }
    public void setSolicitudesReserva(Collection<SolicitudReserva> solicitudesReserva) { this.solicitudesReserva = solicitudesReserva; }

    // --- Constructores ---
    public Inmueble() {
    	super();
    }

    public Inmueble(Long id, Propietario propietario, String calle, String numero, String localizacion,
                    double precioNoche, int capacidad, String tipo) {
        this.id = id;
        this.propietario = propietario;
        this.calle = calle;
        this.numero = numero;
        this.localizacion = localizacion;
        this.precioNoche = precioNoche;
        this.capacidad = capacidad;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "id=" + id +
                ", propietario=" + propietario +
                ", calle='" + calle + '\'' +
                ", numero='" + numero + '\'' +
                ", localizacion='" + localizacion + '\'' +
                ", precioNoche=" + precioNoche +
                ", capacidad=" + capacidad +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
