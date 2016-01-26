/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.entidades.sisgedo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author RyuujiMD
 */
@Entity
@Table(name = "tb_spa_motivos")
public class Motivo implements Serializable {
    @Column(name = "idmotivo")
    @Id
    private Long id;
    @Column(name = "descmotivo")
    private String descripcion;
    @Column(name = "tipopapeleta")
    private int tipoPapeleta;    
    @Column(name = "activo")
    private boolean activo;    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTipoPapeleta() {
        return tipoPapeleta;
    }

    public void setTipoPapeleta(int tipoPapeleta) {
        this.tipoPapeleta = tipoPapeleta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}
