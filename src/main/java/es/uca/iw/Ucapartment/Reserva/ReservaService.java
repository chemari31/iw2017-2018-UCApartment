package es.uca.iw.Ucapartment.Reserva;

import java.sql.Date;
import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.Valor;

@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository repo;
	
	@Autowired
	private EstadoRepository repoEstado;
	
	public Reserva save(Reserva reserva)
	{
		return repo.save(reserva);
	}
	
	@Transactional
	public void pasarelaDePago(double total, Reserva r, Estado e)
	{
		double beneficioEmpresa = total * 0.10;
		double beneficioAnfitrion = total * 0.90;
		
		r.setBenenficioEmpresa(beneficioEmpresa);
		r.setBenenficioAnfitrion(beneficioAnfitrion);
		e.setValor(Valor.REALIZADA);
		repoEstado.save(e);
		repo.save(r);
		
	
	}
	

}
