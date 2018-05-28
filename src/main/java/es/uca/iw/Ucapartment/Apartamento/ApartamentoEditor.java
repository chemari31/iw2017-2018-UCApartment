package es.uca.iw.Ucapartment.Apartamento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToDoubleConverter;
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
import com.vaadin.ui.DateField;
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
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.Ucapartment.Usuario.Usuario;

@SpringComponent
@UIScope
public class ApartamentoEditor extends VerticalLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private final ApartamentoService service;

	
	/**
	 * The currently edited user
	 */
	private Apartamento apartamento;


	private Binder<Apartamento> binder = new Binder<>(Apartamento.class);
	 
	/* Fields to edit properties in Apartamento entity */
	TextField nombre = new TextField("Nombre");
	TextArea descripcion = new TextArea("Descripcion");
	ComboBox<Integer> habitaciones = new ComboBox<>("Habitaciones");
	ComboBox<Integer> camas = new ComboBox<>("Camas");
	ComboBox<Boolean> ac = new ComboBox<>("Acondicionamiento");
	TextField contacto = new TextField("Contacto");
	TextField ciudad = new TextField("Ciudad");
	TextField calle = new TextField("Calle");
	TextField numero = new TextField("Numero");
	TextField cp = new TextField("Código Postal");
	TextField precio = new TextField("precio");
	// Show uploaded file in this placeholder
	final Image image = new Image("Foto");
	final Image image2 = new Image("Foto 2");
	final Image image3 = new Image("Foto 3");
	
	// Directorio base de la aplicación. Lo utilizamos para guardar las imágenes
	String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath(); 
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel);
	
	@Autowired
	public ApartamentoEditor(ApartamentoService service) {
		this.service = service;
		
		ac.setItems(true, false);
	    ac.setItemCaptionGenerator(bool -> {return bool?"Si":"No";});
		habitaciones.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		camas.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		// Implement both receiver that saves upload in a file and
		// listener for successful upload
		class ImageUploader implements Receiver, SucceededListener {
			
			public File file;
			public int tipo; //para saber que imagen se va a subir
			
			public ImageUploader(int i) {
				this.tipo = i;
			}
		    
			public OutputStream receiveUpload(String filename,
                    String mimeType) {
		    	FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		    		//creamos el directorio si no existe.
		    		File uploads = new File(basepath +"/apartamentos/"+ apartamento.getId());
		            if (!uploads.exists() && !uploads.mkdir())
		                System.out.println(new Label("ERROR: Could not create upload dir"));
		            
		            file = new File(basepath +"/apartamentos/" + apartamento.getId() +"/"+ filename);
		            fos = new FileOutputStream(file);
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>",
		                             e.getMessage(),
		                             Notification.Type.ERROR_MESSAGE)
		                .show(Page.getCurrent());
		            return null;
		        }
		        if(tipo == 1)//asignamos la ruta de la foto al atributo de la clase
		        	apartamento.setFoto1("/apartamentos/" + apartamento.getId() +"/"+ filename);
		        else if(tipo == 2)
		        	apartamento.setFoto2("/apartamentos/" + apartamento.getId() +"/"+ filename);
		        else if(tipo == 3)
		        	apartamento.setFoto3("/apartamentos/" + apartamento.getId() +"/"+ filename);
		        return fos; // Return the output stream to write to
		        
		    }

		    public void uploadSucceeded(SucceededEvent event) {
		        // Show the uploaded file in the image viewer
		    	if(tipo == 1) {
			    	image.setVisible(true);
			        image.setSource(new FileResource(file));
			        image.setWidth(200, Unit.PIXELS);
					image.setHeight(200, Unit.PIXELS);
		    	}
		    	else if(tipo == 2) {
		    		image2.setVisible(true);
			        image2.setSource(new FileResource(file));
			        image2.setWidth(200, Unit.PIXELS);
					image2.setHeight(200, Unit.PIXELS);
		    	}
		    	else if(tipo == 3) {
		    		image3.setVisible(true);
			        image3.setSource(new FileResource(file));
			        image3.setWidth(200, Unit.PIXELS);
					image3.setHeight(200, Unit.PIXELS);
		    	}
		    }
		};
		
		ImageUploader receiver = new ImageUploader(1);
		//Campo para subir la foto
		Upload foto_btn = new Upload("Adjunta la foto", receiver);
		ImageUploader receiver2 = new ImageUploader(2);
		//Campo para subir la foto
		Upload foto_btn2 = new Upload("Adjunta la foto", receiver2);
		ImageUploader receiver3 = new ImageUploader(3);
		//Campo para subir la foto
		Upload foto_btn3 = new Upload("Adjunta la foto", receiver3);
		
		Button btnDeleteFoto1 = new Button("Borrar foto 1", FontAwesome.MINUS_CIRCLE);
		Button btnDeleteFoto2 = new Button("Borrar foto 2", FontAwesome.MINUS_CIRCLE);
		Button btnDeleteFoto3 = new Button("Borrar foto 3", FontAwesome.MINUS_CIRCLE);

		foto_btn.addSucceededListener(receiver);
		//código para procesar la foto
		foto_btn.setImmediateMode(true);
		foto_btn.setButtonCaption("Subir Ahora");
		foto_btn2.addSucceededListener(receiver2);
		//código para procesar la foto
		foto_btn2.setImmediateMode(true);
		foto_btn2.setButtonCaption("Subir Ahora");
		foto_btn3.addSucceededListener(receiver3);
		//código para procesar la foto
		foto_btn3.setImmediateMode(true);
		foto_btn3.setButtonCaption("Subir Ahora");
		
		HorizontalLayout form = new HorizontalLayout(nombre, descripcion, contacto, precio);
		HorizontalLayout form2 = new HorizontalLayout(habitaciones, camas, ac);
		HorizontalLayout form3 = new HorizontalLayout(ciudad, calle, numero, cp);
		HorizontalLayout form4 = new HorizontalLayout(image, foto_btn, image2, foto_btn2, image3, foto_btn3);
		HorizontalLayout btnBorrarFotos = new HorizontalLayout(btnDeleteFoto1,btnDeleteFoto2,btnDeleteFoto3);
		
		btnDeleteFoto1.addClickListener(eventFoto1 -> {
			if(apartamento.getFoto1() != null && !apartamento.getFoto1().equals("/apartamentos/null.png")) {
				apartamento.setFoto1("/apartamentos/null.png");
				image.setSource(new ExternalResource(apartamento.getFoto1()));
				service.save(apartamento);
				Notification.show("Se ha eliminado la Foto 1 del apartamento");
			}
			else
				Notification.show("No se puede eliminar la foto por defecto");
		});
		
		btnDeleteFoto2.addClickListener(eventFoto2 -> {
			if(apartamento.getFoto2() != null) {
				apartamento.setFoto2(null);
				image2.setSource(null);
				service.save(apartamento);
				Notification.show("Se ha eliminado la Foto 2 del apartamento");
			}
			else
				Notification.show("La imagen 2 no está definida");
		});
		
		btnDeleteFoto3.addClickListener(eventFoto3 -> {
			if(apartamento.getFoto3() != null) {
				apartamento.setFoto3(null);
				image3.setSource(null);
				service.save(apartamento);
				Notification.show("Se ha eliminado la Foto 3 del apartamento");
			}
			else
				Notification.show("La imagen 3 no está definida");
		});
		
		addComponents(form, form2, form3, form4, btnBorrarFotos, actions);
		
		// Con esto se muestra un * rojo en los campos deseados indicando que es obligatorio
		nombre.setRequiredIndicatorVisible(true);
		descripcion.setRequiredIndicatorVisible(true);
		habitaciones.setRequiredIndicatorVisible(true);
		camas.setRequiredIndicatorVisible(true);
		ac.setRequiredIndicatorVisible(true);
		contacto.setRequiredIndicatorVisible(true);
		ciudad.setRequiredIndicatorVisible(true);
		calle.setRequiredIndicatorVisible(true);
		numero.setRequiredIndicatorVisible(true);
		cp.setRequiredIndicatorVisible(true);
		precio.setRequiredIndicatorVisible(true);
		
		nombre.setMaxLength(128);
		descripcion.setMaxLength(256);
		contacto.setMaxLength(13);
		ciudad.setMaxLength(32);
		calle.setMaxLength(128);
		numero.setMaxLength(16);
		cp.setMaxLength(5);
		precio.setMaxLength(10);
		
		binder.forField(cp)
		  .withConverter(new StringToIntegerConverter("Por favor introduzca un número"))
		  .bind(Apartamento::getCp, Apartamento::setCp);
		binder.forField(numero)
		  .withConverter(new StringToIntegerConverter("Por favor introduzca un número"))
		  .bind(Apartamento::getNumero, Apartamento::setNumero);
		
		binder.forField(precio)
			.withConverter(new StringToDoubleConverter("Por favor introduzca un número"))
			.bind(Apartamento::getPrecio, Apartamento::setPrecio);
		// asigna los campos del formulario al objeto apartamento
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> validar());
		cancel.addClickListener(e -> {return;});
		setVisible(false);
		
	}
		
	public interface ChangeHandler {

		void onChange();
	}
	
	public void validar() {
		// Con los binder lo que hacemos es validar los datos introducidos
		binder.forField(nombre)
		.asRequired("El campo Nombre del Apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Nombre ha de tener una longitud "
				+" máxima de 128 caracteres",1,128))
		.bind(Apartamento::getNombre, Apartamento::setNombre);
		
		binder.forField(descripcion)
		.asRequired("El campo Descripción del Apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Descripción ha de tener una longitud "
				+" máxima de 256 caracteres",1,256))
		.bind(Apartamento::getDescripcion, Apartamento::setDescripcion);
		
		binder.forField(contacto)
		.asRequired("El campo Contacto del Apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Contacto ha de tener una longitud "
				+" máxima de 13 caracteres",1,13))
		.bind(Apartamento::getContacto, Apartamento::setContacto);
		
		binder.forField(ciudad)
		.asRequired("El campo Ciudad del Apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Ciudad ha de tener una longitud "
				+" máxima de 32 caracteres",1,32))
		.bind(Apartamento::getCiudad, Apartamento::setCiudad);
		
		binder.forField(calle)
		.asRequired("El campo Calle del Apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Calle ha de tener una longitud "
				+" máxima de 128 caracteres",1,128))
		.bind(Apartamento::getCalle, Apartamento::setCalle);
		
		binder.forField(cp)
		.asRequired("El campo Código Postal del Apartamento es obligatorio")
		  .withConverter(new StringToIntegerConverter("Por favor introduzca un número"))
		  .bind(Apartamento::getCp, Apartamento::setCp);
		
		binder.forField(numero)
		.asRequired("El campo Número del Apartamento es obligatorio")
		  .withConverter(new StringToIntegerConverter("Por favor introduzca un número"))
		  .bind(Apartamento::getNumero, Apartamento::setNumero);
		
		binder.forField(precio)
		.asRequired("El campo Precio del Apartamento es obligatorio")
		  .withConverter(new StringToDoubleConverter("Por favor introduzca un número"))
		  .bind(Apartamento::getPrecio, Apartamento::setPrecio);

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		if(binder.isValid()) { // Si todas las validaciones se han pasado
			binder.setBean(apartamento);
			service.save(apartamento);
		}
		else{
			Notification.show("El apartamento no se pudo guardar, " +
		        "por favor comprueba cada campo");
		}
		setVisible(false);
	}
	
	
	public final void editApartamento(Apartamento apar) {
		//borra las imagenes para cuando se cambia de apartamento o se le da a nuevo.
		image.setVisible(false);
		image2.setVisible(false);
		image3.setVisible(false);
		if (apar == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = apar.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			apartamento = service.findById(apar.getId());
			if(apartamento.getFoto1() != null) {
				File file = new File(apartamento.getFoto1());
				image.setVisible(true);
				image.setSource(new ExternalResource(apartamento.getFoto1()));
				image.setWidth(200, Unit.PIXELS);
				image.setHeight(200, Unit.PIXELS);
			}else
				image.setVisible(false);
			if(apartamento.getFoto2() != null) {
				File file = new File(apartamento.getFoto2());
				image2.setVisible(true);
				image2.setSource(new ExternalResource(apartamento.getFoto2()));
				image2.setWidth(200, Unit.PIXELS);
				image2.setHeight(200, Unit.PIXELS);
			}else
				image2.setVisible(false);
			if(apartamento.getFoto3() != null) {
				File file = new File(apartamento.getFoto3());
				image3.setVisible(true);
				image3.setSource(new ExternalResource(apartamento.getFoto3()));
				image3.setWidth(200, Unit.PIXELS);
				image3.setHeight(200, Unit.PIXELS);
			}else
				image3.setVisible(false);
		}
		else {
			apartamento = apar;
		}
		binder.setBean(apartamento);
		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		nombre.selectAll();
	}
	
	
	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> 
				h.onChange());
		cancel.addClickListener(e -> h.onChange());
	}

}
