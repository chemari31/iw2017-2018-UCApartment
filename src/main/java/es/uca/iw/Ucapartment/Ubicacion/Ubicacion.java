package es.uca.iw.Ucapartment.Ubicacion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//Indica que Hibernate que es una entidad
@Table(name = "Ubicaciones")//Nombre de la tabla en la BD
public class Ubicacion {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 32)
	private String ciudad;
	
	@Column(length = 128)
	private String calle;
	
	@Column(length = 16)
	private String numero;
	
	@Column(length = 5)
	private String cp;
	
	//Constructor sin parámetros
	protected Ubicacion() {}

	//Constructor con parámetros
	public Ubicacion(String ciudad, String calle, String numero, String cp) {
		this.ciudad = ciudad;
		this.calle = calle;
		this.numero = numero;
		this.cp = cp;
	}
	
	//Getters & Setters
	public Long getId() {
		return id;
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

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}
}
