package es.uca.iw.Ucapartment.Apartamento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.Ucapartment.Precio.Precio;

public interface ApartamentoRepository extends JpaRepository<Apartamento, Long> {

	public void save(String string);
	public void delete(Apartamento arg0);
	public Apartamento findByNombre(String nombre);
	public Apartamento findByNombreStartsWithIgnoreCase(String nombre);
	public Apartamento findById(Long long1);
	public List<Apartamento> findAll();
	public List<Apartamento> findByCiudad(String ciudad);
	public List<Apartamento> findByCiudadAndHabitacion(String ciudad, int habitacion);
	//public List<Apartamento> findByCiudadAndHabitacionAndPrecio(String ciudad, int habitacion, double Precio);
	//public List<Apartamento> findByCiudadAndPrecio(String ciudad, double precio);
	public List<Apartamento> findByHabitacion(int habitacion);
	//public List<Apartamento> findByHabitacionAndPrecio(int habitacion, double Precio);
	//public List<Apartamento> findByPrecio(double precio);
}
