package es.uca.iw.Ucapartment.Administracion;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;

@SpringView(name = ApartamentosView.VIEW_NAME)
public class ApartamentosView extends VerticalLayout implements View{
	
public static final String VIEW_NAME = "apartamentosView";
	
	private Grid<Apartamento> grid = new Grid<>();
	
	private final ApartamentoService apartamentoService;
	
	@Autowired
	public ApartamentosView(ApartamentoService service) {
		this.apartamentoService = service;
	}
	
	@PostConstruct
	void init() {
		
		VerticalLayout layout = new VerticalLayout();
		Panel listaApartamentosPanel = new Panel("Lista de apartamentos");
		VerticalLayout contenidoPanel = new VerticalLayout();
		HorizontalLayout hlFiltro = new HorizontalLayout();
		TextField tfFiltro = new TextField();
		ComboBox<String> cbFiltro = new ComboBox<>();
		cbFiltro.setItems("Nombre","Ciudad");
		cbFiltro.setSelectedItem("Nombre");
		cbFiltro.setEmptySelectionAllowed(false);
		
		listaApartamentosPanel.setWidth("1040px");
		listaApartamentosPanel.setHeight("580px");
		
		hlFiltro.addComponents(tfFiltro,cbFiltro);
		
		layout.addComponent(listaApartamentosPanel);
		layout.setComponentAlignment(listaApartamentosPanel, Alignment.TOP_CENTER);
		
		tfFiltro.setPlaceholder("Filtrar por ");
		
		tfFiltro.setValueChangeMode(ValueChangeMode.LAZY);
		tfFiltro.addValueChangeListener(e -> listaApartamentos(e.getValue(), cbFiltro.getValue()));
		
		//grid.setColumns("nombreUsuario", "email");
		//grid.setItems(lista_apartamentos); 
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
		contenidoPanel.addComponent(hlFiltro);
		contenidoPanel.addComponent(grid);
		listaApartamentosPanel.setContent(contenidoPanel);
		listaApartamentos(null, null); // Lo inicializamos
		addComponent(layout);
	}
	
	public void listaApartamentos(String filtro, String filtrarPor) {
		if (StringUtils.isEmpty(filtro) || StringUtils.isEmpty(filtrarPor)) {
			grid.setItems(apartamentoService.findAll());
		} else {
			if(filtrarPor.equals("Nombre"))
				grid.setItems(apartamentoService.findByNombreStartsWithIgnoreCase(filtro));
			if(filtrarPor.equals("Ciudad"))
				grid.setItems(apartamentoService.findByCiudad(filtro));
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	
	
}
