package es.uca.iw.Ucapartment;


import java.sql.Date;
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
import es.uca.iw.Ucapartment.Apartamento.ApartamentoView;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaRepository;
import es.uca.iw.Ucapartment.Usuario.PopupPago;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.security.SecurityUtils;


@Theme("mytheme")
@SpringView(name = Home.VIEW_NAME)
public class Home extends VerticalLayout implements View {
	
	
	private static final long serialVersionUID = 1L;
	
	TextField home = new TextField();
	List<Apartamento> apartamento = new ArrayList<>();//Lista de apartamentos segun los filtros de fechas
	List<Apartamento> noapartamento = new ArrayList<>();//Lista de apartamentos segun los filtros de fechas
	List<String> ciudad = new ArrayList<>();
	List<String> precio = new ArrayList<>();
	List<Double> precioDouble = new ArrayList<>();
	List<Apartamento> apartFinal = new ArrayList<>();//Lista de apartamentos finales segun de todos los filtros
	List<Estado> listEstado = new ArrayList<>();
	private List<Apartamento> listApart = null;//Lista de apartamentos segun los filtros de Ciudad, habitacion y precio
	private List<Apartamento> ApartFilter = null;//Lista apartamento temporal para los filtros
	private List<Reserva> listReserva = null;//Lista de todas las reservas
	private Usuario user;
	
	
	public static final String VIEW_NAME = "home";
	
	private Panel loginPanel;
	
	@Autowired
	ApartamentoRepository repo;
	
	@Autowired
	ReservaRepository repoReserva;
	@Autowired
	EstadoRepository repoEstado;
	
	Estado estado;
	
	HorizontalLayout layout3 = new HorizontalLayout();
	
	
	
	@PostConstruct
	public void init() {
		
		
		final Button input = new Button("Buscar");
		input.setStyleName("mystyle");
		input.addStyleName("mystyle");
		
        listApart = repo.findAll();  
        ApartFilter = repo.findAll();
        listReserva =  repoReserva.findAll();
        
        //Layout principales
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout2;
		
		
		
		
		
		//Panel principal
		loginPanel = new Panel("<h1 style='color:blue; text-align: center;'>UCApartment</h1>");
		loginPanel.setWidth("800px");
	    loginPanel.setHeight("800px");
	    layout.addComponent(loginPanel);
	    layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER );		
		loginPanel.setWidth(null);
		loginPanel.setStyleName("mytheme");
		Responsive.makeResponsive(loginPanel);
		
		//Declaracion de los dos calendario para entrada y salida
		DateField ida = new DateField("Entrada");
		DateField vuelta = new DateField("Salida");
		ida.setValue(LocalDate.now());
		Date hoy = java.sql.Date.valueOf(ida.getValue());
		vuelta.setValue(LocalDate.now().plusDays(1));

		final FormLayout loginLayout = new FormLayout();
		loginLayout.setWidth(1020, Unit.PIXELS);
	
		layout2 = new HorizontalLayout();
		
		
		//Cargando los filtros de busqueda
		for(Apartamento i : ApartFilter)
		{
			
			precioDouble.add(i.getPrecio());
		
			ciudad.add(i.getCiudad());
			
			precio.add(Double.toString(i.getPrecio()));
			
		}
		
		ciudad.add("Todo");
		precio.add("Todo");
		
		//Metodos para seleccionar los filtros sin repeticiones
		List<String> listDistinctCiudad = ciudad.stream().distinct().collect(Collectors.toList());
		List<String> listDistinctPrecio = precio.stream().distinct().collect(Collectors.toList());
		
		//Mostrar filtros
		ComboBox<String> Ciudad = new ComboBox<>("Ciudad");
		ComboBox<String> Precio = new ComboBox<>("Precio");
		
		Collections.sort(listDistinctPrecio);
		Collections.sort(listDistinctCiudad);
		Ciudad.setItems(listDistinctCiudad);
		Precio.setItems(listDistinctPrecio);
		Ciudad.setValue("Todo");
		Precio.setValue("Todo");
		layout2.addComponent(Ciudad);
		layout2.addComponent(Precio);
		
		//Select para las habitaciones
		NativeSelect<String> select2 = new NativeSelect<>("Habitaciones");
		
		select2.setItems("Todo","1", "2","3","4","5","6","7","8");
		select2.setValue("Todo");
		
		//Funcion al pulsar el boton "Buscar"
		input.addClickListener(new ClickListener() 
		{
		
			private static final long serialVersionUID = 1L;

		public void buttonClick(ClickEvent event) 
		{
			
			apartFinal.clear();//Limpiamos los apartamentos finales para que no se repitan
			apartamento.clear();
			noapartamento.clear();
			
			//Condiciones para buscar los apartamentos según su Ciudad, Habitacion y Precio
			if(Ciudad.getValue() == "Todo")
			{
				if(select2.getValue() == "Todo")
				{
					if(Precio.getValue() == "Todo")
					{
						listApart = repo.findAll();
					}
					else
					{
						listApart = repo.findByPrecio(Double.parseDouble(Precio.getValue()));
					}
				}
				else
				{
					if(Precio.getValue() == "Todo")
					{
						listApart = repo.findByHabitacion(Integer.parseInt(select2.getValue()));
					}
					else
					{
						listApart = repo.findByHabitacionAndPrecio(Integer.parseInt(select2.getValue()), Double.parseDouble(Precio.getValue()));
					}
					
				}
			}
			else
			{
				if(select2.getValue() == "Todo")
				{
					if(Precio.getValue() == "Todo")
					{
						listApart = repo.findByCiudad(Ciudad.getValue());
					}
					else
					{
						listApart = repo.findByCiudadAndPrecio(Ciudad.getValue(), Double.parseDouble(Precio.getValue()));
					}
				}
				else
				{
					if(Precio.getValue() == "Todo")
					{
						listApart = repo.findByCiudadAndHabitacion(Ciudad.getValue(), Integer.parseInt(select2.getValue()));
					}
					else
					{
						listApart = repo.findByCiudadAndHabitacionAndPrecio(Ciudad.getValue(), Integer.parseInt(select2.getValue()), Double.parseDouble(Precio.getValue()));
					}
				}
			}
			
			//Quitamos los apartamentos repetidos
			
			
			
			listApart = listApart.stream().distinct().collect(Collectors.toList());
			
			Date entrada = java.sql.Date.valueOf(ida.getValue());
			Date salida = java.sql.Date.valueOf(vuelta.getValue());
			
			
			//Seleccionamos todos los apartamentos sin reservar
			for(Apartamento a : ApartFilter)
			{
				if(repoReserva.findByApartamento(a).size() == 0)
				{
					if((entrada.compareTo(hoy)>=0 && salida.compareTo(entrada) > 0))
					{
						apartamento.add(a);
						
					}
					
				}
			}
			
			
			
			
			//Apartamentos disponible segun la fecha de entrada y salida seleccionada
			for(Reserva r : listReserva)
			{
				if(!(entrada.compareTo(r.getFechaInicio())>=0 && salida.compareTo(r.getFechaFin()) <= 0))
				{
					if(!(entrada.compareTo(r.getFechaInicio())<=0 && salida.compareTo(r.getFechaInicio()) > 0))
					{
						if(!(entrada.compareTo(r.getFechaInicio())>=0 && entrada.compareTo(r.getFechaFin()) < 0))
						{
							
							if((entrada.compareTo(hoy)>=0 && salida.compareTo(entrada) > 0))
							{
								
								for(Apartamento a : noapartamento)
								{
									if(a.getId() != r.getApartamento().getId())
									{
										apartamento.add(r.getApartamento());
									}
									else
									{
										apartamento = apartamento.stream().distinct().collect(Collectors.toList());
										apartamento.remove(r.getApartamento());
									}
								}
								if(noapartamento.isEmpty())
									apartamento.add(r.getApartamento());
										
							}
							else
							{
								noapartamento.add(r.getApartamento());
								for(Apartamento a : noapartamento)
								{
									if(a.getId() != r.getApartamento().getId())
									{
										apartamento.add(r.getApartamento());
									}
									else
									{
										apartamento = apartamento.stream().distinct().collect(Collectors.toList());
										apartamento.remove(r.getApartamento());
									}
								}
							}
						}
						else
						{
							noapartamento.add(r.getApartamento());
							for(Apartamento a : noapartamento)
							{
								if(a.getId() != r.getApartamento().getId())
								{
									apartamento.add(r.getApartamento());
								}
								else
								{
									apartamento = apartamento.stream().distinct().collect(Collectors.toList());
									apartamento.remove(r.getApartamento());
								}
								for(Reserva rr : repoEstado.findReservaByEstado(Valor.CANCELADA))
								{
									if(r.getId() == rr.getId())
									{
										apartamento.add(r.getApartamento());
									}
								}
							}
						}
							
					}
					else
					{
						noapartamento.add(r.getApartamento());
						for(Apartamento a : noapartamento)
						{
							if(a.getId() != r.getApartamento().getId())
							{
								apartamento.add(r.getApartamento());
							}
							else
							{
								apartamento = apartamento.stream().distinct().collect(Collectors.toList());
								apartamento.remove(r.getApartamento());
							}
							for(Reserva rr : repoEstado.findReservaByEstado(Valor.CANCELADA))
							{
								if(r.getId() == rr.getId())
								{
									apartamento.add(r.getApartamento());
								}
							}
						}
					}
				}
				else
				{
					noapartamento.add(r.getApartamento());
					for(Apartamento a : noapartamento)
					{
						if(a.getId() != r.getApartamento().getId())
						{
							apartamento.add(r.getApartamento());
						}
						else
						{
							apartamento = apartamento.stream().distinct().collect(Collectors.toList());
							apartamento.remove(r.getApartamento());
						}
						for(Reserva rr : repoEstado.findReservaByEstado(Valor.CANCELADA))
						{
							if(r.getId() == rr.getId())
							{
								apartamento.add(r.getApartamento());
							}
						}
					}
				}
						
			}
			
			for( Reserva r : repoReserva.findByFechaInicioAndFechaFin(entrada, salida))
			{
				for(Reserva rr : repoEstado.findReservaByEstado(Valor.CANCELADA))
				{
					if(r.getId() == rr.getId())
					{
						apartamento.add(r.getApartamento());
					}
				}
			}
			
			apartamento = apartamento.stream().distinct().collect(Collectors.toList());
			
			//Unimos los apartamentos buscado por los filtros de Ciudad, Habitacion y Precio con los
			//apartamentos disponible en las fechas seleccionadas
			for(Apartamento a : listApart)
			{
				for(Apartamento b : apartamento)
				{
					if(a.getId() == b.getId())
					{
						if(SecurityUtils.isLoggedIn())
						{
							if(a.getUsuario().getId() != user.getId())
							{
								System.out.println("entro");
								apartFinal.add(a);
							}		
						}
						else
						{
							apartFinal.add(a);
						}
						
						
					}
						
				}
			}
			
			//Nuestros apartamentos finales 
			apartFinal = apartFinal.stream().distinct().collect(Collectors.toList());
			
			
			
			//Grid de busqueda de apartamento
			Grid<Apartamento> filter2 = new Grid<>();
			filter2.setHeightMode(HeightMode.UNDEFINED);
			filter2.setBodyRowHeight(200);
			filter2.setItems(apartFinal);
			filter2.setWidth("900");
			filter2.setHeight("600px");
			//filter2.getSelectionModel().get
			//Object select = ((SingleSelectionModel) filter2.getSelectionModel()).getSelectedItem();
			
			filter2.addColumn(p ->new ExternalResource(p.getFoto1()),new ImageRenderer()).setCaption("Imagen").setWidth(200).setResizable(false);
			filter2.addColumn(Apartamento::getNombre).setCaption("Nombre").setResizable(false);
			filter2.addColumn(Apartamento::getCiudad).setCaption("Ciudad").setResizable(false);
			filter2.addColumn(e -> "Informacion", new ButtonRenderer(clickEvent-> { 
				Apartamento a = ((Apartamento) clickEvent.getItem());
				getUI().getNavigator().navigateTo(ApartamentoView.VIEW_NAME + '/'+String.valueOf(a.getId()) + '/'+ entrada + '/'+ salida);
				
				})).setCaption("Información").setResizable(false);
			
			
			layout3.removeAllComponents();//Borramos la busqueda anterior
			layout3.addComponent(filter2);
			//layout2.addComponent(createNavigationButton("Informacion", ApartamentoView.VIEW_NAME + '/'+String.valueOf(((AbstractComponent) apartamento).getId())));	
		}
				});
		
		

		//Añadimos los componentes de nuestro Home
			//Label prueba = new Label("prueba");
		layout2.addComponent(select2);
		layout2.addComponent(ida);
		layout2.addComponent(vuelta);
		layout2.addComponent(input);
		loginLayout.addComponents(layout2);
		loginLayout.addComponent(layout3);
		layout.setStyleName("mytheme");
		loginPanel.setContent(loginLayout);
		Responsive.makeResponsive(layout);
		addComponent(layout);
		
		
		
		
	}
	

	private Button createNavigationButton(String caption, final String viewName) {
		Button button = new Button(caption);
		button.addStyleName(ValoTheme.BUTTON_SMALL);
		// If you didn't choose Java 8 when creating the project, convert this
		// to an anonymous listener class
		button.addClickListener(event -> getUI().getNavigator().navigateTo(viewName));
		return button;
	}
	@Override
	public void enter(ViewChangeEvent event) {
		
		try {
			user = SecurityUtils.LogedUser();
		}catch(Exception e) {}
		
		//init();
		// TODO Auto-generated method stub
		
	}
	

}
