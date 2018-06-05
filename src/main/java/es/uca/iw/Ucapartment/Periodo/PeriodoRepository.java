package es.uca.iw.Ucapartment.Periodo;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uca.iw.Ucapartment.Apartamento.Apartamento;

public interface PeriodoRepository extends JpaRepository<Periodo, Long>{

	public List<Periodo> findByApartamento(Apartamento a);

	@Query("Select p from Periodo p Where p.apartamento = :apart and p.fechaInicio = :fI and p.fechaFin = :fF")
	public List<Periodo> findPeriodoRango(@Param("fI")Date fInicio, @Param("fF")Date fFin, @Param("apart")Apartamento a);

}
