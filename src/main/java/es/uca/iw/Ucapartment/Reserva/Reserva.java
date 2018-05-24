package es.uca.iw.Ucapartment.Reserva;
import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Usuario.Usuario;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity//Indice a Hibernate que es una entidad
@Table(name = "Reservas")//Nombre de la tabla en la BD
public class Reserva {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)//Valor autogenerado
	private Long id;
	
	private Date fecha;
	
	private Date fechaInicio;
	
	private Date fechaFin;
	
	@Column(length = 7)
	private double precio;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")//Clave foránea
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "apartamento_id")//Clave foránea
	private Apartamento apartamento;
	
	@Column(length = 7)
	private double beneficioEmpresa;
	
	@Column(length = 7)
	private double beneficioAnfitrion;
	
	//Constructor sin parámetros
	protected Reserva() {}

	//Constructor con parámetros
	public Reserva(Date fecha,Date fechaInicio, Date fechaFin, double precio,
		Usuario usuario, Apartamento apartamento) {
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.precio = precio;
		this.usuario = usuario;
		this.apartamento = apartamento;
		this.fecha = fecha;
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

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Apartamento getApartamento() {
		return apartamento;
	}

	public void setApartamento(Apartamento apartamento) {
		this.apartamento = apartamento;
	}
	
	public double getBeneficioEmpresa()
	{
		return beneficioEmpresa;
	}
	
	public void setBenenficioEmpresa(double beneficioEmpresa)
	{
		this.beneficioEmpresa = beneficioEmpresa;
		
	}
	
	public double getBeneficioAnfitrion()
	{
		return beneficioAnfitrion;
	}
	
	public void setBenenficioAnfitrion(double beneficioAnfitrion)
	{
		this.beneficioAnfitrion = beneficioAnfitrion;
		
	}
}
