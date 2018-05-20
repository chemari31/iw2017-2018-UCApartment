package es.uca.iw.Ucapartment.Administracion;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.Ucapartment.Usuario.MiPerfilView;
import es.uca.iw.Ucapartment.Usuario.Rol;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = PerfilUsuarioView.VIEW_NAME)
public class PerfilUsuarioView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "perfilUsuarioView";
	
	private UsuarioService usuarioService;
	private Usuario usuario;
	
	// Mediante el objeto binder validamos los campos
	Binder<Usuario> binder = new Binder<>(Usuario.class);
	
	@Autowired
	public PerfilUsuarioView (UsuarioService servicio) {
		this.usuarioService = servicio;
	}
	
	void init() {
		HorizontalLayout datosUsuario = new HorizontalLayout();
		VerticalLayout layoutDerecho = new VerticalLayout();
		VerticalLayout layoutIzquierdo = new VerticalLayout();
		
		Panel panelUsuario = new Panel("Usuario "+usuario.getUsername());
		Panel panelFoto = new Panel("Foto");
		
		panelUsuario.setWidth("630px");
		panelUsuario.setHeight("570px");
		panelFoto.setWidth("320px");
		panelFoto.setHeight("350px");
		
		layoutIzquierdo.addComponent(panelUsuario);
		layoutDerecho.addComponent(panelFoto);
		
		datosUsuario.addComponent(layoutIzquierdo);
		datosUsuario.addComponent(layoutDerecho);
		
		VerticalLayout elementosUsuario = new VerticalLayout();
		
		HorizontalLayout hlNombre = new HorizontalLayout();
		HorizontalLayout hlApellidos = new HorizontalLayout();
		HorizontalLayout hlDNI = new HorizontalLayout();
		HorizontalLayout hlUsername = new HorizontalLayout();
		HorizontalLayout hlEmail = new HorizontalLayout();
		HorizontalLayout hlRol = new HorizontalLayout();
		HorizontalLayout hlCuentaActiva = new HorizontalLayout();
		HorizontalLayout hlBtnCambiosRol = new HorizontalLayout();
		HorizontalLayout hlBtnCambiosActivo = new HorizontalLayout();
		HorizontalLayout hlBtnModificarDatos = new HorizontalLayout();
		
		Label lNombre, lApell, lDNI, lUsername, lEmail, lRol, lAct;
		Label vNombre, vApell, vDNI, vUsername, vEmail, vRol;
		
		lNombre = new Label("Nombre: ");
		lApell = new Label("Apellidos: ");
		lDNI = new Label("DNI: ");
		lUsername = new Label("Nombre de usuario: ");
		lEmail = new Label("Correo electrónico: ");
		lRol = new Label("Rol: ");
		lAct = new Label();
		if(usuario.isAccountNonLocked()) lAct.setCaption("Cuenta activa");
		else lAct.setCaption("Cuenta bloqueada");
		
		vNombre = new Label();
		vApell = new Label(); 
		vDNI = new Label();
		vUsername = new Label();
		vEmail = new Label();
		vRol = new Label();
		vRol.setCaption(usuario.getRol().toString());
		
		vNombre.setCaption(usuario.getNombre());
		vApell.setCaption(usuario.getApellidos());
		vDNI.setCaption(usuario.getDni());
		vUsername.setCaption(usuario.getNombreUsuario());
		vEmail.setCaption(usuario.getEmail());
		
		Button btnCambioRolAnfitrion = new Button("Cambiar a anfitrion");
		Button btnCambioRolGerente = new Button("Cambiar a gerente");
		Button btnCambioRolAdmin = new Button("Cambiar a administrador");
		
		if(vRol.getCaption().equals("ANFITRION"))
			btnCambioRolAnfitrion.setEnabled(false);
		if(vRol.getCaption().equals("GERENTE"))
			btnCambioRolGerente.setEnabled(false);
		if(vRol.getCaption().equals("ADMINISTRADOR"))
			btnCambioRolAdmin.setEnabled(false);
		
		btnCambioRolAnfitrion.addClickListener(event -> {
			usuario.setRol(Rol.ANFITRION);
			usuarioService.save(usuario);
			Notification.show("Se ha modificado el rol del usuario " +usuario.getUsername() + " a ANFITRION.", Notification.Type.TRAY_NOTIFICATION);
			vRol.setCaption(usuario.getRol().toString());
			btnCambioRolAnfitrion.setEnabled(false);
			btnCambioRolGerente.setEnabled(true);
			btnCambioRolAdmin.setEnabled(true);
		});
		
		btnCambioRolGerente.addClickListener(event -> {
			usuario.setRol(Rol.GERENTE);
			usuarioService.save(usuario);
			Notification.show("Se ha modificado el rol del usuario " +usuario.getUsername() + " a GERENTE.", Notification.Type.TRAY_NOTIFICATION);
			vRol.setCaption(usuario.getRol().toString());
			btnCambioRolAnfitrion.setEnabled(true);
			btnCambioRolGerente.setEnabled(false);
			btnCambioRolAdmin.setEnabled(true);
		});
		
		btnCambioRolAdmin.addClickListener(event -> {
			usuario.setRol(Rol.ADMINISTRADOR);
			usuarioService.save(usuario);
			Notification.show("Se ha modificado el rol del usuario " +usuario.getUsername() + " a ADMINISTRADOR.", Notification.Type.TRAY_NOTIFICATION);
			vRol.setCaption(usuario.getRol().toString());
			btnCambioRolAnfitrion.setEnabled(true);
			btnCambioRolGerente.setEnabled(true);
			btnCambioRolAdmin.setEnabled(false);
		});
		
		Button btnBloquear = new Button("Bloquear cuenta");
		Button btnDesbloquear = new Button("Desbloquear cuenta");
		
		if(usuario.isAccountNonLocked()) {
			btnBloquear.setEnabled(true);
			btnDesbloquear.setEnabled(false);
		}
		else{
			btnBloquear.setEnabled(false);
			btnDesbloquear.setEnabled(true);
		}
		
		btnBloquear.addClickListener(event -> {
			usuario.setDesbloqueo(false);
			usuarioService.save(usuario);
			lAct.setCaption("Cuenta bloqueada");
			Notification.show("La cuenta del usuario "+usuario.getUsername() + " ha sido bloqueada.");
			btnBloquear.setEnabled(false);
			btnDesbloquear.setEnabled(true);
		});
		
		btnDesbloquear.addClickListener(event -> {
			usuario.setDesbloqueo(true);
			usuarioService.save(usuario);
			lAct.setCaption("Cuenta activa");
			Notification.show("La cuenta del usuario "+usuario.getUsername() + " ha sido desbloqueada.");
			btnBloquear.setEnabled(true);
			btnDesbloquear.setEnabled(false);
		});
		
		Button btnModificarDatos = new Button("Modificar datos");
		Button btnGuardar = new Button("Guardar");
		btnGuardar.setVisible(false);
		
		TextField tfNombre = new TextField();
		TextField tfApell = new TextField();
		TextField tfDNI = new TextField();
		TextField tfUsername = new TextField();
		TextField tfEmail = new TextField();
		
		// Con esto se muestra un * rojo en los campos deseados indicando que es obligatorio
		tfUsername.setRequiredIndicatorVisible(true);
		tfEmail.setRequiredIndicatorVisible(true);
		tfDNI.setRequiredIndicatorVisible(true);
		
		// Con los binder lo que hacemos es validar los datos introducidos
		binder.forField(tfUsername)
		.asRequired("El campo Nombre de usuario es obligatorio")
		.bind(Usuario::getNombreUsuario,Usuario::setNombreUsuario);
		
		binder.forField(tfDNI)
		.asRequired("El campo DNI es obligatorio")
		.withValidator(new StringLengthValidator("El campo DNI ha de tener una longitud de "
					+ "9 caracteres",9,9))
		.bind(Usuario::getDni,Usuario::setDni);
		
		binder.forField(tfEmail)
		.asRequired("El campo Correo electrónico es obligatorio")
		.withValidator(new EmailValidator("Introduce un correo electrónico válido"))
		.bind(Usuario::getEmail, Usuario::setEmail);
		
		tfNombre.setVisible(false);
		tfApell.setVisible(false);
		tfDNI.setVisible(false);
		tfUsername.setVisible(false);
		tfEmail.setVisible(false);
		
		btnModificarDatos.addClickListener(event -> {
			btnModificarDatos.setVisible(false);
			btnGuardar.setVisible(true);
			vNombre.setVisible(false);
			vApell.setVisible(false);
			vDNI.setVisible(false);
			vUsername.setVisible(false);
			vEmail.setVisible(false);
			
			tfNombre.setVisible(true);
			tfApell.setVisible(true);
			tfDNI.setVisible(true);
			tfUsername.setVisible(true);
			tfEmail.setVisible(true);
			
			tfNombre.setValue(usuario.getNombre());
			tfApell.setValue(usuario.getApellidos());
			tfDNI.setValue(usuario.getDni());
			tfUsername.setValue(usuario.getUsername());
			tfEmail.setValue(usuario.getEmail());
			
		});
		
		btnGuardar.addClickListener(event -> {
			
			if(binder.isValid()) { // Si todas las validaciones se han pasado
				String sUsername, sNombre, sApell, sDNI, sEmail;
				String notificacion = "";
				boolean existeUsuario = false;
				boolean existeEmail = false;
        		// Obtenemos todos los valores del formulario y actualizamos el usuario
                sUsername = tfUsername.getValue();
                sEmail = tfEmail.getValue();
                sNombre = tfNombre.getValue();
                sApell = tfApell.getValue();
                sDNI = tfDNI.getValue();
                
                if(!usuario.getUsername().equals(sUsername)) {
                	if(usuarioService.nombreUsuarioExistente(sUsername)) {
                		notificacion = notificacion +"Ese nombre de usuario ya existe";
                		existeUsuario = true;
                	}
                	else
                		usuario.setNombreUsuario(sUsername);
                }
                
                if(!usuario.getEmail().equals(sEmail)) {
                	
                	if(usuarioService.emailExistente(sEmail)) {
                		notificacion = notificacion +"\nEse email ya existe";
                		existeEmail = true;
                	}
                	else
                		usuario.setEmail(sEmail);
                }
                
                if(!existeUsuario && !existeEmail) {
                	if(!usuario.getNombre().equals(sNombre)) usuario.setNombre(sNombre);
                	if(!usuario.getApellidos().equals(sApell)) usuario.setApellidos(sApell);
                	if(!usuario.getDni().equals(sDNI)) usuario.setDni(sDNI);
                	
                	usuarioService.save(usuario);
                	vNombre.setCaption(usuario.getNombre());
                	vApell.setCaption(usuario.getApellidos());
                	vDNI.setCaption(usuario.getDni());
                	vUsername.setCaption(usuario.getNombreUsuario());
                	vEmail.setCaption(usuario.getEmail());
                	
        			vNombre.setVisible(true);
        			vApell.setVisible(true);
        			vDNI.setVisible(true);
        			vUsername.setVisible(true);
        			vEmail.setVisible(true);

        			tfNombre.setVisible(false);
        			tfApell.setVisible(false);
        			tfDNI.setVisible(false);
        			tfUsername.setVisible(false);
        			tfEmail.setVisible(false);
        			
        			btnModificarDatos.setVisible(true);
        			btnGuardar.setVisible(false);
                }
                else
                	Notification.show(notificacion);
			}
        	else // Si ha fallado la validación muestra un mensajito
        		Notification.show("Comprueba los datos introducidos");

		});
		
		
		Image img = new Image("foto");
		img.setWidth(300, Unit.PIXELS);
		img.setHeight(300, Unit.PIXELS);
		
		img.setVisible(true);
		try{
			img.setSource(new ExternalResource(usuario.getFoto1()));
		}catch(Exception e) {img.setSource(new ExternalResource("/perfiluser/null.png"));}
    	
    	hlNombre.addComponents(lNombre, vNombre, tfNombre);
    	hlApellidos.addComponents(lApell, vApell, tfApell);
    	hlDNI.addComponents(lDNI, vDNI, tfDNI);
    	hlUsername.addComponents(lUsername, vUsername, tfUsername);
    	hlEmail.addComponents(lEmail, vEmail,tfEmail);
    	hlRol.addComponents(lRol, vRol);
    	hlCuentaActiva.addComponent(lAct);
    	hlBtnCambiosRol.addComponents(btnCambioRolAnfitrion, btnCambioRolGerente, btnCambioRolAdmin);
    	hlBtnCambiosActivo.addComponents(btnBloquear, btnDesbloquear);
    	hlBtnModificarDatos.addComponents(btnModificarDatos, btnGuardar);
    	
    	elementosUsuario.addComponents(hlNombre, hlApellidos, hlDNI, hlUsername, hlEmail, hlRol,
    			hlBtnModificarDatos, hlCuentaActiva, hlBtnCambiosRol, hlBtnCambiosActivo);

    	if(usuario.getUsername().equals(SecurityUtils.LogedUser().getUsername())
    			|| !SecurityUtils.LogedUser().getRol().equals(Rol.ADMINISTRADOR)) {
    		hlBtnCambiosRol.setVisible(false);
    		hlBtnCambiosActivo.setVisible(false);
    		hlBtnModificarDatos.setVisible(false);
    	}
    	
		panelFoto.setContent(img);
		panelUsuario.setContent(elementosUsuario);
		
		addComponent(datosUsuario);
	    setComponentAlignment(datosUsuario, Alignment.TOP_CENTER);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// Obtenemos el id del usuario de la URI
		String args[] = event.getParameters().split("/");
	    String value1 = args[0];
	    
	    long id_usuario = Long.parseLong(value1);
	    usuario = usuarioService.findById(id_usuario);
	    init();
	}

}
