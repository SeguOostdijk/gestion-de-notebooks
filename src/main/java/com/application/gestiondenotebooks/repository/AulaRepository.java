package com.application.gestiondenotebooks.repository;

import com.application.gestiondenotebooks.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AulaRepository extends JpaRepository<Aula,Long> {
}
