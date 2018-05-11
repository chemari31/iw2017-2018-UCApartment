package es.uca.iw.Ucapartment.Apartamento;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

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
	TextField descripcion = new TextField("Descripcion");
	ComboBox<Integer> habitaciones = new ComboBox<>("Habitaciones");
	ComboBox<Integer> camas = new ComboBox<>("Camas");
	ComboBox<Boolean> ac = new ComboBox<>("Acondicionamiento");
	TextField contacto = new TextField("Contacto");
	TextField ciudad = new TextField("Ciudad");
	TextField calle = new TextField("Calle");
	TextField numero = new TextField("Numero");
	TextField cp = new TextField("Código Postal");
	
	// Directorio base de la aplicación. Lo utilizamos para guardar las imágenes
	String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath(); 
	
	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	
	/* Layout for buttons */
	CssLayout actions = new CssLayout(save, cancel, delete);
	
	@Autowired
	public ApartamentoEditor(ApartamentoService service) {
		this.service = service;
		
		ac.setItems(true, false);
	    ac.setItemCaptionGenerator(bool -> {return bool?"Si":"No";});
		habitaciones.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		camas.setItems(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		

		// Show uploaded file in this placeholder
		final Image image = new Image("foto");
		
		//creamos el directorio si no existe.
		File uploads = new File(basepath+"/uploads");
        if (!uploads.exists() && !uploads.mkdir())
            System.out.println(new Label("ERROR: Could not create upload dir"));
        else
        	System.out.println("No falla el directorio");

		// Implement both receiver that saves upload in a file and
		// listener for successful upload
		class ImageUploader implements Receiver, SucceededListener {
			
			public File file;
		    
			public OutputStream receiveUpload(String filename,
                    String mimeType) {
		    	FileOutputStream fos = null; // Stream to write to
		        try {
		            // Open the file for writing.
		            file = new File(basepath+"/uploads/" + filename);
		            fos = new FileOutputStream(file);
		        } catch (final java.io.FileNotFoundException e) {
		            new Notification("Could not open file<br/>",
		                             e.getMessage(),
		                             Notification.Type.ERROR_MESSAGE)
		                .show(Page.getCurrent());
		            return null;
		        }
		        //asignamos la ruta de la foto al atributo de la clase
		        apartamento.setFoto1(basepath+"/uploads/" + filename);
		        return fos; // Return the output stream to write to
		        
		    }

		    public void uploadSucceeded(SucceededEvent event) {
		        // Show the uploaded file in the image viewer
		    	image.setVisible(true);
		        image.setSource(new FileResource(file));
		        addComponents(image);
		    }
		};
		
		ImageUploader receiver = new ImageUploader();
		//Campo para subir la foto
		Upload foto_btn = new Upload("Adjunta la foto", receiver);

		foto_btn.addSucceededListener(receiver);
		//código para procesar la foto
		foto_btn.setImmediateMode(false);
		foto_btn.setButtonCaption("Subir Ahora");
		
		
		addComponents(nombre, descripcion, habitaciones, camas, ac, contacto, ciudad,
				calle, numero, cp, foto_btn, actions);
		

		// asigna los campos del formulario al objeto apartamento
		binder.bindInstanceFields(this);

		
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> service.save(apartamento));
		delete.addClickListener(e -> service.delete(apartamento));
		cancel.addClickListener(e -> editApartamento(apartamento));
		setVisible(false);
	}
		
	public interface ChangeHandler {

		void onChange();
	}
	
	
	public final void editApartamento(Apartamento apar) {
		if (apar == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = apar.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			apartamento = service.findById(apar.getId());
		}
		else {
			apartamento = apar;
		}
		cancel.setVisible(persisted);

		// Bind user properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
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
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

}
