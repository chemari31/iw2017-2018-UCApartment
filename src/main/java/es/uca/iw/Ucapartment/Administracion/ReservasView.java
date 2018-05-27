package es.uca.iw.Ucapartment.Administracion;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.renderers.ButtonRenderer;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoService;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaService;
import es.uca.iw.Ucapartment.Transaccion.Transaccion;
import es.uca.iw.Ucapartment.Usuario.PopupPago;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Usuario.UsuarioService;
import es.uca.iw.Ucapartment.Valoracion.Valoracion;
import es.uca.iw.Ucapartment.Valoracion.ValoracionService;
import es.uca.iw.Ucapartment.email.EmailServiceImpl;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = ReservasView.VIEW_NAME)
public class ReservasView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "reservasView";
	
	private Grid<Reserva> grid = new Grid<>();
	private List<Reserva> lista_reservas;
	private List<Valoracion> lista_valoraciones;
	
	private final ReservaService reservaService;
	private final ApartamentoService apartamentoService;
	private final UsuarioService usuarioService;
	private final EstadoService estadoService;
	private final ValoracionService valoracionService;
	private Estado estado = null;
	private Reserva reservaRow = null;
	private EmailServiceImpl correo = new EmailServiceImpl();

	
	@Autowired
	public ReservasView(ReservaService rService, ApartamentoService aService, UsuarioService uService,
			EstadoService eService, ValoracionService vService) {
		this.reservaService = rService;
		this.apartamentoService = aService;
		this.usuarioService = uService;
		this.estadoService = eService;
		this.valoracionService = vService;
	}
	
	@PostConstruct
	void init () {
		VerticalLayout layout = new VerticalLayout();
		VerticalLayout popupLayout = new VerticalLayout();
		Panel listaReservasPanel = new Panel("Lista de reservas");
		VerticalLayout contenidoPanel = new VerticalLayout();
		HorizontalLayout hlFiltro = new HorizontalLayout();
		TextField tfFiltro = new TextField();
		ComboBox<String> cbFiltroUsuario = new ComboBox<>();
		ComboBox<String> cbFiltroApart = new ComboBox<>();
		ComboBox<String> cbFiltroEstado = new ComboBox<>();
		ComboBox<String> cbFiltros = new ComboBox<>();
		List<String> lista_nom_apart = new ArrayList<String>();
		List<String> lista_nom_usuario = new ArrayList<String>();
		List<Apartamento> lista_apartamentos;
		List<Usuario> lista_usuarios;
		Label lFiltrar = new Label("Filtrar por ");
		PopupPago popupValoraciones = new PopupPago();
		PopupPago popupReservas = new PopupPago();

		cbFiltros.setItems("Apartamento","Usuario", "Estado"); 
		
		lista_apartamentos = apartamentoService.findAll(); 
		lista_usuarios = usuarioService.findAll();

		for(int i = 0; i < lista_apartamentos.size(); i++)
			lista_nom_apart.add(lista_apartamentos.get(i).getNombre());
		
		for(int i = 0; i < lista_usuarios.size(); i++)
			lista_nom_usuario.add(lista_usuarios.get(i).getNombreUsuario());
			
		cbFiltroApart.setItems(lista_nom_apart);
		cbFiltroUsuario.setItems(lista_nom_usuario);
		cbFiltroEstado.setItems("Todos","Pendiente","Aceptada","Cancelada","Realizada");
		cbFiltroApart.setVisible(false);
		cbFiltroUsuario.setVisible(false);
		cbFiltroEstado.setVisible(false);
		cbFiltroUsuario.setEmptySelectionAllowed(false);
		cbFiltroApart.setEmptySelectionAllowed(false);
		cbFiltroEstado.setEmptySelectionAllowed(false);
		cbFiltroEstado.setSelectedItem("Todos");
		
		listaReservasPanel.setWidth("1110px"); 
		listaReservasPanel.setHeight("580px");
		
		hlFiltro.addComponents(lFiltrar,cbFiltros,cbFiltroApart,cbFiltroUsuario,cbFiltroEstado);
		
		cbFiltros.addValueChangeListener(event -> {
			if(cbFiltros.isSelected("Usuario")) {
				cbFiltroApart.setVisible(false);
				cbFiltroUsuario.setVisible(true);
				cbFiltroEstado.setVisible(false);
				cbFiltroUsuario.addValueChangeListener(eventUser -> {
					String username = cbFiltroUsuario.getSelectedItem().toString();
					listaReservas(username.substring(9,username.length()-1),"Usuario");
				});
					
			}
			
			if(cbFiltros.isSelected("Apartamento")) {
				cbFiltroApart.setVisible(true);
				cbFiltroUsuario.setVisible(false);
				cbFiltroEstado.setVisible(false);
				cbFiltroApart.addValueChangeListener(eventApart -> {
					String apartamento_nom = cbFiltroApart.getSelectedItem().toString();
					listaReservas(apartamento_nom.substring(9,apartamento_nom.length()-1),"Apartamento");
				});
			}
			
			if(cbFiltros.isSelected("Estado")) {
				cbFiltroApart.setVisible(false);
				cbFiltroUsuario.setVisible(false);
				cbFiltroEstado.setVisible(true);
				cbFiltroEstado.addValueChangeListener(eventEstado -> {
					String estado = cbFiltroEstado.getSelectedItem().toString();
					if(estado.substring(9,estado.length()-1).equals("Todos"))
						listaReservas(null,null);
					else
						listaReservas(estado.substring(9,estado.length()-1),"Estado");
				});
			}

			if(cbFiltros.isEmpty()) {
				cbFiltroApart.setVisible(false);
				cbFiltroUsuario.setVisible(false);
				cbFiltroEstado.setVisible(false); 
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
			estado = estadoService.findByReserva(e);
			return estado.getValor();
			}, new ButtonRenderer(clickEvent -> {
				reservaRow = ((Reserva)clickEvent.getItem());
				estado = estadoService.findByReserva((Reserva) clickEvent.getItem());
				if(estado.getValor() == Valor.PENDIENTE) {
					Button btn_aceptar_res = new Button("Aceptar");
					Button btn_cancelar_res = new Button("Cancelar");
					HorizontalLayout hlBotones = new HorizontalLayout();
					hlBotones.addComponents(btn_aceptar_res, btn_cancelar_res);
					popupLayout.removeAllComponents();
					popupLayout.addComponent(new Label("¿Confirmar reserva?"));
					popupLayout.addComponent(hlBotones);
					popupReservas.setPosition(550, 200);
					popupReservas.setContent(popupLayout);
					popupReservas.center();
					popupReservas.setDraggable(false);
					btn_aceptar_res.addClickListener(event -> {
						estado.setValor(Valor.ACEPTADA);
						estadoService.save(estado);
						
						correo.enviaremailcliente(reservaRow);
						
						popupReservas.close();
						removeAllComponents();
						init();
					});
					btn_cancelar_res.addClickListener(event -> {
						estado.setValor(Valor.CANCELADA);
						estadoService.save(estado);
						
						correo.enviaremailcliente(reservaRow);

						popupReservas.close();
						removeAllComponents();
						init();
					});
					UI.getCurrent().addWindow(popupReservas);
				}
				if(estado.getValor() == Valor.CANCELADA) {
					Button btn_aceptar = new Button("Aceptar");
					popupLayout.removeAllComponents();
					popupLayout.addComponent(new Label("La reserva fue cancelada el día "
							+reservaRow.getFecha()));
					popupLayout.addComponent(btn_aceptar);
					btn_aceptar.addClickListener(event ->{
						popupReservas.close();
					});
					popupReservas.setWidth("600px");
					popupReservas.setHeight("300px");
					popupReservas.setPosition(550, 200);
					popupReservas.setContent(popupLayout);
					popupReservas.center();
					UI.getCurrent().addWindow(popupReservas);
				}
				if(estado.getValor() == Valor.ACEPTADA) {
					popupLayout.removeAllComponents();
					TextField cuenta = new TextField("Introduzca su numero de cuenta","xxx-xxx-xxx-xxx");
					cuenta.setRequiredIndicatorVisible(true);
					cuenta.setMaxLength(16);
					popupLayout.addComponent(cuenta);
					Binder<Transaccion> bind = new Binder<>(Transaccion.class);
					HorizontalLayout hlInfo = new HorizontalLayout();
					Button btn_cancelar = new Button("Cancelar reserva");
					Button btn_pagar = new Button("Pagar reserva");
					bind.forField(cuenta)
					 .asRequired("El campo Cuenta es obligatorio")
					 .withConverter(new StringToLongConverter("Por favor introduzca un número"))
					 .withValidator(new LongRangeValidator("El número de cuenta debe tener 16 dígitos", 0000000000000005L, 9999999999999999L))
					 .bind(Transaccion::getCuentaOrigen, Transaccion::setCuentaOrigen);
					hlInfo.addComponents(btn_cancelar,btn_pagar);
					
					btn_cancelar.addClickListener(event ->{
						estado.setValor(Valor.CANCELADA);
						estadoService.save(estado);
						popupReservas.close();
						removeAllComponents();
						init();
					});
					
					btn_pagar.addClickListener(event -> {
						if(bind.isValid()) {
							Long lg = null;
							reservaService.pasarelaDePago(reservaRow.getPrecio(), reservaRow,estado, 
									lg.valueOf(cuenta.getValue()));
							
							correo.enviaremailpropietario2(reservaRow);
							correo.enviaremailcliente2(reservaRow);
							
							popupReservas.close();
							removeAllComponents();
							init();
						} else
							Notification.show("Comprueba el número de cuenta");
					});

					popupLayout.addComponent(hlInfo);
					popupReservas.setWidth("400px");
					popupReservas.setHeight("300px");
					popupReservas.setPosition(550, 200);
					popupReservas.setContent(popupLayout);
					popupReservas.center();
					UI.getCurrent().addWindow(popupReservas);
					
				}
				if(estado.getValor() == Valor.REALIZADA) {
					HorizontalLayout hlInfo = new HorizontalLayout();
					Button btn_incid_prop = new Button("Incidencia a propietario");
					Button btn_incid_huesp = new Button("Incidencia a huesped");
					Button btn_cancelar = new Button("Cancelar");
					Button btn_factura = new Button("Generar factura");
					popupLayout.removeAllComponents();
					popupLayout.addComponent(new Label("La reserva con fecha "+reservaRow.getFecha() 
						+ "Ya fue realizada"));
					
					TextArea area = new TextArea();
					area.setValue("Escriba aquí su incidencia");
					area.setSizeFull();
				
					NativeSelect<String> nivel = new NativeSelect<>("Grado");
					nivel.setItems("1", "2","3","4","5");
					nivel.setValue("1");
					
					popupLayout.addComponents(area,nivel);
					
					hlInfo.addComponents(btn_incid_prop,btn_incid_huesp,btn_cancelar, btn_factura);
					
					btn_incid_prop.addClickListener(event ->{
						String comentario = area.getValue();
						int valor = Integer.parseInt(nivel.getValue());
						Date hoy = java.sql.Date.valueOf(LocalDate.now());
						Valoracion v = new Valoracion(comentario, valor,hoy,SecurityUtils.LogedUser(),
								reservaRow.getApartamento());
						valoracionService.save(v);
						Notification.show("Su comentario se ha realizado satisfactoriamente", Notification.Type.HUMANIZED_MESSAGE );	
						popupReservas.close();
						removeAllComponents();
						init();
					});
					btn_incid_huesp.addClickListener(event ->{
						String comentario = area.getValue();
						int valor = Integer.parseInt(nivel.getValue());
						Date hoy = java.sql.Date.valueOf(LocalDate.now());
						Usuario usuarioValorado = usuarioService.findById(reservaRow.getUsuario().getId());
						Valoracion v = new Valoracion(comentario, valor,hoy,SecurityUtils.LogedUser(),usuarioValorado,
								reservaRow.getApartamento());
						valoracionService.save(v);
						Notification.show("Su comentario se ha realizado satisfactoriamente", Notification.Type.HUMANIZED_MESSAGE );	
						popupReservas.close();
						removeAllComponents();
						init();
					});
					btn_cancelar.addClickListener(event ->{
						popupReservas.close();
					});
					btn_factura.addClickListener(event ->{
						reservaService.generarfactura(reservaRow);
					});
					
					popupLayout.addComponent(hlInfo);
					popupReservas.setWidth("800px");
					popupReservas.setHeight("400px");
					popupReservas.setPosition(550, 200);
					popupReservas.setContent(popupLayout);
					popupReservas.center();
					popupReservas.setDraggable(false);
					UI.getCurrent().addWindow(popupReservas);
				}
				
		})).setCaption("Estado").setWidth(140).setResizable(false);
		grid.addColumn(Reserva::getFecha).setCaption("Fecha").setWidth(150)
	      .setResizable(false);
		grid.addColumn(Reserva::getFechaInicio).setCaption("Fecha inicio").setWidth(150)
	      .setResizable(false);
		grid.addColumn(Reserva::getFechaFin).setCaption("Fecha fin").setWidth(150)
	      .setResizable(false);
		grid.addColumn(Reserva::getPrecio).setCaption("Precio (€)").setWidth(120)
	      .setResizable(false);
		grid.addColumn(Reserva->Reserva.getApartamento().getNombre()).setCaption("Apartamento")
		  .setWidth(140).setResizable(false);
		grid.addColumn(Reserva->Reserva.getUsuario().getUsername()).setCaption("Usuario").setWidth(90)
	      .setResizable(false);
		//Columna boton valoraciones
		grid.addColumn(reserva -> "Valoraciones", new ButtonRenderer(clickEvent -> {
					Reserva r = ((Reserva) clickEvent.getItem());
					lista_valoraciones = valoracionService.findByReserva(r);
					Button btn_aceptar = new Button("Aceptar");
					if(lista_valoraciones.size() == 0) {
						popupLayout.removeAllComponents();
						popupLayout.addComponent(new Label("No se han realizado valoraciones sobre"
							+ " esta reserva"));
						popupLayout.addComponent(btn_aceptar);
						popupLayout.setComponentAlignment(btn_aceptar, Alignment.BOTTOM_RIGHT);
						btn_aceptar.addClickListener(event ->{
							popupValoraciones.close();
						});
						popupValoraciones.setWidth("500px");
						popupValoraciones.setHeight("150px");
						popupValoraciones.setPosition(550, 200);
						popupValoraciones.setContent(popupLayout);
						popupValoraciones.center();
						popupValoraciones.setDraggable(false);
						UI.getCurrent().addWindow(popupValoraciones);
					}
					else {
						lista_valoraciones.sort((o1,o2) -> o1.getFecha().compareTo(o2.getFecha()));
						Grid<Valoracion> valGrid = new Grid();
						valGrid.setItems(lista_valoraciones);
						valGrid.setWidth("900px");
						valGrid.setHeight("250px");
						valGrid.addColumn(Valoracion::getFecha).setCaption("Fecha").setWidth(150).setResizable(false);
						valGrid.addColumn(Valoracion->Valoracion.getUsuario().getNombreUsuario())
							.setCaption("Usuario").setWidth(200).setResizable(false);
						valGrid.addColumn(Valoracion::getDescripcion).setCaption("Descripcion").setWidth(800).setResizable(false);
						popupLayout.removeAllComponents();
						popupLayout.addComponent(new Label("Valoraciones realizadas sobre la reserva"));
						popupLayout.addComponent(valGrid);
						popupLayout.addComponent(btn_aceptar);
						popupLayout.setComponentAlignment(btn_aceptar, Alignment.BOTTOM_RIGHT);
						popupValoraciones.setWidth("1000px");
						popupValoraciones.setHeight("400px");
						popupValoraciones.setPosition(550, 200);
						popupValoraciones.setContent(popupLayout);
						popupValoraciones.center(); 
						popupValoraciones.setDraggable(false);
						UI.getCurrent().addWindow(popupValoraciones);
					}
				})).setCaption("Valoraciones").setWidth(150)
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
			if(filtrarPor.equals("Estado"))
				grid.setItems(reservaService.findByEstadoValor(Valor.valueOf(filtro.toUpperCase())));
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
