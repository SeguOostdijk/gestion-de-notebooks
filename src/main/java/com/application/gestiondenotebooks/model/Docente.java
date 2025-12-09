package com.application.gestiondenotebooks.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "docente",
        uniqueConstraints = @UniqueConstraint(name="uq_docente_dni", columnNames = "dni")
)

public class Docente {
    public Docente() {
    }

    public Docente(Long id, String apellido, String nombre, String dni) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.dni = dni;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="dni", length=20)
    private String dni;

    @Column(name="nombre", nullable=false, length=200)
    private String nombre;

    @Column(name="apellido", nullable=false, length=200)
    private String apellido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String toString() {
        return dni +
                ";  "+ nombre.concat(" "+apellido);
    }
}
