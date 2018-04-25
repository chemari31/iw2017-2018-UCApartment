package es.uca.iw.Ucapartment.apartaments;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;



public interface ApartamentsRepository extends JpaRepository<Apartaments, Long> 
{
	public Apartaments findByNombre(String nombre);
	public Apartaments findById(int i);
	public void save(String string);
	public List<Apartaments> findAll();
	public List<Apartaments> findByCiudad(String ciudad);
	public List<Apartaments> findByCiudadAndHabitacion(String ciudad, String habitacion);
	public List<Apartaments> findByCiudadAndHabitacionAndPrecio(String ciudad, String habitacion, String Precio);
	public List<Apartaments> findByCiudadAndPrecio(String ciudad, String precio);
	public List<Apartaments> findByHabitacion(String habitacion);
	public List<Apartaments> findByHabitacionAndPrecio(String habitacion, String Precio);
	public List<Apartaments> findByPrecio(String precio);

}
