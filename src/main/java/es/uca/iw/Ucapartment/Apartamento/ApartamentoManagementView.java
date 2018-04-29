package es.uca.iw.Ucapartment.Apartamento;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringUI(path="/")
@SpringView(name = ApartamentoManagementView.VIEW_NAME)
public class ApartamentoManagementView extends VerticalLayout implements View{
	public static final String VIEW_NAME = "apartamentoManagementView";

	private Grid<Apartamento> grid;
	private TextField filter;
	private Button addNewBtn;

	private ApartamentoEditor editor;

	
	private final ApartamentoService service;
	
	@Autowired
	public ApartamentoManagementView(ApartamentoService service, ApartamentoEditor editor) {
		this.service = service;
		this.editor = editor;
		this.grid = new Grid<>(Apartamento.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Nuevo Apartamento", FontAwesome.PLUS);
		
		init();
		
	}
	
	@PostConstruct
	void init() {
		VerticalLayout layout = new VerticalLayout();
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		layout.addComponent(actions);
		addComponents(actions, grid, editor);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "nombre", "descripcion", "contacto", "ciudad");

		filter.setPlaceholder("Filter by nombre");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listApartamentos(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editApartamento(e.getValue());
		});

		// Instantiate and edit new User the new button is clicked
		addNewBtn.addClickListener(e -> editor.editApartamento(new Apartamento("", "", "", "", "", "", "", 0, 0, false)));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listApartamentos(filter.getValue());
		});

		// Initialize listing
		listApartamentos(null);
		
		addComponent(actions);
		addComponent(grid);
		addComponent(editor);
	
	}
	
	private void addComponents(HorizontalLayout actions, Grid<Apartamento> grid2, ApartamentoEditor editor2) {
		// TODO Auto-generated method stub
		
	}

	private void listApartamentos(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(service.findAll());
		} else {
			grid.setItems(service.findByNombreStartsWithIgnoreCase(filterText));
		}
	}
	
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}


}
