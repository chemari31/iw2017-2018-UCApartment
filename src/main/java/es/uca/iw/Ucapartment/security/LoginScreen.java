package es.uca.iw.Ucapartment.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.Ucapartment.Home;
import es.uca.iw.Ucapartment.MainScreen;
import es.uca.iw.Ucapartment.WelcomeView;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;

import com.vaadin.ui.CustomComponent;


@SpringView(name = LoginScreen.VIEW_NAME)
public class LoginScreen extends VerticalLayout implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME = "loginScreen";
	
	// Campos del formulario de login
	TextField nombreUsuario = new TextField("Nombre de usuario");
	PasswordField password = new PasswordField("Contraseña");
	
	Button sesion = new Button("Iniciar Sesion");
	
	@Autowired
    MainScreen mainScreen;
	
	// El objeto binder se utiliza para validar los campos
	Binder<Usuario> binder = new Binder<>(Usuario.class);
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UsuarioService service;
	
	@Autowired
	public LoginScreen() {
	    
    }
	
	@PostConstruct
	public void init () {
		VerticalLayout layout = new VerticalLayout();
		Panel loginPanel = new Panel("<h1 style='color:blue; text-align: center;'>Login</h1>");
		loginPanel.setWidth("600px");
	    loginPanel.setHeight("500px");
	    layout.addComponent(loginPanel);
	    layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER);
        //setMargin(true);
        //setSpacing(true);
	    
	    loginPanel.setHeight(100.0f, Unit.PERCENTAGE);
		loginPanel.setWidth(null);
		
		
		// Create a layout inside the panel
		final FormLayout loginLayout = new FormLayout();
		// Add some components inside the layout
		loginLayout.setWidth(500, Unit.PIXELS);
		loginLayout.addComponent(nombreUsuario);
		loginLayout.addComponent(password);
		
		loginLayout.setMargin(true);
		
		// set required indicator for text fields
		nombreUsuario.setRequiredIndicatorVisible(true);
		password.setRequiredIndicatorVisible(true);
		
		// Con los binder lo que hacemos es validar los datos introducidos
		binder.forField(nombreUsuario)
		.asRequired("El campo Nombre de usuario es obligatorio")
		.bind(Usuario::getNombreUsuario, Usuario::setNombreUsuario);
		
		binder.forField(password)
		.asRequired("El campo Contraseña es obligatorio")
		.withValidator(new StringLengthValidator("El campo Contraseña ha de tener una longitud "
				+" mínima de 6 caracteres y máximo 12",6,12))
		.bind(Usuario::getPassword, Usuario::setPassword);
		

        Button login = new Button("Iniciar sesión", evt -> {
        	if(binder.isValid()) { // Si todos los campos son validos
        		String pword = password.getValue();
                password.setValue("");
                System.out.println(nombreUsuario.getValue()+" "+pword);
                
	            if (!login(nombreUsuario.getValue(), pword)) {
	            	if(!service.loadUserByUsername(nombreUsuario.getValue()).isAccountNonExpired())
	            		Notification.show("Su cuenta está bloqueada. Contacta con un administrador");
	            	else {
	                    Notification.show("Error en el login. Introduzca de nuevo las credenciales");
	                    nombreUsuario.focus();
	            	}
	            }
        	}
        	else
        		Notification.show("Comprueba los datos introducidos");
            
        });
        
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
             
        // Layout Horizontal para poner los botones en una misma fila
        HorizontalLayout botones = new HorizontalLayout();
        
        botones.addComponent(login);
        
        loginLayout.addComponent(botones);
        
        loginPanel.setContent(loginLayout);
		
		addComponent(layout);
	}

	private boolean login(String username, String password) {
		try {
			Authentication token = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			// Reinitialize the session to protect against session fixation
			// attacks. This does not work with websocket communication.
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			SecurityContextHolder.getContext().setAuthentication(token);
			System.out.println(token);
			
			// Show the home view
			getUI().getPage().reload();
			
			return true;
		} catch (AuthenticationException ex) {
			System.out.println(ex);
			return false;
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	
}
