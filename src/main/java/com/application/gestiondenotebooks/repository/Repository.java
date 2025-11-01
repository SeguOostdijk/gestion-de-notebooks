package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface Repository extends JpaRepository<Equipo,Integer> {
    boolean existsByTipoAndNroEquipo(TipoEquipo tipo, Integer nroEquipo);
}
