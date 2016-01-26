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
import com.biosis.biosislite.entidades.escalafon.Empleado;
import com.biosis.biosislite.entidades.reportes.RptAsistenciaResumen;
import com.personal.utiles.FechaUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Francis
 */
public class InterpreteResumenGRP implements Interprete<RptAsistenciaResumen> {

    @Override
    public List<RptAsistenciaResumen> interpretar(List<Asistencia> registroAsistencia) {
        List<RptAsistenciaResumen> resumen = new ArrayList<>();
        if (!registroAsistencia.isEmpty()) {
            Empleado empleado = registroAsistencia.get(0).getEmpleado();
            RptAsistenciaResumen asistenciaResumen = new RptAsistenciaResumen();

            double horasLaboradas = 0;
            double minutosPermisoConGoce = 0;
            double minutosPermisoSinGoce = 0;
            double tardanza = 0;
            double tardanzaRefrigerio = 0;
            int numeroDiasFalta = 0;
            int numeroDiasPermisoConGoce = 0;
            int numeroDiasPermisoSinGoce = 0;
            int numeroDiasVacaciones = 0;
            int numeroDiasFeriados = 0;

            asistenciaResumen.setEmpleado(empleado);
            for (Asistencia asistencia : registroAsistencia) {
                if (!empleado.equals(asistencia.getEmpleado())) {
                    asistenciaResumen.setHorasLaboradas(horasLaboradas);
                    asistenciaResumen.setMinutosPermisoConGoce(minutosPermisoConGoce);
                    asistenciaResumen.setMinutosPermisoSinGoce(minutosPermisoSinGoce);
                    asistenciaResumen.setMinutosTardanza(tardanza);
                    asistenciaResumen.setMinutosTardanzaRefrigerio(tardanzaRefrigerio);
                    asistenciaResumen.setNumeroDiasFalta(numeroDiasFalta);
                    asistenciaResumen.setNumeroDiasPermisoConGoce(numeroDiasPermisoConGoce);
                    asistenciaResumen.setNumeroDiasPermisoSinGoce(numeroDiasPermisoSinGoce);
                    asistenciaResumen.setNumeroFeriados(numeroDiasFeriados);
                    asistenciaResumen.setNumeroVacaciones(numeroDiasVacaciones);

                    resumen.add(asistenciaResumen);

                    asistenciaResumen = new RptAsistenciaResumen();
                    empleado = asistencia.getEmpleado();

                    horasLaboradas = 0;
                    minutosPermisoConGoce = 0;
                    minutosPermisoSinGoce = 0;
                    tardanza = 0;
                    numeroDiasFalta = 0;
                    numeroDiasPermisoConGoce = 0;
                    numeroDiasPermisoSinGoce = 0;
                    numeroDiasVacaciones = 0;
                    numeroDiasFeriados = 0;
                    asistenciaResumen.setEmpleado(empleado);
                }
                if (asistencia.getResultado() == AnalizadorAsistencia.ASISTENCIA) {
                    System.out.println("ES UNA ASISTENCIA");
                    Long marcacionesMaximas = asistencia.getDetalleAsistenciaList().stream().filter(d -> d.getHoraReferencia() != null).count();
                    int tipo = this.obtenerTipo(asistencia.getDetalleAsistenciaList(), marcacionesMaximas.intValue(), asistencia.getPermisoList());

                    if (tipo == AnalizadorAsistencia.INASISTENCIA) {
                        System.out.println("ES UNA FALTA");
                        numeroDiasFalta++;
                    } else if (tipo == AnalizadorAsistencia.TARDANZA || tipo == AnalizadorAsistencia.INCONSISTENCIA) {
                        tardanza += minutosTardanza(asistencia.getDetalleAsistenciaList(), marcacionesMaximas.intValue());
                        tardanzaRefrigerio += minutosTardanzaRefrigerio(asistencia.getDetalleAsistenciaList());
                    }

                    if (asistencia.getPermisoList() != null) {
                        minutosPermisoConGoce += this.minutosPermisos(asistencia.getPermisoList().stream().filter(perm -> perm.isPermisoConGoce()).collect(Collectors.toList()));
                        System.out.println("MINUTOS CON GOCE " + minutosPermisoConGoce);
                        minutosPermisoSinGoce += this.minutosPermisos(asistencia.getPermisoList().stream().filter(perm -> !perm.isPermisoConGoce()).collect(Collectors.toList()));
                        System.out.println("MINUTOS CON GOCE " + minutosPermisoSinGoce);
                    }

                    if (tipo == AnalizadorAsistencia.TARDANZA || tipo == AnalizadorAsistencia.REGULAR) {
                        horasLaboradas += obtenerHorasLaboradas(asistencia.getDetalleAsistenciaList());
                    }
                } else {
                    switch (asistencia.getResultado()) {
                        case AnalizadorAsistencia.PERMISO_FECHA:
                            if (asistencia.getPermiso().getTipoPermiso().getTipoDescuento() == 'C') {
                                numeroDiasPermisoConGoce++;
                            } else {
                                numeroDiasPermisoSinGoce++;
                            }
                            break;
                        case AnalizadorAsistencia.BOLETA_PERMISO:
                            if (isConGoce(asistencia.getBoleta().getMotivo().getId().intValue())) {
                                numeroDiasPermisoConGoce++;
                            } else {
                                numeroDiasPermisoSinGoce++;
                            }
                            break;
                        case AnalizadorAsistencia.VACACION:
                        case AnalizadorAsistencia.BOLETA_VACACION:
                            numeroDiasVacaciones++;
                            break;
                        case AnalizadorAsistencia.FERIADO:
                            numeroDiasFeriados++;
                            break;
                    }
                }
            }

            asistenciaResumen.setHorasLaboradas(horasLaboradas);
            asistenciaResumen.setMinutosPermisoConGoce(minutosPermisoConGoce);
            asistenciaResumen.setMinutosPermisoSinGoce(minutosPermisoSinGoce);
            asistenciaResumen.setMinutosTardanza(tardanza);
            asistenciaResumen.setMinutosTardanzaRefrigerio(tardanzaRefrigerio);
            asistenciaResumen.setNumeroDiasFalta(numeroDiasFalta);
            asistenciaResumen.setNumeroDiasPermisoConGoce(numeroDiasPermisoConGoce);
            asistenciaResumen.setNumeroDiasPermisoSinGoce(numeroDiasPermisoSinGoce);
            asistenciaResumen.setNumeroFeriados(numeroDiasFeriados);
            asistenciaResumen.setNumeroVacaciones(numeroDiasVacaciones);

            resumen.add(asistenciaResumen);

        }
        return resumen;
    }

    private boolean isConGoce(int idMotivo) {
        //RECORDAR QUE LOS TIPOS DE PERMISO SIN GOCE SON : 19,22,23,25,26
        return !(idMotivo == 19 || idMotivo == 22 || idMotivo == 23 || idMotivo == 25 || idMotivo == 26);
    }

    private double minutosTardanza(List<DetalleAsistencia> detalleAsistenciaList, int conteo) {
        double tardanza = 0;
        DetalleAsistencia detalleEntrada = detalleAsistenciaList.get(0);
        if (detalleEntrada.getHoraEvento() == null) {
        } else if (detalleEntrada.isBandera()) {
            System.out.println("HORA EVENTO: " + detalleEntrada.getHoraEvento() + " HORA TOLERANCIA " + detalleEntrada.getHoraReferenciaTolerancia() + " BANDERA: " + detalleEntrada.isBandera());
            tardanza += tardanzaMin(FechaUtil.soloHora(detalleEntrada.getHoraEvento()), FechaUtil.soloHora(detalleEntrada.getHoraReferenciaTolerancia()));
        }
        return tardanza;
    }
    
    private double minutosTardanzaRefrigerio(List<DetalleAsistencia> detalleAsistenciaList) {
        double tardanza = 0;
        for(int i = 1; i < detalleAsistenciaList.size(); i++){
            DetalleAsistencia detalleEntrada = detalleAsistenciaList.get(i);
            if(detalleEntrada.isBandera() && detalleEntrada.getHoraEvento() != null){
                tardanza += tardanzaMin(FechaUtil.soloHora(detalleEntrada.getHoraEvento()), FechaUtil.soloHora(detalleEntrada.getHoraReferenciaTolerancia()));
            }
        }
        return tardanza;
    }

    private int obtenerTipo(List<DetalleAsistencia> detalleAsistenciaList, int conteo,List<DetalleAsistencia> permisoList) {

//        System.out.println("CONTEO: "+conteo.intValue());
//        int marcacionesPendientes = 0;
//        boolean hayTardanza = false;
//        for (int i = 0; i < conteo; i++) {
//            DetalleAsistencia detalle = detalleAsistenciaList.get(i);
//
//            if (detalle.getHoraEvento() == null) {
//                if (!detalle.isPermiso()) {
//                    marcacionesPendientes++;
//                }
//            } else {
//                hayTardanza
//                        = hayTardanza
//                        || detalle.isBandera() && tardanzaMin(FechaUtil.soloHora(detalle.getHoraEvento()), FechaUtil.soloHora(detalle.getHoraReferenciaTolerancia())) > 1;
//            }
//        }
//
//        if (marcacionesPendientes > 0) {
//            return AnalizadorAsistencia.INASISTENCIA;
//        } else if (hayTardanza) {
//            return AnalizadorAsistencia.TARDANZA;
//        } else {
//            return AnalizadorAsistencia.REGULAR;
//        }
        int marcacionesPendientes = 0;
        boolean hayTardanza = false;
        DetalleAsistencia entrada = detalleAsistenciaList.get(0);
        DetalleAsistencia salida = detalleAsistenciaList.get(detalleAsistenciaList.size() - 1);

        if ((entrada.getHoraEvento() != null || entrada.isPermiso()) && (salida.getHoraEvento() != null || salida.isPermiso())) {
            hayTardanza = (!entrada.isPermiso() && tardanzaMin(FechaUtil.soloHora(entrada.getHoraEvento()), FechaUtil.soloHora(entrada.getHoraReferenciaTolerancia())) > 1);

            if (hayTardanza) {
                return AnalizadorAsistencia.TARDANZA;
            } else {
                return AnalizadorAsistencia.REGULAR;
            }
        } else {
            if ((entrada.getHoraEvento() == null && !entrada.isPermiso()) && (salida.getHoraEvento() == null && !salida.isPermiso())) {
                /*
                 PUEDE SER FALTA, SE HA DE COMPROBAR INCONSISTENCIA
                 */
                if (permisoList != null) {
                    if (!permisoList.isEmpty()) {
                        return AnalizadorAsistencia.INCONSISTENCIA;
                    }
                }

                if (conteo > 2) {
                    for (int i = 1; i < detalleAsistenciaList.size() - 1; i++) {
                        DetalleAsistencia refrigerio = detalleAsistenciaList.get(i);
                        if (refrigerio.getHoraEvento() != null) {
                            return AnalizadorAsistencia.INCONSISTENCIA;
                        }

                    }
                    return AnalizadorAsistencia.INASISTENCIA;
                } else {
                    return AnalizadorAsistencia.INASISTENCIA;
                }
            } else {
                return AnalizadorAsistencia.INCONSISTENCIA;
            }
        }
    }

    private double tardanzaMin(Date evento, Date tolerancia) {
        if (tolerancia.before(evento)) {
            double tardanza = (evento.getTime() - tolerancia.getTime()) / (60 * 1000);
            return tardanza;
        } else {
            return 0.0;
        }
    }

    private double minutosPermisos(List<DetalleAsistencia> detalles) {
        double total = 0;
        int posicion = 0;
        while (posicion < detalles.size()) {
            DetalleAsistencia permisoI = detalles.get(posicion);
            DetalleAsistencia permisoF = detalles.get(posicion + 1);

            total += (permisoF.getHoraReferencia().getTime() - permisoI.getHoraReferencia().getTime()) / (60 * 1000);

            posicion += 2;
        }
        return total;
    }

    private double obtenerHorasLaboradas(List<DetalleAsistencia> detalles) {
        double totalHoras = 0;
        System.out.println("ENTRO A HORAS LABORADAS");
        int posicion = 0;
        while (posicion < detalles.size()) {
            System.out.println("ENTRO AL WHILE");
            DetalleAsistencia ingreso = detalles.get(posicion);
            DetalleAsistencia salida = detalles.get(posicion + 1);
            
            if (!(ingreso.isPermiso() || salida.isPermiso())) {
                System.out.println("ESTA OBTENIENDO HORAS");
                long salidaMilis; 
//                        = salida.getHoraEvento() == null ? salida.getHoraReferencia().getTime() : salida.getHoraEvento().getTime();
                long ingresoMilis; 
//                        = ingreso.getHoraEvento() == null ? ingreso.getHoraReferencia().getTime() : ingreso.getHoraEvento().getTime();
                if(salida.getHoraEvento() == null){
                    salidaMilis = salida.getHoraReferencia().getTime();
                }else if(FechaUtil.soloHora(salida.getHoraEvento()).compareTo(salida.getHoraReferencia())>0){
                    salidaMilis = salida.getHoraReferencia().getTime();
                }else{
                    salidaMilis = FechaUtil.soloHora(salida.getHoraEvento()).getTime();
                }
                if(ingreso.getHoraEvento() == null){
                    ingresoMilis = ingreso.getHoraReferencia().getTime();
                }else if(FechaUtil.soloHora(ingreso.getHoraEvento()).compareTo(ingreso.getHoraReferencia())<0){
                    ingresoMilis = ingreso.getHoraReferencia().getTime();
                }else{
                    ingresoMilis = FechaUtil.soloHora(ingreso.getHoraEvento()).getTime();
                }
                
                totalHoras += (salidaMilis - ingresoMilis);
                Date ingresod = new Date(ingresoMilis);
                Date salidad = new Date(salidaMilis);
                System.out.println("CONTEO DE: "+ingresod+" - "+salidad);
            }

            posicion += 2;
        }
        return totalHoras / (60 * 1000 * 60);
    }
}
