/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.entidades.reportes;

import com.biosis.biosislite.entidades.escalafon.Empleado;
import java.util.Date;

/**
 *
 * @author Francis
 */
public class RptNoMarca {
    private Empleado empleado;
    private Date fecha;
    private Date evento;
    private Date referencia;
    private boolean enPermiso;
    private String motivo;

    public boolean isEnPermiso() {
        return enPermiso;
    }

    public void setEnPermiso(boolean enPermiso) {
        this.enPermiso = enPermiso;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getEvento() {
        return evento;
    }

    public void setEvento(Date evento) {
        this.evento = evento;
    }

    public Date getReferencia() {
        return referencia;
    }

    public void setReferencia(Date referencia) {
        this.referencia = referencia;
    }


}
