package es.uca.iw.Ucapartment.Apartamento;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
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
import com.vaadin.ui.SingleSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.SingleSelectionModel;

import es.uca.iw.Ucapartment.Home;
import es.uca.iw.Ucapartment.Precio.EstablecerPrecioEspeciales;
import es.uca.iw.Ucapartment.Precio.PrecioService;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.security.SecurityUtils;
import es.uca.iw.Ucapartment.Reserva.ReservaService;

@SpringUI(path="/")
@SpringView(name = ApartamentoManagementView.VIEW_NAME)
public class ApartamentoManagementView extends VerticalLayout implements View{
	public static final String VIEW_NAME = "apartamentoManagementView";

	private Grid<Apartamento> grid;
	private TextField filter;
	private Button addNewBtn;
	private Button editarBtn;
	private Button editarPrecioBtn;
	Usuario user = SecurityUtils.LogedUser();
	private Grid<Reserva> gridReservas;
	Window subWindow = new Window("Datos del Apartamento");
	

	private ApartamentoEditor editor;
	private ApartamentoNuevo nuevo;

	@Autowired
	private final ApartamentoService service;
	@Autowired
	private final ReservaService serviceReserva;
	@Autowired
	private final PrecioService precioService;
	
	@Autowired
	public ApartamentoManagementView(ApartamentoService service, ReservaService serviceReserva, ApartamentoEditor editor, PrecioService precioService) {
		this.service = service;
		this.serviceReserva = serviceReserva;
		this.precioService = precioService;
		this.editor = editor;
		nuevo = new ApartamentoNuevo(service, precioService);
		this.grid = new Grid<>(Apartamento.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Nuevo Apartamento", FontAwesome.PLUS);
		this.editarBtn = new Button("Editar Apartamento");
		this.editarPrecioBtn = new Button("Establecer Precio por Fecha");
		this.gridReservas = new Grid<>(Reserva.class);
	}
	
	@PostConstruct
	void init() {
		VerticalLayout layout = new VerticalLayout();
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, editarBtn, editarPrecioBtn);
		layout.addComponents(nuevo, editor, actions, grid, gridReservas);
		actions.setVisible(true);
		grid.setVisible(true);
		nuevo.setVisible(false);

		grid.setHeight(300, Unit.PIXELS);
		grid.setWidth(1000, Unit.PIXELS);
		grid.setColumns("nombre", "ciudad", "habitaciones", "precio");
		
		gridReservas.setHeight(300, Unit.PIXELS);
		gridReservas.setWidth(1000, Unit.PIXELS);
		gridReservas.setColumns("fechaInicio", "fechaFin", "precio", "apartamento");
		gridReservas.setVisible(false);
		
		filter.setPlaceholder("Filtrar por nombre");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listApartamentos(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> listReservas(e.getValue()));
		
		SingleSelect<Apartamento> selection = grid.asSingleSelect();
		// Instantiate and edit new Apartamento the new button is clicked
		addNewBtn.addClickListener(e -> { nuevo.setVisible(true); } );
		editarBtn.addClickListener(clickEvent -> { editor.editApartamento(selection.getValue()); });
		editarPrecioBtn.addClickListener(clickEvent -> { new EstablecerPrecioEspeciales(selection.getValue(), precioService); });
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listApartamentos(filter.getValue());
		});
		nuevo.setChangeHandler(() -> {
			nuevo.setVisible(false);
			listApartamentos(filter.getValue());
		});

		// Initialize listing
		listApartamentos(null);
		
		addComponent(layout);
	
	}
	
	private void addComponents(HorizontalLayout actions, Grid<Apartamento> grid2, ApartamentoEditor editor2) {
		// TODO Auto-generated method stub
		
	}

	public void listApartamentos(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(service.findByUsuario(user));
		} else {
			grid.setItems(service.findByNombreStartsWithIgnoreCase(filterText));
		}
	}
	
	private void listReservas(Apartamento apartamento) {
		gridReservas.setVisible(true);
		gridReservas.setItems(serviceReserva.findByApartamento(apartamento));
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}


}
