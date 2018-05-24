package es.uca.iw.Ucapartment.Administracion;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;
import es.uca.iw.Ucapartment.Usuario.PopupPago;
import es.uca.iw.Ucapartment.Usuario.Usuario;

@SpringView(name = DatosApartamentoView.VIEW_NAME)
public class DatosApartamentoView extends VerticalLayout implements View{
	public static final String VIEW_NAME = "datosApartamentoView";
	
	private final ApartamentoService apartamentoService;
	
	private Apartamento apartamento;
	
	private Binder<Apartamento> binder = new Binder<>(Apartamento.class);
	
	private final Image image = new Image("foto");
	private final Image image1 = new Image("Imagen 1");
	private final Image image2 = new Image("Imagen 2");
	private final Image image3 = new Image("Imagen 3");
	
	// Directorio base de la aplicación. Lo utilizamos para guardar las imágenes
	String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath(); 
	
	@Autowired
	public DatosApartamentoView(ApartamentoService service) {
		this.apartamentoService = service;

	}
	
	void init() {
		
		Usuario duenioApartamento = apartamento.getUsuario();
		Button btnPerfil = new Button("Ver perfil");
		
		HorizontalLayout camposApartamento = new HorizontalLayout();
		VerticalLayout layoutDerecho = new VerticalLayout();
		VerticalLayout layoutIzquierdo = new VerticalLayout();
		
		Panel panelApartamento = new Panel();
		Panel panelFoto = new Panel("Foto");
		Panel panelDuenio = new Panel("Dueño del apartamento");
		
		PopupPago imagenes = new PopupPago();
		
		panelApartamento.setCaption("Apartamento "+apartamento.getNombre());
		
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
	    
		// Lo siguiente se utiliza para guardar imagenes. Creamos el directorio si no existe
		File uploads = new File(basepath +"/apartamentos/");
        if (!uploads.exists() && !uploads.mkdir())
            System.out.println(new Label("ERROR: No podemos crear el directorio."));

		// Clase para almacenar imágenes
		class ImageUploader implements Receiver, SucceededListener {
			
			public File file;
			public int tipo; // Con esto sabremos que imagen hemos almacenado (1, 2 o 3)
			
			public ImageUploader(int i) {
				this.tipo = i;
			}
		    
			public OutputStream receiveUpload(String filename,
                    String mimeType) {
		    	FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		            file = new File(basepath +"/apartamentos/" + apartamento.getId() + filename);
		            fos = new FileOutputStream(file);
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("No se ha podido abrir el archivo",
		                             e.getMessage(),
		                             Notification.Type.ERROR_MESSAGE)
		                .show(Page.getCurrent());
		            return null;
		        }
		        if(tipo == 1) // Asignamos la ruta de la foto al atributo de la clase
		        	apartamento.setFoto1("/apartamentos/" + apartamento.getId() + filename);
		        else if(tipo == 2)
		        	apartamento.setFoto2("/apartamentos/" + apartamento.getId() + filename);
		        else if(tipo == 3)
		        	apartamento.setFoto3("/apartamentos/" + apartamento.getId() + filename);
		        return fos; // Return the output stream to write to 
		    }

		    public void uploadSucceeded(SucceededEvent event) {
		    	apartamentoService.save(apartamento);
		    	if(tipo == 1) {
		    		image.setSource(new FileResource(file));
		    		image.setWidth(300, Unit.PIXELS);
		    		image.setHeight(300, Unit.PIXELS);
		    		image1.setSource(new FileResource(file));
		    		image1.setWidth(400, Unit.PIXELS);
		    		image1.setHeight(400, Unit.PIXELS);
		    	}
		    	else if(tipo == 2) {
		    		image2.setSource(new FileResource(file));
		    		image2.setWidth(400, Unit.PIXELS);
		    		image2.setHeight(400, Unit.PIXELS);
		    	}
		    	else if(tipo == 3) {
		    		image3.setSource(new FileResource(file));
		    		image3.setWidth(400, Unit.PIXELS);
		    		image3.setHeight(400, Unit.PIXELS);
		    	}
		    }
		};
	    
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
		HorizontalLayout hlBtnModificarDatos = new HorizontalLayout();
		HorizontalLayout hlPrecio = new HorizontalLayout();
		
		Label vNombre, vDesc, vContacto, vCiudad, vCalle, vNumero, vCp, vHabit, vCamas, vAcond,
			vNombreDuenio, vApellidosDuenio, vEmailDuenio, vPrecio;
		
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
		Label precio = new Label("Precio: ");
		
		vNombre = new Label();
		vDesc = new Label();
		vContacto = new Label();
		vCiudad = new Label();
		vCalle = new Label();
		vNumero = new Label();
		vCp = new Label();
		vHabit = new Label();
		vCamas = new Label();
		vAcond = new Label();
		vPrecio = new Label();
		
		vNombre.setSizeUndefined();
		vDesc.setSizeUndefined();
		vContacto.setSizeUndefined();
		vCiudad.setSizeUndefined();
		vCalle.setSizeUndefined();
		vNumero.setSizeUndefined();
		vCp.setSizeUndefined();
		vHabit.setSizeUndefined();
		vCamas.setSizeUndefined();
		vAcond.setSizeUndefined();
		vPrecio.setSizeUndefined();
		
		vDesc.setSizeFull();
		
		vNombre.setValue(apartamento.getNombre());
		vDesc.setValue(apartamento.getDescripcion());
		vContacto.setValue(apartamento.getContacto());
		vCiudad.setValue(apartamento.getCiudad());
		vCalle.setValue(apartamento.getCalle());
		vNumero.setValue(Integer.toString(apartamento.getNumero()));
		vCp.setValue(Integer.toString(apartamento.getCp()));
		vHabit.setValue(Integer.toString(apartamento.getHabitaciones()));
		vCamas.setValue(Integer.toString(apartamento.getCamas()));
		vPrecio.setValue(Double.toString(apartamento.getPrecio()));
		if(apartamento.isAc())
			vAcond.setValue("Sí");
		else
			vAcond.setValue("No");
		
		vNombreDuenio = new Label(duenioApartamento.getNombre());
		vApellidosDuenio = new Label(duenioApartamento.getApellidos());
		vEmailDuenio = new Label(duenioApartamento.getEmail());
		
    	image.setVisible(true);
    	image1.setVisible(true);
    	image2.setVisible(true);
    	image3.setVisible(true);
    	
    	image.setDescription("Pulsa para más información");
    	
    	if(apartamento.getFoto1() != null) {
    		image.setSource(new ExternalResource(apartamento.getFoto1()));
    		image.setWidth(300, Unit.PIXELS);
    		image.setHeight(300, Unit.PIXELS);
    		image1.setSource(new ExternalResource(apartamento.getFoto1()));
    		image1.setWidth(400, Unit.PIXELS);
    		image1.setHeight(400, Unit.PIXELS);
    	}
    	
    	if(apartamento.getFoto2() != null) {
    		image2.setSource(new ExternalResource(apartamento.getFoto2()));
    		image2.setWidth(400, Unit.PIXELS);
    		image2.setHeight(400, Unit.PIXELS);
    	}
    	
    	if(apartamento.getFoto3() != null) {
    		image3.setSource(new ExternalResource(apartamento.getFoto3()));
    		image3.setWidth(400, Unit.PIXELS);
    		image3.setHeight(400, Unit.PIXELS);
    	}
    	
    	VerticalLayout popupLayout = new VerticalLayout();
    	image.addClickListener(event -> { 
    		popupLayout.removeAllComponents();
			HorizontalLayout hlImagenes = new HorizontalLayout();
			HorizontalLayout hlBtnModificar = new HorizontalLayout();
			HorizontalLayout hlBtnEliminarFotos = new HorizontalLayout();

			ImageUploader receiver = new ImageUploader(1);
			ImageUploader receiver2 = new ImageUploader(2);
			ImageUploader receiver3 = new ImageUploader(3);
			
			// Campos para subir la imagen
			Upload btnFoto1 = new Upload("Adjunta la foto 1", receiver);
			Upload btnFoto2 = new Upload("Adjunta la foto 2", receiver2);
			Upload btnFoto3 = new Upload("Adjunta la foto 3", receiver3);
			
			Button btnDeleteFoto1 = new Button("Borrar foto 1", FontAwesome.MINUS_CIRCLE);
			Button btnDeleteFoto2 = new Button("Borrar foto 2", FontAwesome.MINUS_CIRCLE);
			Button btnDeleteFoto3 = new Button("Borrar foto 3", FontAwesome.MINUS_CIRCLE);

			btnFoto1.addSucceededListener(receiver);
			btnFoto1.setImmediateMode(true);
			btnFoto1.setButtonCaption("Insertar imagen");
			
			btnFoto2.addSucceededListener(receiver2);
			btnFoto2.setImmediateMode(true);
			btnFoto2.setButtonCaption("Insertar imagen");
			
			btnFoto3.addSucceededListener(receiver3);
			btnFoto3.setImmediateMode(true);
			btnFoto3.setButtonCaption("Insertar imagen");
			
			btnDeleteFoto1.addClickListener(eventFoto1 -> {
				if(apartamento.getFoto1() != null) {
					apartamento.setFoto1(null);
					image1.setSource(null);
					image.setSource(null);
					apartamentoService.save(apartamento);
					Notification.show("Se ha eliminado la Foto 1 del apartamento");
				}
			});
			
			btnDeleteFoto2.addClickListener(eventFoto2 -> {
				if(apartamento.getFoto2() != null) {
					apartamento.setFoto2(null);
					image2.setSource(null);
					apartamentoService.save(apartamento);
					Notification.show("Se ha eliminado la Foto 2 del apartamento");
				}
			});
			
			btnDeleteFoto3.addClickListener(eventFoto3 -> {
				if(apartamento.getFoto3() != null) {
					apartamento.setFoto3(null);
					image3.setSource(null);
					apartamentoService.save(apartamento);
					Notification.show("Se ha eliminado la Foto 3 del apartamento");
				}
			});
			
			hlImagenes.addComponents(image1, image2, image3);
			hlBtnModificar.addComponents(btnFoto1,btnFoto2,btnFoto3);
			hlBtnEliminarFotos.addComponents(btnDeleteFoto1, btnDeleteFoto2, btnDeleteFoto3);
			
			popupLayout.addComponent(hlImagenes);
			popupLayout.addComponent(hlBtnModificar);
			popupLayout.addComponent(hlBtnEliminarFotos);
			popupLayout.setComponentAlignment(hlImagenes, Alignment.TOP_CENTER);
			
			imagenes.setWidth("1250px");
			imagenes.setHeight("650px");
			imagenes.setPosition(550, 200);
			imagenes.setContent(popupLayout);
			imagenes.center();
			imagenes.setDraggable(false);
			UI.getCurrent().addWindow(imagenes);
			
    	});
    	
		Button btnModificarDatos = new Button("Modificar datos");
		Button btnGuardar = new Button("Guardar");
		btnGuardar.setVisible(false);
		
		TextField tfNombre = new TextField();
		TextArea tfDesc = new TextArea();
		TextField tfContacto = new TextField();
		TextField tfCiudad = new TextField();
		TextField tfCalle = new TextField();
		TextField tfNumero = new TextField();
		TextField tfCP = new TextField();
		TextField tfPrecio = new TextField();
		ComboBox<Integer> cbHabit = new ComboBox<>("Habitaciones");
		ComboBox<Integer> cbCamas = new ComboBox<>("Camas");
		ComboBox<String> cbAcond = new ComboBox<>("Acondicionamiento");
		
		cbHabit.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		cbCamas.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		cbAcond.setItems("Sí", "No");
		
		// Con esto se muestra un * rojo en los campos deseados indicando que es obligatorio
		tfNombre.setRequiredIndicatorVisible(true);
		tfDesc.setRequiredIndicatorVisible(true);
		tfContacto.setRequiredIndicatorVisible(true);
		tfCiudad.setRequiredIndicatorVisible(true);
		tfCalle.setRequiredIndicatorVisible(true);
		tfNumero.setRequiredIndicatorVisible(true);
		tfCP.setRequiredIndicatorVisible(true);
		tfPrecio.setRequiredIndicatorVisible(true);
		cbHabit.setRequiredIndicatorVisible(true);
		cbCamas.setRequiredIndicatorVisible(true);
		cbAcond.setRequiredIndicatorVisible(true);
		
		tfNombre.setMaxLength(128);
		tfDesc.setMaxLength(256);
		tfContacto.setMaxLength(13);
		tfCiudad.setMaxLength(32);
		tfCalle.setMaxLength(128);
		tfNumero.setMaxLength(16);
		tfCP.setMaxLength(5);
		
		// Con los binder lo que hacemos es validar los datos introducidos
		binder.forField(tfNombre)
		.asRequired("El campo Nombre del apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Nombre ha de tener una longitud de "
				+ "128 caracteres máximo",0,128))
		.bind(Apartamento::getNombre,Apartamento::setNombre);
		
		binder.forField(tfDesc)
		.asRequired("El campo Descripción del apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Descripción ha de tener una longitud de "
				+ "256 caracteres máximo",0,256))
		.bind(Apartamento::getDescripcion,Apartamento::setDescripcion);
		
		binder.forField(tfContacto)
		.asRequired("El campo Contacto del apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Contacto ha de tener una longitud de "
				+ "13 caracteres máximo",0,13))
		.bind(Apartamento::getContacto,Apartamento::setContacto);
		
		binder.forField(tfCiudad)
		.asRequired("El campo Ciudad del apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Ciudad ha de tener una longitud de "
				+ "32 caracteres máximo",0,32))
		.bind(Apartamento::getCiudad,Apartamento::setCiudad);
		
		binder.forField(tfCalle)
		.asRequired("El campo Calle del apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Calle ha de tener una longitud de "
				+ "128 caracteres máximo",0,128))
		.bind(Apartamento::getCalle,Apartamento::setCalle);
		
		binder.forField(tfCP)
		.asRequired("El campo CP del apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo CP ha de tener una longitud de "
				+ "5 caracteres",5,5))
		 .withConverter(new StringToIntegerConverter("Por favor introduzca un número"))
		  .bind(Apartamento::getCp, Apartamento::setCp);
		
		binder.forField(tfNumero)
		.asRequired("El campo Numero del apartamento es obligatorio")
		.withValidator(new StringLengthValidator("El campo Numero ha de tener una longitud de "
				+ "16 caracteres máximo",0,16))
		  .withConverter(new StringToIntegerConverter("Por favor introduzca un número"))
		  .bind(Apartamento::getNumero, Apartamento::setNumero);
		
		binder.forField(tfPrecio)
		.asRequired("El campo Precio del apartamento es obligatorio")
		.withConverter(new StringToDoubleConverter("Por favor introduzca un número"))
		.bind(Apartamento::getPrecio, Apartamento::setPrecio);

		binder.bindInstanceFields(this);
		
		tfNombre.setVisible(false);
		tfDesc.setVisible(false);
		tfContacto.setVisible(false);
		tfCiudad.setVisible(false);
		tfCalle.setVisible(false);
		tfNumero.setVisible(false);
		tfCP.setVisible(false);
		tfPrecio.setVisible(false);
		cbHabit.setVisible(false);
		cbCamas.setVisible(false);
		cbAcond.setVisible(false);
		
		btnModificarDatos.addClickListener(event -> {
			btnModificarDatos.setVisible(false);
			btnGuardar.setVisible(true);
			vNombre.setVisible(false);
			vDesc.setVisible(false);
			vContacto.setVisible(false);
			vCiudad.setVisible(false);
			vCalle.setVisible(false);
			vNumero.setVisible(false);
			vCp.setVisible(false);
			vHabit.setVisible(false);
			vCamas.setVisible(false);
			vAcond.setVisible(false);
			vPrecio.setVisible(false);
			
			tfNombre.setVisible(true);
			tfDesc.setVisible(true);
			tfContacto.setVisible(true);
			tfCiudad.setVisible(true);
			tfCalle.setVisible(true);
			tfNumero.setVisible(true);
			tfCP.setVisible(true);
			tfPrecio.setVisible(true);
			cbHabit.setVisible(true);
			cbCamas.setVisible(true);
			cbAcond.setVisible(true);
			
			tfNombre.setValue(apartamento.getNombre());
			tfDesc.setValue(apartamento.getDescripcion());
			tfContacto.setValue(apartamento.getContacto());
			tfCiudad.setValue(apartamento.getCiudad());
			tfCalle.setValue(apartamento.getCalle());
			tfNumero.setValue(Integer.toString(apartamento.getNumero()));
			tfCP.setValue(Integer.toString(apartamento.getCp()));
			tfPrecio.setValue(Double.toString(apartamento.getPrecio()));
			cbHabit.setValue(apartamento.getHabitaciones());
			cbCamas.setValue(apartamento.getCamas());
			if(apartamento.isAc())
				cbAcond.setValue("Sí");
			else
				cbAcond.setValue("No");
			
		});
		
		btnGuardar.addClickListener(event -> {
			
			if(binder.isValid()) { // Si todas las validaciones se han pasado
				String sNombre, sDesc, sContacto, sCiudad, sCalle, sNumero, sCp, sPrecio;
				int iHabit, iCamas;
				boolean bAcond;

        		// Obtenemos todos los valores del formulario y actualizamos el usuario
				sNombre = tfNombre.getValue();
				sDesc = tfDesc.getValue();
				sContacto = tfContacto.getValue();
				sCiudad = tfCiudad.getValue();
				sCalle = tfCalle.getValue();
				sNumero = tfNumero.getValue();
				sCp = tfCP.getValue();
				sPrecio = tfPrecio.getValue();
				iHabit = cbHabit.getValue();
				iCamas = cbCamas.getValue();
				bAcond = (cbAcond.getValue().equals("Sí")) ? true:false;
                
            	if(!apartamento.getNombre().equals(sNombre)) apartamento.setNombre(sNombre);
            	if(!apartamento.getDescripcion().equals(sDesc)) apartamento.setDescripcion(sDesc);
            	if(!apartamento.getContacto().equals(sContacto)) apartamento.setContacto(sContacto);
            	if(!apartamento.getCiudad().equals(sCiudad)) apartamento.setCiudad(sCiudad);
            	if(!apartamento.getCalle().equals(sCalle)) apartamento.setCalle(sCalle);
            	if(!Integer.toString(apartamento.getNumero()).equals(sNumero)) apartamento.setNumero(Integer.parseInt(sNumero));
            	if(!Integer.toString(apartamento.getCp()).equals(sCp)) apartamento.setCp(Integer.parseInt(sCp));
            	if(!Double.toString(apartamento.getPrecio()).equals(sPrecio)) apartamento.setPrecio(Double.parseDouble(sPrecio));
            	apartamento.setAc(bAcond);
            	
            	apartamentoService.save(apartamento);
            	vNombre.setValue(apartamento.getNombre());
    			vDesc.setValue(apartamento.getDescripcion());
    			vContacto.setValue(apartamento.getContacto());
    			vCiudad.setValue(apartamento.getCiudad());
    			vCalle.setValue(apartamento.getCalle());
    			vNumero.setValue(Integer.toString(apartamento.getNumero()));
    			vCp.setValue(Integer.toString(apartamento.getCp()));
    			vPrecio.setValue(Double.toString(apartamento.getPrecio()));
    			vHabit.setValue(Integer.toString(iHabit));
    			vCamas.setValue(Integer.toString(iCamas));
    			vAcond.setValue((bAcond) ? "Sí":"No");
    			
    			vNombre.setVisible(true);
    			vDesc.setVisible(true);
    			vContacto.setVisible(true);
    			vCiudad.setVisible(true);
    			vCalle.setVisible(true);
    			vNumero.setVisible(true);
    			vCp.setVisible(true);
    			vPrecio.setVisible(true);
    			vHabit.setVisible(true);
    			vCamas.setVisible(true);
    			vAcond.setVisible(true);
    			
    			vDesc.setSizeFull();
    			
    			tfNombre.setVisible(false);
    			tfDesc.setVisible(false);
    			tfContacto.setVisible(false);
    			tfCiudad.setVisible(false);
    			tfCalle.setVisible(false);
    			tfNumero.setVisible(false);
    			tfCP.setVisible(false);
    			tfPrecio.setVisible(false);
    			cbHabit.setVisible(false);
    			cbCamas.setVisible(false);
    			cbAcond.setVisible(false);
    			
    			btnModificarDatos.setVisible(true);
    			btnGuardar.setVisible(false);
    			panelApartamento.setCaption("Apartamento "+apartamento.getNombre());
			}
			else
				Notification.show("Errores en el formulario. Corrígelo");
		});
		
		hlNombre.addComponents(nombre, vNombre, tfNombre);
		hlDesc.addComponents(desc, vDesc, tfDesc);
		hlContacto.addComponents(contacto, vContacto, tfContacto);
		hlCiudad.addComponents(ciudad, vCiudad, tfCiudad);
		hlCalle.addComponents(calle, vCalle, tfCalle);
		hlNumero.addComponents(numero, vNumero, tfNumero);
		hlCP.addComponents(cp, vCp, tfCP);
		hlHabit.addComponents(habit, vHabit, cbHabit);
		hlCamas.addComponents(camas, vCamas, cbCamas);
		hlAcond.addComponents(acond, vAcond, cbAcond);
		hlPrecio.addComponents(precio, vPrecio, tfPrecio, new Label(" €"));
		
		hlNomDuenio.addComponents(nombreDuenio, vNombreDuenio);
		hlApellDuenio.addComponents(apellidosDuenio, vApellidosDuenio);
		hlEmailDuenio.addComponents(emailDuenio, vEmailDuenio);
		
		hlBtnModificarDatos.addComponents(btnModificarDatos,btnGuardar);
		
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
		elementosApartamento.addComponent(hlPrecio);
		elementosApartamento.addComponent(hlBtnModificarDatos);
		
		
		/*botonPerfil.addClickListener(event -> {
			getUI().getNavigator().navigateTo(MiPerfilView.VIEW_NAME + '/'+String.valueOf(duenio.getId()));
		});*/
		
		datosDuenio.addComponent(hlNomDuenio);
		datosDuenio.addComponent(hlApellDuenio);
		datosDuenio.addComponent(hlEmailDuenio);
		datosDuenio.addComponent(btnPerfil);
		
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
	    apartamento = apartamentoService.findById(id_apart); // Obtenemos el apartamento en cuestión de la BD
	    init(); // Y llamamos al metodo init que genera la vista
	}
}
