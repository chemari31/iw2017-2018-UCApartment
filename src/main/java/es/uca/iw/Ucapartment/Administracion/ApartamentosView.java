package es.uca.iw.Ucapartment.Administracion;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;

@SpringView(name = ApartamentosView.VIEW_NAME)
public class ApartamentosView extends VerticalLayout implements View{
	
public static final String VIEW_NAME = "apartamentosView";
	
	private Grid<Apartamento> grid = new Grid<>();
	private List<Apartamento> lista_apartamentos;
	
	private final ApartamentoService apartamentoService;
	
	@Autowired
	public ApartamentosView(ApartamentoService service) {
		this.apartamentoService = service;
	}
	
	@PostConstruct
	void init() {
		
		VerticalLayout layout = new VerticalLayout();
		Panel listaApartamentosPanel = new Panel("Lista de apartamentos");
		
		listaApartamentosPanel.setWidth("1020px");
		listaApartamentosPanel.setHeight("580px");
		
		layout.addComponent(listaApartamentosPanel);
		layout.setComponentAlignment(listaApartamentosPanel, Alignment.TOP_CENTER);

		lista_apartamentos = apartamentoService.findAll();
		
		//grid.setColumns("nombreUsuario", "email");
		grid.setItems(lista_apartamentos);
		grid.addColumn(Apartamento::getNombre).setCaption("Nombre").setWidth(200)
	      .setResizable(false);
		grid.addColumn(Apartamento::getCiudad).setCaption("Ciudad").setWidth(200)
	      .setResizable(false);
		grid.addColumn(Apartamento::getHabitaciones).setCaption("Habitaciones").setWidth(200)
	      .setResizable(false);
		grid.addColumn(Apartamento::getPrecio).setCaption("Precio").setWidth(200)
	      .setResizable(false);
		grid.addColumn(e -> "Información", new ButtonRenderer(clickEvent-> { 
			Apartamento apartamento = ((Apartamento) clickEvent.getItem());
			getUI().getNavigator().navigateTo(DatosApartamentoView.VIEW_NAME + '/'+String.valueOf(apartamento.getId()));
			
			})).setCaption("Ver detalles").setWidth(200)
	      .setResizable(false);
		
		grid.setWidth("1000px");
		grid.setHeight("570px");
		listaApartamentosPanel.setContent(grid);
		addComponent(layout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
}
