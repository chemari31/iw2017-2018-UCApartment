package es.uca.iw.Ucapartment.Usuario;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "usuarios")//Nombre de la tabla en la BD
public class Usuario {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 32)
	private String Nombre;
	
	@Column(length = 64)
	private String Apellidos;
	
	@Column(unique = true, length = 9)
	private String dni;
	
	@Column(unique = true, length = 32)
	private String email;
	
	@Column(unique = true, length = 32)
	private String nombreUsuario;
	
	@Column(length = 255)
	private String password;
	
	private Rol rol;
	
	//Constructor sin parámetros
	protected Usuario() {}

	//Contructor con parámetros
	public Usuario(String nombre, String apellidos, String dni, String email,
			String nombreUsuario, String password, Rol rol) {
		Nombre = nombre;
		Apellidos = apellidos;
		this.dni = dni;
		this.email = email;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.rol = rol;
	}

	//Getters & Setters
	public Long getId() {
		return id; 
	}
	
	public String getNombre() {
		return Nombre;
	}
	
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	
	public String getApellidos() {
		return Apellidos;
	}
	
	public void setApellidos(String apellidos) {
		Apellidos = apellidos;
	}
	
	public String getDni() {
		return dni;
	}
	
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	
	public void setNombreUsuario(String nombreUsuario) { 
		this.nombreUsuario = nombreUsuario; 
	}
	
	public String getPassword() { 
		return password; 
	}
	
	public void setPassword(String password) { 
		this.password = password; 
	}
	
	public Rol getRol() { 
		return rol;
	}
	
	public void setRol(Rol rol) {
		this.rol = rol;
	}
}
