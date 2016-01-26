/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores;

import com.biosis.biosislite.entidades.AsignacionPermiso;
import com.biosis.biosislite.entidades.Permiso;
import com.biosis.biosislite.entidades.escalafon.Empleado;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fesquivelc
 */
public class PermisoControlador extends Controlador<Permiso>{

    public PermisoControlador() {
        super(Permiso.class);
    }

    @Override
    public void prepararCrear() {
        super.prepararCrear(); //To change body of generated methods, choose Tools | Templates.
        this.getSeleccionado().setAsignacionPermisoList(new ArrayList<AsignacionPermiso>());
    }
    
    public List<Permiso> buscarXEmpleadoXFechaEntreHora(Empleado empleado, Date fecha, Date horaInicio, Date horaFin){
        String jpql = "SELECT p FROM Permiso p WHERE "
                + "p.porFecha = FALSE AND "
                + "p.fechaInicio = :fecha AND "
//                + "(:horaInicio BETWEEN p.horaInicio AND p.horaFin) AND "
                + "EXISTS(SELECT a FROM AsignacionPermiso a WHERE a.empleado = :empleado AND a.permiso = p) "
                + "ORDER BY p.horaInicio ASC";
        
        Map<String, Object> variables = new HashMap();
        variables.put("empleado", empleado);
        variables.put("fecha", fecha);
//        variables.put("horaInicio", horaInicio);
//        variables.put("horaFin", horaFin);
        
        return this.getDao().buscar(jpql, variables);
    }
    
    public Permiso buscarXEmpleadoXFecha(Empleado empleado, Date fecha){
        String jpql = "SELECT p FROM Permiso p WHERE "
                + "p.porFecha = TRUE AND "
                + ":fecha BETWEEN p.fechaInicio AND p.fechaFin  AND "
//                + "(:horaInicio BETWEEN p.horaInicio AND p.horaFin) AND "
                + "EXISTS(SELECT a FROM AsignacionPermiso a WHERE a.empleado = :empleado AND a.permiso = p) "
                + "ORDER BY p.horaInicio ASC";
        
        Map<String, Object> variables = new HashMap();
        variables.put("empleado", empleado);
        variables.put("fecha", fecha);
//        variables.put("horaInicio", horaInicio);
//        variables.put("horaFin", horaFin);
        List<Permiso> permisos = this.getDao().buscar(jpql, variables);
        
        return permisos.isEmpty() ? null : permisos.get(0);
    }
    
    public List<Permiso> buscarXEmpleadoXHoraEntreFecha(Empleado empleado, Date fechaInicio, Date fechaFin){
        String jpql = "SELECT ap.permiso FROM AsignacionPermiso ap WHERE "
                + "ap.empleado = :empleado AND "
                + "((ap.permiso.fechaInicio <= :fechaInicio AND ap.permiso.fechaFin >= :fechaInicio) OR (ap.permiso.fechaInicio BETWEEN :fechaInicio AND :fechaFin)) AND "
                + "ap.permiso.opcion = 'H' "
                + "ORDER BY ap.permiso.fechaInicio, ap.permiso.horaInicio";
        Map<String, Object> variables = new HashMap();
        variables.put("empleado", empleado);
        variables.put("fechaInicio", fechaInicio);
        variables.put("fechaFin", fechaFin);
        return this.getDao().buscar(jpql, variables);
    }
    
    public List<Permiso> buscarXEmpleadoXFechaEntreFecha(Empleado empleado, Date fechaInicio, Date fechaFin){
        String jpql = "SELECT ap.permiso FROM AsignacionPermiso ap WHERE "
                + "ap.empleado = :empleado AND "
                + "((ap.permiso.fechaInicio <= :fechaInicio AND ap.permiso.fechaFin >= :fechaInicio) OR (ap.permiso.fechaInicio BETWEEN :fechaInicio AND :fechaFin)) AND "
                + "ap.permiso.opcion = 'F' "
                + "ORDER BY ap.permiso.fechaInicio, ap.permiso.horaInicio";
        Map<String, Object> variables = new HashMap();
        variables.put("empleado", empleado);
        variables.put("fechaInicio", fechaInicio);
        variables.put("fechaFin", fechaFin);
        return this.getDao().buscar(jpql, variables);
    }
    
    public List<Permiso> buscarPermisosPorHoraEnFecha(Empleado empleado, Date fecha){
        String jpql = "SELECT ap.permiso FROM AsignacionPermiso ap WHERE "
                + "ap.empleado = :empleado AND "
                + "ap.permiso.fechaInicio = :fecha AND "
                + "ap.permiso.opcion = 'H' "
                + "ORDER BY ap.permiso.fechaInicio,ap.permiso.horaInicio";
        Map<String, Object> variables = new HashMap();
        variables.put("empleado", empleado);
        variables.put("fecha", fecha);
        
        return this.getDao().buscar(jpql, variables);
    }
    
}
