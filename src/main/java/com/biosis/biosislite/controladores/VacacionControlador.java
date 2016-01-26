/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores;

import com.biosis.biosislite.entidades.Periodo;
import com.biosis.biosislite.entidades.Vacacion;
import com.biosis.biosislite.entidades.escalafon.Empleado;
import com.personal.utiles.FechaUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fesquivelc
 */
public class VacacionControlador extends Controlador<Vacacion> {

    public VacacionControlador() {
        super(Vacacion.class);
    }

    public List<Vacacion> buscarXEmpleadoXFecha(Empleado empleado, Date fechaInicio, Date fechaFin, int desde, int tamanio) {
        String jpql = "SELECT v FROM Vacacion v"
                + " WHERE v.empleado = :dni"
                + " AND v.fechaInicio BETWEEN :fechaInicio AND :fechaFin"
                + " ORDER BY v.empleado,v.fechaInicio DESC";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("dni", empleado);
        mapa.put("fechaInicio", fechaInicio);
        mapa.put("fechaFin", fechaFin);
        return this.getDao().buscar(jpql, mapa, desde, tamanio);
    }

    public List<Vacacion> buscarXFecha(Date fechaInicio, Date fechaFin, int desde, int tamanio) {
        String jpql = "SELECT v FROM Vacacion v"
                + " WHERE v.fechaInicio BETWEEN :fechaInicio AND :fechaFin"
                + " ORDER BY v.fechaInicio DESC";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("fechaInicio", fechaInicio);
        mapa.put("fechaFin", fechaFin);
        return this.getDao().buscar(jpql, mapa, desde, tamanio);
    }

    public int contarXEmpleadoXFecha(Empleado empleado, Date fechaInicio, Date fechaFin) {
        String jpql = "SELECT COUNT(v.id) FROM Vacacion v"
                + " WHERE v.empleado = :dni"
                + " AND v.fechaInicio BETWEEN :fechaInicio AND :fechaFin";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("dni", empleado);
        mapa.put("fechaInicio", fechaInicio);
        mapa.put("fechaFin", fechaFin);
        return this.getDao().contar(jpql, mapa);
    }

    public int contarXFecha(Date fechaInicio, Date fechaFin) {
        String jpql = "SELECT COUNT(v.id) FROM Vacacion v"
                + " WHERE v.fechaInicio BETWEEN :fechaInicio AND :fechaFin";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("fechaInicio", fechaInicio);
        mapa.put("fechaFin", fechaFin);
        return this.getDao().contar(jpql, mapa);
    }

    public Vacacion buscarXDia(Empleado empleado, Date dia) {
//        String jpql =  "SELECT v FROM Vacacion v"
//                + " WHERE v.empleado = :dni "
//                + " AND ( "
//                + "(v.fechaInterrupcion IS NULL AND :dia BETWEEN v.fechaInicio AND v.fechaFin) OR "
//                + "(v.fechaInterrupcion IS NOT NULL AND :dia >= v.fechaInicio AND :dia < v.fechaInterrupcion)"
//                + ") AND ("
//                + "v.interrupcionVacacion IS NULL OR (v.interrupcionVacacion IS NOT NULL AND "
//                + "((:dia >= v.fechaInicio AND :dia < v.interrupcionVacacion.fechaInicio) OR (:dia > v.interrupcionVacacion.fechaFin AND :dia <= v.fechaFin))"
//                + ")"
//                + ")";

        //CONSULTA PROVISIONAL
        String jpql = "SELECT v FROM Vacacion v"
                + " WHERE v.empleado = :dni ";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("dni", empleado);

        List<Vacacion> listaVacaciones = this.getDao().buscar(jpql, mapa);
        System.out.println("VACACIONES: " + listaVacaciones.size());

        //NUEVO ALGORITMO
        List<Vacacion> vacacionFinal = null;
        
        for (Vacacion vac : listaVacaciones) {

            String jpql2 = "SELECT v FROM Vacacion v"
                    + " WHERE v.empleado=:dni "
                    + " AND :dia BETWEEN :fechaInicio AND :fechaFin";

            Map<String, Object> mapa2 = new HashMap<>();
            mapa2.put("dni", empleado);
            mapa2.put("dia", FechaUtil.soloFecha(dia));
            mapa2.put("fechaInicio", vac.getFechaInicio());
            mapa2.put("fechaFin", vac.getFechaFin());
            
            System.out.println( "DIA:"+ FechaUtil.soloFecha(dia)+", FECHA FIN:" +vac.getFechaFin());
            
            List<Vacacion> vacacionEncontrada = this.getDao().buscar(jpql2, mapa2);
            
            
            
            if(!vacacionEncontrada.isEmpty()){
                System.out.println("Encontro Vacacion: "+ FechaUtil.soloFecha(dia));
                vacacionFinal = vacacionEncontrada;
                break;
            }

        }
//        mapa2.put("dni", empleado);
//        mapa2.put("dia", dia);
//        List<Vacacion> vacacion = this.getDao().buscar(jpql, mapa, -1, 1);
//        List<Vacacion> vacacion = this.getDao().buscar(jpql, mapa);

//        System.out.println("TAMAÑO: " + vacacion.size());
        if (vacacionFinal== null) {
            return null;
        } else {
            return vacacionFinal.get(0);
        }
    }

    public List<Vacacion> buscarXEmpleadoXPeriodo(Empleado empleado, Periodo periodo) {
        String jpql = "SELECT v FROM Vacacion v WHERE v.empleado = :dni AND v.periodo = :periodo";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("dni", empleado);
        mapa.put("periodo", periodo);
        List<Vacacion> vacacion = this.getDao().buscar(jpql, mapa);
        return vacacion;
    }

    public List<Vacacion> buscarXEmpleadoXPeriodoNoReprogramacion(Empleado empleado, Periodo periodo) {
        String jpql = "SELECT v FROM Vacacion v WHERE v.empleado = :dni AND v.periodo = :periodo AND v.vacacionOrigen IS NULL";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("dni", empleado);
        mapa.put("periodo", periodo);
        List<Vacacion> vacacion = this.getDao().buscar(jpql, mapa);
        return vacacion;
    }
    
    public List<Vacacion> buscarXEmpleadoEntreFecha(Empleado empleado, Date fechaInicio, Date fechaFin){
        String jpql = "SELECT v FROM Vacacion v "
                + "WHERE "
                + "v.empleado = :empleado AND "
                + "((v.fechaInicio <= :fechaInicio AND v.fechaFin >= :fechaInicio) OR (v.fechaInicio BETWEEN :fechaInicio AND :fechaFin)) "
                + "ORDER BY v.fechaInicio";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("empleado", empleado);
        mapa.put("fechaInicio", fechaInicio);
        mapa.put("fechaFin", fechaFin);
        return this.getDao().buscar(jpql, mapa);
    }
}
