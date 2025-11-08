package com.application.gestiondenotebooks.repository;

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
}

