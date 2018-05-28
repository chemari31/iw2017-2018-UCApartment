package es.uca.iw.Ucapartment.Apartamento;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SingleSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.grid.SingleSelectionModel;
import com.vaadin.ui.renderers.ButtonRenderer;

import es.uca.iw.Ucapartment.Home;
import es.uca.iw.Ucapartment.Administracion.PerfilUsuarioView;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.EstadoService;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Precio.EstablecerPrecioEspeciales;
import es.uca.iw.Ucapartment.Precio.PrecioService;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Usuario.MiPerfilView;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;
import es.uca.iw.Ucapartment.Valoracion.Valoracion;
import es.uca.iw.Ucapartment.Valoracion.ValoracionService;
import es.uca.iw.Ucapartment.email.EmailServiceImpl;
import es.uca.iw.Ucapartment.security.SecurityUtils;
import es.uca.iw.Ucapartment.Reserva.ReservaService;

@SpringUI(path="/")
@SpringView(name = ApartamentoManagementView.VIEW_NAME)
public class ApartamentoManagementView extends VerticalLayout implements View{
	public static final String VIEW_NAME = "apartamentoManagementView";

	private Grid<Apartamento> grid;
	private TextField filter;
	private Button addNewBtn;
	private Button editarBtn;
	private Button editarPrecioBtn;
	Usuario user = SecurityUtils.LogedUser();
	private Grid<Reserva> gridReservas;
	Window confirmarReserva = new Window("Confirmación o Denegación Reserva");
	Window ventanaValoracion = new Window("Introducir una Valoración");
	

	private ApartamentoEditor editor;
	private ApartamentoNuevo nuevo;

	@Autowired
	private final ApartamentoService service;
	@Autowired
	private final ReservaService serviceReserva;
	@Autowired
	private final PrecioService precioService;
	@Autowired
	private final EstadoService estadoService;
	private Apartamento apar = null;
	@Autowired
	private EstadoRepository repoEstado;
	private Estado estado = null;
	private static Reserva r = null;
	private Usuario usuario = null;
	private Reserva reserva;
	@Autowired
	private final UsuarioService usuarioService;
	@Autowired
	private final ValoracionService valoracionService;
	
	private EmailServiceImpl correo = new EmailServiceImpl();

	
	@Autowired
	public ApartamentoManagementView(ApartamentoService service, ReservaService serviceReserva, ApartamentoEditor editor, PrecioService precioService, UsuarioService usuarioService, ValoracionService valoracionService) {
		this.service = service;
		this.serviceReserva = serviceReserva;
		this.precioService = precioService;
		this.usuarioService = usuarioService;
		this.valoracionService = valoracionService;
		this.editor = editor;
		nuevo = new ApartamentoNuevo(service, precioService);
		this.grid = new Grid<>(Apartamento.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Nuevo Apartamento", FontAwesome.PLUS);
		this.editarBtn = new Button("Editar Apartamento");
		this.editarPrecioBtn = new Button("Establecer Precio por Fecha");
		this.gridReservas = new Grid<>();
		this.estadoService = new EstadoService();
	}
	
	@PostConstruct
	void init() {
		VerticalLayout layout = new VerticalLayout();
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, editarBtn, editarPrecioBtn);
		layout.removeAllComponents();
		
		layout.addComponents(nuevo, editor, actions, grid, gridReservas);
		actions.setVisible(true);
		grid.setVisible(true);
		nuevo.setVisible(false);

		grid.setHeight(300, Unit.PIXELS);
		grid.setWidth(1000, Unit.PIXELS);
		grid.setColumns("nombre", "ciudad", "habitaciones", "precio");
		grid.addColumn(e -> "Información", new ButtonRenderer(clickEvent-> { 
			Apartamento apartamento = ((Apartamento) clickEvent.getItem());
			getUI().getNavigator().navigateTo(ApartamentoView.VIEW_NAME + '/'+String.valueOf(apartamento.getId()));
			
			})).setCaption("Ver detalles").setWidth(200)
	      .setResizable(false);

		gridReservas.setHeight(300, Unit.PIXELS);
		gridReservas.setWidth(1000, Unit.PIXELS);
		//gridReservas.setColumns("fechaInicio", "fechaFin", "precio", "apartamento");
		gridReservas.setVisible(false);
		
		filter.setPlaceholder("Filtrar por nombre");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listApartamentos(e.getValue()));

		// Connect selected User to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {listReservas(e.getValue());});
		
		SingleSelect<Apartamento> selection = grid.asSingleSelect();
		SingleSelect<Reserva> selReserva = gridReservas.asSingleSelect();
		gridReservas.asSingleSelect().addValueChangeListener(e -> { reserva = serviceReserva.findById(selReserva.getValue().getId());
																	estado = repoEstado.findByReserva(reserva); 
																	if(estado.getValor() == Valor.PENDIENTE)
																		mostrarVentanaConfirmacion(estado);
																	else {
																		if(estado.getValor() == Valor.REALIZADA) {
																			Apartamento apartam = selection.getValue();
																			Reserva res = serviceReserva.findById(selReserva.getValue().getId());
																			mostrarVentanaValoracion(apartam, res);
																		}
																	}
																	});
		// Instantiate and edit new Apartamento the new button is clicked
		addNewBtn.addClickListener(e -> { nuevo.setVisible(true); } );
		editarBtn.addClickListener(clickEvent -> { editor.editApartamento(selection.getValue()); });
		editarPrecioBtn.addClickListener(clickEvent -> { new EstablecerPrecioEspeciales(selection.getValue(), precioService); });
		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listApartamentos(filter.getValue());
		});
		nuevo.setChangeHandler(() -> {
			nuevo.setVisible(false);
			listApartamentos(filter.getValue());
		});

		// Initialize listing
		listApartamentos(null);
		addComponent(layout);
	
	}
	
	public void mostrarVentanaConfirmacion(Estado estado) {
		VerticalLayout subContent = new VerticalLayout();
        Label titulo = new Label("¿Confirmar Reserva?");
        /* Action buttons */
    	Button aceptar = new Button("Aceptar");
    	Button cancelar = new Button("Cancel");
    	subContent.addComponent(titulo);
    	HorizontalLayout botones = new HorizontalLayout(aceptar, cancelar);
    	subContent.addComponent(botones);
    	confirmarReserva.setContent(subContent);
    	confirmarReserva.center();
    	UI.getCurrent().addWindow(confirmarReserva);
    	aceptar.addClickListener(e -> { 
    								estado.setValor(Valor.ACEPTADA);
    								estadoService.save(estado);
    								
    								correo.enviaremailcliente(estado.getReserva());

    								confirmarReserva.close();
    								gridReservas.clearSortOrder();
    								gridReservas.deselectAll();});
    	
    	cancelar.addClickListener(e -> { 
    								estado.setValor(Valor.CANCELADA);
    								estadoService.save(estado);
    								
    								correo.enviaremailcliente(estado.getReserva());

    								confirmarReserva.close();
    								gridReservas.clearSortOrder();
    								gridReservas.deselectAll();});
    	
	}
	
	public void mostrarVentanaValoracion(Apartamento apartam, Reserva res) {
		VerticalLayout subContent = new VerticalLayout();
        TextArea descripcionValoracion = new TextArea("Descripcion");
        ComboBox<Integer> valoracion = new ComboBox<>("Valoracion");
        valoracion.setItems(1, 2, 3, 4, 5);
        /* Action buttons */
    	Button aceptar = new Button("Aceptar");
    	Button cancelar = new Button("Cancel");
    	subContent.addComponent(descripcionValoracion);
    	subContent.addComponent(valoracion);
    	HorizontalLayout botones = new HorizontalLayout(aceptar, cancelar);
    	subContent.addComponent(botones);
    	ventanaValoracion.setContent(subContent);
    	ventanaValoracion.center();
    	UI.getCurrent().addWindow(ventanaValoracion);
    	cancelar.addClickListener(e -> { ventanaValoracion.close(); gridReservas.clearSortOrder(); gridReservas.deselectAll();});
    	aceptar.addClickListener(e -> {
						    		String comentario = descripcionValoracion.getValue();
									int valor = valoracion.getValue();
									Date hoy = java.sql.Date.valueOf(LocalDate.now());
									Usuario usuarioValorado = usuarioService.findById(res.getUsuario().getId());
									Valoracion v = new Valoracion(comentario, valor, hoy, user, usuarioValorado, apartam,res);
									valoracionService.save(v);
									ventanaValoracion.close(); 
    							});
	}
	
	private void addComponents(HorizontalLayout actions, Grid<Apartamento> grid2, ApartamentoEditor editor2) {
		// TODO Auto-generated method stub
		
	}

	public void listApartamentos(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(service.findByUsuario(user));
		} else {
			grid.setItems(service.findByNombreStartsWithIgnoreCase(filterText));
		}
	}
	
	private void listReservas(Apartamento apartamento) {
		List<Reserva> listaReservas = null;
		gridReservas.setVisible(true);
		gridReservas.removeAllColumns();
		listaReservas = serviceReserva.findByApartamento(apartamento);
		gridReservas.setItems(listaReservas); 
		gridReservas.addColumn(Reserva::getId).setHidden(true).setCaption("Id");
		gridReservas.addColumn (Reserva::getFechaInicio).setCaption("Fecha Inicio"); 
		gridReservas.addColumn(Reserva::getFechaFin).setCaption("Fecha Fin");
		gridReservas.addColumn(Reserva::getPrecio).setCaption("Precio");
		gridReservas.addColumn(e -> {
			  apar = e.getApartamento();
			  return apar.getNombre();
			}).setCaption("Apartamento");
		gridReservas.addColumn(e -> {
			estado = repoEstado.findByReserva(e);
			return estado.getValor();
		}).setCaption("Estado");
		gridReservas.addColumn(e -> "Perfil del Solicitante"
		, new ButtonRenderer(ClickEvent ->  { usuario = ((Reserva) ClickEvent.getItem()).getUsuario();
		getUI().getNavigator().navigateTo(PerfilUsuarioView.VIEW_NAME + '/'+String.valueOf(usuario.getId()));
		})).setCaption("Perfil");
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}


}
