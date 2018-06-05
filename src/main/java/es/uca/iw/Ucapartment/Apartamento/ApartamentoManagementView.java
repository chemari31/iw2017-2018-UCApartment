package es.uca.iw.Ucapartment.Apartamento;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MultiSelect;
import com.vaadin.ui.Notification;
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
import es.uca.iw.Ucapartment.Periodo.Periodo;
import es.uca.iw.Ucapartment.Periodo.PeriodoService;
import es.uca.iw.Ucapartment.Precio.EstablecerPrecioEspeciales;
import es.uca.iw.Ucapartment.Precio.Precio;
import es.uca.iw.Ucapartment.Precio.PrecioService;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Usuario.MiPerfilView;
import es.uca.iw.Ucapartment.Usuario.Popup;
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
	private Button periodosNoDispBtn;
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
	@Autowired
	private final PeriodoService periodoService;
	
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
	
	private List<Periodo> lista_periodos;
	private List<Precio> lista_precios_esp;

	
	@Autowired
	public ApartamentoManagementView(ApartamentoService service, ReservaService serviceReserva, ApartamentoEditor editor, 
			PrecioService precioService, UsuarioService usuarioService, ValoracionService valoracionService,
			PeriodoService periodoService) {
		this.service = service;
		this.serviceReserva = serviceReserva;
		this.precioService = precioService;
		this.usuarioService = usuarioService;
		this.valoracionService = valoracionService;
		this.periodoService = periodoService;
		this.editor = editor;
		nuevo = new ApartamentoNuevo(service, precioService);
		this.grid = new Grid<>(Apartamento.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("Nuevo Apartamento", FontAwesome.PLUS);
		this.editarBtn = new Button("Editar Apartamento");
		this.editarPrecioBtn = new Button("Gestionar precios especiales");
		this.periodosNoDispBtn = new Button("Gestionar periodos");
		this.gridReservas = new Grid<>();
		this.estadoService = new EstadoService();
	}
	
	@PostConstruct
	void init() {
		VerticalLayout layout = new VerticalLayout();
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, editarBtn, editarPrecioBtn, periodosNoDispBtn);
		Popup popupPeriodoNoDisp = new Popup();
		VerticalLayout popupLayout = new VerticalLayout();
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
		gridReservas.asSingleSelect().addValueChangeListener(e -> { if(e.getValue()!= null) {
																		reserva = serviceReserva.findById(selReserva.getValue().getId());
																		estado = repoEstado.findByReserva(reserva); 
																		if(estado.getValor() == Valor.PENDIENTE)
																			mostrarVentanaConfirmacion(estado);
																		else {
																			if(estado.getValor() == Valor.REALIZADA) {
																				Apartamento apartam = selection.getValue();
																				Reserva res = serviceReserva.findById(selReserva.getValue().getId());
																				mostrarVentanaValoracion(apartam, res);
																			}
																		}}
																		else
																			Notification.show("No se ha seleccionado ningún apartamento");
																	});
		// Instantiate and edit new Apartamento the new button is clicked
		addNewBtn.addClickListener(e -> { nuevo.setVisible(true); } );
		editarBtn.addClickListener(clickEvent -> { editor.editApartamento(selection.getValue()); });
		editarPrecioBtn.addClickListener(clickEvent -> { 
			if(selection.getValue()!= null) {
				VerticalLayout layoutPopupPrecios = new VerticalLayout();
				Popup popupPrecios = new Popup();
				lista_precios_esp = precioService.findByApartamento(selection.getValue());
				HorizontalLayout hlBotones = new HorizontalLayout();
				Button btn_anadir = new Button("Añadir",FontAwesome.PLUS);
				Button btn_eliminar = new Button("Eliminar",FontAwesome.MINUS);
				Button btn_aceptar = new Button("Aceptar");
				Grid<Precio> precios = new Grid<>();
				precios.setItems(lista_precios_esp);
				SingleSelect<Precio> precio_seleccionado = precios.asSingleSelect();
				precios.setWidth("860px");
				precios.setHeight("250px");
				precios.addColumn(Precio::getFecha_inicio).setCaption("Fecha inicio").setWidth(330).setResizable(false);
				precios.addColumn(Precio::getFecha_fin).setCaption("Fecha fin").setWidth(330).setResizable(false);
				precios.addColumn(Precio::getValor).setCaption("Valor (€)").setWidth(200).setResizable(false);
				layoutPopupPrecios.removeAllComponents();
				layoutPopupPrecios.addComponent(new Label("Nombre apartamento: "+selection.getValue().getNombre()));
				layoutPopupPrecios.addComponent(new Label("Precios especiales del apartamento (no se tienen en cuenta para "
						+ "reservas en estado Aceptada o Realizada)"));
				layoutPopupPrecios.addComponent(precios);
				hlBotones.addComponents(btn_anadir,btn_eliminar,btn_aceptar);
				hlBotones.setComponentAlignment(btn_anadir, Alignment.BOTTOM_RIGHT);
				hlBotones.setComponentAlignment(btn_aceptar, Alignment.BOTTOM_RIGHT);
				layoutPopupPrecios.addComponent(hlBotones);
				layoutPopupPrecios.setComponentAlignment(hlBotones, Alignment.BOTTOM_RIGHT);
				
				Popup popupNuevoPrecio = new Popup();
				VerticalLayout layoutPopupPrecio = new VerticalLayout();
				btn_anadir.addClickListener(e -> {
					HorizontalLayout hlFechas = new HorizontalLayout();
					DateField fechaInicio = new DateField("Inicio");
					DateField fechaFin = new DateField("Fin");
					TextField tfPrecio = new TextField("Precio Especial");
					Date hoy = java.sql.Date.valueOf(LocalDate.now());
					Button btn_nuevoPrecio = new Button("Añadir",FontAwesome.PLUS);
			    	Binder<Precio> binder = new Binder<>(Precio.class);
			    	binder.forField(tfPrecio)
					  .asRequired("No puede estar vacío")
					  .withConverter(
					    new StringToDoubleConverter("Por favor introduce un número decimal"))
					  .bind(Precio::getValor, Precio::setValor);
					Button btn_cancelar = new Button("Cancelar");
					fechaInicio.setValue(LocalDate.now());
					fechaFin.setValue(LocalDate.now().plusDays(1));
					layoutPopupPrecio.removeAllComponents();
					hlFechas.addComponents(fechaInicio, fechaFin, tfPrecio, btn_nuevoPrecio, btn_cancelar);
					layoutPopupPrecio.addComponent(new Label("Nuevo precio especial del apartamento"));
					layoutPopupPrecio.addComponent(hlFechas);

					btn_nuevoPrecio.addClickListener(event -> {
						if(binder.isValid()) {
							Date fInicio, fFin;
							if(fechaInicio.isEmpty() || fechaFin.isEmpty())
								Notification.show("Compruebe que ha introducido una fecha con formato dd/mm/aa válido");
							else {
								fInicio = java.sql.Date.valueOf(fechaInicio.getValue());
								fFin =  java.sql.Date.valueOf(fechaFin.getValue());
								if(fInicio.after(fFin) || (fInicio.before(hoy) || fFin.before(hoy))
										|| fInicio.equals(fFin)) {
									Notification.show("Error en las fechas. Introduzca un intervalo válido");
								}
								else {
									Precio precio = new Precio(Double.parseDouble(tfPrecio.getValue().replace(',', '.')), 
											fInicio, fFin, null, selection.getValue());
									precioService.save(precio);
									lista_precios_esp.clear();
									lista_precios_esp = precioService.findByApartamento(selection.getValue());
									precios.clearSortOrder();
									precios.setItems(lista_precios_esp);
									Notification.show("Se ha añadido un nuevo precio especial");
									popupNuevoPrecio.close();
								}
							}
						}
						else
							Notification.show("Comprueba los datos introducidos");
					});
					btn_cancelar.addClickListener(event-> {
						popupNuevoPrecio.close();
					});
					popupNuevoPrecio.setWidth("900px");
					popupNuevoPrecio.setHeight("200px");
					popupNuevoPrecio.setPosition(550, 200);
					popupNuevoPrecio.setContent(layoutPopupPrecio);
					popupNuevoPrecio.center(); 
					popupNuevoPrecio.setDraggable(false);
					UI.getCurrent().addWindow(popupNuevoPrecio);
				});
				
				Popup popupConfirmar = new Popup();
				VerticalLayout layoutPopupConfirmar = new VerticalLayout();
				btn_eliminar.addClickListener(event-> {
					if(precio_seleccionado.getValue() == null)
						Notification.show("No ha elegido ningún precio especial");
					else {
						HorizontalLayout hlElementos = new HorizontalLayout();
						Button btn_confirmar = new Button("Confirmar");
						Button btn_cancelarEliminacion = new Button("Cancelar");
						layoutPopupConfirmar.removeAllComponents();
						hlElementos.addComponent(new Label("¿Está seguro de que desea eliminar el precio especial?"));
						hlElementos.addComponents(btn_confirmar,btn_cancelarEliminacion);
						layoutPopupConfirmar.addComponent(hlElementos);
						btn_confirmar.addClickListener(eConf -> {
							precioService.delete(precio_seleccionado.getValue());
							Notification.show("Se ha eliminado el precio especial");
							lista_precios_esp.clear();
							lista_precios_esp = precioService.findByApartamento(selection.getValue());
							precios.clearSortOrder();
							precios.setItems(lista_precios_esp);
							popupConfirmar.close();
						});
						btn_cancelarEliminacion.addClickListener(eCanc -> {
							popupConfirmar.close();
						});
						popupConfirmar.setWidth("700px");
						popupConfirmar.setHeight("100px");
						popupConfirmar.setPosition(550, 200);
						popupConfirmar.setContent(layoutPopupConfirmar);
						popupConfirmar.center(); 
						popupConfirmar.setDraggable(false);
						UI.getCurrent().addWindow(popupConfirmar);
					}
				});
				btn_aceptar.addClickListener(event -> {
					popupPrecios.close();
				});
				popupPrecios.setWidth("1000px");
				popupPrecios.setHeight("450px");
				popupPrecios.setPosition(550, 200);
				popupPrecios.setContent(layoutPopupPrecios);
				popupPrecios.center(); 
				popupPrecios.setDraggable(false);
				UI.getCurrent().addWindow(popupPrecios);
			}
			else
				Notification.show("No se ha seleccionado ningún apartamento");
		});
		// Listen changes made by the editor, refresh data from backend
		periodosNoDispBtn.addClickListener(clickEvent -> {
			
			if(selection.getValue()!= null) {
				lista_periodos = periodoService.findByApartamento(selection.getValue());
				HorizontalLayout hlBotones = new HorizontalLayout();
				Button btn_anadir = new Button("Añadir",FontAwesome.PLUS);
				Button btn_eliminar = new Button("Eliminar",FontAwesome.MINUS);
				Button btn_aceptar = new Button("Aceptar");
				Grid<Periodo> periodos = new Grid<>();
				periodos.setItems(lista_periodos);
				SingleSelect<Periodo> periodo_seleccionado = periodos.asSingleSelect();
				periodos.setWidth("660px");
				periodos.setHeight("250px");
				periodos.addColumn(Periodo::getFechaInicio).setCaption("Fecha inicio").setWidth(330).setResizable(false);
				periodos.addColumn(Periodo::getFechaFin).setCaption("Fecha fin").setWidth(330).setResizable(false);
				popupLayout.removeAllComponents();
				popupLayout.addComponent(new Label("Nombre apartamento: "+selection.getValue().getNombre()));
				popupLayout.addComponent(new Label("Periodos de no disponibilidad del apartamento (no se tienen en cuenta para "
						+ "reservas en estado Aceptada o Realizada)"));
				popupLayout.addComponent(periodos);
				hlBotones.addComponents(btn_anadir,btn_eliminar,btn_aceptar);
				hlBotones.setComponentAlignment(btn_anadir, Alignment.BOTTOM_RIGHT);
				hlBotones.setComponentAlignment(btn_aceptar, Alignment.BOTTOM_RIGHT);
				popupLayout.addComponent(hlBotones);
				popupLayout.setComponentAlignment(hlBotones, Alignment.BOTTOM_RIGHT);
				
				Popup popupNuevoPeriodo = new Popup();
				VerticalLayout layoutPopupPeriodo = new VerticalLayout();
				btn_anadir.addClickListener(e -> {
					HorizontalLayout hlFechas = new HorizontalLayout();
					DateField fechaInicio = new DateField("Inicio");
					DateField fechaFin = new DateField("Fin");
					Date hoy = java.sql.Date.valueOf(LocalDate.now());
					Button btn_nuevoPeriodo = new Button("Añadir",FontAwesome.PLUS);

					Button btn_cancelar = new Button("Cancelar");
					fechaInicio.setValue(LocalDate.now());
					fechaFin.setValue(LocalDate.now().plusDays(1));
					layoutPopupPeriodo.removeAllComponents();
					hlFechas.addComponents(fechaInicio, fechaFin, btn_nuevoPeriodo, btn_cancelar);
					layoutPopupPeriodo.addComponent(new Label("Nuevo periodo de no disponibilidad del apartamento"));
					layoutPopupPeriodo.addComponent(hlFechas);

					btn_nuevoPeriodo.addClickListener(event -> {
						Date fInicio, fFin;
						if(fechaInicio.isEmpty() || fechaFin.isEmpty())
							Notification.show("Compruebe que ha introducido una fecha con formato dd/mm/aa válido");
						else {
							fInicio = java.sql.Date.valueOf(fechaInicio.getValue());
							fFin =  java.sql.Date.valueOf(fechaFin.getValue());
							if(fInicio.after(fFin) || (fInicio.before(hoy) || fFin.before(hoy))
									|| fInicio.equals(fFin)) {
								Notification.show("Error en las fechas. Introduzca un intervalo válido");
							}
							else {
								boolean noExiste = periodoService.noExistePeriodo(fInicio, fFin, selection.getValue());
								if(!noExiste)
									Notification.show("El periodo introducido para dicho apartamento ya existe");
								else {
									Periodo periodo = new Periodo(fInicio, fFin, selection.getValue());
									periodoService.save(periodo);
									lista_periodos.clear();
									lista_periodos = periodoService.findByApartamento(selection.getValue());
									periodos.clearSortOrder();
									periodos.setItems(lista_periodos);
									Notification.show("Se ha añadido un nuevo periodo");
									popupNuevoPeriodo.close();
								}
							}
						}
					});
					btn_cancelar.addClickListener(event-> {
						popupNuevoPeriodo.close();
					});
					popupNuevoPeriodo.setWidth("700px");
					popupNuevoPeriodo.setHeight("200px");
					popupNuevoPeriodo.setPosition(550, 200);
					popupNuevoPeriodo.setContent(layoutPopupPeriodo);
					popupNuevoPeriodo.center(); 
					popupNuevoPeriodo.setDraggable(false);
					UI.getCurrent().addWindow(popupNuevoPeriodo);
				});
				Popup popupConfirmar = new Popup();
				VerticalLayout layoutPopupConfirmar = new VerticalLayout();
				btn_eliminar.addClickListener(event-> {
					if(periodo_seleccionado.getValue() == null)
						Notification.show("No ha elegido ningún periodo");
					else {
						HorizontalLayout hlElementos = new HorizontalLayout();
						Button btn_confirmar = new Button("Confirmar");
						Button btn_cancelarEliminacion = new Button("Cancelar");
						layoutPopupConfirmar.removeAllComponents();
						hlElementos.addComponent(new Label("¿Está seguro de que desea eliminar el periodo?"));
						hlElementos.addComponents(btn_confirmar,btn_cancelarEliminacion);
						layoutPopupConfirmar.addComponent(hlElementos);
						btn_confirmar.addClickListener(eConf -> {
							periodoService.delete(periodo_seleccionado.getValue());
							Notification.show("Se ha eliminado el periodo");
							lista_periodos.clear();
							lista_periodos = periodoService.findByApartamento(selection.getValue());
							periodos.clearSortOrder();
							periodos.setItems(lista_periodos);
							popupConfirmar.close();
						});
						btn_cancelarEliminacion.addClickListener(eCanc -> {
							popupConfirmar.close();
						});
						popupConfirmar.setWidth("600px");
						popupConfirmar.setHeight("100px");
						popupConfirmar.setPosition(550, 200);
						popupConfirmar.setContent(layoutPopupConfirmar);
						popupConfirmar.center(); 
						popupConfirmar.setDraggable(false);
						UI.getCurrent().addWindow(popupConfirmar);
					}
				});
				btn_aceptar.addClickListener(e -> {
					popupPeriodoNoDisp.close();
				});
				popupPeriodoNoDisp.setWidth("1000px");
				popupPeriodoNoDisp.setHeight("450px");
				popupPeriodoNoDisp.setPosition(550, 200);
				popupPeriodoNoDisp.setContent(popupLayout);
				popupPeriodoNoDisp.center(); 
				popupPeriodoNoDisp.setDraggable(false);
				UI.getCurrent().addWindow(popupPeriodoNoDisp);
			}
			else
				Notification.show("No se ha seleccionado ningún apartamento");
		});
		editor.setChangeHandler(() -> {
			if(editor.valido()) {
				editor.setVisible(false);
				listApartamentos(filter.getValue());
			}
		});
		nuevo.setChangeHandler(() -> {
			if(nuevo.valido()) {
				nuevo.setVisible(false);
				listApartamentos(filter.getValue());
			}
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
