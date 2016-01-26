/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores;


import com.biosis.biosislite.entidades.AsignacionHorario;
import com.biosis.biosislite.entidades.GrupoHorario;
import com.biosis.biosislite.entidades.Horario;
import com.biosis.biosislite.entidades.Turno;
import com.biosis.biosislite.entidades.escalafon.Departamento;
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
public class HorarioControlador extends Controlador<Horario>{

    public HorarioControlador() {
        super(Horario.class);
    }
    
    public List<Horario> buscarXEmpleado(Empleado empleado){
        String jpql = "SELECT h FROM Horario h WHERE "
                + "EXISTS(SELECT a FROM AsignacionHorario a WHERE "
                + "a.horario = h AND (a.empleado = :empleado OR "
                + "EXISTS(SELECT d FROM DetalleGrupoHorario d WHERE d.empleado = :empleado AND d.grupoHorario = a.grupoHorario))"
                + ")";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("empleado", empleado);
        return this.getDao().buscar(jpql, mapa);
    }
    
    public List<Horario> buscarXEmpleado(Empleado empleado, Date fechaInicio, Date fechaFin){
        String jpql = "SELECT h FROM Horario h WHERE "
                + "EXISTS(SELECT a FROM AsignacionHorario a WHERE "
                + "a.horario = h AND "
                + "(a.fechaInicio BETWEEN :fechaInicio AND :fechaFin OR :fechaInicio BETWEEN a.fechaInicio AND a.fechaFin)"
                + "AND (a.empleado = :empleado OR "
                + "EXISTS(SELECT d FROM DetalleGrupoHorario d WHERE d.empleado = :empleado AND d.grupoHorario = a.grupoHorario))"
                + ")";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("empleado", empleado);
        mapa.put("fechaInicio", fechaInicio);
        mapa.put("fechaFin", fechaFin);
        return this.getDao().buscar(jpql, mapa);
    }
    
    public List<Horario> buscarXGrupo(GrupoHorario grupo){
        String jpql = "SELECT h FROM Horario h WHERE "
                + "EXISTS(SELECT a FROM AsignacionHorario a WHERE "
                + "a.horario = h AND a.grupoHorario = :grupo)";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("grupo", grupo);
        return this.getDao().buscar(jpql, mapa);
    }
    
    public List<Horario> buscarXDepartamento(Departamento departamento){
        String jpql = "SELECT h FROM Horario h WHERE "
                + "EXISTS(SELECT a FROM AsignacionHorario a WHERE "
                + "a.horario = h AND (a.empleado.fichaLaboral.area = :departamento OR "
                + "EXISTS(SELECT d FROM DetalleGrupoHorario d WHERE d.empleado.fichaLaboral.area = :departamento AND d.grupoHorario = a.grupoHorario))"
                + ")";
        Map<String, Object> mapa = new HashMap<>();
        mapa.put("departamento", departamento);
        return this.getDao().buscar(jpql, mapa);
    }
    
    @Override
    public void prepararCrear() {
        super.prepararCrear(); //To change body of generated methods, choose Tools | Templates.
        this.getSeleccionado().setTurnoList(new ArrayList<Turno>());
        this.getSeleccionado().setAsignacionHorarioList(new ArrayList<AsignacionHorario>());
    }
    
}
