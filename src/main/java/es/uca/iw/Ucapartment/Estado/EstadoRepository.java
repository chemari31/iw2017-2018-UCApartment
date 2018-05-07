package es.uca.iw.Ucapartment.Estado;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.Ucapartment.Reserva.Reserva;

public interface EstadoRepository extends JpaRepository<Estado, Long> 
{
	public Estado findByReserva(Reserva reserva);
}
