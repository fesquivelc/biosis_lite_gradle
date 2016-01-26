/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.algoritmo;

import com.biosis.biosislite.entidades.Marcacion;
import com.biosis.biosislite.entidades.asistencia.Asistencia;
import com.biosis.biosislite.entidades.asistencia.DetalleAsistencia;
import com.personal.utiles.FechaUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author Francis
 */
public class AnalizadorDiario {

    private int posicion;
    private Asistencia asistencia;
    private List<Marcacion> marcaciones;
    private List<DetalleAsistencia> permisos;
    private final int INICIO = 0;
    private int FIN;
    private Calendar calendar = Calendar.getInstance();

    public List<DetalleAsistencia> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<DetalleAsistencia> permisos) {
        this.permisos = permisos;
    }

    public void setMarcaciones(List<Marcacion> marcaciones) {
        this.marcaciones = marcaciones;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Asistencia asistencia) {
        this.FIN = asistencia.getDetalleAsistenciaList().size() - 1;
        this.asistencia = asistencia;
    }

    /*
     Método con el que se dará inicio al análisis de asistencia
     */
    public void iniciar() {

        /*
         Primero se analiza el turno de forma normal, sin complicaciones       
         luego se sobreponen los permisos
         */
        Date diaInicio = this.getAsistencia().getFecha();

        calendar.setTime(diaInicio);
        calendar.add(Calendar.DATE, 1);

        Date diaSiguiente = calendar.getTime();

        /*
         Buscamos las marcaciones referentes a la asistencia
         */
        this.getAsistencia().getDetalleAsistenciaList().stream().forEach((detalle) -> {
            System.out.println(String.format("-- DETALLE TOLERANCIA -- REFERENCIA: %s TOLERANCIA: %s BANDERA: %s", detalle.getHoraReferencia(), detalle.getHoraReferenciaTolerancia(), detalle.isBandera()));
            Marcacion marcacion
                    = this.buscarMarcacion(
                            detalle.isDiaSiguiente() ? diaSiguiente : diaInicio, detalle.getHoraReferenciaDesde(),
                            detalle.isDiaSiguiente() ? diaSiguiente : diaInicio, detalle.getHoraReferenciaHasta()
                    );
            if (marcacion != null) {
                detalle.setHoraEvento(marcacion.getFechaHora());
                this.removerMarcacionesDuplicadas(marcacion.getFechaHora());
            }
        });

        /*
         Se compara con los permisos y se sobrepone en la asistencia, debe tenerse en cuenta que los permisos pueden ser 
         en varias fechas, o en una sola fecha
         */
        if (this.getPermisos() != null) {
            DetalleAsistencia permisoI = null;
            DetalleAsistencia permisoF = null;
            for (DetalleAsistencia permiso : this.getAsistencia().getPermisoList()) {
                if (permiso.isBandera()) {
                    permisoI = permiso;
                } else {
                    permisoF = permiso;
                    System.out.println("PERMISO I: " + permisoI.getHoraReferencia());
                    System.out.println("PERMISO F: " + permisoF.getHoraReferencia());
                    /*
                     Se buscan los detalles que coinciden con los permisos
                     */
                    for (DetalleAsistencia detalle : this.getAsistencia().getDetalleAsistenciaList()) {

                        if (permisoI.getHoraReferencia().compareTo(detalle.getHoraReferencia()) <= 0
                                && permisoF.getHoraReferencia().compareTo(detalle.getHoraReferencia()) >= 0) {
                            if(detalle.getHoraEvento() == null){
                                detalle.setPermiso(true);
                            }
                        }
                    }
                }
            }
//            this.getPermisos().stream().sorted((p1, p2) -> p1.getHoraReferencia().compareTo(p2.getHoraReferencia())).forEach((permiso) -> {
//                /*
//                 Debemos buscar las marcaciones para los permisos, sin olvidar eliminar los posibles duplicados de marcacion, suponiendo que las marcaciones eliminadaas corresponden
//                 para ello arbitriaramente definimos un espacio para la marcacion de inicio y la marcación final vendrá dada por la 
//                 */
//
//                if (permiso.isBandera()) {
//                    permisoI = permiso;
//                } else {
//                    permisoF = permiso;
//
//                    /*
//                     Se buscan los detalles que coinciden con los permisos
//                     */
//                    for (DetalleAsistencia detalle : this.getAsistencia().getDetalleAsistenciaList()) {
//                        System.out.println("PERMISO I: " + permisoI.getHoraReferencia());
//                        System.out.println("PERMISO F: " + permisoF.getHoraReferencia());
//                        if (permisoI.getHoraReferencia().compareTo(detalle.getHoraReferencia()) <= 0
//                                && permisoF.getHoraReferencia().compareTo(detalle.getHoraReferencia()) >= 0) {
//                            detalle.setPermiso(true);
//                        }
//                    }
//                }
//            });
        }

//        this.posicion = this.INICIO;
//
//        switch (detalleActual().getTipo()) {
//            case 'P':
//                estadoPermiso();
//                break;
//            case 'A':
//                estadoAsistencia();
//                break;
//        }
    }

//    private void estadoAsistencia() {
//    }
//
//    private void estadoPermiso() {
//        if (this.posicion == this.INICIO) {
//            if (this.detalleActual().isBandera()) {
//                //AQUÍ DEBE OBTENERSE
//                this.detalleActual().setPermiso(true);
//            } else {
//
//            }
//            this.posicion++;
//
//        }
//    }
    private DetalleAsistencia detalleActual() {
        DetalleAsistencia detalle = this.asistencia.getDetalleAsistenciaList().get(this.posicion);
        return detalle;
    }

    private Marcacion buscarMarcacion(Date fechaInicio, Date horaInicio, Date fechaFin, Date horaFin) {
        Date fechaHoraInicio = FechaUtil.unirFechaHora(fechaInicio, horaInicio);
        Date fechaHoraFin = FechaUtil.unirFechaHora(fechaFin, horaFin);
        System.out.println("FECHA HORA INICIO: " + fechaHoraInicio);
        System.out.println("FECHA HORA FIN: " + fechaHoraFin);
        try {
            return this.marcaciones
                    .stream()
                    .filter(m -> fechaHoraInicio.compareTo(m.getFechaHora()) <= 0 && fechaHoraFin.compareTo(m.getFechaHora()) >= 0)
                    .sorted((m1, m2) -> m1.getFechaHora().compareTo(m2.getFechaHora()))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            return null;
        }

    }

    /*
     método para eliminar las marcaciones en un rango de tiempo de X minutos
     */
    private void removerMarcacionesDuplicadas(Date fechaHora) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaHora);
        cal.add(Calendar.MINUTE, 3);
        this.marcaciones.removeIf(m -> fechaHora.compareTo(m.getFechaHora()) <= 0 && cal.getTime().compareTo(m.getFechaHora()) >= 0);
    }

    private DetalleAsistencia buscarDetalleAsistencia(Date horaReferencia) {
        try {
            return this.getAsistencia().getDetalleAsistenciaList()
                    .stream()
                    .filter(detalle -> detalle.getHoraReferencia().equals(horaReferencia))
                    .findFirst()
                    .get();
        } catch (NoSuchElementException e) {
            return null;
        }

    }
}
