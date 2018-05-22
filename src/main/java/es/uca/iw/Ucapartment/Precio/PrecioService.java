package es.uca.iw.Ucapartment.Precio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Precio.PrecioRepository;

@Service
public class PrecioService {
	
	@Autowired
	private PrecioRepository repo;

	public Precio save(Precio precio) {
		return repo.save(precio);
	}
	
	public void delete(Precio arg0) {
		repo.delete(arg0);
	}
	
	public List<Precio> findById(Long id){
		return repo.findById(id);
	}
	
	public List<Precio> findByApartamento(Apartamento apartamento){
		return repo.findByApartamento(apartamento);
	}
	
	public List<Precio>  findAll(){
		return repo.findAll();
	}
}
