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

    	if(SecurityUtils.hasRole("ADMINISTRADOR"))
    	{
    		return true;
    	} 
    	else if (beanName.equals("registroScreen"))
    	{
    		if(!SecurityUtils.isLoggedIn())
    			return true;
    		else
    			return false;
    	} 
    	else if(beanName.equals("loginScreen")) 
    	{
    		if(!SecurityUtils.isLoggedIn())
    			return true;
    		else
    			return false;
    	} 
    	else if(beanName.equals("home")) 
    	{
    		return true;
        } 
    	else if (beanName.equals("apartamentoManagementView")) 
    	{
            return SecurityUtils.hasRole("ANFITRION") || SecurityUtils.hasRole("GERENTE");
        }
    	else if(beanName.equals("MiPerfil"))
    	{
    		return SecurityUtils.hasRole("ANFITRION") || SecurityUtils.hasRole("GERENTE");
    	}
    	else if(beanName.equals("MisReserva"))
    	{
    		return SecurityUtils.hasRole("ANFITRION") || SecurityUtils.hasRole("GERENTE");
    	}
    	else if(beanName.equals("apartamentoView")) 
    	{
        	return true;
        } 
    	else 
    	{
        	return false;
        }
    }
}



