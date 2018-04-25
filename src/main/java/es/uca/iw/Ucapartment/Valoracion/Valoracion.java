package es.uca.iw.Ucapartment.Valoracion;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity//Indica a Hibernate que es una entidad
@Table(name = "Valoraciones")//Nombre de la tabla en la BD
public class Valoracion {

	@Id//Clave primaria
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(length = 512)
	private String descripcion;
	
	private Date fecha;
	
	//Constructor sin parámetros
	protected Valoracion() {}
	
	
}
