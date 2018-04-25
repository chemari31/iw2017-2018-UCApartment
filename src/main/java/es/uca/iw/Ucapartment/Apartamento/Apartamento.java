package es.uca.iw.Ucapartment.Apartamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.uca.iw.Ucapartment.Precio.Precio;
import es.uca.iw.Ucapartment.Ubicacion.Ubicacion;
import es.uca.iw.Ucapartment.Usuario.Usuario;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "apartamentos")//Nombre de la tabla en la BD
public class Apartamento {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 256)//La longitud es provisional
	private String descripcion;
	
	@Column(length = 13)
	private String contacto;
	
	@ManyToOne//Poner fetch?
	@JoinColumn(name = "usuario_id")//Clave foránea
	private Usuario usuario;
	
	@OneToOne
	@JoinColumn(name = "ubicacion_id")//Clave foránea
	private Ubicacion ubicacion;
	
	@OneToOne
	@JoinColumn(name = "Precio_id")//Clave foránea
	private Precio precio;
	
	//Constructor sin parámetros
	protected Apartamento() {}

	//Constructor con parámetros
	public Apartamento(String descripcion, String contacto, Usuario usuario,
		Ubicacion ubicacion, Precio precio) {
		this.descripcion = descripcion;
		this.contacto = contacto;
		this.usuario = usuario;
		this.ubicacion = ubicacion;
		this.precio = precio;
	}

	//Getters & Setters
	public Long getId() {
		return id;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public Precio getPrecio() {
		return precio;
	}

	public void setPrecio(Precio precio) {
		this.precio = precio;
	}
}
