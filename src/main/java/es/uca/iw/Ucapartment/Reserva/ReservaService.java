package es.uca.iw.Ucapartment.Reserva;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository repo;
	
	public Reserva save(Reserva reserva)
	{
		return repo.save(reserva);
	}
	
	public List<Reserva> findByApartamento(Apartamento apart){
		return repo.findByApartamento(apart);
	}
	
	public Reserva findById(Long id) {
		return repo.findById(id);
	}


}
