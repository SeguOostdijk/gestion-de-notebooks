package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<Materia,Long> {
}
