package TestModelo;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.mockito.Mock;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Usuario.Usuario;

public class ReservaModelTest {

	@Mock
	Usuario usuario1;
	
	@Mock
	Usuario usuario2;
	
	@Mock
	Apartamento apartamento1;
	
	@Mock
	Apartamento apartamento2;
	
	@Mock
	Date fecha1;
	
	@Mock
	Date fecha2;
	
	@Test
	public void test() {
		Reserva reserva = new Reserva(fecha1, fecha1, fecha1, 10, usuario1, apartamento1);
		assertEquals(reserva.getFecha(), fecha1);
		assertEquals(reserva.getFechaInicio(), fecha1);
		assertEquals(reserva.getFechaFin(), fecha1);
		assertEquals(reserva.getPrecio(), 10, 0);
		assertEquals(reserva.getUsuario(), usuario1);
		assertEquals(reserva.getApartamento(), apartamento1);
		
		reserva.setFecha(fecha2);
		reserva.setFechaInicio(fecha2);
		reserva.setFechaFin(fecha2);
		reserva.setPrecio(20);
		reserva.setUsuario(usuario2);
		reserva.setApartamento(apartamento2);
		
		assertEquals(reserva.getFecha(), fecha2);
		assertEquals(reserva.getFechaInicio(), fecha2);
		assertEquals(reserva.getFechaFin(), fecha2);
		assertEquals(reserva.getPrecio(), 20, 0);
		assertEquals(reserva.getUsuario(), usuario2);
		assertEquals(reserva.getApartamento(), apartamento2);
	}

}
