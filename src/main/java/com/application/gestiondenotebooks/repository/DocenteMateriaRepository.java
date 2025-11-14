package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.DocenteMateria;
import com.application.gestiondenotebooks.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteMateriaRepository extends JpaRepository<DocenteMateria,Long> {

}
