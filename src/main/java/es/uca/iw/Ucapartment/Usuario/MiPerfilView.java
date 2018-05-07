package es.uca.iw.Ucapartment.Usuario;



import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Reserva.ReservaRepository;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringView(name = MiPerfilView.VIEW_NAME)
public class MiPerfilView extends VerticalLayout implements View
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME="MiPerfil";
	Usuario user = SecurityUtils.LogedUser();
	
	
	@Autowired
	private ReservaRepository repoReserva;
	@Autowired
	private EstadoRepository repoEstado;
	private Apartamento apart = null;
	private Estado estado = null;
	Button pendiente = new Button("Pendiente");
	Button pago;
	Button cancelada = new Button("Cancelada");
	private int contReserva = 1;
	VerticalLayout vertical = new VerticalLayout();
	VerticalLayout popupVertical = new VerticalLayout();
	Button[] open = new Button[100];//Vector para los botones de los valores Aceptados
	
	
	@PostConstruct
	void init()
	{
		//Datos Personales de los Usuarios
		addComponents(new Label("<h2>Datos Personales</h2>"));
		addComponents(new Label(user.getApellidos()));
		addComponents(new Label(user.getNombre()));
		addComponents(new Label(user.getDni()));
		addComponents(new Label(user.getEmail()));
		addComponents(new Label("Mis Reservas"));
		HorizontalLayout menuSuperior;
		
		//Historial de Reserva de los usuarios
		try {
			for(Reserva reserva : repoReserva.findByUsuario(user))
			{
				menuSuperior = new HorizontalLayout();
				menuSuperior.addComponent(new Label("Reserva "+ contReserva + ": "));
				contReserva++;
				apart = reserva.getApartamento();
				menuSuperior.addComponent(new Label("Nombre: "));
				menuSuperior.addComponents(new Label(apart.getNombre()));
				menuSuperior.addComponents(new Label("Fecha: "));
				estado = repoEstado.findByReserva(reserva);
				menuSuperior.addComponent(new Label(String.valueOf(estado.getFecha())));
				menuSuperior.addComponents(new Label("Estado: "));
				//Vizualizar los estados de las reservas
				if(estado.getValor() == Valor.PENDIENTE )
				{
					menuSuperior.addComponent(pendiente);
				}
				
				//Toda reserva Aceptada estará a la espera de la realizacion del pago
				//Se muestrará un Popup para la gestion del pago
				if(estado.getValor() == Valor.ACEPTADA)
				{
					open[contReserva] = new Button("Realizar Reserva");
					open[contReserva].addClickListener(event -> {
						PopupPago sub = new PopupPago();
						popupVertical.addComponent(new TextField("Introduzca su numero de cuenta","xxx-xxx-xxx-xxx"));
						sub.setWidth("400px");
						sub.setHeight("300px");
						sub.setPosition(550, 200);
						sub.setContent(popupVertical);
						sub.center();
						UI.getCurrent().addWindow(sub);
								
					});
					menuSuperior.addComponent(open[contReserva]);
					
				}
				if(estado.getValor() == Valor.CANCELADA)
				{
					menuSuperior.addComponent(cancelada);
				}
				
				vertical.addComponents(menuSuperior);

			}
		//En caso de que el usuario no tenga reservas
		}catch(Exception e) {addComponents(new Label("No tienes ninguna reserva"));}

		addComponents(vertical);
		addComponents(popupVertical);
	
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}
