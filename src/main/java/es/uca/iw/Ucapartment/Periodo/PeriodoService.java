package es.uca.iw.Ucapartment.Periodo;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;

@Service
public class PeriodoService {
	@Autowired
	private PeriodoRepository repo;
	
	public List<Periodo> findByApartamento(Apartamento a){
		return repo.findByApartamento(a);
	}
	
	public void save(Periodo periodo) {
		repo.save(periodo);
	}
	
	public boolean noExistePeriodo(Date fInicio, Date fFin, Apartamento a) {
		List<Periodo> periodos = repo.findPeriodoRango(fInicio, fFin, a);
		
		return (periodos.size() == 0);
	}

	public void delete(Periodo periodo) {
		repo.delete(periodo);
	}
}
