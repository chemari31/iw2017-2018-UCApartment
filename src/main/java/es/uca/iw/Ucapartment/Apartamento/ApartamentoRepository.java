package es.uca.iw.Ucapartment.Apartamento;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uca.iw.Ucapartment.Estado.Valor;
import es.uca.iw.Ucapartment.Precio.Precio;
import es.uca.iw.Ucapartment.Usuario.Usuario;

public interface ApartamentoRepository extends JpaRepository<Apartamento, Long> {

	public void save(String string);
	public void delete(Apartamento arg0);
	public Apartamento findByNombre(String nombre);
	public List<Apartamento> findByNombreStartsWithIgnoreCase(String nombre);
	public Apartamento findById(Long long1);
	public List<Apartamento> findByUsuario(Usuario user);
	public List<Apartamento> findAll();
	public List<Apartamento> findByCiudad(String ciudad);
	public List<Apartamento> findByCiudadAndHabitacion(String ciudad, int habitacion);
	public List<Apartamento> findByCiudadAndHabitacionAndPrecio(String ciudad, int habitacion, double precio);
	public List<Apartamento> findByCiudadAndPrecio(String ciudad, double precio);
	public List<Apartamento> findByHabitacion(int habitacion);
	public List<Apartamento> findByPrecio(double precio);
	public List<Apartamento> findByHabitacionAndPrecio(int habitacion, double precio);
	//public List<Apartamento> findByApartamentoAndApartamento(List<Apartamento> ap1, List<Apartamento> ap2);
	//public List<Apartamento> findByPrecio(double precio);
	
	@Query("Select a from Apartamento a Where a.id not in (Select e.reserva.apartamento.id from Estado e "
			+ "Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF and (e.valor = :vA "
			+ "or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo p "
			+ "Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findAllByPeriodo(@Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA,
			@Param("vP") Valor vP, @Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.id not in (Select e.reserva.apartamento.id from Estado e "
			+ "Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF and (e.valor = :vA "
			+ "or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo p "
			+ "Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF) and a.usuario != :usuario")
	public List<Apartamento> findAllByPeriodoUsuario(@Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA,
			@Param("vP") Valor vP, @Param("vR") Valor vR, @Param("usuario") Usuario usuario);
	
	@Query("Select a from Apartamento a Where a.habitacion >= :habit and a.id not in (Select e.reserva.apartamento.id "
			+ "from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF and (e.valor = :vA "
			+ "or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo p Where "
			+ "p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findByHabitacionPeriodo(@Param("habit") int habit, @Param("fechaI") Date fInicio,
			@Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.habitacion >= :habit and a.id not in (Select e.reserva.apartamento.id "
			+ "from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF and (e.valor = :vA "
			+ "or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo p Where "
			+ "p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF) and a.usuario != :usuario")
	public List<Apartamento> findByHabitacionPeriodoUsuario(@Param("habit") int habit, @Param("fechaI") Date fInicio,
			@Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR,
			@Param("usuario") Usuario usuario);
	
	@Query("Select a from Apartamento a Where a.ciudad like :ciudad and a.id not in (Select e.reserva.apartamento.id "
			+ "from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF and (e.valor = :vA "
			+ "or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo p Where "
			+ "p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findByCiudadPeriodo(@Param("ciudad") String ciudad, @Param("fechaI") Date fInicio,
			@Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.ciudad like :ciudad and a.id not in (Select e.reserva.apartamento.id "
			+ "from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF and (e.valor = :vA "
			+ "or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo p Where "
			+ "p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF) and a.usuario != :usuario")
	public List<Apartamento> findByCiudadPeriodoUsuario(@Param("ciudad") String ciudad, @Param("fechaI") Date fInicio,
			@Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR, 
			@Param("usuario") Usuario usuario);
	
	@Query("Select a from Apartamento a Where a.ciudad like :ciudad and a.habitacion >= :habit and a.id not in (Select "
			+ "e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from "
			+ "Periodo p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findByCiudadAndHabitacionPeriodo(@Param("ciudad") String ciudad, @Param("habit") int habit, 
			@Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, 
			@Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.ciudad like :ciudad and a.habitacion >= :habit and a.id not in (Select "
			+ "e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from "
			+ "Periodo p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF) and a.usuario != :usuario")
	public List<Apartamento> findByCiudadAndHabitacionPeriodoUsuario(@Param("ciudad") String ciudad, @Param("habit") int habit, 
			@Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, 
			@Param("vR") Valor vR, @Param("usuario") Usuario usuario);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.id not in (Select e.reserva.apartamento.id from Estado e "
			+ "Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo "
			+ "p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findByMaxPrecioPeriodo(@Param("precio") double precio, @Param("fechaI") Date fInicio, 
			@Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.id not in (Select e.reserva.apartamento.id from Estado e "
			+ "Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo "
			+ "p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF) and a.usuario != :usuario")
	public List<Apartamento> findByMaxPrecioPeriodoUsuario(@Param("precio") double precio, @Param("fechaI") Date fInicio, 
			@Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR,  
			@Param("usuario") Usuario usuario);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.habitacion >= :habit and a.id not in (Select "
			+ "e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo p "
			+ "Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findByMaxPrecioAndHabitacionPeriodo(@Param("precio") double precio, 
			@Param("habit") int habit, @Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA, 
			@Param("vP") Valor vP, @Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.habitacion >= :habit and a.id not in (Select "
			+ "e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo "
			+ "p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)and a.usuario != :usuario")
	public List<Apartamento> findByMaxPrecioAndHabitacionPeriodoUsuario(@Param("precio") double precio, 
			@Param("habit") int habit, @Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA, 
			@Param("vP") Valor vP, @Param("vR") Valor vR, @Param("usuario") Usuario usuario);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.ciudad like :ciudad and a.id not in (Select "
			+ "e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo "
			+ "p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findByMaxPrecioAndCiudadPeriodo(@Param("precio") double precio, @Param("ciudad") String ciudad, 
			@Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, 
			@Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.ciudad like :ciudad and a.id not in (Select "
			+ "e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and e.reserva.fechaFin <= :fechaF "
			+ "and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select p.apartamento.id from Periodo "
			+ "p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF) and a.usuario != :usuario")
	public List<Apartamento> findByMaxPrecioAndCiudadPeriodoUsuario(@Param("precio") double precio, @Param("ciudad") String ciudad, 
			@Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, @Param("vA") Valor vA, @Param("vP") Valor vP, 
			@Param("vR") Valor vR, @Param("usuario") Usuario usuario);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.ciudad like :ciudad and a.habitacion >= :habit and "
			+ "a.id not in (Select e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and "
			+ "e.reserva.fechaFin <= :fechaF and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in (Select "
			+ "p.apartamento.id from Periodo p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF)")
	public List<Apartamento> findByMaxPrecioAndCiudadAndHabitacionPeriodo(@Param("precio") double precio, 
			@Param("ciudad") String ciudad, @Param("habit") int habit, @Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, 
			@Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR);
	
	@Query("Select a from Apartamento a Where a.precio <= :precio and a.ciudad like :ciudad and a.habitacion >= :habit and "
			+ "a.id not in (Select e.reserva.apartamento.id from Estado e Where e.reserva.fechaInicio >= :fechaI and "
			+ "e.reserva.fechaFin <= :fechaF and (e.valor = :vA or e.valor = :vP or e.valor = :vR)) and a.id not in "
			+ "(Select p.apartamento.id from Periodo p Where p.fechaInicio >= :fechaI and p.fechaFin <= :fechaF) and a.usuario != :usuario")
	public List<Apartamento> findByMaxPrecioAndCiudadAndHabitacionPeriodoUsuario(@Param("precio") double precio, 
			@Param("ciudad") String ciudad, @Param("habit") int habit, @Param("fechaI") Date fInicio, @Param("fechaF") Date fFin, 
			@Param("vA") Valor vA, @Param("vP") Valor vP, @Param("vR") Valor vR, @Param("usuario") Usuario usuari);
	
	@Query("Select distinct precio from Apartamento Order by precio")
	public List<Double> findAllPreciosDouble();
	
	@Query("Select distinct precio from Apartamento Order by precio")
	public List<String> findAllPreciosString();
	
	@Query("Select distinct ciudad from Apartamento Order by ciudad")
	public List<String> findAllCiudades();
}
