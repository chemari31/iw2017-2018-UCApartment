package es.uca.iw.Ucapartment.Transaccion;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.uca.iw.Ucapartment.Reserva.Reserva;

@Entity
@Table(name = "transacciones")
public class Transaccion {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private double importe;
	
	private Long cuentaOrigen;
	
	private Long cuentaDestino;
	
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "reserva")
	private Reserva reserva;

	
	
	public Transaccion(double importe, Long cuentaOrigen, Long cuentaDestino, Reserva reserva) {
		this.importe = importe;
		this.cuentaOrigen = cuentaOrigen;
		this.cuentaDestino = cuentaDestino;
		this.reserva = reserva;
		this.fecha = java.sql.Date.valueOf(LocalDate.now());
	}
	
	public Transaccion() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public Long getCuentaOrigen() {
		return cuentaOrigen;
	}

	public void setCuentaOrigen(Long cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}

	public Long getCuentaDestino() {
		return cuentaDestino;
	}

	public void setCuentaDestino(Long cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}
	
	public Date getFecha() {
		return this.fecha;
	}
	
	public void setFecha() {
		this.fecha = this.fecha = java.sql.Date.valueOf(LocalDate.now());
	}
	
	
}
