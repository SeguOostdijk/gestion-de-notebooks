package com.application.gestiondenotebooks.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "docente",
        uniqueConstraints = @UniqueConstraint(name="uq_docente_dni", columnNames = "dni")
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Docente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="dni", length=20)
    private String dni;

    @Column(name="nombre", nullable=false, length=200)
    private String nombre;

    @Column(name="apellido", nullable=false, length=200)
    private String apellido;

    @Override
    public String toString() {
        return dni +
                ";  "+ nombre.concat(" "+apellido);
    }
}
