package es.uca.iw.Ucapartment.Precio;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import es.uca.iw.Ucapartment.Iva.Iva;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "Precios")//Nombre de la tabla en la BD
public class Precio {

	@Id//Clave Primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 7)
	private double valor;
	
	@OneToOne
	@JoinColumn(name = "iva_id")//Clave foránea
	private Iva iva;
	
	//Contructor sin parámetros
	protected Precio() {}

	//Constructor con parámetros
	public Precio(double valor, Iva iva) {
		this.valor = valor;
		this.iva = iva;
	}

	//Getters & Setters
	public Long getId() {
		return id;
	}
	
	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public Iva getIva() {
		return iva;
	}

	public void setIva(Iva iva) {
		this.iva = iva;
	}

}
