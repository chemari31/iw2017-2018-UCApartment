package es.uca.iw.Ucapartment.email;

import es.uca.iw.Ucapartment.Reserva.Reserva;

public interface emailRepository {

	
	public void enviaremailpropietario(Reserva r);
	public void enviaremailpropietario2(Reserva r);
	public void enviaremailpropietario3(Reserva r);
	public void enviaremailcliente(Reserva r);




}
