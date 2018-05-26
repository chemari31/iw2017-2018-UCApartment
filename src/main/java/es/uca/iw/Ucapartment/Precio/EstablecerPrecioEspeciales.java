package es.uca.iw.Ucapartment.Precio;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.converter.StringToFloatConverter;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Iva.Iva;

public class EstablecerPrecioEspeciales extends Window{
	private final Apartamento apartamento;
	@Autowired
	PrecioService precioService;
	public EstablecerPrecioEspeciales(Apartamento apartamento, PrecioService precioService) {
		this.apartamento = apartamento;
		this.precioService = precioService;
		Window subWindow = new Window("Precios Especiales");
        VerticalLayout subContent = new VerticalLayout();
        subWindow.setContent(subContent);
        /* Action buttons */
    	Button save = new Button("Save", FontAwesome.SAVE);
    	Button cancel = new Button("Cancel");
    	/* Layout for buttons */
    	CssLayout actions = new CssLayout(save, cancel);
    	DateField fechaInicio = new DateField("Fecha Inicio");
    	DateField fechaFin = new DateField("Fecha Fin");
    	TextField precio = new TextField("Precio Especial");
    	HorizontalLayout form = new HorizontalLayout(fechaInicio, fechaFin, precio);
    	
    	Binder<Precio> binder = new Binder<>(Precio.class);
    	
    	subContent.addComponent(form);
    	subContent.addComponent(actions);
    	
    	subWindow.center();
    	UI.getCurrent().addWindow(subWindow);
    	
    	binder.forField(precio)
		  .asRequired("No puede estar vacío")
		  .withConverter(
		    new StringToDoubleConverter("Por favor introduce un número decimal"))
		  .bind(Precio::getValor, Precio::setValor);
    	
    	save.addClickListener(e -> { if (binder.isValid()) { Precio pre = new Precio(Double.parseDouble(precio.getValue().replace(',', '.')), 
    			java.sql.Date.valueOf(fechaInicio.getValue()), java.sql.Date.valueOf(fechaFin.getValue()), null, apartamento);
    			if(pre.getFecha_fin().compareTo(pre.getFecha_inicio()) > 0) {
    				precioService.save(pre); subWindow.close();}
    			else
    				Notification.show("La fecha de fin no puede ser anterior a la fecha de inicio"); 
    		}else Notification.show("Compruebe los campos");});
    	
    	cancel.addClickListener(e -> subWindow.close());
	}

}
