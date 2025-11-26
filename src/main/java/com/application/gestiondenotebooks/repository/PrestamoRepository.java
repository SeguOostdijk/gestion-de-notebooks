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

    /**
     * Busca préstamos por su estado y los ordena por la fecha de finalización
     * (fecha en que fue cerrado) de forma descendente (los más recientes primero).
     * Ideal para el historial o la vista de cerrados.
     */
    List<Prestamo> findByEstadoOrderByFechaFinDesc(EstadoPrestamo estado); // ESTE ES EL MÉTODO REQUERIDO

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

    /**
     * Método simple para buscar por estado (útil para Préstamos Activos = ABIERTO).
     */
    List<Prestamo> findByEstado(EstadoPrestamo estado);
}

