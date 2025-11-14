package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends JpaRepository<Materia,Long> {
    @Query("""
  select m
  from DocenteMateria dm
  join dm.materia m
  where dm.docente.id = :docenteId
""")
    List<Materia> findAllByDocenteId(@Param("docenteId") Long docenteId);
}
