package es.uca.iw.Ucapartment.Transaccion;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uca.iw.Ucapartment.Reserva.Reserva;

public interface TransaccionRepository extends JpaRepository<Transaccion, Long>{
	public List<Transaccion> findByReserva(Reserva reserva);
	public List<Transaccion> findByCuentaOrigen(Long cuentaOrigen);
	public List<Transaccion> findByCuentaDestino(Long cuentaDestino);
	
	@Query("Select sum(t.importe) from Transaccion t Where t.cuentaDestino = :cuenta")
	public double SumaImporteCuenta(@Param("cuenta") Long cuenta);
	
	@Query("Select sum(t.importe) from Transaccion t Where t.cuentaDestino = :cuenta "
			+ "and t.fecha >= :fIni and t.fecha <= :fFin")
	public double beneficioCuentaIntervalo(@Param("cuenta") Long cuenta, 
			@Param("fIni") Date fechaInicio, @Param("fFin") Date fechaFin);
	
	@Query("Select sum(t.importe) from Transaccion t Where t.cuentaDestino = :cuenta "
			+ "and t.fecha >= :fIni")
	public double beneficioCuentaFini(@Param("cuenta") Long cuenta, @Param("fIni") Date fechaInicio);
	
	@Query("Select sum(t.importe) from Transaccion t Where t.cuentaDestino = :cuenta "
			+ "and t.fecha <= :fFin")
	public double beneficioCuentaFfin(@Param("cuenta") Long cuenta, @Param("fFin") Date fechaFin);
	
	@Query("Select t.id from Transaccion t Where t.cuentaDestino = :cuenta "
			+ "and t.fecha >= :fIni")
	public List<Transaccion> findFromFechaIniCuenta(@Param("cuenta") Long cuenta, 
			@Param("fIni") Date fechaInicio);
	
	@Query("Select t.id from Transaccion t Where t.cuentaDestino = :cuenta "
			+ "and t.fecha <= :fFin")
	public List<Transaccion> findToFechaFinCuenta(@Param("cuenta") Long cuenta, 
			@Param("fFin") Date fechaFin);
	
	@Query("Select t.id from Transaccion t Where t.cuentaDestino = :cuenta "
			+ "and t.fecha >= :fIni and t.fecha <= :fFin")
	public List<Transaccion> findFromFechaIniToFechaFinCuenta(@Param("cuenta") Long cuenta, 
			@Param("fIni") Date fechaInicio, @Param("fFin") Date fechaFin);
	
}
