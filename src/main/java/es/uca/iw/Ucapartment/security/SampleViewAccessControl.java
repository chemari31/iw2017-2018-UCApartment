package es.uca.iw.Ucapartment.security;

import org.springframework.stereotype.Component;

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;

/**
 * This demonstrates how you can control access to views.
 */
@Component
public class SampleViewAccessControl implements ViewAccessControl {

    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
    	
    	System.out.println("COMPROBANDO " + beanName + " PARA USUARIO CON ROLES: "+SecurityUtils.roles());

    	if(SecurityUtils.hasRole("ADMINISTRADOR")){
    		return true;
    	} else if (beanName.equals("registroScreen")){
            return true;
    	} else if(beanName.equals("loginScreen")) {
    		return true;
    	} else if(beanName.equals("home")) {
    		return true;
        } else if (beanName.equals("apartamentoView")) {
            return SecurityUtils.hasRole("ANFITRION") || SecurityUtils.hasRole("GERENTE");
        } else {
        	return false;
        }
    }
}



