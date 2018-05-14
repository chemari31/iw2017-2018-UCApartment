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
	@Autowired
	private ReservaRepository repoReserva;
	@Autowired
	private EstadoRepository repoEstado;
	
	@Autowired
	private ReservaService serviceReserva;
	private Apartamento apart = null;
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
	
	
	
	
	@PostConstruct
	public void init()
	{
		
		
		//Label principal
		addComponents(new Label("Historial de Reserva"));
		ButtonRenderer button = new ButtonRenderer("Pulsa");
		
		//Lista de reserva
		listReserva = repoReserva.findByUsuario(user);
		
		//Accion del button aceptar
		aceptar.addClickListener(event -> {
			sub.close();
		});
		
		
		
		
		
		
		Grid<Reserva> grid = new Grid<>();
		grid.setItems(listReserva);
		grid.addColumn(Reserva::getFecha).setCaption("Fecha");
		grid.addColumn(reserva -> {
			  apart = reserva.getApartamento();
			  
			  return apart.getNombre();
			}).setCaption("Apartamento");
		
		grid.addColumn(e ->{
			estado = repoEstado.findByReserva(e);
			return estado.getValor();	
		}, new ButtonRenderer(clickEvent-> {
			
			Reserva r = ((Reserva) clickEvent.getItem());
			estado = repoEstado.findByReserva((Reserva) clickEvent.getItem());
			if(estado.getValor() == Valor.ACEPTADA)
			{
				popupLayout.removeAllComponents();
				popupLayout.addComponent(new TextField("Introduzca su numero de cuenta","xxx-xxx-xxx-xxx"));
				horizontal = new HorizontalLayout();
				horizontal.addComponent(aceptar);
				cancelar.addClickListener(event ->{
					sub.close();
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
					sub.close();
				});
				
				ModificarReserva.addClickListener(event ->{
					
					listReserva2 = repoReserva.findByApartamento(apart);
					Date entrada = java.sql.Date.valueOf(ida.getValue());
					Date salida = java.sql.Date.valueOf(vuelta.getValue());
					int correcto = 0;
					for(Reserva re : listReserva2)
					{
						
						if(!(entrada.compareTo(re.getFechaInicio())>=0 && salida.compareTo(re.getFechaFin()) <= 0))
						{
							
							if(!(entrada.compareTo(re.getFechaInicio())<0 && salida.compareTo(re.getFechaInicio()) > 0))
							{
								
								if((entrada.compareTo(hoy)>=0 && salida.compareTo(entrada) > 0))
								{
									correcto++;
								}
								else
								{
									Notification.show("no se puede modificar a esa fecha", Notification.Type.WARNING_MESSAGE);
										
								}	
							}
							else
							{
								Notification.show("no se puede modificar a esa fecha", Notification.Type.WARNING_MESSAGE);
								
							}
							
						}
						else
						{
							Notification.show("no se puede modificar a esa fecha", Notification.Type.WARNING_MESSAGE);
							
						}
							
					}
					
					
					if(correcto == listReserva2.size())
					{
						r.setFechaInicio(entrada);
						r.setFechaFin(salida);
						r.setFecha(hoy);
						serviceReserva.save(r);
						
						
						Notification.show("Su reserva se ha modificado", Notification.Type.WARNING_MESSAGE);
						sub.close();
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
		
		
				
				
			
		
		layout.addComponent(grid);
		addComponent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	} 
}
