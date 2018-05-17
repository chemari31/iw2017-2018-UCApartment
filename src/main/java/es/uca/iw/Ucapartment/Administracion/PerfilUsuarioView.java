package es.uca.iw.Ucapartment.Administracion;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.Ucapartment.Usuario.MiPerfilView;
import es.uca.iw.Ucapartment.Usuario.Rol;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;

@SpringView(name = PerfilUsuarioView.VIEW_NAME)
public class PerfilUsuarioView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "perfilUsuarioView";
	
	private UsuarioService usuarioService;
	private Usuario usuario;
	
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
		
		panelUsuario.setWidth("600px");
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
		
		Label lNombre, lApell, lDNI, lUsername, lEmail, lRol;
		Label vNombre, vApell, vDNI, vUsername, vEmail, vRol;
		
		lNombre = new Label("Nombre: ");
		lApell = new Label("Apellidos: ");
		lDNI = new Label("DNI: ");
		lUsername = new Label("Nombre de usuario: ");
		lEmail = new Label("Correo electrónico: ");
		lRol = new Label("Rol: ");
		
		vNombre = new Label(usuario.getNombre());
		vApell = new Label(usuario.getApellidos());
		vDNI = new Label(usuario.getDni());
		vUsername = new Label(usuario.getUsername());
		vEmail = new Label(usuario.getEmail());
		vRol = new Label();
		vRol.setCaption(usuario.getRol().toString());
		
		Button rolAnfitrion = new Button("Cambiar a anfitrion");
		Button rolGerente = new Button("Cambiar a gerente");
		Button rolAdmin = new Button("Cambiar a administrador");
		
		rolAnfitrion.addClickListener(event -> {
			usuario.setRol(Rol.ANFITRION);
			usuarioService.save(usuario);
			Notification.show("Se ha modificado el rol del usuario " +usuario.getUsername() + " a ANFITRION.", Notification.Type.TRAY_NOTIFICATION);
			vRol.setCaption(usuario.getRol().toString());
		});
		
		rolGerente.addClickListener(event -> {
			usuario.setRol(Rol.GERENTE);
			usuarioService.save(usuario);
			Notification.show("Se ha modificado el rol del usuario " +usuario.getUsername() + " a GERENTE.", Notification.Type.TRAY_NOTIFICATION);
			vRol.setCaption(usuario.getRol().toString());
		});
		
		rolAdmin.addClickListener(event -> {
			usuario.setRol(Rol.ADMINISTRADOR);
			usuarioService.save(usuario);
			Notification.show("Se ha modificado el rol del usuario " +usuario.getUsername() + " a ADMINISTRADOR.", Notification.Type.TRAY_NOTIFICATION);
			vRol.setCaption(usuario.getRol().toString());
		});
		
		Image img = new Image("foto");
		img.setWidth(300, Unit.PIXELS);
		img.setHeight(300, Unit.PIXELS);
		
		img.setVisible(true);
		try{
			img.setSource(new ExternalResource(usuario.getFoto1()));
		}catch(Exception e) {img.setSource(new ExternalResource("/perfiluser/null.png"));}
    	
    	hlNombre.addComponents(lNombre, vNombre);
    	hlApellidos.addComponents(lApell, vApell);
    	hlDNI.addComponents(lDNI, vDNI);
    	hlUsername.addComponents(lUsername, vUsername);
    	hlEmail.addComponents(lEmail, vEmail);
    	hlRol.addComponents(lRol, vRol);
    	if(usuario.getRol().toString().equals("ANFITRION"))
    		hlRol.addComponents(rolGerente, rolAdmin);
    	else if(usuario.getRol().toString().equals("GERENTE"))
    		hlRol.addComponents(rolAnfitrion, rolAdmin);
    	else if(usuario.getRol().toString().equals("ADMINISTRADOR"))
    		hlRol.addComponents(rolAnfitrion, rolGerente);
    	
    	elementosUsuario.addComponents(hlNombre, hlApellidos, hlDNI, hlUsername, hlEmail, hlRol);
    	
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
