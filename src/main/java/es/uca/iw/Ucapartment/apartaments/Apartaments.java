package es.uca.iw.Ucapartment.apartaments;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Apartaments
{
	
	private static final long serialVersionUID = -8883789651072229337L;
	
	@Id
	@GeneratedValue
	private int id;
	
	private String nombre;
	
	private String ciudad;
	
	private String precio;
	
	private String habitacion;
	
	private String image;
	
	
	public Apartaments() {}
	
	public Apartaments(String nombre)
	{
		this.nombre = nombre;
	}
	public Apartaments(String nombre, String ciudad, String precio, String image, String habitacion)
	{
		this.ciudad = ciudad;
		this.nombre = nombre;
		this.precio = precio;
		this.image = image;
		this.habitacion = habitacion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(String habitacion) {
		this.habitacion = habitacion;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	

}