package es.uca.iw.Ucapartment.Apartamento;

import java.io.File;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.Ucapartment.Home;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoEditor.ChangeHandler;
import es.uca.iw.Ucapartment.Reserva.ReservaService;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.security.SecurityUtils;
import es.uca.iw.Ucapartment.Precio.Precio;
import es.uca.iw.Ucapartment.Precio.PrecioService;

@SpringComponent
@UIScope
public class ApartamentoNuevo extends VerticalLayout{
	private final ApartamentoService service;	
	/**
	 * The currently edited user
	 */
	private Apartamento apartamento;
	Usuario user = SecurityUtils.LogedUser();
	@Autowired
	private final PrecioService precioService;


	private Binder<Apartamento> binder = new Binder<>(Apartamento.class);
	
	/*Campos para crear un nuevo Apartamento*/
	TextField nombre = new TextField("Nombre");
	TextField ciudad = new TextField("Ciudad");
	ComboBox<Integer> habitaciones = new ComboBox<>("Habitaciones");
	TextField precio = new TextField("Precio");
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel);
	
	@Autowired
	public ApartamentoNuevo(ApartamentoService service, PrecioService precioService) {
		this.apartamento = new Apartamento("", "", "", "", "", 0, 0,user, 0, 0, false,0);
		this.service = service;
		this.precioService = precioService;
		
		habitaciones.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		
		HorizontalLayout form = new HorizontalLayout(nombre, ciudad, habitaciones, precio);
		addComponents(form, actions);
		
		nombre.setRequiredIndicatorVisible(true);
		ciudad.setRequiredIndicatorVisible(true);
		habitaciones.setRequiredIndicatorVisible(true);
		precio.setRequiredIndicatorVisible(true);
		nombre.setMaxLength(128);
		ciudad.setMaxLength(32);
		
		binder.forField(nombre)
		.asRequired("El campo Nombre del Apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Nombre ha de tener una longitud "
				+" máxima de 128 caracteres",1,128))
		.bind(Apartamento::getNombre, Apartamento::setNombre);
		
		binder.forField(ciudad)
		.asRequired("El campo Ciudad del Apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Ciudad ha de tener una longitud "
				+" máxima de 32 caracteres",1,32))
		.bind(Apartamento::getCiudad, Apartamento::setCiudad);
		
		binder.forField(habitaciones)
		.asRequired("El campo Habitaciones del Apartamento es obligatorio")
		.bind(Apartamento::getHabitaciones, Apartamento::setHabitaciones);
		
		binder.forField(precio)
		  .asRequired("No puede estar vacío")
		  .withConverter(
		    new StringToDoubleConverter("Por favor introduce un número decimal"))
		  .bind(Apartamento::getPrecio, Apartamento::setPrecio);
		  
		

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> { 
			if (binder.isValid()) {
				apartamento.setNombre(nombre.getValue());
				apartamento.setCiudad(ciudad.getValue());
				apartamento.setHabitaciones(Integer.valueOf(habitaciones.getValue()));
				apartamento.setPrecio(Double.parseDouble(precio.getValue().replace(',', '.')));
				apartamento.setFoto1("/apartamentos/null.png");
				service.save(apartamento);
				limpiar();
				setVisible(false);
			}
			else {
				Notification.show("Por favor, comprueba los datos introducidos");
				setVisible(true);
			}
		});
		cancel.addClickListener(e -> {return;});
		
	}
	public interface ChangeHandler {

		void onChange();
	}
	
	public void limpiar() {
		apartamento = new Apartamento("", "", "", "", "", 0, 0,user, 0, 0, false,0);
		nombre.clear();
		ciudad.clear();
		habitaciones.clear();
		precio.clear();
	}
	
	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> 
				h.onChange());
		cancel.addClickListener(e -> h.onChange());	
	}
}

