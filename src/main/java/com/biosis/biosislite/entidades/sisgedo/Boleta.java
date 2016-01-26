/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.entidades.sisgedo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author RyuujiMD
 */

@Table(name = "tb_spa_papeletas")
@Entity
public class Boleta implements Serializable {
    @Id
    @Column(name = "idPapeleta")
    private Long id;
    @Column(name = "activo")
    private char activo;
    @Column(name = "tipoPapeleta")
    private int tipo;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechaAutoriza")
    private Date aprobacionFechaHora;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechaSalidaDel")
    private Date inicioFechaHora;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechaSalidaAl")
    private Date finFechaHora;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fechaRetorno")
    private Date retornoFechaHora;
    @ManyToOne(targetEntity = Motivo.class,optional = true)
    @JoinColumn(name = "idMotivo", referencedColumnName = "idmotivo",nullable = true)
    private Motivo motivo;
    @ManyToOne(targetEntity = UsuarioSISGEDO.class, optional = true)
    @JoinColumn(name = "loginUsuario", referencedColumnName = "idUsuario")
    private UsuarioSISGEDO usuario;

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public char getActivo() {
        return activo;
    }

    public void setActivo(char activo) {
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAprobacionFechaHora() {
        return aprobacionFechaHora;
    }

    public void setAprobacionFechaHora(Date aprobacionFechaHora) {
        this.aprobacionFechaHora = aprobacionFechaHora;
    }

    public Date getInicioFechaHora() {
        return inicioFechaHora;
    }

    public void setInicioFechaHora(Date inicioFechaHora) {
        this.inicioFechaHora = inicioFechaHora;
    }

    public Date getFinFechaHora() {
        return finFechaHora;
    }

    public void setFinFechaHora(Date finFechaHora) {
        this.finFechaHora = finFechaHora;
    }

    public Date getRetornoFechaHora() {
        return retornoFechaHora;
    }

    public void setRetornoFechaHora(Date retornoFechaHora) {
        this.retornoFechaHora = retornoFechaHora;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public UsuarioSISGEDO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioSISGEDO usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Boleta{" + "id=" + id + ", aprobacionFechaHora=" + aprobacionFechaHora + ", inicioFechaHora=" + inicioFechaHora + ", finFechaHora=" + finFechaHora + ", retornoFechaHora=" + retornoFechaHora + ", motivo=" + motivo + ", usuario=" + usuario + '}';
    }
    
    
    
}
