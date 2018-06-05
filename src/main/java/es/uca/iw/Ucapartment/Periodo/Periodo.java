package es.uca.iw.Ucapartment.Periodo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "periodos_nodisp")//Nombre de la tabla en la BD
public class Periodo {
	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	private Date fechaInicio;
	
	private Date fechaFin;
	
	@ManyToOne
	@JoinColumn(name = "apartamento_id")//Clave foránea
	private Apartamento apartamento;
	
	//Constructor sin parámetros
	protected Periodo() {}
	
	public Periodo(Date fechaInicio, Date fechaFin, Apartamento apartamento) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.apartamento = apartamento;
	}
	
	// Getters & Setters
	
	public Date getFechaInicio() {
		return this.fechaInicio;
	}
	
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaFin() {
		return this.fechaFin;
	}
	
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
}
