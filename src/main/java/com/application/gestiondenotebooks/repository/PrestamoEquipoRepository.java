package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.PrestamoEquipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoEquipoRepository extends JpaRepository<PrestamoEquipo,Long> {

}
