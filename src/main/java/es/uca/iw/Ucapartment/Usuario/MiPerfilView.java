package es.uca.iw.Ucapartment.Usuario;



import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaRepository;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = MiPerfilView.VIEW_NAME)
public class MiPerfilView extends VerticalLayout implements View
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME="MiPerfil";
	private Usuario user = SecurityUtils.LogedUser();
	
	@Autowired
	UsuarioService service;
	
	
	@Autowired
	private ReservaRepository repoReserva;
	@Autowired
	private EstadoRepository repoEstado;
	private Apartamento apart = null;
	private Estado estado = null;
	final Image image = new Image();
	Button pendiente = new Button("Pendiente");
	Button pago;
	Button cancelada = new Button("Cancelada");
	Button cancelar = new Button("Cancelar");
	Button aceptar = new Button("Aceptar");
	Button incidencias = new Button("Dejar una Incidencia");
	Button realizada = new Button("Realizada");
	private int contReserva = 1;
	VerticalLayout vertical = new VerticalLayout();
	VerticalLayout popupVertical = new VerticalLayout();
	Button[] open = new Button[100];//Vector para los botones de los valores Aceptados
	
	
	@PostConstruct
	void init()
	{
		//Datos Personales de los Usuarios
		image.setSource(new ExternalResource(user.getFoto1()));
		image.setWidth(200, Unit.PIXELS);
		image.setHeight(200, Unit.PIXELS);
		addComponents(image);
		addComponents(new Label("Datos Personales"));
		addComponents(new Label("Apellido: "+user.getApellidos()));
		addComponents(new Label("Nombre: "+user.getNombre()));
		addComponents(new Label("DNI: " +user.getDni()));
		addComponents(new Label("Email: "+user.getEmail()));	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		/*try {
			// Obtenemos el id del apartamento de la URI
			String args[] = event.getParameters().split("/");
		    String value1 = args[0]; 
		    long id_user = Long.parseLong(value1); // Como es un String lo convertimos a Long
		    user = service.findById(id_user); // Obtenemos el apartamento en cuestión de la BD
		    init(); // Y llamamos al metodo init que genera la vista
		}catch(Exception e) {user = SecurityUtils.LogedUser();}*/
		
		
	}
}
