package es.uca.iw.Ucapartment.Reserva;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.Ucapartment.Usuario.Usuario;



public interface ReservaRepository extends JpaRepository<Reserva, Long>
{
	public List<Reserva> findByUsuario(Usuario user);
}
