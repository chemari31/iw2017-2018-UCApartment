/**
 * 
 */
package es.uca.iw.Ucapartment;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.annotation.PostConstruct;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
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
		Button politica = new Button("Política de empresa");


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
			System.out.printf("ENTRO EN EL HOME");
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
			politica.addClickListener(event ->{
				generarpolitica();
			});
			menuSuperior.addComponent(politica);
		}
		else {
			
			menuSuperior.addComponent(createNavigationButton("Iniciar sesión", LoginScreen.VIEW_NAME));
			menuSuperior.addComponent(createNavigationButton("Registro", RegistroScreen.VIEW_NAME));
			politica.addClickListener(event ->{
				generarpolitica();
			});
			menuSuperior.addComponent(politica);
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
	
	
	public void generarpolitica()
	{	
		try {
			Document doc = new Document();
			FileOutputStream fichero = new FileOutputStream("Politica de la empresa.pdf");
			
			PdfWriter.getInstance(doc, fichero).setInitialLeading(40);
			doc.open();
			
			doc.add(new Paragraph("Política de nuestra empresa"));
			doc.add(new Paragraph("_________________________________________________________"));
			doc.add(new Paragraph("Nuestra empresa sigue las siguientes reglas a la hora de proceder ante una cancelación:"));
			doc.add(new Paragraph("	- Si se realiza una cancelacíon después de realizar el pago, no se te devolverá nada."));
			doc.add(new Paragraph(" - Si se cancela una reserva antes de realizar el pago no pasará nada, y el apartamento volverá a estar disponible."));
			doc.add(new Paragraph(" - Si se modifica la fecha de la reserva se incrementará el precio en esta un 10%."));
			doc.add(new Paragraph("	- La empresa se quedará un 5% de todas las reservas."));
			doc.add(new Paragraph("	- El método de pago se realizará mediante una tarjeta de 16 dígitos."));
			
			doc.add(new Paragraph("_________________________________________________________"));
			doc.add(new Paragraph("	La empresa no se hace responsable de los daños ocasionados por los usuarios en los apartamentos. "));
			doc.add(new Paragraph("	La empresa no se hace responsable de objetos perdidos durante alguna reserva. "));
			doc.add(new Paragraph("	Y la empresa se reserva el derecho a modificar cualquier dato así de gestionar cualquier apartamento, perfil e incidencia que surga."));



			
			doc.close();
			Notification.show("Política de empresa generada.");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Notification.show("Se produjo un error generando el documento.");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Notification.show("Se produjo un error generando el documento.");
		}
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
