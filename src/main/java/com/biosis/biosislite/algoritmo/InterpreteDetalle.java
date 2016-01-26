/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.algoritmo;

import com.biosis.biosislite.entidades.asistencia.Asistencia;
import com.biosis.biosislite.entidades.asistencia.DetalleAsistencia;
import com.biosis.biosislite.entidades.escalafon.RegimenLaboral;
import com.biosis.biosislite.entidades.reportes.RptAsistenciaDetallado;
import com.biosis.biosislite.utiles.HerramientaGeneral;
import com.personal.utiles.FechaUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Francis
 */
public class InterpreteDetalle implements Interprete<RptAsistenciaDetallado> {

    /*
     EN ESTE INTÉRPRETE SE TOMA COMO FALTA CON EL SÓLO HECHO QUE EL EMPLEADO NO HAYA
     MARCADO UNA VEZ
    
     TARDANZA CON CUALQUIER TARDANZA QUE TENGA EN EL DÍA 
    
     SE HARÁN EN BLOQUES DE 4 PARA EL REPORTE
     */
    @Override
    public List<RptAsistenciaDetallado> interpretar(List<Asistencia> registroAsistencia) {
        List<RptAsistenciaDetallado> registro = new ArrayList<>();

        for (Asistencia asistencia : registroAsistencia) {

            RptAsistenciaDetallado detalleAsistencia = null;
            RegimenLaboral regLab = asistencia.getEmpleado().getContratoList().isEmpty() ? null : asistencia.getEmpleado().getContratoList().get(0).getRegimenLaboral();
            String regimenLaboral = regLab == null ? "" : regLab.getNombre();
            if (asistencia.getResultado() == AnalizadorAsistencia.ASISTENCIA) {
                Long marcacionesMaximas = asistencia.getDetalleAsistenciaList().stream().filter(d -> d.getHoraReferencia() != null).count();
                int tipo = this.obtenerTipo(asistencia.getDetalleAsistenciaList(), marcacionesMaximas.intValue());
                int contador = 0;
                int marcacionContador = 0;
                double minutosTardanza = 0;
                
                for (DetalleAsistencia detalle : asistencia.getDetalleAsistenciaList()) {
                    marcacionContador++;
                    if (contador == 0) {
                        detalleAsistencia = new RptAsistenciaDetallado();
                        detalleAsistencia.setEmpleado(asistencia.getEmpleado());
                        detalleAsistencia.setFecha(asistencia.getFecha());
                        detalleAsistencia.setTipo(tipo);
                        detalleAsistencia.setPermisos(this.traducirPermisos(asistencia.getPermisoList()));
                        detalleAsistencia.setReferencias(this.traducirReferencias(asistencia.getDetalleAsistenciaList()));
                        detalleAsistencia.setRegimenLaboral(regimenLaboral);
                    }

                    if (tipo == AnalizadorAsistencia.TARDANZA) {
                        
                        if(detalle.isBandera() && detalle.getHoraReferenciaTolerancia() != null && !detalle.isPermiso()){
                            System.out.println("BANDERA: "+detalle.getHoraReferencia());
                            
                            minutosTardanza = minutosTardanza + this.tardanzaMin(detalle.getHoraEvento(), detalle.getHoraReferenciaTolerancia());
                        }
                    }

                    switch (contador) {
                        case 0:
                            detalleAsistencia.setEnPermiso1(detalle.isPermiso());
                            detalleAsistencia.setHoraReferencia1(detalle.getHoraReferencia());
                            detalleAsistencia.setHoraTolerancia1(detalle.getHoraReferenciaTolerancia());
                            detalleAsistencia.setHoraEvento1(detalle.getHoraEvento());

                            break;
                        case 1:
                            detalleAsistencia.setEnPermiso2(detalle.isPermiso());
                            detalleAsistencia.setHoraReferencia2(detalle.getHoraReferencia());
                            detalleAsistencia.setHoraTolerancia2(detalle.getHoraReferenciaTolerancia());
                            detalleAsistencia.setHoraEvento2(detalle.getHoraEvento());
                            break;
                        case 2:
                            detalleAsistencia.setEnPermiso3(detalle.isPermiso());
                            detalleAsistencia.setHoraReferencia3(detalle.getHoraReferencia());
                            detalleAsistencia.setHoraTolerancia3(detalle.getHoraReferenciaTolerancia());
                            detalleAsistencia.setHoraEvento3(detalle.getHoraEvento());
                            break;
                        case 3:
                            detalleAsistencia.setEnPermiso4(detalle.isPermiso());
                            detalleAsistencia.setHoraReferencia4(detalle.getHoraReferencia());
                            detalleAsistencia.setHoraTolerancia4(detalle.getHoraReferenciaTolerancia());
                            detalleAsistencia.setHoraEvento4(detalle.getHoraEvento());
                            break;
                    }
//                    detalleAsistencia.getEnPermiso()[contador] = detalle.isPermiso();
//                    detalleAsistencia.getHoraReferencia()[contador] = detalle.getHoraReferencia();
//                    detalleAsistencia.getHoraTolerancia()[contador] = detalle.getHoraReferenciaTolerancia();
//                    detalleAsistencia.getHoraEvento()[contador] = detalle.getHoraEvento();
                    detalleAsistencia.setMarcacionesTotales(marcacionesMaximas.intValue());

                    contador++;
                    detalleAsistencia.setDetalleFinal(marcacionContador == marcacionesMaximas.intValue());
                    if(contador == marcacionesMaximas.intValue() && tipo != AnalizadorAsistencia.INASISTENCIA && !detalle.isPermiso()){
                        detalleAsistencia.setMinutosExtra(this.tardanzaMin(detalle.getHoraEvento(), detalle.getHoraReferencia()));
                    }
                    if (contador == 4 || contador == marcacionesMaximas.intValue()) {
                        detalleAsistencia.setMinutosTardanza(minutosTardanza);
                        registro.add(detalleAsistencia);
//                        detalleAsistencia = null;
                        contador = 0;
                        minutosTardanza = 0;
                    }
                }
            } else {
                detalleAsistencia = new RptAsistenciaDetallado();
                detalleAsistencia.setEmpleado(asistencia.getEmpleado());
                detalleAsistencia.setTipo(asistencia.getResultado());
                detalleAsistencia.setFecha(asistencia.getFecha());
                detalleAsistencia.setPermisos(obtenerMotivo(asistencia.getResultado(), asistencia));
                registro.add(detalleAsistencia);
                detalleAsistencia.setRegimenLaboral(regimenLaboral);
            }

        }

        return registro;
    }

    private String traducirPermisos(List<DetalleAsistencia> permisoList) {
        String permisos = "";
        Date horaInicio = null;
        Date horaFin = null;
        for (DetalleAsistencia detalle : permisoList) {
            if (detalle.isBandera()) {
                horaInicio = detalle.getHoraReferencia();
            } else {
                horaFin = detalle.getHoraReferencia();

                permisos += String.format(" %s - %s: %s ",
                        HerramientaGeneral.formatoHoraMinuto.format(horaInicio),
                        HerramientaGeneral.formatoHoraMinuto.format(horaFin),
                        detalle.getMotivo());
            }
        }
        return permisos;
    }

    private String traducirReferencias(List<DetalleAsistencia> detalleAsistenciaList) {
        String detalles = "";
        Date horaInicio = null;
        Date horaFin = null;
        for (DetalleAsistencia detalle : detalleAsistenciaList) {
            if (detalle.isBandera()) {
                horaInicio = detalle.getHoraReferencia();
            } else {
                horaFin = detalle.getHoraReferencia();

                detalles += String.format(" %s - %s ",
                        HerramientaGeneral.formatoHoraMinuto.format(horaInicio),
                        HerramientaGeneral.formatoHoraMinuto.format(horaFin));
            }
        }
        return detalles;
    }

    private int obtenerTipo(List<DetalleAsistencia> detalleAsistenciaList, int conteo) {

//        System.out.println("CONTEO: "+conteo.intValue());
        int marcacionesPendientes = 0;
        boolean hayTardanza = false;
        for (int i = 0; i < conteo; i++) {
            DetalleAsistencia detalle = detalleAsistenciaList.get(i);

            if (detalle.getHoraEvento() == null) {
                if (!detalle.isPermiso()) {
                    marcacionesPendientes++;
                }
            } else {
                hayTardanza
                        = hayTardanza
                        || detalle.isBandera() && tardanzaMin(FechaUtil.soloHora(detalle.getHoraEvento()), FechaUtil.soloHora(detalle.getHoraReferenciaTolerancia())) > 1;
            }
        }

        if (marcacionesPendientes > 0) {
            return AnalizadorAsistencia.INASISTENCIA;
        } else if (hayTardanza) {
            return AnalizadorAsistencia.TARDANZA;
        } else {
            return AnalizadorAsistencia.REGULAR;
        }
    }

    private String obtenerMotivo(int tipo, Asistencia asistencia) {
        switch (tipo) {
            case AnalizadorAsistencia.FERIADO:
                return asistencia.getFeriado().getNombre();
            case AnalizadorAsistencia.VACACION:
                return asistencia.getVacacion().getDocumento();
            case AnalizadorAsistencia.PERMISO_FECHA:
                return asistencia.getPermiso().getDocumento();
            case AnalizadorAsistencia.BOLETA_PERMISO:
                return asistencia.getBoleta().getMotivo().getDescripcion();
            default:
                return "";
        }
    }

    private double tardanzaMin(Date evento, Date tolerancia) {
        System.out.println("HORA EVENTO: "+evento+" TOLERANCIA: "+tolerancia);
        if (tolerancia.before(evento)) {
            double tardanza = (FechaUtil.soloHora(evento).getTime() - FechaUtil.soloHora(tolerancia).getTime()) / (60 * 1000);
            if(tardanza >= 1){
                return tardanza;
            }else{
                return 0.0;
            }
            
        } else {
            return 0.0;
        }
    }
}
