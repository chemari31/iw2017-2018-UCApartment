package es.uca.iw.Ucapartment.email;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import es.uca.iw.Ucapartment.Reserva.Reserva;

public class EmailServiceImpl {
	
	private MailSender mailSender;
    private SimpleMailMessage templateMessage;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }
	
	public void enviaremailpropietario(Reserva r) {

        mailSender = getJavaMailSender();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(r.getApartamento().getUsuario().getEmail());
        msg.setSubject("UcApartment");
        msg.setText(
        		"Hola "+ r.getApartamento().getUsuario().getNombre() +" se ha realizado una reserva a tu apartamento " 
        				+ r.getApartamento().getNombre() + " desde " +r.getFechaInicio() + " hasta " + r.getFechaFin()
        				+ " a nombre de " +r.getUsuario().getNombre() + ". Debes acertarla o rechazarla lo más pronto posible."
           );
        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
	
	public void enviaremailpropietario2(Reserva r) {

        mailSender = getJavaMailSender();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(r.getApartamento().getUsuario().getEmail());
        msg.setSubject("UcApartment");
        msg.setText(
        		"Hola "+ r.getApartamento().getUsuario().getNombre() +" se ha realizado el pago a la reserva a tu apartamento " +
        				r.getApartamento().getNombre() + " a nombre de " +r.getUsuario().getNombre() + ". "
           );
        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }

	public void enviaremailpropietario3(Reserva r) {

        mailSender = getJavaMailSender();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(r.getApartamento().getUsuario().getEmail());
        msg.setSubject("UcApartment");
        msg.setText(
        		"Hola "+ r.getApartamento().getUsuario().getNombre() +" se ha cancelado una reserva a tu apartamento " +
        				r.getApartamento().getNombre() + " a nombre de " +r.getUsuario().getNombre() + ". "
           );
        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
	
	public void enviaremailcliente(Reserva r) {

        mailSender = getJavaMailSender();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(r.getUsuario().getEmail());
        msg.setSubject("UcApartment");
        msg.setText(
        		"Hola "+ r.getUsuario().getNombre() +" el propietario del apartamento ha aceptado o rechazado tu reserva en el apartamento " +
        				r.getApartamento().getNombre() + " ya puedes realizar el pago en el caso de que la haya aceptado. "
           );
        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }
	
	public void enviaremailcliente2(Reserva r) {

        mailSender = getJavaMailSender();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(r.getUsuario().getEmail());
        msg.setSubject("UcApartment");
        msg.setText(
        		"Hola "+ r.getUsuario().getNombre() +" has realizado el pago de tu reserva en el apartamento " +
        				r.getApartamento().getNombre() + " correctamente. "
           );
        try{
            this.mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }


	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtp.gmail.com");
	    mailSender.setPort(587);
	     
	    mailSender.setUsername("ingenieriaweb10@gmail.com");
	    mailSender.setPassword("Ingenieriaweb1");
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	
}
