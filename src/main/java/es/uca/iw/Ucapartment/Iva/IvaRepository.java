package es.uca.iw.Ucapartment.Iva;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface IvaRepository extends JpaRepository<Iva,Long> 
{
	
	public Iva findByPais(String pais);

}
