package es.uca.iw.Ucapartment.Apartamento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



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
	
	public Apartamento findById(int id) {
		return repo.findById(id);
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
}
