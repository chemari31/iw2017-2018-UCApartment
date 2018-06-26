package TestServicio;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoRepository;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;
import es.uca.iw.Ucapartment.Usuario.Usuario;

@RunWith(MockitoJUnitRunner.class)
public class ApartamentoServiceTest {
	
	private static final Long id = (long) 1;
	
	private static final String nombre = "Prueba";
	
	private static final String descripcion = "Descripcion de prueba";
	
	private static final String contacto = "111222333";
	
	private static final String ciudad = "Cadiz";
	
	private static final String calle = "Nueva";
	
	private static final Integer numero = 2;
	
	private static final Integer cp = 11111;
	
	private static final int habitaciones = 2;
	
	private static final int camas = 4;
	
	private static final boolean ac = true;
	
	private static final double precio = 100.00;
	
	private Apartamento apartamento;
	
	@InjectMocks
	private ApartamentoService service= new ApartamentoService();

	@Mock
	private Usuario usuarioMock;
	
	@Mock
	private ApartamentoRepository repo;
	
	@Before
	public void preparar() {
		
		apartamento = new Apartamento(nombre, descripcion, contacto, ciudad, calle, numero,
			cp, usuarioMock, habitaciones, camas, ac, precio);
		
		List<Apartamento> todos = new ArrayList<Apartamento>();
		todos.add(apartamento);
		
		when(repo.findAll()).thenReturn(todos);
	}
	
	@Test
	public void testFindAll() {
		
		List<Apartamento> todos = service.findAll();
		
		Assert.assertNotNull(todos);
		Assert.assertTrue(todos.size() > 0);
		Assert.assertEquals(todos.size(), 1);
		Assert.assertEquals(todos.get(0).getNombre(), nombre);
	}
}
