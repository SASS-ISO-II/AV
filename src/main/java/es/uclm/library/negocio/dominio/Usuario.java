package es.uclm.library.negocio.dominio;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Usuario {
	
	@Id
	@Column(name = "login", nullable = false, length = 50)
	private String login;
	
	private String pass;
	private String nombre;
	private String apellidos;
	private String direccion;
	private int attribute;
	
	public Usuario() {
		
	}

	public Usuario(String login, String pass, String nombre, String apellidos, String direccion, int attribute) {
		super();
		this.login = login;
		this.pass = pass;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.attribute = attribute;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getAttribute() {
		return attribute;
	}
	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	@Override
	public String toString() {
		return "Usuario [login=" + login + ", pass=" + pass + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", direccion=" + direccion + ", attribute=" + attribute + "]";
	}
	
}