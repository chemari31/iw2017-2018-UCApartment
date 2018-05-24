package es.uca.iw.Ucapartment.Valoracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValoracionService
{
	
	@Autowired
	private ValoracionRepository repo;
	
	public Valoracion save(Valoracion valoracion)
	{
		return repo.saveAndFlush(valoracion);
	}
	

}
