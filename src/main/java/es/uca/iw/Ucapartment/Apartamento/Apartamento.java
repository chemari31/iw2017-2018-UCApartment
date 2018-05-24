package es.uca.iw.Ucapartment.Apartamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.uca.iw.Ucapartment.Usuario.Usuario;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "apartamentos")//Nombre de la tabla en la BD
public class Apartamento {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 128)//La longitud es provisional
	private String nombre;
	
	@Column(length = 256)//La longitud es provisional
	private String descripcion;
	
	@Column(length = 13)
	private String contacto;
	
	@Column(length = 32)
	private String ciudad;
	
	@Column(length = 128)
	private String calle;
	
	@Column(length = 16)
	private Integer numero;
	
	@Column(length = 5)
	private Integer cp;
	
	@ManyToOne//Poner fetch?
	@JoinColumn(name = "usuario_id")//Clave foránea
	private Usuario usuario;
	
	@Column(length = 2)
	private int habitacion;
	
	@Column(length = 2)
	private int camas;
	
	private boolean ac;
	
	@Column(length = 10)
	private double precio;
	
	@Column(length = 256)//La longitud es provisional
	private String Foto1; //falta añadirlo al constructor
	
	@Column(length = 256)//La longitud es provisional
	private String Foto2; //falta añadirlo al constructor
	

	@Column(length = 256)//La longitud es provisional
	private String Foto3; //falta añadirlo al constructor

	//Constructor sin parámetros
	protected Apartamento() {}

	//Constructor con parámetros
	public Apartamento(String Nombre, String descripcion, String contacto, String ciudad, String calle,
		Integer numero, Integer cp, Usuario usuario, int habitaciones, int camas, boolean ac, double precio) {
		this.nombre = Nombre;
		this.descripcion = descripcion;
		this.contacto = contacto;
		this.ciudad = ciudad;
		this.calle = calle;
		this.numero = numero;
		this.cp = cp;
		this.usuario = usuario;
		this.habitacion = habitaciones;
		this.camas = camas;
		this.ac = ac;
		this.precio = precio;
	}
	
	//Constructor sin usuario
	public Apartamento(String Nombre, String descripcion, String contacto, String ciudad, String calle,
			Integer numero, int cp, int habitaciones, int camas, boolean ac, double precio) {
			this.nombre = Nombre;
			this.descripcion = descripcion;
			this.contacto = contacto;
			this.ciudad = ciudad;
			this.calle = calle;
			this.numero = numero;
			this.cp = cp;
			this.habitacion = habitaciones;
			this.camas = camas;
			this.ac = ac;
			this.precio = precio;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Integer getCp() {
		return cp;
	}

	public void setCp(int cp) {
		this.cp = cp;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getHabitaciones() {
		return habitacion;
	}

	public void setHabitaciones(int habitaciones) {
		this.habitacion = habitaciones;
	}

	public int getCamas() {
		return camas;
	}

	public void setCamas(int camas) {
		this.camas = camas;
	}

	public boolean isAc() {
		return ac;
	}

	public void setAc(boolean ac) {
		this.ac = ac;
	}
	
	public double getPrecio()
	{
		return precio;
	}
	
	public void setPrecio(double precio)
	{
		this.precio = precio;
	}
	
	public void setFoto1(String cadena) {
		this.Foto1 = cadena;
	}
	
	public String getFoto1() {
		return Foto1;
	}
	
	public String getFoto2() {
		return Foto2;
	}

	public void setFoto2(String cadena) {
		this.Foto2 = cadena;
	}
	
	public String getFoto3() {
		return Foto3;
	}

	public void setFoto3(String cadena) {
		this.Foto3 = cadena;
	}


}
