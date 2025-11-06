package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.DocenteMateria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteMateriaRepository extends JpaRepository<DocenteMateria,Long> {
}
