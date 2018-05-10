package es.uca.iw.Ucapartment.Apartamento;

import java.io.File;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

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
		
		VerticalLayout camposApartamento = new VerticalLayout();
		/*Panel panel = new Panel("Apartamento "+apartamento.getNombre());
		panel.setWidth("800px");
	    panel.setHeight("500px");
	    camposApartamento.addComponent(panel);
	    camposApartamento.setComponentAlignment(panel, Alignment.BOTTOM_CENTER);*/
	    
	    HorizontalLayout campos = new HorizontalLayout();
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
		
		Label vNombre, vDesc, vContacto, vCiudad, vCalle, vNumero, vCp, vHabit, vCamas, vAcond;
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
		
		/*Image image = new Image("foto");
		
    	image.setVisible(true);
        image.setSource(new FileResource(new File(apartamento.getFoto1())));*/
		
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
		
		camposApartamento.addComponent(hlNombre);
		camposApartamento.addComponent(hlDesc);
		camposApartamento.addComponent(hlContacto);
		camposApartamento.addComponent(hlCiudad);
		camposApartamento.addComponent(hlCalle);
		camposApartamento.addComponent(hlNumero);
		camposApartamento.addComponent(hlCP);
		camposApartamento.addComponent(hlHabit);
		camposApartamento.addComponent(hlCamas);
		camposApartamento.addComponent(hlAcond);
		campos.addComponent(camposApartamento);
		//campos.addComponent(image);
		
		
		addComponent(camposApartamento);
		
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
