package es.uca.iw.Ucapartment.Valoracion;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Usuario.Usuario;

@Service
public class ValoracionService
{
	
	@Autowired
	private ValoracionRepository repo;
	
	public Valoracion save(Valoracion valoracion)
	{
		return repo.saveAndFlush(valoracion);
	}
	
	public List<Valoracion> findByUsuarioValorado(Usuario usuarioValorado){
		return repo.findByUsuarioValorado(usuarioValorado);
	}
	
	public List<Valoracion> findByApartamentoValorado(Apartamento apartamentoValorado){
		return repo.findByApartamentoValorado(apartamentoValorado);
	}
	
	public List<Valoracion> findByReserva(Reserva reserva){
		return repo.findByReserva(reserva);
	}
}
