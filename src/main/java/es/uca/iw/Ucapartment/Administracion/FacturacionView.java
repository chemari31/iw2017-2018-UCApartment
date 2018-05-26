package es.uca.iw.Ucapartment.Administracion;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;
import es.uca.iw.Ucapartment.Estado.EstadoService;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;

@SpringView(name = FacturacionView.VIEW_NAME)
public class FacturacionView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "facturacionView";
	

	private final ApartamentoService apartamentoService;
	private final EstadoService estadoService;
	
	@Autowired
	public FacturacionView(ApartamentoService aService, EstadoService eService) {
		this.apartamentoService = aService;
		this.estadoService = eService;
	}
	
	@PostConstruct
	void init() {
		
		Panel listaDatosPanel = new Panel("Facturación");
		VerticalLayout contenidoPanel = new VerticalLayout();
		
		HorizontalLayout hlApart, hlReservas, hlFact;
		Label lApart, lReservas, lFact;
		Label lvApart, lvReservas, lvFact;
		
		List<Apartamento> lista_apartamentos;
		List<Reserva> lista_reservas_realizadas;
		
		hlApart = new HorizontalLayout();
		hlReservas = new HorizontalLayout();
		hlFact = new HorizontalLayout();
		
		lApart = new Label("Total apartamentos alojados: ");
		lReservas = new Label("Total reservas realizadas: ");
		lFact = new Label("Facturación total de la empresa: ");
		
		lvApart = new Label();
		lvReservas = new Label();
		lvFact = new Label();
		
		lista_apartamentos = apartamentoService.findAll();
		lvApart.setValue(String.valueOf(lista_apartamentos.size()));
		
		lista_reservas_realizadas = estadoService.findReservaByEstado(Valor.REALIZADA);
		lvReservas.setValue(String.valueOf(lista_reservas_realizadas.size()));
		
		hlApart.addComponents(lApart,lvApart);
		hlReservas.addComponents(lReservas,lvReservas);
		hlFact.addComponents(lFact,lvFact);
		
		listaDatosPanel.setWidth("500px"); 
		listaDatosPanel.setHeight("200px");
		
		contenidoPanel.addComponents(hlApart, hlReservas, hlFact);
		
		listaDatosPanel.setContent(contenidoPanel);
		
		addComponent(listaDatosPanel);
		setComponentAlignment(listaDatosPanel, Alignment.TOP_CENTER);
	}
	
}
