package es.uca.iw.Ucapartment.Reserva;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Usuario.Usuario;



public interface ReservaRepository extends JpaRepository<Reserva, Long>
{
	public List<Reserva> findByUsuario(Usuario user);
	public List<Reserva> findAll();
	public List<Reserva> findByApartamento(Apartamento apart);
	
	@Query("Select e.reserva from Estado e Where e.valor = :valor")
	public List<Reserva> findByEstadoValor(@Param("valor")Valor valor);
	public Reserva findById(Long id);
	
}
