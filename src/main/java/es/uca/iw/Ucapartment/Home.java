package es.uca.iw.Ucapartment;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.sass.internal.parser.ParseException;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;

import com.vaadin.ui.Label;

import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


import es.uca.iw.Ucapartment.Apartamento.Apartamento;

import es.uca.iw.Ucapartment.Apartamento.ApartamentoRepository;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoView;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.EstadoService;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaRepository;
import es.uca.iw.Ucapartment.Reserva.ReservaService;
import es.uca.iw.Ucapartment.Usuario.Popup;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.security.SecurityUtils;


@Theme("mytheme")
@SpringView(name = Home.VIEW_NAME)
public class Home extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "home";
	
	
	@Autowired
	ApartamentoService apartamentoService;
	@Autowired
	ReservaService reservaService;
	@Autowired
	EstadoService estadoService;
	
	HorizontalLayout layout3 = new HorizontalLayout();
	
	Usuario usuario = null;
	
	List<Apartamento> listaApartamentos;
	
	@PostConstruct
	public void init() {
		
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout hlFiltro = new HorizontalLayout();
		List<String> sPreciosApartamentos = apartamentoService.findAllPreciosString();
		List<String> ciudadesApartamentos = apartamentoService.findAllCiudades();
		ComboBox<String> cbPrecios = new ComboBox<>("Precio máximo");
		ComboBox<String> cbCiudades = new ComboBox<>("Ciudad");
		ComboBox<String> cbHabitaciones = new ComboBox<>("Habitaciones mínimas");
		
		//Declaracion de los dos calendario para entrada y salida
		DateField fechaInicio = new DateField("Entrada");
		DateField fechaFin = new DateField("Salida");
		fechaInicio.setValue(LocalDate.now());
		Date hoy = java.sql.Date.valueOf(fechaInicio.getValue());
		fechaFin.setValue(LocalDate.now().plusDays(1));
		
		Panel panelPrincipal = new Panel("<h1 style='color:blue; text-align: center;'>UCApartment</h1>");
		panelPrincipal.setWidth("1000px");
		panelPrincipal.setHeight("1000px");
	    layout.addComponent(panelPrincipal);
	    layout.setComponentAlignment(panelPrincipal, Alignment.BOTTOM_CENTER );		
	    panelPrincipal.setWidth(null);
	    panelPrincipal.setStyleName("mytheme");
	    
		final FormLayout loginLayout = new FormLayout();
		loginLayout.setWidth(1100, Unit.PIXELS);
	
		HorizontalLayout layout2 = new HorizontalLayout();
		HorizontalLayout layout3 = new HorizontalLayout();
		
		sPreciosApartamentos.add(0,"Todo");
		ciudadesApartamentos.add(0,"Todo");
		
		cbPrecios.setItems(sPreciosApartamentos);
		cbPrecios.setSelectedItem("Todo");
		cbPrecios.setEmptySelectionAllowed(false);
		
		cbCiudades.setItems(ciudadesApartamentos);
		cbCiudades.setSelectedItem("Todo");
		cbCiudades.setEmptySelectionAllowed(false);
		
		cbHabitaciones.setItems("Todo","0","1","2","3","4","5","6","7","8","9");
		cbHabitaciones.setSelectedItem("Todo");
		cbHabitaciones.setEmptySelectionAllowed(false);
		
		layout2.addComponent(cbCiudades);
		layout2.addComponent(cbPrecios);
		layout2.addComponent(cbHabitaciones);
		
		Button btnFiltrar = new Button("Buscar");
		
		btnFiltrar.addClickListener(e -> {
			if(listaApartamentos != null)
				listaApartamentos.clear();
			Grid<Apartamento> tablaApartamentos = new Grid<>();
			tablaApartamentos.clearSortOrder();
			String sPrecio = cbPrecios.getSelectedItem().toString();
			String sCiudad = cbCiudades.getSelectedItem().toString();
			String sHabit = cbHabitaciones.getSelectedItem().toString();
			Date fechaEntrada; //= (fechaInicio != null) ? java.sql.Date.valueOf(fechaInicio.getValue()) : null;
			Date fechaSalida;// =  (fechaFin != null) ? java.sql.Date.valueOf(fechaFin.getValue()) : null;
			sPrecio = sPrecio.substring(9, sPrecio.length()-1);
			sCiudad = sCiudad.substring(9, sCiudad.length()-1);
			sHabit = sHabit.substring(9, sHabit.length()-1);
			if(fechaInicio.isEmpty() || fechaFin.isEmpty())
				Notification.show("Compruebe que ha introducido una fecha con formato dd/mm/aa válido");
			else {
				fechaEntrada = java.sql.Date.valueOf(fechaInicio.getValue());
				fechaSalida =  java.sql.Date.valueOf(fechaFin.getValue());
				if(fechaEntrada.after(fechaSalida) || (fechaEntrada.before(hoy) || fechaSalida.before(hoy))
						|| fechaEntrada.equals(fechaSalida)) {
					Notification.show("Error en las fechas. Introduzca un intervalo válido");
				}
				else {
					if(usuario == null)
						listaApartamentos = apartamentoService.findApartamentos(sPrecio, sCiudad, sHabit, fechaEntrada, fechaSalida, usuario);
					else
						listaApartamentos = apartamentoService.findApartamentos(sPrecio, sCiudad, sHabit, fechaEntrada, fechaSalida, usuario);
					
					tablaApartamentos.setHeightMode(HeightMode.UNDEFINED);
					tablaApartamentos.setBodyRowHeight(200);
					tablaApartamentos.setItems(listaApartamentos);
					tablaApartamentos.setWidth("900");
					tablaApartamentos.setHeight("600px");
					
					tablaApartamentos.addColumn(p ->new ExternalResource(p.getFoto1()),new ImageRenderer()).setCaption("Imagen").setWidth(200).setResizable(false);
					tablaApartamentos.addColumn(Apartamento::getNombre).setCaption("Nombre").setResizable(false);
					tablaApartamentos.addColumn(Apartamento::getCiudad).setCaption("Ciudad").setResizable(false);
					tablaApartamentos.addColumn(event -> "Informacion", new ButtonRenderer(clickEvent-> { 
						Apartamento a = ((Apartamento) clickEvent.getItem());
						getUI().getNavigator().navigateTo(ApartamentoView.VIEW_NAME + '/'+String.valueOf(a.getId()) + '/'+ fechaEntrada + '/'+ fechaSalida);
						
						})).setCaption("Información").setResizable(false);
					
					layout3.removeAllComponents();//Borramos la busqueda anterior
					layout3.addComponent(tablaApartamentos);
				}
			}
		});
		
		layout2.addComponent(fechaInicio);
		layout2.addComponent(fechaFin);
		layout2.addComponent(btnFiltrar);
		loginLayout.addComponents(layout2);
		loginLayout.addComponent(layout3);
		layout.setStyleName("mytheme");
		panelPrincipal.setContent(loginLayout);
		Responsive.makeResponsive(layout);
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		usuario = null;
		if(SecurityUtils.isLoggedIn())
			usuario = SecurityUtils.LogedUser();
	}
	
	
}
