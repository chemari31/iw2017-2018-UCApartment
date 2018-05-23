package es.uca.iw.Ucapartment.Precio;

import java.util.List;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;
import es.uca.iw.Ucapartment.Precio.PrecioRepository;
	
import es.uca.iw.Ucapartment.Apartamento.Apartamento;

@Service
public class PrecioService 
{
	@Autowired
	PrecioRepository repo;
	
	public Precio save(Precio precio) {
		return repo.save(precio);
	}
	
	public void delete(Precio arg0) {
		repo.delete(arg0);
	}
	
	public List<Precio> findById(Long id){
		return repo.findById(id);
	}
	
	public List<Precio> findByApartamento(Apartamento apartamento){
		return repo.findByApartamento(apartamento);
	}
	
	public List<Precio>  findAll(){
		return repo.findAll();
	}
	
	public double TotalPrecio(Apartamento a, Date inicio, Date fin)
	{
		List<Precio> especial = repo.findByApartamento(a);
		ListIterator<Precio> iter = especial.listIterator(especial.size());
		List<Precio> especialInvertida = new ArrayList<>();
		while (iter.hasPrevious())
		{
			 especialInvertida.add(iter.previous());
		}
		double precioTotal = 0;
		boolean fechaEspecial;
		Date fechaActual = inicio;
		Date fechaActualEspecial;
		
		
		while(fechaActual.compareTo(fin) != 0)
		{
			fechaEspecial = false;
			for(Precio p : especialInvertida)
			{
				
					java.sql.Date sqlDate2 = new java.sql.Date(p.getFecha_inicio().getTime());
					fechaActualEspecial = sqlDate2;
						while(fechaActualEspecial.compareTo(p.getFecha_fin()) != 0 && fechaEspecial == false)
						{
							if(fechaActual.compareTo(fechaActualEspecial) == 0)
							{
								
								precioTotal = precioTotal + p.getValor();
								System.out.println("Especial: "+precioTotal);
								fechaEspecial = true;
								Calendar c = Calendar.getInstance();
						        c.setTime(fechaActual);
						        c.add(Calendar.DATE, 1);
						        java.util.Date utilDate = c.getTime();
						        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
						        fechaActual = sqlDate;
						        System.out.println(fechaActual);
								 
							}
							else
							{
								Calendar c = Calendar.getInstance();
						        c.setTime(fechaActualEspecial);
						        c.add(Calendar.DATE, 1);
						        java.util.Date utilDate = c.getTime();
						        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
						        fechaActualEspecial = sqlDate;
							}	
						}
					
			}
			if(fechaEspecial == false)
			{
				precioTotal = precioTotal + a.getPrecio();
				Calendar c = Calendar.getInstance();
		        c.setTime(fechaActual);
		        c.add(Calendar.DATE, 1);
		        java.util.Date utilDate = c.getTime();
		        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		        fechaActual = sqlDate;
		        System.out.println(fechaActual);
		        System.out.println("Normal: "+precioTotal);
				
			}
		}
	
		
		
		return precioTotal;
	}
}
