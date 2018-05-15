package es.uca.iw.Ucapartment.Estado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadoService {
	
	@Autowired
	EstadoRepository repo;
	
	public Estado save(Estado estado)
	{
		return repo.save(estado);
	}

}
