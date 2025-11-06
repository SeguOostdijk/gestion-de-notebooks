package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    // Devuelve todos los préstamos activos
}

