package es.uca.iw.Ucapartment.Transaccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uca.iw.Ucapartment.Reserva.Reserva;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{
	public List<Transaccion> findByReserva(Reserva reserva);
	public List<Transaccion> findByCuentaOrigen(Long cuentaOrigen);
	
	@Query("Select sum(t.importe) from Transaccion t Where t.cuentaDestino = :cuenta")
	public double SumaImporteCuenta(@Param("cuenta") Long cuenta);
}
