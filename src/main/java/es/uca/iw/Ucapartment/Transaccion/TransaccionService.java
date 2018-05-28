package es.uca.iw.Ucapartment.Transaccion;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Reserva.Reserva;

@Service
public class TransaccionService {
	
	@Autowired
	private TransaccionRepository repo;
	
	public Transaccion save(Transaccion transaccion) {
		return repo.save(transaccion);
	}
	
	public List<Transaccion> findByReserva(Reserva reserva){
		return repo.findByReserva(reserva);
	}
	
	public List<Transaccion> findByCuentaOrigen(Long cuentaOrigen){
		return repo.findByCuentaOrigen(cuentaOrigen);
	}
	
	public double SumaImporteCuenta(Long cuenta) {
		List<Transaccion> transacciones = repo.findByCuentaDestino(cuenta);
		if(transacciones.size() > 0)
			return repo.SumaImporteCuenta(cuenta);
		else
			return 0;
	}
	
	public double beneficioCuentaIntervalo(Long cuenta, Date fechaInicio, Date fechaFin) {
		
		List<Transaccion> transacciones;
		double valorBeneficio = 0;
		if(fechaInicio != null && fechaFin != null) {
			transacciones = repo.findFromFechaIniToFechaFinCuenta(cuenta, fechaInicio, fechaFin);
			if(transacciones.size() > 0)
				valorBeneficio = repo.beneficioCuentaIntervalo(cuenta, fechaInicio, fechaFin);
		}
		else {
			if(fechaInicio != null) {
				transacciones = repo.findFromFechaIniCuenta(cuenta, fechaInicio);
				if(transacciones.size() > 0)
					valorBeneficio = repo.beneficioCuentaFini(cuenta, fechaInicio);
			}
			if(fechaFin != null) {
				transacciones = repo.findToFechaFinCuenta(cuenta, fechaFin);
				if(transacciones.size() > 0)
					valorBeneficio = repo.beneficioCuentaFfin(cuenta, fechaFin);
			}
		}

		return valorBeneficio;
	}

}
