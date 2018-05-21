package es.uca.iw.Ucapartment.Precio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;

public interface PrecioRepository extends JpaRepository<Precio,Long> 
{
	
	public List<Precio> findByApartamento(Apartamento a);

}
