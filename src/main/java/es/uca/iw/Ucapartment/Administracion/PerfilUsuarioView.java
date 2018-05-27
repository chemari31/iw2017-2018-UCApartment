package es.uca.iw.Ucapartment.Administracion;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.renderers.ImageRenderer;

import es.uca.iw.Ucapartment.Usuario.MiPerfilView;
import es.uca.iw.Ucapartment.Usuario.Rol;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;
import es.uca.iw.Ucapartment.Valoracion.Valoracion;
import es.uca.iw.Ucapartment.Valoracion.ValoracionService;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = PerfilUsuarioView.VIEW_NAME)
public class PerfilUsuarioView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "perfilUsuarioView";
	
	private UsuarioService usuarioService;
	private ValoracionService valoracionService;
	private Usuario usuario;
	
	private final Image image = new Image("foto");
	
	// Mediante el objeto binder validamos los campos
	Binder<Usuario> binder = new Binder<>(Usuario.class);
	
	// Directorio base de la aplicación. Lo utilizamos para guardar las imágenes
	String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath(); 
	
	@Autowired
	public PerfilUsuarioView (UsuarioService uService, ValoracionService vService) {
		this.usuarioService = uService;
		this.valoracionService = vService;
	}
	
	void init() {
		HorizontalLayout datosUsuario = new HorizontalLayout();
		VerticalLayout layoutDerecho = new VerticalLayout();
		VerticalLayout layoutIzquierdo = new VerticalLayout();
		VerticalLayout layoutElementos = new VerticalLayout();
		
		Panel panelUsuario = new Panel("Usuario "+usuario.getUsername());
		Panel panelFoto = new Panel("Foto");
		Panel panelComentario = new Panel("Comentarios y valoraciones");
		panelComentario.setWidth("900px");
		
		panelUsuario.setWidth("630px");
		panelUsuario.setHeight("570px");
		panelFoto.setWidth("320px");
		panelFoto.setHeight("350px");
		
		layoutIzquierdo.addComponent(panelUsuario);
		layoutDerecho.addComponent(panelFoto);
		
		datosUsuario.addComponent(layoutIzquierdo);
		datosUsuario.addComponent(layoutDerecho);
		
		layoutElementos.addComponents(datosUsuario, panelComentario);
		layoutElementos.setComponentAlignment(datosUsuario, Alignment.TOP_CENTER);
		layoutElementos.setComponentAlignment(panelComentario, Alignment.TOP_CENTER);
		
		VerticalLayout elementosUsuario = new VerticalLayout();
		
		HorizontalLayout hlNombre = new HorizontalLayout();
		HorizontalLayout hlApellidos = new HorizontalLayout();
		HorizontalLayout hlDNI = new HorizontalLayout();
		HorizontalLayout hlUsername = new HorizontalLayout();
		HorizontalLayout hlEmail = new HorizontalLayout();
		HorizontalLayout hlRol = new HorizontalLayout();
		HorizontalLayout hlCuentaActiva = new HorizontalLayout();
		HorizontalLayout hlBtnCambioImagen = new HorizontalLayout();
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
		if(usuario.isAccountNonLocked()) lAct.setValue("Cuenta activa");
		else lAct.setValue("Cuenta bloqueada");
		
		vNombre = new Label();
		vApell = new Label(); 
		vDNI = new Label();
		vUsername = new Label();
		vEmail = new Label();
		vRol = new Label();
		vRol.setCaption(usuario.getRol().toString());
		
		vNombre.setValue(usuario.getNombre());
		vApell.setValue(usuario.getApellidos());
		vDNI.setValue(usuario.getDni());
		vUsername.setValue(usuario.getNombreUsuario());
		vEmail.setValue(usuario.getEmail());
		
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
		
		 // Lo siguiente se utiliza para guardar imagenes. Creamos el directorio si no existe
 		File uploads = new File(basepath +"/usuarios/" + usuario.getId());
         if (!uploads.exists() && !uploads.mkdir())
             System.out.println(new Label("ERROR: No podemos crear el directorio."));

 		// Clase para almacenar imágenes
 		class ImageUploader implements Receiver, SucceededListener {
 			
 			public File file;
 		    
 			public OutputStream receiveUpload(String filename,
                     String mimeType) {
 		    	FileOutputStream fos = null; // Stream to write to
 		        try {
 		            // Open the file for writing.
 		            file = new File(basepath +"/usuarios/" + usuario.getId() +"/"+ filename);
 		            fos = new FileOutputStream(file);
 		        } catch (final java.io.FileNotFoundException e) {
 		            new Notification("No se ha podido abrir el archivo",
 		                             e.getMessage(),
 		                             Notification.Type.ERROR_MESSAGE)
 		                .show(Page.getCurrent());
 		            return null;
 		        }
 		        usuario.setFoto1("/usuarios/" + usuario.getId() +"/"+ filename);
 		        return fos; // Return the output stream to write to 
 		    }

 		    public void uploadSucceeded(SucceededEvent event) {
 		    	usuarioService.save(usuario);
 		    	image.setSource(new FileResource(file));
 		    	image.setWidth(300, Unit.PIXELS);
 		    	image.setHeight(300, Unit.PIXELS);
 		    	if(usuario.getUsername().equals(SecurityUtils.LogedUser().getUsername()))
 	 				getUI().getPage().reload();
 		    }
 		};
		
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
			lAct.setValue("Cuenta bloqueada");
			Notification.show("La cuenta del usuario "+usuario.getUsername() + " ha sido bloqueada.");
			btnBloquear.setEnabled(false);
			btnDesbloquear.setEnabled(true);
		});
		
		btnDesbloquear.addClickListener(event -> {
			usuario.setDesbloqueo(true);
			usuarioService.save(usuario);
			lAct.setValue("Cuenta activa");
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
                	vNombre.setValue(usuario.getNombre());
                	vApell.setValue(usuario.getApellidos());
                	vDNI.setValue(usuario.getDni());
                	vUsername.setValue(usuario.getNombreUsuario());
                	vEmail.setValue(usuario.getEmail());
                	
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
		
		
		image.setWidth(300, Unit.PIXELS);
		image.setHeight(300, Unit.PIXELS);
		
		image.setVisible(true);
		try{
			image.setSource(new ExternalResource(usuario.getFoto1()));
		}catch(Exception e) { image.setSource(new ExternalResource("/perfiluser/null.png")); }
		
		ImageUploader receiver = new ImageUploader();
		Upload btnFoto = new Upload("Adjunta la foto de perfil", receiver);

		btnFoto.addSucceededListener(receiver);
		btnFoto.setImmediateMode(true);
		btnFoto.setButtonCaption("Cambiar imagen");
    	
    	hlNombre.addComponents(lNombre, vNombre, tfNombre);
    	hlApellidos.addComponents(lApell, vApell, tfApell);
    	hlDNI.addComponents(lDNI, vDNI, tfDNI);
    	hlUsername.addComponents(lUsername, vUsername, tfUsername);
    	hlEmail.addComponents(lEmail, vEmail,tfEmail);
    	hlRol.addComponents(lRol, vRol);
    	hlBtnCambioImagen.addComponent(btnFoto);
    	hlCuentaActiva.addComponent(lAct);
    	hlBtnCambiosRol.addComponents(btnCambioRolAnfitrion, btnCambioRolGerente, btnCambioRolAdmin);
    	hlBtnCambiosActivo.addComponents(btnBloquear, btnDesbloquear);
    	hlBtnModificarDatos.addComponents(btnModificarDatos, btnGuardar);
    	
    	elementosUsuario.addComponents(hlNombre, hlApellidos, hlDNI, hlUsername, hlEmail, hlRol);
    	
    	if(SecurityUtils.isLoggedIn() && (SecurityUtils.hasRole("ADMINISTRADOR") || 
    			SecurityUtils.LogedUser().getUsername().equals(usuario.getUsername()))) 
    		elementosUsuario.addComponents(hlBtnModificarDatos,hlBtnCambioImagen);
    	
    	if(SecurityUtils.hasRole("ADMINISTRADOR")) 
    		elementosUsuario.addComponents(hlCuentaActiva, hlBtnCambiosRol, hlBtnCambiosActivo);
    	
    	if(SecurityUtils.isLoggedIn())
    		if(usuario.getUsername().equals(SecurityUtils.LogedUser().getUsername())
    			|| !SecurityUtils.LogedUser().getRol().equals(Rol.ADMINISTRADOR)) {
    		hlBtnCambiosRol.setVisible(false);
    		hlBtnCambiosActivo.setVisible(false);
    		hlBtnModificarDatos.setVisible(false);
    	}
    	
		//Comentarios
		Grid<Valoracion> gridValoracion = new Grid<>();
		List<Valoracion> lista_valoraciones;
		gridValoracion.setWidth("897px");
		lista_valoraciones = valoracionService.findByUsuarioValorado(usuario);
		gridValoracion.setItems(lista_valoraciones);
		gridValoracion.addColumn(usuario ->{
			
			return usuario.getUsuario().getUsername();
		}).setCaption("Usuario").setResizable(false);
		gridValoracion.addColumn(Valoracion::getDescripcion).setCaption("Comentario").setResizable(false);
		gridValoracion.addColumn(p ->new ExternalResource("/valoracion/"
		+String.valueOf(p.getGrado()+".png")),new ImageRenderer()).setCaption("Valoración").setResizable(false);
		
		panelFoto.setContent(image);
		panelUsuario.setContent(elementosUsuario);
		panelComentario.setContent(gridValoracion);
		
		addComponent(layoutElementos);
	    setComponentAlignment(layoutElementos, Alignment.TOP_CENTER);
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
