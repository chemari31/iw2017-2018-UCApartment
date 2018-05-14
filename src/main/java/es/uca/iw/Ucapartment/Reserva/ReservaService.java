package es.uca.iw.Ucapartment.Reserva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository repo;
	
	public Reserva save(Reserva reserva)
	{
		return repo.save(reserva);
	}

}
