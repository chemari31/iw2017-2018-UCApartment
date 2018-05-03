package es.uca.iw.Ucapartment;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoRepository;
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
		HorizontalLayout layout2 = new HorizontalLayout();
		
		Panel loginPanel = new Panel("<h1 style='color:blue; text-align: center;'>UCApartment</h1>");
		loginPanel.setWidth("800px");
	    loginPanel.setHeight("800px");
	    
	    layout.addComponent(loginPanel);
	    
	    layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER );
		
		loginPanel.setWidth(null);
		
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
		
		NativeSelect<String> select3 = new NativeSelect<>("Precio");
		
		select3.setItems("Todo","20","30","40");
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
						Apartamento apar = repo.findByNombre((select3.getValue()));
					}
				}
				else
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByHabitacion(Integer.parseInt(select2.getValue()));
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
				}
				else
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByCiudadAndHabitacion(select.getValue(), Integer.parseInt(select2.getValue()));
					}
				}
			}
			
			filter[0] = select.getValue();
			filter[1] = select2.getValue();
			filter[2] = select3.getValue();
		}
				});
			
		loginLayout.addComponent(select);
		loginLayout.addComponent(select2);
		loginLayout.addComponent(select3);
		loginLayout.addComponent(input);
		
		
		try{
			for(Apartamento apartamentoa : apartamentoo)
			{
				//loginLayout.addComponent(new Image(null,
				        //new ClassResource(apartamentoa.getImage())));
				//loginLayout.addComponent(new Label(apartamentoa.getNombre()));
				loginLayout.addComponent(new Button("Reservar"));
				loginLayout.addComponent(new Button("Informacion"));
			}
		}catch(Exception e) {
			
			for(Apartamento apartamentoa : repo.findAll())
			{
				//loginLayout.addComponent(new Image(null,
				        //new ClassResource(apartamentoa.getImage())));
				//loginLayout.addComponent(new Label(apartamentoa.getNombre()));
				loginLayout.addComponent(new Button("Reservar"));
				loginLayout.addComponent(new Button("Informacion"));
			}
			
		}
		
		
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	

}
