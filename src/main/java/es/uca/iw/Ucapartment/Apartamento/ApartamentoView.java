package es.uca.iw.Ucapartment.Apartamento;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.Ucapartment.Usuario.PopupPago;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = ApartamentoView.VIEW_NAME)
public class ApartamentoView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "apartamentoView";

	private Grid<Apartamento> grid;
	private TextField filter;
	
	
	private final ApartamentoService service;
	private Apartamento apartamento;

	@Autowired
	public ApartamentoView(ApartamentoService service) {
		this.service = service;
	}
	
	void init() {
		
		Usuario duenio = apartamento.getUsuario();
		Button botonPerfil = new Button("Ver perfil");
		HorizontalLayout camposApartamento = new HorizontalLayout();
		VerticalLayout layoutDerecho = new VerticalLayout();
		VerticalLayout layoutIzquierdo = new VerticalLayout();
		Panel panelApartamento = new Panel("Apartamento "+apartamento.getNombre());
		Panel panelFoto = new Panel("Foto");
		Panel panelDuenio = new Panel("Dueño del apartamento");
		Button botonReserva = new Button("Reservar");
		botonReserva.setVisible(false);
		if(SecurityUtils.isLoggedIn() && (SecurityUtils.LogedUser().getId() != duenio.getId())) {
			botonReserva.setVisible(true);
		}

		panelApartamento.setWidth("600px");
		panelApartamento.setHeight("570px");
		panelFoto.setWidth("320px");
		panelFoto.setHeight("350px");
		panelDuenio.setWidth("320px");
		layoutIzquierdo.addComponent(panelApartamento);
		layoutDerecho.addComponent(panelFoto);
		layoutDerecho.addComponent(panelDuenio);

	    camposApartamento.addComponent(layoutIzquierdo);
	    camposApartamento.addComponent(layoutDerecho);

	    VerticalLayout elementosApartamento = new VerticalLayout();
	    VerticalLayout datosDuenio = new VerticalLayout();
	    
		HorizontalLayout hlNombre = new HorizontalLayout();
		HorizontalLayout hlDesc = new HorizontalLayout();
		HorizontalLayout hlContacto = new HorizontalLayout();
		HorizontalLayout hlCiudad = new HorizontalLayout();
		HorizontalLayout hlCalle = new HorizontalLayout();
		HorizontalLayout hlNumero = new HorizontalLayout();
		HorizontalLayout hlCP = new HorizontalLayout();
		HorizontalLayout hlHabit = new HorizontalLayout();
		HorizontalLayout hlCamas = new HorizontalLayout();
		HorizontalLayout hlAcond = new HorizontalLayout();
		HorizontalLayout hlNomDuenio = new HorizontalLayout();
		HorizontalLayout hlApellDuenio = new HorizontalLayout();
		HorizontalLayout hlEmailDuenio = new HorizontalLayout();
		
		Label vNombre, vDesc, vContacto, vCiudad, vCalle, vNumero, vCp, vHabit, vCamas, vAcond,
			vNombreDuenio, vApellidosDuenio, vEmailDuenio;
		Label nombre = new Label("Nombre: ");
		Label desc = new Label("Descripción: ");
		Label contacto = new Label("Contacto: ");
		Label ciudad = new Label("Ciudad: ");
		Label calle = new Label("Calle: ");
		Label numero = new Label("Número: ");
		Label cp = new Label("CP:");
		Label habit = new Label("Número de habitaciones: ");
		Label camas = new Label("Número de camas: ");
		Label acond = new Label("¿Tiene aire acondicionado? ");
		Label nombreDuenio = new Label("Nombre: ");
		Label apellidosDuenio = new Label("Apellidos: ");
		Label emailDuenio = new Label("Correo electrónico: ");
		
		vNombre = new Label(apartamento.getNombre());
		vDesc = new Label(apartamento.getDescripcion());
		vContacto = new Label(apartamento.getContacto());
		vCiudad = new Label(apartamento.getCiudad());
		vCalle = new Label(apartamento.getCalle());
		vNumero = new Label(apartamento.getNumero());
		vCp = new Label(apartamento.getCp());
		vHabit = new Label(String.valueOf(apartamento.getHabitaciones()));
		vCamas = new Label(String.valueOf(apartamento.getCamas()));
		if(apartamento.isAc())
			vAcond = new Label("Sí");
		else
			vAcond = new Label("No");
		
		vNombreDuenio = new Label(duenio.getNombre());
		vApellidosDuenio = new Label(duenio.getApellidos());
		vEmailDuenio = new Label(duenio.getEmail());
		
		Image image = new Image("foto");
		image.setWidth(300, Unit.PIXELS);
		image.setHeight(300, Unit.PIXELS);
		
    	image.setVisible(true);
    	if(apartamento.getFoto1() != null)
    		image.setSource(new FileResource(new File(apartamento.getFoto1())));
		
		hlNombre.addComponents(nombre, vNombre);
		hlDesc.addComponents(desc, vDesc);
		hlContacto.addComponents(contacto, vContacto);
		hlCiudad.addComponents(ciudad, vCiudad);
		hlCalle.addComponents(calle, vCalle);
		hlNumero.addComponents(numero, vNumero);
		hlCP.addComponents(cp, vCp);
		hlHabit.addComponents(habit, vHabit);
		hlCamas.addComponents(camas, vCamas);
		hlAcond.addComponents(acond, vAcond);
		
		hlNomDuenio.addComponents(nombreDuenio, vNombreDuenio);
		hlApellDuenio.addComponents(apellidosDuenio, vApellidosDuenio);
		hlEmailDuenio.addComponents(emailDuenio, vEmailDuenio);
		
		elementosApartamento.addComponent(hlNombre);
		elementosApartamento.addComponent(hlDesc);
		elementosApartamento.addComponent(hlContacto);
		elementosApartamento.addComponent(hlCiudad);
		elementosApartamento.addComponent(hlCalle);
		elementosApartamento.addComponent(hlNumero);
		elementosApartamento.addComponent(hlCP);
		elementosApartamento.addComponent(hlHabit);
		elementosApartamento.addComponent(hlCamas);
		elementosApartamento.addComponent(hlAcond);
		elementosApartamento.addComponent(botonReserva);
		
		datosDuenio.addComponent(hlNomDuenio);
		datosDuenio.addComponent(hlApellDuenio);
		datosDuenio.addComponent(hlEmailDuenio);
		datosDuenio.addComponent(botonPerfil);
		
		panelFoto.setContent(image);
		panelApartamento.setContent(elementosApartamento);
		panelDuenio.setContent(datosDuenio);
		
		addComponent(camposApartamento);
	    setComponentAlignment(camposApartamento, Alignment.TOP_CENTER);
		
	}	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// Obtenemos el id del apartamento de la URI
		String args[] = event.getParameters().split("/");
	    String value1 = args[0]; 
	    long id_apart = Long.parseLong(value1); // Como es un String lo convertimos a Long
	    apartamento = service.findById(id_apart); // Obtenemos el apartamento en cuestión de la BD
	    init(); // Y llamamos al metodo init que genera la vista
	}

}
