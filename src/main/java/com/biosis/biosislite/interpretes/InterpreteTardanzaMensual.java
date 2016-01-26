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
import com.biosis.biosislite.entidades.reportes.RptTardanzaMensual;
import com.personal.utiles.FechaUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author Francis
 */
public class InterpreteTardanzaMensual implements Interprete<RptTardanzaMensual> {

    @Override
    public List<RptTardanzaMensual> interpretar(List<Asistencia> registroAsistencia) {
        List<RptTardanzaMensual> tardanzas = new ArrayList<>();
        Calendar iterador = Calendar.getInstance();
        if (!registroAsistencia.isEmpty()) {
            double total = 0.0;
            Empleado empleado = registroAsistencia.get(0).getEmpleado();
            RptTardanzaMensual tardanzaMensual = new RptTardanzaMensual();
            tardanzaMensual.setEmpleado(empleado);
            tardanzaMensual.setFecha(registroAsistencia.get(0).getFecha());

            for (Asistencia asistencia : registroAsistencia) {
                if (asistencia.getResultado() == AnalizadorAsistencia.ASISTENCIA) {
                    if (!empleado.equals(asistencia.getEmpleado())) {
                        tardanzaMensual.setTotal(total);
                        tardanzas.add(tardanzaMensual);
                        empleado = asistencia.getEmpleado();
                        tardanzaMensual = new RptTardanzaMensual();
                        tardanzaMensual.setEmpleado(empleado);
                        tardanzaMensual.setFecha(iterador.getTime());
                    }
                    iterador.setTime(asistencia.getFecha());
                    int dia = iterador.get(Calendar.DAY_OF_MONTH);
                    System.out.println("DIA: " + dia);
                    try {
                        double tardanzaDia = this.minutosTardanza(asistencia.getDetalleAsistenciaList());
                        String valor = BeanUtils.getProperty(tardanzaMensual, "dia" + dia);
                        double valorDouble = Double.parseDouble(valor);
                        total += tardanzaDia;
                        valorDouble += tardanzaDia;
                        System.out.println(String.format("TOTAL: %s VALOR: %s", total, valorDouble));
                        BeanUtils.setProperty(tardanzaMensual, "dia" + dia, valorDouble);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        Logger.getLogger(InterpreteTardanzaMensual.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
            tardanzaMensual.setTotal(total);
            tardanzas.add(tardanzaMensual);

        }

        return tardanzas;
    }

    private int tipoAsistencia(int resultado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private double minutosTardanza(List<DetalleAsistencia> detalleList) {
        Double tardanzaTotal = 0.0;
        tardanzaTotal = detalleList.stream().filter((DetalleAsistencia detalle) -> (detalle.isBandera() && detalle.getHoraEvento() != null)).map((detalle) -> tardanzaMin(detalle.getHoraEvento(), detalle.getHoraReferenciaTolerancia())).reduce(tardanzaTotal, (accumulator, _item) -> accumulator + _item);
        return tardanzaTotal;
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

        if (marcacionesPendientes == conteo) {
            return AnalizadorAsistencia.INASISTENCIA;
        } else if (hayTardanza) {
            return AnalizadorAsistencia.TARDANZA;
        } else {
            return AnalizadorAsistencia.REGULAR;
        }
    }

    private double tardanzaMin(Date evento, Date tolerancia) {
        System.out.println("HORA EVENTO: " + evento + " TOLERANCIA: " + tolerancia);
        if (tolerancia.before(evento)) {
            double tardanza = (FechaUtil.soloHora(evento).getTime() - FechaUtil.soloHora(tolerancia).getTime()) / (60 * 1000);
            if (tardanza >= 1) {
                return tardanza;
            } else {
                return 0.0;
            }

        } else {
            return 0.0;
        }
    }
}
