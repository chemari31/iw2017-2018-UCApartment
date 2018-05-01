package es.uca.iw.Ucapartment.Apartamento;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class ApartamentoEditor extends VerticalLayout{
	private final ApartamentoService service;

	
	/**
	 * The currently edited user
	 */
	private Apartamento apartamento;

	private Binder<Apartamento> binder = new Binder<>(Apartamento.class);
	
	/* Fields to edit properties in User entity */
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
		addComponents(nombre, descripcion, habitaciones, camas, ac, contacto, ciudad,
				calle, numero, cp, actions);

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
