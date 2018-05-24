package es.uca.iw.Ucapartment.Administracion;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoService;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaService;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;

@SpringView(name = ReservasView.VIEW_NAME)
public class ReservasView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "reservasView";
	
	private Grid<Reserva> grid = new Grid<>();
	private List<Reserva> lista_reservas;
	
	private final ReservaService reservaService;
	private final ApartamentoService apartamentoService;
	private final UsuarioService usuarioService;
	private final EstadoService estadoService;
	
	@Autowired
	public ReservasView(ReservaService rService, ApartamentoService aService, UsuarioService uService,
			EstadoService eService) {
		this.reservaService = rService;
		this.apartamentoService = aService;
		this.usuarioService = uService;
		this.estadoService = eService;
	}
	
	@PostConstruct
	void init () {
		VerticalLayout layout = new VerticalLayout();
		Panel listaReservasPanel = new Panel("Lista de reservas");
		VerticalLayout contenidoPanel = new VerticalLayout();
		HorizontalLayout hlFiltro = new HorizontalLayout();
		TextField tfFiltro = new TextField();
		ComboBox<String> cbFiltroUsuario = new ComboBox<>();
		ComboBox<String> cbFiltroApart = new ComboBox<>();
		List<String> lista_nom_apart = new ArrayList<String>();
		List<String> lista_nom_usuario = new ArrayList<String>();
		List<Apartamento> lista_apartamentos;
		List<Usuario> lista_usuarios;
		Label lFiltrar = new Label("Filtrar por ");
		ComboBox<String> cbFiltros = new ComboBox<>();
		
		cbFiltros.setItems("Apartamento","Usuario");
		
		lista_apartamentos = apartamentoService.findAll();
		lista_usuarios = usuarioService.findAll();

		for(int i = 0; i < lista_apartamentos.size(); i++)
			lista_nom_apart.add(lista_apartamentos.get(i).getNombre());
		
		for(int i = 0; i < lista_usuarios.size(); i++)
			lista_nom_usuario.add(lista_usuarios.get(i).getNombreUsuario());
			
		cbFiltroApart.setItems(lista_nom_apart);
		cbFiltroUsuario.setItems(lista_nom_usuario);
		cbFiltroApart.setVisible(false);
		cbFiltroUsuario.setVisible(false);
		cbFiltroUsuario.setEmptySelectionAllowed(false);
		cbFiltroApart.setEmptySelectionAllowed(false);
		
		listaReservasPanel.setWidth("1110px"); 
		listaReservasPanel.setHeight("580px");
		
		hlFiltro.addComponents(lFiltrar,cbFiltros,cbFiltroApart,cbFiltroUsuario);
		
		cbFiltros.addValueChangeListener(event -> {
			if(cbFiltros.isSelected("Usuario")) {
				cbFiltroApart.setVisible(false);
				cbFiltroUsuario.setVisible(true);
				cbFiltroUsuario.addValueChangeListener(eventUser -> {
					String username = cbFiltroUsuario.getSelectedItem().toString();
					listaReservas(username.substring(9,username.length()-1),"Usuario");
				});
					
			}
			
			if(cbFiltros.isSelected("Apartamento")) {
				cbFiltroApart.setVisible(true);
				cbFiltroUsuario.setVisible(false);
				cbFiltroApart.addValueChangeListener(eventApart -> {
					String apartamento_nom = cbFiltroApart.getSelectedItem().toString();
					listaReservas(apartamento_nom.substring(9,apartamento_nom.length()-1),"Apartamento");
				});
			}

			if(cbFiltros.isEmpty()) {
				cbFiltroApart.setVisible(false);
				cbFiltroUsuario.setVisible(false);
				listaReservas(null,null);
			}
		});
		
		
		layout.addComponent(listaReservasPanel);
		layout.setComponentAlignment(listaReservasPanel, Alignment.TOP_CENTER);

		lista_reservas = reservaService.findAll();
		//tfFiltro.setValueChangeMode(ValueChangeMode.LAZY);
		//tfFiltro.addValueChangeListener(e -> listaApartamentos(e.getValue(), cbFiltro.getValue()));
		
		//grid.setColumns("nombreUsuario", "email");
		grid.addColumn(e -> {
			Estado estado = estadoService.findByReserva(e); 
			return estado.getValor();
		}).setCaption("Estado").setWidth(125);
		grid.addColumn(Reserva::getFecha).setCaption("Fecha").setWidth(150)
	      .setResizable(false);
		grid.addColumn(Reserva::getFechaInicio).setCaption("Fecha inicio").setWidth(150)
	      .setResizable(false);
		grid.addColumn(Reserva::getFechaFin).setCaption("Fecha fin").setWidth(150)
	      .setResizable(false);
		grid.addColumn(Reserva::getPrecio).setCaption("Precio (€)").setWidth(120)
	      .setResizable(false);
		grid.addColumn(Reserva->Reserva.getApartamento().getNombre()).setCaption("Apartamento").setWidth(150)
	      .setResizable(false);
		grid.addColumn(Reserva->Reserva.getUsuario().getUsername()).setCaption("Usuario").setWidth(90)
	      .setResizable(false);
		grid.addColumn(e -> "Incidencias", new ButtonRenderer(clickEvent-> { 
			Reserva reserva = ((Reserva) clickEvent.getItem());
			/*getUI().getNavigator().navigateTo(DatosReserva.VIEW_NAME + '/'+String.valueOf(reserva.getId()));
			
			*/})).setCaption("Ver incidencias").setWidth(150)
	      .setResizable(false);
		
		grid.setWidth("1090px");
		grid.setHeight("570px");
		contenidoPanel.addComponent(hlFiltro);
		contenidoPanel.addComponent(grid);
		listaReservasPanel.setContent(contenidoPanel);
		listaReservas(null, null); // Lo inicializamos
		addComponent(layout);
	}
	
	public void listaReservas(String filtro, String filtrarPor) {
		if (StringUtils.isEmpty(filtro) || StringUtils.isEmpty(filtrarPor)) {
			grid.setItems(reservaService.findAll());
		} else {
			if(filtrarPor.equals("Usuario"))
				grid.setItems(reservaService.findByUsuario(usuarioService.loadUserByUsername(filtro)));
			if(filtrarPor.equals("Apartamento"))
				grid.setItems(reservaService.findByApartamento(apartamentoService.loadApartamentoByApartamentoname(filtro)));
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
