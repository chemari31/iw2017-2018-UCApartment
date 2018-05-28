package TestModelo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mock;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Usuario.Usuario;

public class ApartamentoModelTest {

	@Mock
	Usuario usuario1;
	
	@Mock
	Usuario usuario2;
	
	@Test
	public void ApartamentoTesting() {
		Apartamento apartamento = new Apartamento("Prueba", "Prueba de apartamento",
			"111222333", "Cadiz", "Ancha", 3, 11111, usuario1, 1, 2, false, 10);
		
		assertEquals(apartamento.getNombre(), "Prueba");
		assertEquals(apartamento.getDescripcion(), "Prueba de apartamento");
		assertEquals(apartamento.getContacto(), "111222333");
		assertEquals(apartamento.getCiudad(), "Cadiz");
		assertEquals(apartamento.getCalle(), "Ancha");
		assertEquals(apartamento.getNumero(), 3);
		assertEquals(apartamento.getCp(), (Integer)11111);
		assertEquals(apartamento.getUsuario(), usuario1);
		assertEquals(apartamento.getHabitaciones(), 1);
		assertEquals(apartamento.getCamas(), 2);
		assertEquals(apartamento.isAc(), false);
		assertEquals(apartamento.getPrecio(), (double)10, 0);
		
		apartamento.setNombre("Probado");
		apartamento.setDescripcion("Apartamento ya probado");
		apartamento.setContacto("333222111");
		apartamento.setCiudad("Jerez");
		apartamento.setCalle("Nueva");
		apartamento.setNumero(1);
		apartamento.setCp(22222);
		apartamento.setUsuario(usuario2);
		apartamento.setHabitaciones(2);
		apartamento.setCamas(3);
		apartamento.setAc(true);
		apartamento.setPrecio(20);
		
		assertEquals(apartamento.getNombre(), "Probado");
		assertEquals(apartamento.getDescripcion(), "Apartamento ya probado");
		assertEquals(apartamento.getContacto(), "333222111");
		assertEquals(apartamento.getCiudad(), "Jerez");
		assertEquals(apartamento.getCalle(), "Nueva");
		assertEquals(apartamento.getNumero(), 1);
		assertEquals(apartamento.getCp(), (Integer)22222);
		assertEquals(apartamento.getUsuario(), usuario2);
		assertEquals(apartamento.getHabitaciones(), 2);
		assertEquals(apartamento.getCamas(), 3);
		assertEquals(apartamento.isAc(), true);
		assertEquals(apartamento.getPrecio(), (double)20, 0);
	}

}
