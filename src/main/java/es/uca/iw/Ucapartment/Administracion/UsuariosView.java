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
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoView;
import es.uca.iw.Ucapartment.Usuario.Rol;
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
		VerticalLayout contenidoPanel = new VerticalLayout();
		HorizontalLayout hlBotonesFiltro = new HorizontalLayout();
		Button btnTodos = new Button("Todos");
		Button btnRolAnf = new Button("Rol anfitrión");
		Button btnRolGer = new Button("Rol gerente");
		Button btnRolAdm = new Button("Rol administrador");
		
		listaUsuariosPanel.setWidth("640px");
		listaUsuariosPanel.setHeight("580px");
		
		hlBotonesFiltro.addComponents(btnTodos, btnRolAnf, btnRolGer, btnRolAdm);
		
		layout.addComponent(listaUsuariosPanel);
		layout.setComponentAlignment(listaUsuariosPanel, Alignment.TOP_CENTER);
		
		lista_usuarios = usuarioService.findAll();
		grid.setItems(lista_usuarios);
		
		btnTodos.addClickListener(event -> {
			lista_usuarios = usuarioService.findAll();
			grid.setItems(lista_usuarios);
		});
		
		btnRolAnf.addClickListener(event -> {
			lista_usuarios = usuarioService.findByRol(Rol.ANFITRION);
			grid.setItems(lista_usuarios);
		});
		
		btnRolGer.addClickListener(event -> {
			lista_usuarios = usuarioService.findByRol(Rol.GERENTE);
			grid.setItems(lista_usuarios);
		});
		
		btnRolAdm.addClickListener(event -> {
			lista_usuarios = usuarioService.findByRol(Rol.ADMINISTRADOR);
			grid.setItems(lista_usuarios);
		});

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
		
		contenidoPanel.addComponent(hlBotonesFiltro);
		contenidoPanel.addComponent(grid);
		listaUsuariosPanel.setContent(contenidoPanel);
		addComponent(layout);
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
