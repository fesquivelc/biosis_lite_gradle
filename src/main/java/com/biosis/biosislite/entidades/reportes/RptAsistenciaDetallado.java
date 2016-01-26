/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.entidades.reportes;

//import entidades.sisgedo.Salida;
import com.biosis.biosislite.entidades.escalafon.Empleado;
import java.util.Date;

/**
 *
 * @author RyuujiMD
 */
public class RptAsistenciaDetallado {

    private Empleado empleado;
    private Date horaReferencia1;
    private Date horaReferencia2;
    private Date horaReferencia3;
    private Date horaReferencia4;
    private Date horaTolerancia1;
    private Date horaTolerancia2;
    private Date horaTolerancia3;
    private Date horaTolerancia4;
    private Date horaEvento1;
    private Date horaEvento2;
    private Date horaEvento3;
    private Date horaEvento4;
    private boolean enPermiso1;
    private boolean enPermiso2;
    private boolean enPermiso3;
    private boolean enPermiso4;
    private int tipo;
    private String permisos;
    private Date fecha;
    private String regimenLaboral;
    private String referencias;
    private int marcacionesTotales;
    private boolean detalleFinal;
    private double minutosTardanza;
    private double minutosTardanzaRefrigerio;
    private double minutosExtra;
    private int mes;

    public double getMinutosTardanzaRefrigerio() {
        return minutosTardanzaRefrigerio;
    }

    public void setMinutosTardanzaRefrigerio(double minutosTardanzaRefrigerio) {
        this.minutosTardanzaRefrigerio = minutosTardanzaRefrigerio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public double getMinutosTardanza() {
        return minutosTardanza;
    }

    public void setMinutosTardanza(double minutosTardanza) {
        this.minutosTardanza = minutosTardanza;
    }

    public double getMinutosExtra() {
        return minutosExtra;
    }

    public void setMinutosExtra(double minutosExtra) {
        this.minutosExtra = minutosExtra;
    }

    public Date getHoraReferencia1() {
        return horaReferencia1;
    }

    public void setHoraReferencia1(Date horaReferencia1) {
        this.horaReferencia1 = horaReferencia1;
    }

    public Date getHoraReferencia2() {
        return horaReferencia2;
    }

    public void setHoraReferencia2(Date horaReferencia2) {
        this.horaReferencia2 = horaReferencia2;
    }

    public Date getHoraReferencia3() {
        return horaReferencia3;
    }

    public void setHoraReferencia3(Date horaReferencia3) {
        this.horaReferencia3 = horaReferencia3;
    }

    public Date getHoraReferencia4() {
        return horaReferencia4;
    }

    public void setHoraReferencia4(Date horaReferencia4) {
        this.horaReferencia4 = horaReferencia4;
    }

    public Date getHoraTolerancia1() {
        return horaTolerancia1;
    }

    public void setHoraTolerancia1(Date horaTolerancia1) {
        this.horaTolerancia1 = horaTolerancia1;
    }

    public Date getHoraTolerancia2() {
        return horaTolerancia2;
    }

    public void setHoraTolerancia2(Date horaTolerancia2) {
        this.horaTolerancia2 = horaTolerancia2;
    }

    public Date getHoraTolerancia3() {
        return horaTolerancia3;
    }

    public void setHoraTolerancia3(Date horaTolerancia3) {
        this.horaTolerancia3 = horaTolerancia3;
    }

    public Date getHoraTolerancia4() {
        return horaTolerancia4;
    }

    public void setHoraTolerancia4(Date horaTolerancia4) {
        this.horaTolerancia4 = horaTolerancia4;
    }

    public Date getHoraEvento1() {
        return horaEvento1;
    }

    public void setHoraEvento1(Date horaEvento1) {
        this.horaEvento1 = horaEvento1;
    }

    public Date getHoraEvento2() {
        return horaEvento2;
    }

    public void setHoraEvento2(Date horaEvento2) {
        this.horaEvento2 = horaEvento2;
    }

    public Date getHoraEvento3() {
        return horaEvento3;
    }

    public void setHoraEvento3(Date horaEvento3) {
        this.horaEvento3 = horaEvento3;
    }

    public Date getHoraEvento4() {
        return horaEvento4;
    }

    public void setHoraEvento4(Date horaEvento4) {
        this.horaEvento4 = horaEvento4;
    }

    public boolean isEnPermiso1() {
        return enPermiso1;
    }

    public void setEnPermiso1(boolean enPermiso1) {
        this.enPermiso1 = enPermiso1;
    }

    public boolean isEnPermiso2() {
        return enPermiso2;
    }

    public void setEnPermiso2(boolean enPermiso2) {
        this.enPermiso2 = enPermiso2;
    }

    public boolean isEnPermiso3() {
        return enPermiso3;
    }

    public void setEnPermiso3(boolean enPermiso3) {
        this.enPermiso3 = enPermiso3;
    }

    public boolean isEnPermiso4() {
        return enPermiso4;
    }

    public void setEnPermiso4(boolean enPermiso4) {
        this.enPermiso4 = enPermiso4;
    }

    public boolean isDetalleFinal() {
        return detalleFinal;
    }

    public void setDetalleFinal(boolean detalleFinal) {
        this.detalleFinal = detalleFinal;
    }

    public int getMarcacionesTotales() {
        return marcacionesTotales;
    }

    public void setMarcacionesTotales(int marcacionesTotales) {
        this.marcacionesTotales = marcacionesTotales;
    }

//    public RptAsistenciaDetallado() {
//        horaReferencia = new Date[4];
//        horaTolerancia = new Date[4];
//        horaEvento = new Date[4];
//        enPermiso = new boolean[4];
//    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

//    public Date[] getHoraReferencia() {
//        return horaReferencia;
//    }
//
//    public void setHoraReferencia(Date[] horaReferencia) {
//        this.horaReferencia = horaReferencia;
//    }
//
//    public Date[] getHoraTolerancia() {
//        return horaTolerancia;
//    }
//
//    public void setHoraTolerancia(Date[] horaTolerancia) {
//        this.horaTolerancia = horaTolerancia;
//    }
//
//    public Date[] getHoraEvento() {
//        return horaEvento;
//    }
//
//    public void setHoraEvento(Date[] horaEvento) {
//        this.horaEvento = horaEvento;
//    }
//
//    public boolean[] getEnPermiso() {
//        return enPermiso;
//    }
//
//    public void setEnPermiso(boolean[] enPermiso) {
//        this.enPermiso = enPermiso;
//    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getRegimenLaboral() {
        return regimenLaboral;
    }

    public void setRegimenLaboral(String regimenLaboral) {
        this.regimenLaboral = regimenLaboral;
    }

    public String getReferencias() {
        return referencias;
    }

    public void setReferencias(String referencias) {
        this.referencias = referencias;
    }

}
