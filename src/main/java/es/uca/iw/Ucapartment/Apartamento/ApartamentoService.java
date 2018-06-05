package es.uca.iw.Ucapartment.Apartamento;

import java.sql.Date;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoRepository;
import es.uca.iw.Ucapartment.Estado.Valor;
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
	
	public List<Double> findAllPreciosDouble() {
		return repo.findAllPreciosDouble();
	}
	
	public List<String> findAllPreciosString() {
		return repo.findAllPreciosString();
	}
	
	public List<String> findAllCiudades() {
		return repo.findAllCiudades();
	}
	
	// Función para buscar apartamentos en función de los filtros
	public List<Apartamento> findApartamentos(String precio, String ciudad, String habitaciones, Date fInicio, Date fFin, 
			Usuario u) {
		List<Apartamento> apartamentos;

		if(precio.equals("Todo")) { // En caso de que se elijan todos los precios
			if(ciudad.equals("Todo")) { // En caso de que se elijan todas las ciudades
				if(habitaciones.equals("Todo")) { // En caso de que se elijan todo número de habitaciones
					if(u == null) // Si el usuario no está logueado se muestran TODOS los apartamentos con ese filtro
						apartamentos = repo.findAllByPeriodo(fInicio, fFin, Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA);
					else // En caso contrario solo se mostraría aquellos apartamentos que no son del propio usuario logueado
						apartamentos = repo.findAllByPeriodoUsuario(fInicio, fFin, Valor.ACEPTADA, Valor.PENDIENTE, 
								Valor.REALIZADA, u);
				}
				else { // En caso de que se elija un número de habitaciones en concreto
					if(u == null)
						apartamentos = repo.findByHabitacionPeriodo(Integer.valueOf(habitaciones), fInicio, fFin, Valor.ACEPTADA, 
							Valor.PENDIENTE, Valor.REALIZADA);
					else
						apartamentos = repo.findByHabitacionPeriodoUsuario(Integer.valueOf(habitaciones), fInicio, fFin, 
								Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA, u);
				}
			}
			else { // En caso de que se elija una ciudad en concreto
				if(habitaciones.equals("Todo")) { // Se puede elegir filtrar por todo número de habitaciones
					if(u == null)
						apartamentos = repo.findByCiudadPeriodo(ciudad, fInicio, fFin, Valor.ACEPTADA, 
								Valor.PENDIENTE, Valor.REALIZADA);
					else
						apartamentos = repo.findByCiudadPeriodoUsuario(ciudad, fInicio, fFin, Valor.ACEPTADA, 
								Valor.PENDIENTE, Valor.REALIZADA, u);
				}
				else { // O bien filtrar por un número de habitaciones mínimo
					if(u == null)
						apartamentos = repo.findByCiudadAndHabitacionPeriodo(ciudad, Integer.valueOf(habitaciones), fInicio, 
								fFin, Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA);
					else
						apartamentos = repo.findByCiudadAndHabitacionPeriodoUsuario(ciudad, Integer.valueOf(habitaciones), fInicio, 
								fFin, Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA, u);
				}
			}
		}
		else { // En caso de elegir un precio máximo
			if(ciudad.equals("Todo")) { // Se puede filtrar por todas las ciudades
				if(habitaciones.equals("Todo")) { // Y a su vez todas las habitaciones
					if(u == null)
						apartamentos = repo.findByMaxPrecioPeriodo(Double.valueOf(precio), fInicio, fFin, Valor.ACEPTADA, 
								Valor.PENDIENTE, Valor.REALIZADA);
					else
						apartamentos = repo.findByMaxPrecioPeriodoUsuario(Double.valueOf(precio), fInicio, fFin, Valor.ACEPTADA, 
								Valor.PENDIENTE, Valor.REALIZADA, u);
				}
				else { // O bien un número mínimo de habitaciones
					if(u == null)
						apartamentos = repo.findByMaxPrecioAndHabitacionPeriodo(Double.valueOf(precio), 
								Integer.valueOf(habitaciones), fInicio, fFin, Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA);
					else
						apartamentos = repo.findByMaxPrecioAndHabitacionPeriodoUsuario(Double.valueOf(precio), 
								Integer.valueOf(habitaciones), fInicio, fFin, Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA, u);
				}
			}
			else { // O bien una ciudad concreta
				if(habitaciones.equals("Todo")) { // Con cualquier número de habitaciones
					if(u == null)
						apartamentos = repo.findByMaxPrecioAndCiudadPeriodo(Double.valueOf(precio), ciudad, fInicio, fFin, 
								Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA);
					else
						apartamentos = repo.findByMaxPrecioAndCiudadPeriodoUsuario(Double.valueOf(precio), ciudad, fInicio, fFin, 
								Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA, u);
				}
				else { // O un número concreto de habitaciones mínimas
					if(u == null)
						apartamentos = repo.findByMaxPrecioAndCiudadAndHabitacionPeriodo(Double.valueOf(precio), ciudad, 
							Integer.valueOf(habitaciones), fInicio, fFin, Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA);
					else
						apartamentos = repo.findByMaxPrecioAndCiudadAndHabitacionPeriodoUsuario(Double.valueOf(precio), ciudad, 
								Integer.valueOf(habitaciones), fInicio, fFin, Valor.ACEPTADA, Valor.PENDIENTE, Valor.REALIZADA, u);
				}
			}
		}
		return apartamentos;
	}
	
}
