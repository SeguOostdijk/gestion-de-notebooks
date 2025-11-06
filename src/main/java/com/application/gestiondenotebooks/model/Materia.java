package com.application.gestiondenotebooks.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "materia"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="plan_id", nullable=false, length=50)
    private String planId;

    @Column(name="nombre", nullable=false, length=200)
    private String nombre;

}