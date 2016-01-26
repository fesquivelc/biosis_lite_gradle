/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.algoritmo;

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
public class InterpreteResumen implements Interprete<RptAsistenciaResumen> {

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
                    Long marcacionesMaximas = asistencia.getDetalleAsistenciaList().stream().filter(d -> d.getHoraReferencia() != null).count();
                    int tipo = this.obtenerTipo(asistencia.getDetalleAsistenciaList(), marcacionesMaximas.intValue());

                    if (tipo == AnalizadorAsistencia.INASISTENCIA) {
                        numeroDiasFalta++;
                    } else if (tipo == AnalizadorAsistencia.TARDANZA) {
                        tardanza += minutosTardanza(asistencia.getDetalleAsistenciaList(), marcacionesMaximas.intValue());
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
        for (int i = 0; i < conteo; i++) {
            DetalleAsistencia detalle = detalleAsistenciaList.get(i);

            if (detalle.getHoraEvento() == null) {
            } else if (detalle.isBandera()) {
                System.out.println("HORA EVENTO: " + detalle.getHoraEvento() + " HORA TOLERANCIA " + detalle.getHoraReferenciaTolerancia() + " BANDERA: " + detalle.isBandera());
                tardanza += tardanzaMin(FechaUtil.soloHora(detalle.getHoraEvento()), FechaUtil.soloHora(detalle.getHoraReferenciaTolerancia()));
            }
        }
        return tardanza;
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

        int posicion = 0;
        while (posicion < detalles.size()) {
            DetalleAsistencia ingreso = detalles.get(posicion);
            DetalleAsistencia salida = detalles.get(posicion + 1);

            if (!(ingreso.isPermiso() || salida.isPermiso())) {
                totalHoras += (salida.getHoraEvento().getTime() - ingreso.getHoraEvento().getTime()) / (60 * 1000 * 60);
            }

            posicion += 2;
        }

        return totalHoras;
    }
}
