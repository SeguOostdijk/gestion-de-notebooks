package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocenteRepository extends JpaRepository<Docente,Long> {
    @Query("""
  select d
  from DocenteMateria dm
  join dm.docente d
  where dm.materia.id = :materiaId
""")
    List<Docente> findAllByMateriaId(@Param("materiaId") Long materiaId);

}

