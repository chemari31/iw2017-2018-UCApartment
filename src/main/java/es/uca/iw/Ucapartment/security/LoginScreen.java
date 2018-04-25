package es.uca.iw.Ucapartment.security;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.CustomComponent;

public class LoginScreen extends VerticalLayout 
{
	
	TextField userName = new TextField("Username");
	//userName.setIcon(VaadinIcons.USER);
	PasswordField password = new PasswordField("Password");
	Button sesion = new Button("Iniciar Sesion");
	
	

  
	public LoginScreen(LoginCallback callback) 
	{
		
		VerticalLayout layout = new VerticalLayout();
		Panel loginPanel = new Panel("<h1 style='color:blue; text-align: center;'>Login</h1>");
		loginPanel.setWidth("600px");
	    loginPanel.setHeight("500px");
	    layout.addComponent(loginPanel);
	    layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER);
        //setMargin(true);
        //setSpacing(true);
	    
	    loginPanel.setHeight(100.0f, Unit.PERCENTAGE);
		
		loginPanel.setWidth(null);
		
		
		
		//grid.addComponent(loginPanel);
		
		
		// Create a layout inside the panel
		final FormLayout loginLayout = new FormLayout();
		// Add some components inside the layout
		loginLayout.setWidth(500, Unit.PIXELS);
		loginLayout.addComponent(userName);
		loginLayout.addComponent(password);
		//loginLayout.addComponent(login);
		
		

		loginLayout.setMargin(true);
		
		// set required indicator for text fields
		userName.setRequiredIndicatorVisible(true);
		password.setRequiredIndicatorVisible(true);
		

        Button login = new Button("Login", evt -> {
            String pword = password.getValue();
            password.setValue("");
            if (!callback.login(userName.getValue(), pword)) {
                Notification.show("Login failed");
                userName.focus();
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        loginLayout.addComponent(login);   
        loginLayout.addComponent(sesion);
        loginLayout.setComponentAlignment(login, Alignment.MIDDLE_LEFT);
        loginLayout.setComponentAlignment(sesion, Alignment.MIDDLE_RIGHT);
        
        
        loginPanel.setContent(loginLayout);
		
		addComponent(layout);
        
    }

    @FunctionalInterface
    public interface LoginCallback {

        boolean login(String username, String password);
    }

	
    /**
  	 * 
  	 */
  	private static final long serialVersionUID = 5304492736395275231L;

	
}
