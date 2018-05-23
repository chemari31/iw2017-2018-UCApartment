package es.uca.iw.Ucapartment.Administracion;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoView;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = UsuariosView.VIEW_NAME)
public class UsuariosView extends VerticalLayout implements View {
	
	public static final String VIEW_NAME = "usuariosView";
	
	private Grid<Usuario> grid = new Grid<>();
	private List<Usuario> lista_usuarios;
	
	private final UsuarioService usuarioService;
	
	@Autowired
	public UsuariosView(UsuarioService service) {
		this.usuarioService = service;
	}
	
	@PostConstruct
	void init() {
		
		VerticalLayout layout = new VerticalLayout();
		Panel listaUsuariosPanel = new Panel("Lista de usuarios");
		
		listaUsuariosPanel.setWidth("620px");
		listaUsuariosPanel.setHeight("580px");
		
		layout.addComponent(listaUsuariosPanel);
		layout.setComponentAlignment(listaUsuariosPanel, Alignment.TOP_CENTER);

		lista_usuarios = usuarioService.findAll();
		
		//grid.setColumns("nombreUsuario", "email");
		grid.setItems(lista_usuarios);
		grid.addColumn(Usuario::getUsername).setCaption("Nombre de usuario").setWidth(200)
	      .setResizable(false);
		grid.addColumn(Usuario::getEmail).setCaption("Email").setWidth(200)
	      .setResizable(false);
		grid.addColumn(e -> "Información", new ButtonRenderer(clickEvent-> { 
			Usuario usuario = ((Usuario) clickEvent.getItem());
			getUI().getNavigator().navigateTo(PerfilUsuarioView.VIEW_NAME + '/'+String.valueOf(usuario.getId()));
			
			})).setCaption("Ver perfil").setWidth(200)
	      .setResizable(false);
		
		grid.setWidth("600px");
		grid.setHeight("570px");
		listaUsuariosPanel.setContent(grid);
		addComponent(layout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
