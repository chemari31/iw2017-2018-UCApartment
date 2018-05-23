package es.uca.iw.Ucapartment.Precio;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Iva.Iva;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "precios")//Nombre de la tabla en la BD
public class Precio {

	@Id//Clave Primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	@Column(length = 7)
	private double valor;
	
	private Date fecha_inicio;
	
	private Date fecha_fin;

	@OneToOne
	@JoinColumn(name = "iva_id")//Clave foránea
	private Iva iva;
	
	@ManyToOne
	@JoinColumn(name = "apartamento_id")
	private Apartamento apartamento;
	
	//Contructor sin parámetros
	public Precio() {
		this.valor = 0;
		this.fecha_inicio = null;
		this.fecha_fin = null;
		this.iva = null;
		this.apartamento = null;
	}

	//Constructor con parámetros
	public Precio(double valor, Iva iva) {
		this.valor = valor;
		this.iva = iva;
	}
	
	public Precio(double valor, Apartamento apartamento) {
		this.valor = valor;
		this.apartamento = apartamento;
		this.fecha_inicio = null;
		this.fecha_fin = null;
		this.iva = null;
	}
	
	public Precio(double valor, Date date1, Date date2, Iva iva, Apartamento apartamento) {
		this.valor = valor;
		this.fecha_inicio = date1;
		this.fecha_fin = date2;
		this.iva = iva;
		this.apartamento = apartamento;
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

	public Date getFecha_inicio() {
		return fecha_inicio;
	}

	public void setFecha_inicio(Date fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
	}

	public Date getFecha_fin() {
		return fecha_fin;
	}

	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	
	public Iva getIva() {
		return iva;
	}

	public void setIva(Iva iva) {
		this.iva = iva;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}
}
