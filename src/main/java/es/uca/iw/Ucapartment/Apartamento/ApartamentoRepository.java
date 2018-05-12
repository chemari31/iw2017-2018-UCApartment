package es.uca.iw.Ucapartment.Apartamento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.Ucapartment.Precio.Precio;
import es.uca.iw.Ucapartment.Usuario.Usuario;

public interface ApartamentoRepository extends JpaRepository<Apartamento, Long> {

	public void save(String string);
	public void delete(Apartamento arg0);
	public Apartamento findByNombre(String nombre);
	public List<Apartamento> findByNombreStartsWithIgnoreCase(String nombre);
	public Apartamento findById(Long long1);
	public List<Apartamento> findByUsuario(Usuario user);
	public List<Apartamento> findAll();
	public List<Apartamento> findByCiudad(String ciudad);
	public List<Apartamento> findByCiudadAndHabitacion(String ciudad, int habitacion);
	public List<Apartamento> findByCiudadAndHabitacionAndCamas(String ciudad, int habitacion, int camas);
	public List<Apartamento> findByCiudadAndCamas(String ciudad, int camas);
	public List<Apartamento> findByHabitacion(int habitacion);
	public List<Apartamento> findByCamas(int camas);
	public List<Apartamento> findByHabitacionAndCamas(int habitacion, int camas);
	//public List<Apartamento> findByPrecio(double precio);
}
