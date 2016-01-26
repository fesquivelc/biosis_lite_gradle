/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.entidades.reportes;

import com.biosis.biosislite.entidades.escalafon.Empleado;


/**
 *
 * @author Francis
 */
public class RptAsistenciaResumen {
    private Empleado empleado;
    private int numeroDiasLaborados;
    private int numeroDiasFalta;
    private int numeroFeriados;
    private int numeroVacaciones;
    private double horasLaboradas;
    private double minutosTardanza;
    private double minutosTardanzaRefrigerio;
    private double minutosPermisoConGoce;
    private double minutosPermisoSinGoce;
    private int numeroDiasPermisoConGoce;
    private int numeroDiasPermisoSinGoce;

    public double getHorasLaboradas() {
        return horasLaboradas;
    }

    public double getMinutosTardanzaRefrigerio() {
        return minutosTardanzaRefrigerio;
    }

    public void setMinutosTardanzaRefrigerio(double minutosTardanzaRefrigerio) {
        this.minutosTardanzaRefrigerio = minutosTardanzaRefrigerio;
    }

    public void setHorasLaboradas(double horasLaboradas) {
        this.horasLaboradas = horasLaboradas;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public int getNumeroDiasLaborados() {
        return numeroDiasLaborados;
    }

    public void setNumeroDiasLaborados(int numeroDiasLaborados) {
        this.numeroDiasLaborados = numeroDiasLaborados;
    }

    public int getNumeroDiasFalta() {
        return numeroDiasFalta;
    }

    public void setNumeroDiasFalta(int numeroDiasFalta) {
        this.numeroDiasFalta = numeroDiasFalta;
    }

    public int getNumeroFeriados() {
        return numeroFeriados;
    }

    public void setNumeroFeriados(int numeroFeriados) {
        this.numeroFeriados = numeroFeriados;
    }

    public int getNumeroVacaciones() {
        return numeroVacaciones;
    }

    public void setNumeroVacaciones(int numeroVacaciones) {
        this.numeroVacaciones = numeroVacaciones;
    }

    public double getMinutosTardanza() {
        return minutosTardanza;
    }

    public void setMinutosTardanza(double minutosTardanza) {
        this.minutosTardanza = minutosTardanza;
    }

    public double getMinutosPermisoConGoce() {
        return minutosPermisoConGoce;
    }

    public void setMinutosPermisoConGoce(double minutosPermisoConGoce) {
        this.minutosPermisoConGoce = minutosPermisoConGoce;
    }

    public double getMinutosPermisoSinGoce() {
        return minutosPermisoSinGoce;
    }

    public void setMinutosPermisoSinGoce(double minutosPermisoSinGoce) {
        this.minutosPermisoSinGoce = minutosPermisoSinGoce;
    }

    public int getNumeroDiasPermisoConGoce() {
        return numeroDiasPermisoConGoce;
    }

    public void setNumeroDiasPermisoConGoce(int numeroDiasPermisoConGoce) {
        this.numeroDiasPermisoConGoce = numeroDiasPermisoConGoce;
    }

    public int getNumeroDiasPermisoSinGoce() {
        return numeroDiasPermisoSinGoce;
    }

    public void setNumeroDiasPermisoSinGoce(int numeroDiasPermisoSinGoce) {
        this.numeroDiasPermisoSinGoce = numeroDiasPermisoSinGoce;
    }
    
}
