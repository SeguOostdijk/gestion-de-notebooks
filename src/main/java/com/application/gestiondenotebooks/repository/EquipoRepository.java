package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import com.application.gestiondenotebooks.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    Optional<Equipo> findByTipoAndNroEquipo(TipoEquipo tipo, Integer nroEquipo);
    Optional<Equipo> findByCodigoQr(String codigoqr);
}
