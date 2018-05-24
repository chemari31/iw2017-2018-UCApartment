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

import es.uca.iw.Ucapartment.Apartamento.Apartamento;


@Service
public class ReservaService {
	
	@Autowired
	private ReservaRepository repo;
	
	@Autowired
	private EstadoRepository repoEstado;
	
	public Reserva save(Reserva reserva)
	{
		return repo.save(reserva);
	}
	

	@Transactional
	public void pasarelaDePago(double total, Reserva r, Estado e)
	{
		double beneficioEmpresa = total * 0.10;
		double beneficioAnfitrion = total * 0.90;
		
		r.setBenenficioEmpresa(beneficioEmpresa);
		r.setBenenficioAnfitrion(beneficioAnfitrion);
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


	public void generarfactura(Reserva reserva)
	{	
		try {
			Document doc = new Document();
			FileOutputStream fichero = new FileOutputStream("factura.pdf");
			
			PdfWriter.getInstance(doc, fichero).setInitialLeading(40);
			doc.open();
			doc.add(new Paragraph("UcaApartament-Factura"));
			doc.add(new Paragraph(": " + reserva.getFecha()));
			doc.add(new Paragraph(": " + reserva.getPrecio()));
			doc.add(new Paragraph(": " + reserva.getFechaInicio()));
			doc.add(new Paragraph(": " + reserva.getFechaFin()));
			doc.add(new Paragraph(": " + reserva.getUsuario().getNombre()));
			doc.add(new Paragraph(": " + reserva.getApartamento().getUsuario().getNombre()));

				
			
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

}
