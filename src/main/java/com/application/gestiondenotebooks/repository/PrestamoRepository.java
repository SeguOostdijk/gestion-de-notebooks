package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.enums.EstadoPrestamo;
import com.application.gestiondenotebooks.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    @Query("select p.id from Prestamo p where p.nroReferencia = :ref")
    Optional<Long> findIdByNroReferencia(@Param("ref") String nroReferencia);

    @Query("""
  select p
  from Prestamo p
  join fetch p.docente
  join fetch p.materia
  join fetch p.aula
  where p.id = :id
""")
    Optional<Prestamo> findDetalleById(@Param("id") Long id);

    EstadoPrestamo estado = EstadoPrestamo.ABIERTO;
    // Método de consulta derivado para encontrar por el campo 'estado'
    // Spring genera: SELECT p FROM Prestamo p WHERE p.estado = ?1
    List<Prestamo> findByEstado(EstadoPrestamo estado);
}

