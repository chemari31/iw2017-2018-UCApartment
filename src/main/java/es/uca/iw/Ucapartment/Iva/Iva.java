package es.uca.iw.Ucapartment.Iva;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "Ivas")//Nombre de la tabla en la BD
public class Iva {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 32)
	private String pais;
	
	@Column(length = 2)
	private int porcentaje;
	
	//Constructor sin parámetros
	public Iva() {
		this.pais ="";
		this.porcentaje = 0;
	}

	//Constructor con parámetros
	public Iva(String pais, int porcentaje) {
		this.pais = pais;
		this.porcentaje = porcentaje;
	}

	//Getters & Setters
	public Long getId() {
		return id;
	}
	
	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public int getPorcentaje() {
		return porcentaje;
	}

	public void setPorcentaje(int porcentaje) {
		this.porcentaje = porcentaje;
	}

}
