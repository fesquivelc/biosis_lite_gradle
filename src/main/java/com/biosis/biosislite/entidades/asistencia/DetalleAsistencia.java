/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.entidades.asistencia;

import java.util.Date;

/**
 *
 * @author Francis
 */
public class DetalleAsistencia {
    private Date horaEvento;
    private Date horaReferencia;
    private Date horaReferenciaDesde;
    private Date horaReferenciaHasta;
    private Date horaReferenciaTolerancia;
    private boolean permiso;
    //bandera: 1 es inicio, 0 es fin
    private boolean bandera;
    private boolean diaSiguiente;
    private char tipo;
    private String motivo;
    private boolean permisoConGoce;

    public boolean isPermisoConGoce() {
        return permisoConGoce;
    }

    public void setPermisoConGoce(boolean permisoConGoce) {
        this.permisoConGoce = permisoConGoce;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isDiaSiguiente() {
        return diaSiguiente;
    }

    public void setDiaSiguiente(boolean diaSiguiente) {
        this.diaSiguiente = diaSiguiente;
    }
    
    public boolean isBandera() {
        return bandera;
    }

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
    }

    public Date getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(Date horaEvento) {
        this.horaEvento = horaEvento;
    }

    public Date getHoraReferencia() {
        return horaReferencia;
    }

    public void setHoraReferencia(Date horaReferencia) {
        this.horaReferencia = horaReferencia;
    }

    public Date getHoraReferenciaDesde() {
        return horaReferenciaDesde;
    }

    public void setHoraReferenciaDesde(Date horaReferenciaDesde) {
        this.horaReferenciaDesde = horaReferenciaDesde;
    }

    public Date getHoraReferenciaHasta() {
        return horaReferenciaHasta;
    }

    public void setHoraReferenciaHasta(Date horaReferenciaHasta) {
        this.horaReferenciaHasta = horaReferenciaHasta;
    }

    public Date getHoraReferenciaTolerancia() {
        return horaReferenciaTolerancia;
    }

    public void setHoraReferenciaTolerancia(Date horaReferenciaTolerancia) {
        this.horaReferenciaTolerancia = horaReferenciaTolerancia;
    }

    public boolean isPermiso() {
        return permiso;
    }

    public void setPermiso(boolean permiso) {
        this.permiso = permiso;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

}
