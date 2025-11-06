package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    boolean existsByTipoAndNroEquipo(TipoEquipo tipo, Integer nroEquipo);
}
