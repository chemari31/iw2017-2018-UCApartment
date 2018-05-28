package es.uca.iw.Ucapartment.Usuario;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.validator.LongRangeValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoView;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.EstadoService;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Iva.Iva;
import es.uca.iw.Ucapartment.Iva.IvaRepository;
import es.uca.iw.Ucapartment.Precio.PrecioService;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaRepository;
import es.uca.iw.Ucapartment.Reserva.ReservaService;
import es.uca.iw.Ucapartment.Transaccion.Transaccion;
import es.uca.iw.Ucapartment.Transaccion.TransaccionRepository;
import es.uca.iw.Ucapartment.Valoracion.Valoracion;
import es.uca.iw.Ucapartment.Valoracion.ValoracionRepository;
import es.uca.iw.Ucapartment.Valoracion.ValoracionService;
import es.uca.iw.Ucapartment.email.EmailServiceImpl;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = MisReserva.VIEW_NAME)
public class MisReserva extends VerticalLayout implements View
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String VIEW_NAME ="MisReserva";
	
	//Lista de mis reserva
	private Usuario user = SecurityUtils.LogedUser();
	private List<Reserva> listReserva = new ArrayList<Reserva>();
	private List<Reserva> listReserva2 = new ArrayList<Reserva>();
	private List<Reserva> listReservaFinal = new ArrayList<Reserva>();
	
	@Autowired
	private PrecioService precioService;
	@Autowired
	private ReservaRepository repoReserva;
	@Autowired
	private EstadoRepository repoEstado;
	@Autowired
	private EstadoService serviceEstado;
	@Autowired
	private IvaRepository repoIva;
	@Autowired
	private ValoracionRepository repoValoracion;
	@Autowired
	private ValoracionService serviceValoracion;
	@Autowired
	private ReservaService serviceReserva;
	
	private List<Valoracion> valoraciones;
	private Apartamento apart = null;
	private Reserva rese = null;
	private static Reserva r = null;
	private Estado estado = null;
	private String i;
	private VerticalLayout layout = new VerticalLayout();
	private VerticalLayout popupLayout = new VerticalLayout();
	private HorizontalLayout horizontal;
	private PopupPago sub = new PopupPago();
	private Button factura = new Button("Generar Factura");
	private Button aceptar = new Button("Aceptar");
	private Button pagar = new Button("Pagar");
	private Button incidencia = new Button("Enviar Incidencia");
	private Button cancelar = new Button("Cancelar Reserva");
	private Button cancelarIncidencia = new Button("Cancelar");
	private Button ModificarReserva = new Button("Modificar Reserva");
	DateField ida = new DateField("Entrada");
	DateField vuelta = new DateField("Salida");
	Date hoy = java.sql.Date.valueOf(LocalDate.now());
	private EmailServiceImpl correo = new EmailServiceImpl();

	
	
	@Autowired
	public MisReserva() {}
	
	
	@PostConstruct
	public void init()
	{
		System.out.println("entro en init()");
		
		
		//Label principal
		Label historial = new Label("Historial de Reserva");
		ButtonRenderer button = new ButtonRenderer("Pulsa");
		
		//Lista de reserva
		listReserva = repoReserva.findByUsuario(user);
		
		//Accion del button aceptar
		aceptar.addClickListener(event -> {
			sub.close();
		});
		
		
		
		
		
		Grid<Reserva> grid = new Grid<>();	
		layout.addComponent(historial);
		layout.addComponent(grid);
		
		
		
		grid.setItems(listReserva);
		grid.setWidth("755px");
		grid.addColumn(Reserva::getFecha).setCaption("Fecha");
		grid.addColumn(reserva -> {
			  apart = reserva.getApartamento();
			  
			  return apart.getNombre();
			}).setCaption("Apartamento");
		grid.addColumn(Reserva::getPrecio).setCaption("Precio");
		
		grid.addColumn(e ->{
			estado = repoEstado.findByReserva(e);
			return estado.getValor();	
		}, new ButtonRenderer(clickEvent-> {
			
			r = ((Reserva) clickEvent.getItem());
			long idReserva = r.getId();
			
			
			
			estado = repoEstado.findByReserva((Reserva) clickEvent.getItem());
			if(estado.getValor() == Valor.ACEPTADA)
			{
				popupLayout.removeAllComponents();
				TextField cuenta = new TextField("Introduzca su numero de cuenta (16 dígitos)","xxxx-xxxx-xxxx-xxxx");
				cuenta.setRequiredIndicatorVisible(true);
				cuenta.setMaxLength(16);
				popupLayout.addComponent(cuenta);
				Binder<Transaccion> bind = new Binder<>(Transaccion.class);
				bind.forField(cuenta)
				 .asRequired("El campo Cuenta es obligatorio")
				 .withConverter(new StringToLongConverter("Por favor introduzca un número"))
				 .withValidator(new LongRangeValidator("El número de cuenta debe tener 16 dígitos", 0000000000000005L, 9999999999999999L))
				 .bind(Transaccion::getCuentaOrigen, Transaccion::setCuentaOrigen);
				horizontal = new HorizontalLayout();
				cancelar.addClickListener(event ->{
					estado.setValor(Valor.CANCELADA);
					serviceEstado.save(estado);
					sub.close();
					layout.removeAllComponents();
					init();
				});

				horizontal.addComponent(cancelar);
				
				pagar.addClickListener(event -> {
					if(bind.isValid()) {
						Long lg = null;
						serviceReserva.pasarelaDePago(r.getPrecio(), r,estado, lg.valueOf(cuenta.getValue()));
						
						correo.enviaremailpropietario2(r);
						correo.enviaremailcliente2(r);
						
						sub.close();
						layout.removeAllComponents();
						init();
					} else
						Notification.show("Comprueba el número de cuenta");
				});
				horizontal.addComponent(pagar);
				popupLayout.addComponent(horizontal);
				sub.setWidth("400px");
				sub.setHeight("300px");
				sub.setPosition(550, 200);
				sub.setContent(popupLayout);
				sub.center();
				UI.getCurrent().addWindow(sub);
				
			}
			if(estado.getValor() == Valor.PENDIENTE)
			{
				
				ida.setValue(r.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				vuelta.setValue(r.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
				
				Usuario userA;
				apart = ((Reserva) clickEvent.getItem()).getApartamento();
				userA = apart.getUsuario();
				popupLayout.removeAllComponents();
				popupLayout.addComponent(new Label("El usuario "+userA.getNombre() + " esta pediente de confirmación"));
				horizontal = new HorizontalLayout();
				horizontal.addComponent(ida);
				horizontal.addComponent(vuelta);
				horizontal.addComponent(aceptar);
				cancelar.addClickListener(event ->{
					estado.setValor(Valor.CANCELADA);
					serviceEstado.save(estado);
					
					correo.enviaremailpropietario3(r);

					sub.close();
					layout.removeAllComponents();
					init();
				});
				
				ModificarReserva.addClickListener(event ->{
					
					//System.out.println(idReserva);
					listReserva2 = repoReserva.findByApartamento(apart);
					for(Reserva res : listReserva2)
					{
						
						if(res.getId()!=idReserva)
						{
							System.out.println(idReserva);
							listReservaFinal.add(res);
						}	
					}
					Date entrada = java.sql.Date.valueOf(ida.getValue());
					Date salida = java.sql.Date.valueOf(vuelta.getValue());
					int correcto = 0;
					for(Reserva re : listReservaFinal)
					{
						System.out.println("entro en el bucle");
						if(!(entrada.compareTo(re.getFechaInicio())>=0 && salida.compareTo(re.getFechaFin()) <= 0))
						{
							System.out.println("entro en el 1 if");
							if(!(entrada.compareTo(re.getFechaInicio())<0 && salida.compareTo(re.getFechaInicio()) > 0))
							{
								System.out.println("entro en el 2 if");
								System.out.println(entrada);
								System.out.println(re.getFechaInicio());
								System.out.println(re.getFechaFin());
								
								if(!(entrada.compareTo(re.getFechaInicio())>=0 && entrada.compareTo(re.getFechaFin()) < 0))
								{
									System.out.println("entro en el 3 if");
									if((entrada.compareTo(hoy)>=0 && salida.compareTo(entrada) > 0))
									{
										System.out.println("entro en correcto");
										correcto++;
									}
								}
							}
						}						
					}
					
					
					if(correcto == listReservaFinal.size() && (entrada.compareTo(hoy)>=0 && salida.compareTo(entrada) > 0) )
					{
						System.out.println("empieza "+r.getPrecio());
						double precioTotalSinIva = precioService.TotalPrecio(apart, entrada, salida);
						System.out.println(precioTotalSinIva);
						double totalCancelacion = 0.10 * r.getPrecio();
						System.out.println(totalCancelacion);
						Iva iva = repoIva.findByPais("es");
						double porcentaje = (double)iva.getPorcentaje()/100;
						porcentaje = porcentaje * precioTotalSinIva;
						System.out.println(porcentaje);
						double precioTotal = precioTotalSinIva + porcentaje + totalCancelacion;
						System.out.println(precioTotal);
						System.out.println("Entro en si");
						System.out.println(r.getId());
						r.setFechaInicio(entrada);
						r.setFechaFin(salida);
						r.setFecha(hoy);
						r.setPrecio(precioTotal);
						serviceReserva.save(r);
						sub.close();
						layout.removeAllComponents();
						Notification.show("Su reserva se ha modificado", Notification.Type.WARNING_MESSAGE);
						
						getUI().getNavigator().navigateTo(MisReserva.VIEW_NAME);
						
					}
					else
					{
						System.out.println("Entro en no");
						System.out.println(r.getId());
						sub.close();
						layout.removeAllComponents();
						Notification.show("Su Reserva no se ha podido modificar", Notification.Type.WARNING_MESSAGE);
						
						getUI().getNavigator().navigateTo(MisReserva.VIEW_NAME);
						
					}
	
				});
				
				horizontal.addComponent(cancelar);
				horizontal.addComponent(ModificarReserva);
				popupLayout.addComponent(horizontal);
				popupLayout.addComponent(new Label("Cualquier modificación de la reserva se le penalizara"
						+ " con un coste del 10% del actual precio"));
				sub.setWidth("900px");
				sub.setHeight("300px");
				sub.setPosition(550, 200);
				sub.setContent(popupLayout);
				sub.center();
				UI.getCurrent().addWindow(sub);
			}
			
			
			if(estado.getValor() == Valor.CANCELADA)
			{
				popupLayout.removeAllComponents();
				popupLayout.addComponent(new Label("La reserva fue cancelada el día "+r.getFecha()));
				popupLayout.addComponent(aceptar);
				sub.setWidth("600px");
				sub.setHeight("300px");
				sub.setPosition(550, 200);
				sub.setContent(popupLayout);
				sub.center();
				UI.getCurrent().addWindow(sub);
			}
			if(estado.getValor() == Valor.REALIZADA)
			{
				
				popupLayout.removeAllComponents();
				popupLayout.addComponent(new Label("La reserva con fecha "+r.getFecha() + "Ya fue realizada"));
				TextArea area = new TextArea("Incidencia");
				area.setValue("Escriba aquí su incidencia");
				area.setSizeFull();
				popupLayout.addComponent(area);
				NativeSelect<String> nivel = new NativeSelect<>("Grado");
				
				nivel.setItems("1", "2","3","4","5");
				nivel.setValue("1");
				popupLayout.addComponent(nivel);
				horizontal = new HorizontalLayout();
				horizontal.addComponent(incidencia);
				incidencia.addClickListener(event ->
				{
					System.out.println("entro en grid");
					rese = ((Reserva) clickEvent.getItem());
					apart = ((Reserva) clickEvent.getItem()).getApartamento();
					String comentario = area.getValue();
					int valor = Integer.parseInt(nivel.getValue());
					Date hoy = java.sql.Date.valueOf(LocalDate.now());
					Valoracion v = new Valoracion(comentario, valor,hoy,user,apart,rese);
					serviceValoracion.save(v);
					Notification.show("Su comentario se ha realizado satisfactoriamente", Notification.Type.HUMANIZED_MESSAGE );	
					sub.close();
					layout.removeAllComponents();
					getUI().getNavigator().navigateTo(MisReserva.VIEW_NAME);
				});
				cancelarIncidencia.addClickListener(event ->{
					sub.close();
				});
				factura.addClickListener(event ->{
					serviceReserva.generarfactura(r);
				});
				
				horizontal.addComponent(cancelarIncidencia);
				horizontal.addComponent(factura);
				popupLayout.addComponent(horizontal);
				sub.setWidth("600px");
				sub.setHeight("400px");
				sub.setPosition(550, 200);
				sub.setContent(popupLayout);
				sub.center();
				UI.getCurrent().addWindow(sub);
			}

				})).setCaption("Estado");
		
		//Columna boton valoraciones
		grid.addColumn(reserva -> "Valoraciones", new ButtonRenderer(clickEvent -> {
			r = ((Reserva) clickEvent.getItem());
			valoraciones = serviceValoracion.findByReserva(r);
			if(valoraciones.size() == 0) {
				popupLayout.removeAllComponents();
				popupLayout.addComponent(new Label("No se han realizado valoraciones sobre"
					+ " esta reserva"));
				popupLayout.addComponent(aceptar);
				popupLayout.setComponentAlignment(aceptar, Alignment.BOTTOM_RIGHT);
				sub.setWidth("500px");
				sub.setHeight("150px");
				sub.setPosition(550, 200);
				sub.setContent(popupLayout);
				sub.center();
				UI.getCurrent().addWindow(sub);
			}
			else {
				valoraciones.sort((o1,o2) -> o1.getFecha().compareTo(o2.getFecha()));
				Grid<Valoracion> valGrid = new Grid();
				valGrid.setItems(valoraciones);
				valGrid.setWidth("900px");
				valGrid.setHeight("250px");
				valGrid.addColumn(Valoracion::getFecha).setCaption("Fecha").setWidth(150);
				valGrid.addColumn(Valoracion->Valoracion.getUsuario().getNombreUsuario())
					.setCaption("Usuario").setWidth(200);
				valGrid.addColumn(Valoracion::getDescripcion).setCaption("Descripcion").setWidth(800);
				popupLayout.removeAllComponents();
				popupLayout.addComponent(new Label("Valoraciones realizadas sobre la reserva"));
				popupLayout.addComponent(valGrid);
				popupLayout.addComponent(aceptar);
				popupLayout.setComponentAlignment(aceptar, Alignment.BOTTOM_RIGHT);
				sub.setWidth("1000px");
				sub.setHeight("400px");
				sub.setPosition(550, 200);
				sub.setContent(popupLayout);
				sub.center();
				UI.getCurrent().addWindow(sub);
			}
		})).setCaption("Valoraciones");		
				
				
		
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	} 
}
