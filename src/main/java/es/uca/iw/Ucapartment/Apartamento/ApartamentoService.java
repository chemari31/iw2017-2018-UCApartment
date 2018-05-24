package es.uca.iw.Ucapartment.Apartamento;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoRepository;
import es.uca.iw.Ucapartment.Usuario.Usuario;


@Service
public class ApartamentoService {

	@Autowired
	private ApartamentoRepository repo;


	public Apartamento loadApartamentoByApartamentoname(String nombre)  {
		return repo.findByNombre(nombre);
	}
	
	public Apartamento save(Apartamento apartamento) {
		return repo.save(apartamento);
	}
	
	public Apartamento findByNombre(String nombre) {
		return repo.findByNombre(nombre);
	}
	
	public List<Apartamento> findByNombreStartsWithIgnoreCase(String nombre) {
		return repo.findByNombreStartsWithIgnoreCase(nombre);
	}
	
	public Apartamento findById(Long long1) {
		return repo.findById(long1);
	}
	
	public List<Apartamento> findByUsuario(Usuario user){
		return repo.findByUsuario(user);
	}
	
	public List<Apartamento> findAll() {
		return repo.findAll();
	}
	
	public List<Apartamento> findByCiudad(String ciudad) {
		return repo.findByCiudad(ciudad);
	}
	
	public List<Apartamento> findByCiudadAndHabitacion(String ciudad, int habitacion) {
		return repo.findByCiudadAndHabitacion(ciudad, habitacion);
	}
	
	/*public List<Apartamento> findByHabitacion(String ciudad, int habitacion, double precio){
		
	}*/
	
	public void delete(Apartamento arg0) {
		repo.delete(arg0);
	}

	public List<Apartamento> findByHabitacion(String filtro) {
		return repo.findByHabitacion(Integer.parseInt(filtro));
	}
}
