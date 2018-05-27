package es.uca.iw.Ucapartment.security;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
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

@SpringView(name = RegistroScreen.VIEW_NAME)
public class RegistroScreen extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "registroScreen";
	
	private final UsuarioService usuarioService;
	
	// Campos del formulario
	TextField nombreUsuario = new TextField("Nombre de usuario");
	PasswordField password = new PasswordField("Contraseña");
	TextField nombre = new TextField("Nombre");
	TextField apellidos = new TextField("Apellidos");
	TextField dni = new TextField("DNI");
	TextField email = new TextField("Correo electrónico");
	
	// Mediante el objeto binder validamos los campos
	Binder<Usuario> binder = new Binder<>(Usuario.class);
	
	@Autowired
	public RegistroScreen(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	@PostConstruct
	void init() {
		VerticalLayout layout = new VerticalLayout();
		Panel registerPanel = new Panel("Registro");
		registerPanel.setWidth("600px");
		registerPanel.setHeight("500px");
	    layout.addComponent(registerPanel);
	    layout.setComponentAlignment(registerPanel, Alignment.BOTTOM_CENTER);

	    registerPanel.setHeight(100.0f, Unit.PERCENTAGE);
	    registerPanel.setWidth(null);
			
		// Create a layout inside the panel
		final FormLayout registerLayout = new FormLayout();
		// Add some components inside the layout
		registerLayout.setWidth(500, Unit.PIXELS);
		registerLayout.addComponent(nombreUsuario);
		registerLayout.addComponent(password);
		registerLayout.addComponent(email);
		registerLayout.addComponent(nombre);
		registerLayout.addComponent(apellidos);
		registerLayout.addComponent(dni);
		
		registerLayout.setMargin(true);
		
		// Con esto se muestra un * rojo en los campos deseados indicando que es obligatorio
		nombreUsuario.setRequiredIndicatorVisible(true);
		password.setRequiredIndicatorVisible(true);
		email.setRequiredIndicatorVisible(true);
		dni.setRequiredIndicatorVisible(true);
		
		// Con los binder lo que hacemos es validar los datos introducidos
		binder.forField(nombreUsuario)
		.asRequired("El campo Nombre de usuario es obligatorio")
		.bind(Usuario::getNombreUsuario,Usuario::setNombreUsuario);
		
		binder.forField(password)
		.asRequired("El campo Contraseña es obligatorio")
		.withValidator(new StringLengthValidator("El campo Contraseña ha de tener una longitud "
				+" mínima de 6 caracteres y máximo 12",6,12))
		.bind(Usuario::getPassword, Usuario::setPassword);
		
		binder.forField(dni)
		.asRequired("El campo DNI es obligatorio")
		.withValidator(new StringLengthValidator("El campo DNI ha de tener una longitud de "
					+ "9 caracteres",9,9))
		.bind(Usuario::getDni,Usuario::setDni);
		
		binder.forField(email)
		.asRequired("El campo Correo electrónico es obligatorio")
		.withValidator(new EmailValidator("Introduce un correo electrónico válido"))
		.bind(Usuario::getEmail, Usuario::setEmail);
			
		
		// Botón de registro
        Button registro = new Button("Registro", evt -> {
        	if(binder.isValid()) { // Si todas las validaciones se han pasado
        		
        		String sNombreUsuario, sPassword, sNombre, sApellidos, sDNI, sEmail;
        		// Obtenemos todos los valores del formulario y creamos el usuario
                sNombreUsuario = nombreUsuario.getValue();
                sPassword = password.getValue();
                sEmail = email.getValue();
                sNombre = nombre.getValue();
                sApellidos = apellidos.getValue();
                sDNI = dni.getValue();
                
                Usuario usuario = new Usuario(sNombre, sApellidos, sDNI, sEmail, sNombreUsuario, sPassword);

                // Llamamos a la funcion registro definidad en VaadinUI con el usuario
                // la cual comprueba que el usuario con ese username y email no existe en la BD ya
               if(!registro(usuario)) {
                	Notification.show("Nombre de usuario "+usuario.getNombreUsuario()
                	+ ", Correo electrónico "+usuario.getEmail()+" o DNI "+usuario.getDni()+" ya existentes");
                    nombreUsuario.focus(); // Esto lo que hace es que el cursor para introducir texto
                    						// aparezca al fallar directamente en el campo de username
                }
        	}
        	else // Si ha fallado la validación muestra un mensajito
        		Notification.show("Comprueba los datos introducidos");
        });
        
        // Creamos un layout horizontal para añadir los botones de registro y atras
        HorizontalLayout botones = new HorizontalLayout();
        registro.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        botones.addComponent(registro);
        registerLayout.addComponent(botones); // Con esto añadimos el layout creado a la ventana

        registerPanel.setContent(registerLayout);
		addComponent(layout);
	}
	
	private boolean registro(Usuario usuario) {
		boolean valido = false;
		if(!usuarioService.nombreUsuarioExistente(usuario.getNombreUsuario())
				&& !usuarioService.emailExistente(usuario.getEmail()) 
				&& !usuarioService.dniExistente(usuario.getDni())) {
			usuarioService.save(usuario); // Se guarda el usuario en la BD
			//springViewDisplay.setContent((Component) view);
				valido = true;

				getUI().getNavigator().navigateTo(LoginScreen.VIEW_NAME);
		}
		return valido;

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
