package es.uca.iw.Ucapartment.Valoracion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Usuario.Usuario;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long>{

	public List<Valoracion> findByUsuarioValorado(Usuario usuarioValorado);
	public List<Valoracion> findByApartamentoValorado(Apartamento apartamentoValorado);
	public List<Valoracion> findByReserva(Reserva reserva);
}
