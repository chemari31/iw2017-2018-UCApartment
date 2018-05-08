package es.uca.iw.Ucapartment.Usuario;

import com.vaadin.ui.Button;

import com.vaadin.ui.Window;

public class PopupPago extends Window {
	
	
	private static final long serialVersionUID = 1L;

	public PopupPago() {
        super("Ventana emergente");
        center();
        
        setClosable(true);
        
        

        setContent(new Button("Cerrar", event -> close()));
    }
	
}
