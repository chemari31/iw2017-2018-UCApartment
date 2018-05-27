/**
 * 
 */
package es.uca.iw.Ucapartment;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.Ucapartment.Administracion.ApartamentosView;
import es.uca.iw.Ucapartment.Administracion.FacturacionView;
import es.uca.iw.Ucapartment.Administracion.ReservasView;
import es.uca.iw.Ucapartment.Administracion.UsuariosView;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoManagementView;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoView;
import es.uca.iw.Ucapartment.Usuario.MiPerfilView;
import es.uca.iw.Ucapartment.Usuario.MisReserva;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.security.LoginScreen;
import es.uca.iw.Ucapartment.security.RegistroScreen;
import es.uca.iw.Ucapartment.security.SecurityUtils;



@SpringViewDisplay
public class MainScreen extends VerticalLayout implements ViewDisplay {

	private Panel springViewDisplay;
	
	@Override
    public void attach() {
        super.attach();
        this.getUI().getNavigator().navigateTo(Home.VIEW_NAME);
    }
	 
	@PostConstruct
	void init() {
		
		final VerticalLayout root = new VerticalLayout();
		HorizontalLayout menuSuperior = new HorizontalLayout();
		final Image image = new Image();
		MenuBar menuDesplegable = new MenuBar();
		menuDesplegable.setWidth(100.0f, Unit.PERCENTAGE);
		
		root.setSizeFull();
		
		// Creamos la cabecera 
		//root.addComponent(new Label("This is the session: " + VaadinSession.getCurrent()));
		//root.addComponent(new Label("This is the UI: " + this.toString()));
		
		Button logoutButton = new Button("Logout", event -> logout());
		logoutButton.setStyleName(ValoTheme.BUTTON_LINK);
		//root.addComponent(logoutButton);

		// Creamos la barra de navegación
		/*final CssLayout navigationBar = new CssLayout();*/
		menuSuperior.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		menuSuperior.addComponent(createNavigationButton("Inicio", Home.VIEW_NAME));
		
		MenuItem gestion = menuDesplegable.addItem("Gestión", null, null);
		if(SecurityUtils.hasRole("ADMINISTRADOR")) {
			gestion.addItem("Usuarios", null, new MenuBar.Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					getUI().getNavigator().navigateTo(UsuariosView.VIEW_NAME);
					
				}
			});
			gestion.addItem("Apartamentos", null, new MenuBar.Command() {
				
				@Override
				public void menuSelected(MenuItem selectedItem) {
					getUI().getNavigator().navigateTo(ApartamentosView.VIEW_NAME);
					
				}
			});
		}
		gestion.addItem("Reservas", null, new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getUI().getNavigator().navigateTo(ReservasView.VIEW_NAME);
				
			}
		});
		gestion.addItem("Facturación", null, new MenuBar.Command() {
			
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getUI().getNavigator().navigateTo(FacturacionView.VIEW_NAME);
				
			}
		});
		
		//gestion.addItem("Apartamentos", null, createNavigationButton("ApartamentosView", ApartamentosView.VIEW_NAME));
		//gestion.addItem("Reservas", null, createNavigationButton("ReservasView", ReservasView.VIEW_NAME));
		
		if(SecurityUtils.isLoggedIn()) {
			menuSuperior.addComponent(createNavigationButton("Mi perfil", MiPerfilView.VIEW_NAME));
			menuSuperior.addComponent(createNavigationButton("Mis apartamentos", ApartamentoManagementView.VIEW_NAME));
			menuSuperior.addComponent(createNavigationButton("Mis Reserva", MisReserva.VIEW_NAME));
			if(SecurityUtils.hasRole("ADMINISTRADOR") || SecurityUtils.hasRole("GERENTE"))
				menuSuperior.addComponent(menuDesplegable);
			Usuario user = SecurityUtils.LogedUser();

			try{
				image.setSource(new ExternalResource(user.getFoto1()));
			}catch(Exception e) {image.setSource(new ExternalResource("/perfiluser/null.png"));}
			image.setWidth(100, Unit.PIXELS);
			image.setHeight(100, Unit.PIXELS);
			menuSuperior.addComponent(image);

			menuSuperior.addComponent(new Label("Hola, "+user.getUsername()));
			menuSuperior.addComponent(logoutButton);
		}
		else {
			
			menuSuperior.addComponent(createNavigationButton("Iniciar sesión", LoginScreen.VIEW_NAME));
			menuSuperior.addComponent(createNavigationButton("Registro", RegistroScreen.VIEW_NAME));
		}
		
		root.addComponent(menuSuperior);
		root.setComponentAlignment(menuSuperior, Alignment.BOTTOM_CENTER);

		// Creamos el panel
		springViewDisplay = new Panel();
		springViewDisplay.setSizeFull();
		root.addComponent(springViewDisplay);
		root.setExpandRatio(springViewDisplay, 1.0f);
		addComponent(root);
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
	public void showView(View view) {
		springViewDisplay.setContent((Component) view);
	}

	
	private void logout() {
		getUI().getPage().reload();
		getSession().close();
	}
}
