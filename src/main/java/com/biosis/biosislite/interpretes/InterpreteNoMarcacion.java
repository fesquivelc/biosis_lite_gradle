/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.interpretes;

import com.biosis.biosislite.algoritmo.AnalizadorAsistencia;
import com.biosis.biosislite.algoritmo.Interprete;
import com.biosis.biosislite.entidades.asistencia.Asistencia;
import com.biosis.biosislite.entidades.asistencia.DetalleAsistencia;
import com.biosis.biosislite.entidades.reportes.RptNoMarca;
import com.personal.utiles.FechaUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Francis
 */
public class InterpreteNoMarcacion implements Interprete<RptNoMarca> {

    /*
     Fecha en la que sólo se tendrá en cuenta la marcacion al final del turno
     */
    private Date inicio;
    /*
     Fecha en la que sólo se tendrá en cuenta la marcacion al inicio del turno
     */
    private Date fin;

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    @Override
    public List<RptNoMarca> interpretar(List<Asistencia> registroAsistencia) {
        List<RptNoMarca> noMarcanList = new ArrayList<>();
        registroAsistencia.stream().filter((asistencia) -> (asistencia.getResultado() == AnalizadorAsistencia.ASISTENCIA)).forEach((asistencia) -> {
            if (FechaUtil.soloFecha(asistencia.getFecha()).equals(FechaUtil.soloFecha(inicio))) {
                DetalleAsistencia detalleFin = asistencia.getDetalleAsistenciaList().get(asistencia.getDetalleAsistenciaList().size() - 1);
                if (detalleFin.getHoraEvento() == null && !detalleFin.isPermiso()) {
                    RptNoMarca noMarca = new RptNoMarca();
                    noMarca.setEmpleado(asistencia.getEmpleado());
                    noMarca.setFecha(asistencia.getFecha());
                    noMarca.setEvento(detalleFin.getHoraEvento());
                    noMarca.setEnPermiso(detalleFin.isPermiso());
                    noMarca.setReferencia(detalleFin.getHoraReferencia());
                    noMarcanList.add(noMarca);
                }
            } else if (FechaUtil.soloFecha(asistencia.getFecha()).equals(FechaUtil.soloFecha(fin))) {
                DetalleAsistencia detalleInicio = asistencia.getDetalleAsistenciaList().get(0);
                if (detalleInicio.getHoraEvento() == null && !detalleInicio.isPermiso()) {
                    RptNoMarca noMarca = new RptNoMarca();
                    noMarca.setEmpleado(asistencia.getEmpleado());
                    noMarca.setFecha(asistencia.getFecha());
                    noMarca.setEvento(detalleInicio.getHoraEvento());
                    noMarca.setEnPermiso(detalleInicio.isPermiso());
                    noMarca.setReferencia(detalleInicio.getHoraReferencia());
                    noMarcanList.add(noMarca);
                }

            }
        });
        return noMarcanList;
    }

}
