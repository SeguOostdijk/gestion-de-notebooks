package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocenteRepository extends JpaRepository<Docente,Long> {
}
