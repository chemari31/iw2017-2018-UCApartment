package es.uca.iw.Ucapartment.Estado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uca.iw.Ucapartment.Reserva.Reserva;

public interface EstadoRepository extends JpaRepository<Estado, Long> 
{
	public Estado findByReserva(Reserva reserva);
	
	@Query("SELECT e.reserva FROM Estado e WHERE e.valor = :valor")
	public List<Reserva> findReservaByEstado(@Param("valor") Valor valor);
	
	@Query("SELECT e.reserva FROM Estado e WHERE e.valor != :valor")
	public List<Reserva> findReservaByValor(@Param("valor") Valor valor);
	
	
}
