package es.uca.iw.Ucapartment.Administracion;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Apartamento.ApartamentoService;
import es.uca.iw.Ucapartment.Estado.EstadoService;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Reserva.Reserva;
import es.uca.iw.Ucapartment.Transaccion.Transaccion;
import es.uca.iw.Ucapartment.Transaccion.TransaccionService;

@SpringView(name = FacturacionView.VIEW_NAME)
public class FacturacionView extends VerticalLayout implements View{
	
	public static final String VIEW_NAME = "facturacionView";
	

	private final ApartamentoService apartamentoService;
	private final EstadoService estadoService;
	private final TransaccionService transaccionService;
	
	@Autowired
	public FacturacionView(ApartamentoService aService, EstadoService eService,
			TransaccionService tService) {
		this.apartamentoService = aService;
		this.estadoService = eService;
		this.transaccionService = tService;
	}
	
	@PostConstruct
	void init() {
		
		Panel listaDatosPanel = new Panel("Facturación");
		VerticalLayout contenidoPanel = new VerticalLayout();
		
		HorizontalLayout hlApart, hlReservas, hlFact, hlFiltroFact;
		Label lApart, lReservas, lFact;
		Label lvApart, lvReservas, lvFact;
		
		Button btn_filtrar = new Button("Filtrar");
		
		DateField dfInicio = new DateField("Fecha inicio");
		DateField dfFin = new DateField("Fecha fin");

		
		dfInicio.setVisible(false);
		dfFin.setVisible(false);
		btn_filtrar.setVisible(false);
		
		List<Apartamento> lista_apartamentos;
		List<Reserva> lista_reservas_realizadas;
		List<Transaccion> lista_transacciones;
		
		ComboBox<String> cbFiltro = new ComboBox<>("Filtrar por: ");
		cbFiltro.setItems("Total beneficio","Intervalo fechas");
		cbFiltro.setEmptySelectionAllowed(false);
		cbFiltro.setSelectedItem("Total beneficio");
		
		hlApart = new HorizontalLayout();
		hlReservas = new HorizontalLayout();
		hlFact = new HorizontalLayout();
		hlFiltroFact = new HorizontalLayout();
		
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
		
		double beneficio = transaccionService.SumaImporteCuenta(new Long(0));
		lvFact.setValue(String.valueOf(beneficio) + " €");
		
		cbFiltro.addValueChangeListener(event -> {
			if(cbFiltro.isSelected("Total beneficio")) {
				dfInicio.setVisible(false);
				dfFin.setVisible(false);
				btn_filtrar.setVisible(false);
				double totalBeneficio = transaccionService.SumaImporteCuenta(new Long(0));
				lvFact.setValue(String.valueOf(totalBeneficio) + " €");
			}
			if(cbFiltro.isSelected("Intervalo fechas")) {
				dfInicio.setVisible(true);
				dfFin.setVisible(true);
				btn_filtrar.setVisible(true);
				btn_filtrar.addClickListener(e -> {
					double totalBeneficio = transaccionService.beneficioCuentaIntervalo(
							new Long(0), 
							dfInicio.isEmpty() ? null : java.sql.Date.valueOf(dfInicio.getValue()),
							dfFin.isEmpty() ? null : java.sql.Date.valueOf(dfFin.getValue())
							);
					lvFact.setValue(String.valueOf(totalBeneficio) + " €");
				});
			}
		});
		
		//lista_transacciones = transaccionService.findByCuentaOrigen(new Long(0));

		
		hlApart.addComponents(lApart,lvApart);
		hlReservas.addComponents(lReservas,lvReservas);
		hlFact.addComponents(lFact,lvFact);
		hlFiltroFact.addComponents(cbFiltro, dfInicio, dfFin, btn_filtrar);
		
		listaDatosPanel.setWidth("700px"); 
		listaDatosPanel.setHeight("300px");
		
		contenidoPanel.addComponents(hlApart, hlReservas, hlFact, hlFiltroFact);
		
		listaDatosPanel.setContent(contenidoPanel);
		
		addComponent(listaDatosPanel);
		setComponentAlignment(listaDatosPanel, Alignment.TOP_CENTER);
	}
	
}
