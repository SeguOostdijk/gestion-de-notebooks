package com.application.gestiondenotebooks.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table( name = "docente_materia",
        uniqueConstraints = @UniqueConstraint(name = "uq_dm_doc_mat",
        columnNames = {"docente_id","materia_id"}))
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class DocenteMateria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "docente_id",foreignKey = @ForeignKey(name = "fk_dm_docente"))
    private Docente docente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "materia_id",foreignKey = @ForeignKey(name = "fk_dm_materia"))
    private Materia materia;
    
}
