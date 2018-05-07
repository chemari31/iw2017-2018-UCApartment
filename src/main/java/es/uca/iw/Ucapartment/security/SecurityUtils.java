package es.uca.iw.Ucapartment.security;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import es.uca.iw.Ucapartment.Usuario.Usuario;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
    
    public static Collection<? extends GrantedAuthority> roles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(authentication != null ){
        	return authentication.getAuthorities();
        } else{
        	return null;
        }
    }
    
public static Usuario LogedUser() {
    	
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Usuario)authentication.getPrincipal();	
    }

}
