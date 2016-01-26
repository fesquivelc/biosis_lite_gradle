/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores.sisgedo;

import com.biosis.biosislite.controladores.Controlador;
import com.biosis.biosislite.dao.DAOSISGEDO;
import com.biosis.biosislite.entidades.escalafon.Empleado;
import com.biosis.biosislite.entidades.sisgedo.Boleta;
import com.biosis.biosislite.utiles.HerramientaGeneral;
import com.personal.utiles.FechaUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author RyuujiMD
 */
public class BoletaControlador extends Controlador<Boleta>{
    
    private BoletaControlador() {
        super(Boleta.class, new DAOSISGEDO<Boleta>());
    }
    
    public static BoletaControlador getInstance() {
        return BoletaControladorHolder.INSTANCE;
    }
    
    public List<Boleta> permisoXFechaXEmpleadoEntreFecha(Empleado empleado, Date fechaInicio, Date fechaFin){
        String jpql = "SELECT b FROM Boleta b WHERE b.activo = 'A' AND b.tipo = 2 AND b.usuario.numeroDocumento = :empleado AND ((:inicio <= b.inicioFechaHora AND :fin >= b.inicioFechaHora) OR (b.inicioFechaHora <= :inicio AND :inicio <= b.finFechaHora))";
        Map<String,Object> parametros = new HashMap<>();
        parametros.put("empleado", empleado.getNroDocumento());
        parametros.put("inicio", FechaUtil.soloFecha(fechaInicio));
        parametros.put("fin", FechaUtil.unirFechaHora(fechaFin,HerramientaGeneral.horaFinal));
        return this.getDao().buscar(jpql, parametros);
    }
    
    public List<Boleta> permisoXHoraXEmpleadoEntreFecha(Empleado empleado, Date fechaInicio, Date fechaFin){
        String jpql = "SELECT b FROM Boleta b WHERE b.activo = 'C' AND b.tipo = 1  AND (b.motivo NOT IN (31,32,33,34)) AND b.usuario.numeroDocumento = :empleado AND ((:inicio <= b.inicioFechaHora AND :fin >= b.inicioFechaHora) OR (b.inicioFechaHora <= :inicio AND :inicio <= b.retornoFechaHora))";
        Map<String,Object> parametros = new HashMap<>();
        parametros.put("empleado", empleado.getNroDocumento());
        parametros.put("inicio", FechaUtil.soloFecha(fechaInicio));
        parametros.put("fin", FechaUtil.unirFechaHora(fechaFin,HerramientaGeneral.horaFinal));
        return this.getDao().buscar(jpql, parametros);
    }
    
    private static class BoletaControladorHolder {

        private static final BoletaControlador INSTANCE = new BoletaControlador();
    }
}
