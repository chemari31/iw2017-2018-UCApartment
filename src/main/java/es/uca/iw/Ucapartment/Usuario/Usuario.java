package es.uca.iw.Ucapartment.Usuario;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "usuarios")//Nombre de la tabla en la BD
public class Usuario implements UserDetails{

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 32)
	private String nombre;
	
	@Column(length = 64)
	private String apellidos;
	
	@Column(unique = true, length = 9)
	private String dni;
	
	@Column(unique = true, length = 32)
	private String email;
	
	@Column(unique = true, length = 32)
	private String nombreUsuario;
	
	@Column(length = 255)
	private String password;
	
	@Column
	private boolean cuentaNoBloqueada;
	
	private Rol rol;
	
	@Column(length = 256)//La longitud es provisional
	private String Foto1; //falta añadirlo al constructor
	
	// Constructor predeterminado
	protected Usuario() {}

	// Contructor con parámetros
	public Usuario(String nombre, String apellidos, String dni, String email,
			String nombreUsuario, String password) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.email = email;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.rol = Rol.ANFITRION;
		this.cuentaNoBloqueada = true;
		this.Foto1 = "/perfiluser/null.png";
	}
	
	public Usuario(String nombre, String apellidos, String dni) {
		this(nombre, apellidos, dni, new String(nombre+"@"+apellidos+".es"), nombre, "default");
	}

	//Getters & Setters
	public Long getId() {
		return id; 
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
	
	public void setFoto1(String cadena) {
		this.Foto1 = cadena;
	}
	
	public String getFoto1() {
		return Foto1;
	}
	
	public void setDesbloqueo(boolean valor) {
		this.cuentaNoBloqueada = valor;
	}
	
	@Override
	public String toString() {
		return String.format("Usuario[id=%d, Nombre='%s', Apellidos='%s', DNI='%s', Correo electrónico"
				+ "='%s', Nombre de usuario='%s', Contraseña='%s']", id, nombre, apellidos, dni, 
				email,nombreUsuario,password);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> list=new ArrayList<GrantedAuthority>();
		//list.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
		if (this.rol.getId() == 0) 
			list.add(new SimpleGrantedAuthority("ADMINISTRADOR"));
		else if (this.rol.getId() == 1)
			list.add(new SimpleGrantedAuthority("GERENTE"));
		else if (this.rol.getId() == 2)
			list.add(new SimpleGrantedAuthority("ANFITRION"));
		
		return list;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return cuentaNoBloqueada;
		//return true; 
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return nombreUsuario;
	}
}
