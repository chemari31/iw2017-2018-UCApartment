package es.uca.iw.Ucapartment.Valoracion;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Usuario.Usuario;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "Valoraciones")//Nombre de la tabla en la BD
public class Valoracion {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 512)
	private String descripcion;
	
	private int grado;
	
	private Date fecha;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")//Usuario que realiza la valoracion
	private Usuario usuario;
	
	@ManyToOne
	@JoinColumn(name = "usuario_valorado_id")//Usuario valorado
	private Usuario usuarioValorado;
	
	@ManyToOne
	@JoinColumn(name = "apartamento_valorado_id")//Apartamento valorado
	private Apartamento apartamentoValorado;
	
	@ManyToOne
	@JoinColumn(name = "reserva_id")
	private Reserva reserva;
	
	//Constructor sin parámetros
	protected Valoracion() {}
	
	//Constructor con parametros, valoracion a usuario
	public Valoracion(String descripcion, int grado, Date fecha, Usuario usuario,
		Usuario usuarioValorado) {
		this.descripcion = descripcion;
		this.grado = grado;
		this.fecha = fecha;
		this.usuario = usuario;
		this.usuarioValorado = usuarioValorado;
	}
	
	//Constructor con parametros, valoracion a apartamento
	public Valoracion(String descripcion, int grado, Date fecha, Usuario usuario,
		Apartamento apartamentoValorado) {
		this.descripcion = descripcion;
		this.grado = grado;
		this.fecha = fecha;
		this.usuario = usuario;
		this.apartamentoValorado = apartamentoValorado;
	}
	
	//Constructor con parametros, valoracion a apartamento
		public Valoracion(String descripcion, int grado, Date fecha, Usuario usuario,
			Apartamento apartamentoValorado, Reserva reserva) {
			this.descripcion = descripcion;
			this.grado = grado;
			this.fecha = fecha;
			this.usuario = usuario;
			this.apartamentoValorado = apartamentoValorado;
			this.reserva = reserva;
		}
	
	public Valoracion(String descripcion, int grado, Date fecha, Usuario usuario,
			Usuario usuarioValorado, Apartamento apartamentoValorado) {
			this.descripcion = descripcion;
			this.grado = grado;
			this.fecha = fecha;
			this.usuario = usuario;
			this.usuarioValorado = usuarioValorado;
			this.apartamentoValorado = apartamentoValorado;
		}
	
	public Valoracion(String descripcion, int grado, Date fecha, Usuario usuario,
			Usuario usuarioValorado, Apartamento apartamentoValorado, Reserva reserva) {
			this.descripcion = descripcion;
			this.grado = grado;
			this.fecha = fecha;
			this.usuario = usuario;
			this.usuarioValorado = usuarioValorado;
			this.apartamentoValorado = apartamentoValorado;
			this.reserva = reserva;
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

	public int getGrado() {
		return grado;
	}

	public void setGrado(int grado) {
		this.grado = grado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioValorado() {
		return usuarioValorado;
	}

	public void setUsuarioValorado(Usuario usuarioValorado) {
		this.usuarioValorado = usuarioValorado;
	}

	public Apartamento getApartamentoValorado() {
		return apartamentoValorado;
	}

	public void setApartamentoValorado(Apartamento apartamentoValorado) {
		this.apartamentoValorado = apartamentoValorado;
	}
	
}
