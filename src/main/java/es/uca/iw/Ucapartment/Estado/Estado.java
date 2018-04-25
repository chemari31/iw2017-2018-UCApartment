package es.uca.iw.Ucapartment.Estado;
import es.uca.iw.Ucapartment.Reserva.Reserva;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "Estados")//Nombre de la tabla en la BD
public class Estado {
	
	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	private Date fecha;
	
	private Valor valor;
	
	@ManyToOne//Poner fetch?
	@JoinColumn(name = "reserva_id")//Clave foránea
	private Reserva reserva;
	
	//Constructor sin parámetros
	protected Estado() {}

	//Constructor con parámetros
	public Estado(Date fecha, Valor valor, Reserva reserva) {
		this.fecha = fecha;
		this.valor = valor;
		this.reserva = reserva;
	}

	//Getters & Setters
	public Long getId() {
		return id;
	}
	
	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Valor getValor() {
		return valor;
	}

	public void setValor(Valor valor) {
		this.valor = valor;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
}
