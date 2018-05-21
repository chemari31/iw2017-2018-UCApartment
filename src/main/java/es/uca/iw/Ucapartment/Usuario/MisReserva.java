package es.uca.iw.Ucapartment.Usuario;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
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
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaRepository;
import es.uca.iw.Ucapartment.Reserva.ReservaService;
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
	private ReservaRepository repoReserva;
	@Autowired
	private EstadoRepository repoEstado;
	@Autowired
	private EstadoService serviceEstado;
	
	@Autowired
	private ReservaService serviceReserva;
	private Apartamento apart = null;
	private static Reserva r = null;
	private Estado estado = null;
	private String i;
	private VerticalLayout layout = new VerticalLayout();
	private VerticalLayout popupLayout = new VerticalLayout();
	private HorizontalLayout horizontal;
	private PopupPago sub = new PopupPago();
	private Button aceptar = new Button("Aceptar");
	private Button incidencia = new Button("Enviar Incidencia");
	private Button cancelar = new Button("Cancelar Reserva");
	private Button cancelarIncidencia = new Button("Cancelar");
	private Button ModificarReserva = new Button("Modificar Reserva");
	DateField ida = new DateField("Entrada");
	DateField vuelta = new DateField("Salida");
	Date hoy = java.sql.Date.valueOf(LocalDate.now());
	
	
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
		grid.setWidth("650px");
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
				popupLayout.addComponent(new TextField("Introduzca su numero de cuenta","xxx-xxx-xxx-xxx"));
				horizontal = new HorizontalLayout();
				horizontal.addComponent(aceptar);
				cancelar.addClickListener(event ->{
					estado.setValor(Valor.CANCELADA);
					serviceEstado.save(estado);
					sub.close();
					layout.removeAllComponents();
					init();
				});

				horizontal.addComponent(cancelar);
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
						System.out.println("Entro en si");
						System.out.println(r.getId());
						r.setFechaInicio(entrada);
						r.setFechaFin(salida);
						r.setFecha(hoy);
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
				incidencia.addClickListener(event ->{
					sub.close();
				});
				cancelarIncidencia.addClickListener(event ->{
					sub.close();
				});
				horizontal.addComponent(cancelarIncidencia);
				popupLayout.addComponent(horizontal);
				sub.setWidth("600px");
				sub.setHeight("400px");
				sub.setPosition(550, 200);
				sub.setContent(popupLayout);
				sub.center();
				UI.getCurrent().addWindow(sub);
			}

				})).setCaption("Estado");
		
		
				
				
		
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	} 
}
