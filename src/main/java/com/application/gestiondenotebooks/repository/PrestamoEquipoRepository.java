package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.Equipo;
import com.application.gestiondenotebooks.model.Prestamo;
import com.application.gestiondenotebooks.model.PrestamoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoEquipoRepository extends JpaRepository<PrestamoEquipo, Long> {
    List<PrestamoEquipo> findByPrestamo(Prestamo prestamo);
    Optional<PrestamoEquipo> findByEquipoAndPrestamo(Equipo equipo,Prestamo prestamo);
}

