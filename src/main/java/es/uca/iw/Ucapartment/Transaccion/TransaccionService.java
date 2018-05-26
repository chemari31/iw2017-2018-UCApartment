package es.uca.iw.Ucapartment.Transaccion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Reserva.Reserva;

@Service
public class TransaccionService {
	
	@Autowired
	private TransaccionRepository repo;
	
	public Transaccion save(Transaccion transaccion) {
		return repo.save(transaccion);
	}
	
	public List<Transaccion> findByReserva(Reserva reserva){
		return repo.findByReserva(reserva);
	}
	
	public List<Transaccion> findByCuentaOrigen(Long cuentaOrigen){
		return repo.findByCuentaOrigen(cuentaOrigen);
	}

}
