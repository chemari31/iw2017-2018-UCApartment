package es.uca.iw.Ucapartment.Apartamento;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = ApartamentoView.VIEW_NAME)
public class ApartamentoView extends VerticalLayout implements View {
	public static final String VIEW_NAME = "apartamentoView";

	private Grid<Apartamento> grid;
	private TextField filter;
	
	
	private final ApartamentoService service;

	@Autowired
	public ApartamentoView(ApartamentoService service, ApartamentoEditor editor) {
		this.service = service;
		this.grid = new Grid<>(Apartamento.class);
		this.filter = new TextField();
	    init();
	}
	
	@PostConstruct
	void init() {
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter);
		
		addComponents(actions, grid);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "descripcion", "contacto");

		filter.setPlaceholder("Filter by descripcion");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listApartamentos(e.getValue()));

		// Initialize listing
		listApartamentos(null);
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
