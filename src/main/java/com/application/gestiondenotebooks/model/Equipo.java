package com.application.gestiondenotebooks.model;

import com.application.gestiondenotebooks.enums.TipoEquipo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "equipo",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_equipo_tipo_nro", columnNames = {"tipo", "nro_equipo"}),
                @UniqueConstraint(name = "uq_equipo_qr", columnNames = "codigo_qr")
        }
)
public class Equipo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Equipo(Long id, int nroEquipo, TipoEquipo tipo, String codigoQr) {
        this.id = id;
        this.nroEquipo = nroEquipo;
        this.tipo = tipo;
        this.codigoQr = codigoQr;
    }

    public Equipo() {
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoEquipo tipo;

    @Column(name = "nro_equipo", nullable = false)
    private int nroEquipo;

    @Column(name = "codigo_qr",nullable = false,length = 100)
    private String codigoQr;

    @Override
    public String toString() {
        return "Tipo: " + tipo +
                ", Numero de equipo: " + nroEquipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEquipo getTipo() {
        return tipo;
    }

    public void setTipo(TipoEquipo tipo) {
        this.tipo = tipo;
    }

    public int getNroEquipo() {
        return nroEquipo;
    }

    public void setNroEquipo(int nroEquipo) {
        this.nroEquipo = nroEquipo;
    }

    public String getCodigoQr() {
        return codigoQr;
    }

    public void setCodigoQr(String codigoQr) {
        this.codigoQr = codigoQr;
    }
}