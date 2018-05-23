package es.uca.iw.Ucapartment.Precio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;

public interface PrecioRepository extends JpaRepository<Precio, Long> {
	public void delete(Precio arg0);
	public List<Precio> findById(Long id);
	public List<Precio> findAll();
	public List<Precio> findByApartamento(Apartamento apartamento);
}
