package es.uca.iw.Ucapartment;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Layout;
import org.springframework.beans.factory.annotation.Autowired;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateTimeField;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoManagementView;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoRepository;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoView;
import es.uca.iw.Ucapartment.Usuario.PopupPago;
import es.uca.iw.Ucapartment.security.LoginScreen;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = Home.VIEW_NAME)
public class Home extends VerticalLayout implements View {
	
	TextField home = new TextField();
	List<Apartamento> apartamentoo = null;
	
	public static final String VIEW_NAME = "home";
	String [] filter;
	
	@Autowired
	ApartamentoRepository repo;
	
	@Autowired
	public Home() { 
		filter = new String[3];
	}
	
	@PostConstruct
	public void init() {
		
		final Button input = new Button("Buscar");
        
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout2;
		
		Panel loginPanel = new Panel("<h1 style='color:blue; text-align: center;'>UCApartment</h1>");
		loginPanel.setWidth("800px");
	    loginPanel.setHeight("800px");	    
	    layout.addComponent(loginPanel);	    
	    layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER );		
		loginPanel.setWidth(null);
		DateTimeField ida = new DateTimeField();
		DateTimeField vuelta = new DateTimeField();
		ida.setValue(LocalDateTime.now());
		PopupPago sub = new PopupPago();
		HorizontalLayout popupHorizontal = new HorizontalLayout();
		VerticalLayout popupVertical = new VerticalLayout();
		Button reservar = new Button("Reservar");
		Label mensaje = new Label("Introduzca la fecha de ida y vuelta");
		
		final FormLayout loginLayout = new FormLayout();
		// Add some components inside the layout
		loginLayout.setWidth(500, Unit.PIXELS);
		
		loginPanel.setContent(loginLayout);
		String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		
		/*FileResource resource = new FileResource(new File(basepath +
                "/WEB-INF/images/descarga.png"));
		
		Image image = new Image("Image from file", resource);*/
		
		for(int i = 0; i<3 ;i++)
		{
			filter[i] = "Todo";
		}
		
		NativeSelect<String> select = new NativeSelect<>("Ciudad");
		
		// Add some items
		select.setItems("Todo","Cadiz", "Malaga","Jaen");
		select.setSelectedItem(filter[0]);
			
		NativeSelect<String> select2 = new NativeSelect<>("Habitaciones");

				// Add some items
		select2.setItems("Todo","1", "2","3");
		select2.setSelectedItem(filter[1]);
		
		NativeSelect<String> select3 = new NativeSelect<>("Camas");
		
		select3.setItems("Todo","1","2","3");
		select3.setSelectedItem(filter[2]);
					
		input.addClickListener(new ClickListener() {
		public void buttonClick(ClickEvent event) 
		{
			
			
			if(select.getValue() == "Todo")
			{
				if(select2.getValue() == "Todo")
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findAll();
					}
					else
					{
						apartamentoo = repo.findByCamas(Integer.parseInt(select3.getValue()));
					}
				}
				else
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByHabitacion(Integer.parseInt(select2.getValue()));
					}
					else
					{
						apartamentoo = repo.findByHabitacionAndCamas(Integer.parseInt(select2.getValue()), Integer.parseInt(select3.getValue()));
					}
					
				}
			}
			else
			{
				if(select2.getValue() == "Todo")
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByCiudad(select.getValue());
					}
					else
					{
						apartamentoo = repo.findByCiudadAndCamas(select.getValue(), Integer.parseInt(select3.getValue()));
					}
				}
				else
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByCiudadAndHabitacion(select.getValue(), Integer.parseInt(select2.getValue()));
					}
					else
					{
						apartamentoo = repo.findByCiudadAndHabitacionAndCamas(select.getValue(), Integer.parseInt(select2.getValue()), Integer.parseInt(select3.getValue()));
					}
				}
			}
			
			filter[0] = select.getValue();
			filter[1] = select2.getValue();
			filter[2] = select3.getValue();
		}
				});
		layout2 = new HorizontalLayout();
		layout2.addComponent(select);
		layout2.addComponent(select2);
		layout2.addComponent(select3);
		layout2.addComponent(input);
		loginLayout.addComponents(layout2);
		//loginLayout.addComponent(ida);
		reservar.addClickListener(event -> {
			sub.close();
			
		});
		
		
		try{
			for(Apartamento apartamentoa : apartamentoo)
			{
				//loginLayout.addComponent(new Image(null,
				        //new ClassResource(apartamentoa.getImage())));
				loginLayout.addComponent(new Label(apartamentoa.getNombre()));
				layout2 = new HorizontalLayout();
					layout2.addComponent(new Button("Reservar",event -> {
					
					popupVertical.addComponents(mensaje);
					popupHorizontal.addComponent(ida);
					vuelta.setValue(ida.getValue());
					popupHorizontal.addComponent(vuelta);
					popupVertical.addComponent(popupHorizontal);
					popupVertical.addComponent(reservar);
					sub.setWidth("800px");
					sub.setHeight("600px");
					sub.setPosition(550, 200);
					sub.setContent(popupVertical);
					sub.center();
					UI.getCurrent().addWindow(sub);
					
					
				}));
				layout2.addComponent(new Button("Informacion"));
				loginLayout.addComponent(layout2);
				
			}
		}catch(Exception e) {
			
			for(Apartamento apartamentoa : repo.findAll())
			{
				//loginLayout.addComponent(new Image(null,
				        //new ClassResource(apartamentoa.getImage())));
				loginLayout.addComponent(new Label(apartamentoa.getNombre()));
				layout2 = new HorizontalLayout();
				layout2.addComponent(new Button("Reservar",event -> {
					
					popupVertical.addComponents(new Label("Introduzca la fecha de ida y vuelta"));
					popupHorizontal.addComponent(ida);
					vuelta.setValue(ida.getValue());
					popupHorizontal.addComponent(vuelta);
					popupVertical.addComponent(popupHorizontal);
					popupVertical.addComponent(reservar);
					sub.setWidth("800px");
					sub.setHeight("600px");
					sub.setPosition(550, 200);
					sub.setContent(popupVertical);
					sub.center();
					UI.getCurrent().addWindow(sub);
					
					
				}));
				layout2.addComponent(createNavigationButton("Informacion", ApartamentoView.VIEW_NAME));
				loginLayout.addComponent(layout2);
			}
			
		}
		
		
		addComponent(layout);
		addComponent(popupVertical);
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
		// TODO Auto-generated method stub
		
	}
	

}
