package es.uca.iw.Ucapartment.Reserva;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

//import org.jsoup.nodes.Document;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.ui.Notification;


import java.sql.Date;
import java.time.LocalDate;
import javax.transaction.Transactional;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.uca.iw.Ucapartment.Estado.Estado;
import es.uca.iw.Ucapartment.Estado.EstadoRepository;
import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Transaccion.Transaccion;
import es.uca.iw.Ucapartment.Transaccion.TransaccionService;
import es.uca.iw.Ucapartment.Usuario.Usuario;
import es.uca.iw.Ucapartment.Apartamento.Apartamento;


@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository repo;
	
	@Autowired
	private EstadoRepository repoEstado;
	
	@Autowired
	private TransaccionService transaccionService;
	
	public Reserva save(Reserva reserva)
	{
		return repo.save(reserva);
	}
	

	@Transactional
	public void pasarelaDePago(double total, Reserva r, Estado e, Long cuentaOrigen)
	{
		Long cuentaEmpresa = 0000000000000000L;
		Long cuentaAnfitrion = 0000000000000002L + (long) (Math.random() * (9999999999999999L - 0000000000000003L));;
		Long cuentaSS = 0000000000000001L;
		double beneficioEmpresa = total * 0.05;
		double beneficioAnfitrion = total * 0.74;
		double beneficioMontoro = total * 0.21;
		
		Transaccion transaccionEmpresa = new Transaccion(beneficioEmpresa, cuentaOrigen, cuentaEmpresa, r);
		Transaccion transaccionAnfitrion = new Transaccion(beneficioAnfitrion, cuentaOrigen, cuentaAnfitrion, r);
		Transaccion transaccionIva = new Transaccion(beneficioMontoro, cuentaOrigen, cuentaSS, r);
		
		transaccionService.save(transaccionEmpresa);
		transaccionService.save(transaccionAnfitrion);
		transaccionService.save(transaccionIva);
		/*r.setBenenficioEmpresa(beneficioEmpresa);
		r.setBenenficioAnfitrion(beneficioAnfitrion);*/
		e.setValor(Valor.REALIZADA);
		repoEstado.save(e);
		repo.save(r);
		
	
	}
	

	public List<Reserva> findByApartamento(Apartamento apart){
		return repo.findByApartamento(apart);
	}
	
	public Reserva findById(Long id) {
		return repo.findById(id);
	}
	
	public List<Reserva> findAll() {
		return repo.findAll();
	}
	
	public List<Reserva> findByUsuario(Usuario usuario) {
		return repo.findByUsuario(usuario);
	}

	public void generarfactura(Reserva reserva)
	{	
		try {
			Document doc = new Document();
			FileOutputStream fichero = new FileOutputStream("facturas/"+reserva.getId()+"factura.pdf");
			
			PdfWriter.getInstance(doc, fichero).setInitialLeading(40);
			doc.open();
			
			doc.add(new Paragraph("UcaApartament-Factura"));
			doc.add(new Paragraph("_____________________"));
			doc.add(new Paragraph("Nombre de apartamento: " + reserva.getApartamento().getNombre()));
			doc.add(new Paragraph("Dirección: " + reserva.getApartamento().getCalle() + " " + reserva.getApartamento().getNumero()));
			doc.add(new Paragraph("Ciudad: " + reserva.getApartamento().getCiudad()));
			doc.add(new Paragraph("Propietario: " + reserva.getApartamento().getUsuario().getNombre() + " " + reserva.getApartamento().getUsuario().getApellidos()));
			doc.add(new Paragraph("DNI: " + reserva.getApartamento().getUsuario().getDni()));
			doc.add(new Paragraph("_____________________"));
			doc.add(new Paragraph("Arrendatario: " + reserva.getUsuario().getNombre() + " " + reserva.getUsuario().getApellidos()));
			doc.add(new Paragraph("DNI: " + reserva.getUsuario().getDni()));
			doc.add(new Paragraph("_____________________"));
			doc.add(new Paragraph("Fecha inicio de reserva: " + reserva.getFechaInicio()));
			doc.add(new Paragraph("Fecha fin de la reserva: " + reserva.getFechaFin()));
			doc.add(new Paragraph("La reserva se ha realizado a fecha de: " + reserva.getFecha() + " con un precio total de " + reserva.getPrecio() + " euros."));

			doc.close();
			Notification.show("Factura generada.");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Notification.show("Se produjo un error generando la factura.");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Notification.show("Se produjo un error generando la factura.");
		}
	}
	
	public List<Reserva> findByEstadoValor(Valor v) {
		return repo.findByEstadoValor(v);
	}
	
	public boolean intervaloDisponible(Date fechaInicio, Date fechaFin, Long id_apart) {
		List<Reserva> aceptadas =  repo.findByEstadoIntervalo(Valor.ACEPTADA, fechaInicio, fechaFin, id_apart);
		List<Reserva> realizadas = repo.findByEstadoIntervalo(Valor.REALIZADA, fechaInicio, fechaFin, id_apart);
		if(aceptadas.size() > 0 || realizadas.size() > 0)
			return false;
		else
			return true;
	}

}
