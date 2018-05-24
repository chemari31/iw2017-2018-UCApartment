package es.uca.iw.Ucapartment.Estado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Reserva.Reserva;

@Service
public class EstadoService {
	
	@Autowired
	EstadoRepository repo;
	
	public Estado save(Estado estado)
	{
		return repo.save(estado);
	}

	public Estado findByReserva(Reserva reserva) {
		// TODO Auto-generated method stub
		return repo.findByReserva(reserva);
	}

}
