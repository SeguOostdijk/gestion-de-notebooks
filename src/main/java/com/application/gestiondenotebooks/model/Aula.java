package com.application.gestiondenotebooks.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "aula",
        uniqueConstraints = @UniqueConstraint(name = "uq_aula_codigo_aula",columnNames = "codigo_aula")
)
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_aula",nullable = false,length = 20)
    private String codigo_aula;

}
