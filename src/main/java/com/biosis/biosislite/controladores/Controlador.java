/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biosis.biosislite.controladores;

import com.biosis.biosislite.dao.DAO;
import com.biosis.biosislite.dao.DAOBiosis;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gabriel
 * @param <T>
 */
public abstract class Controlador<T>{
    private final DAO<T> dao;
    public static final int NUEVO = 1;
    public static final int MODIFICAR = 2;
    public static final int ELIMINAR = 3;
    public static final int LEER = 4;
    private T seleccionado;
    private final Class<T> seleccionadoClass;

    public T getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(T seleccionado) {
        this.seleccionado = seleccionado;
    }        

    public DAO<T> getDao() {
        return dao;
    }

    public Controlador(Class<T> clase) {
        this.seleccionadoClass = clase;
        this.dao = new DAOBiosis<>(clase);
    }        
    
    public Controlador(Class<T> clase, DAO<T> dao){
        this.seleccionadoClass = clase;
        this.dao = dao;
    }

    public boolean guardar(T objeto) {
        try {
            return this.dao.guardar(objeto);
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean guardarLote(List<T> lote){
        try {
            return this.dao.guardarLote(lote);
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean modificar(T objeto) {
        try {
            return this.dao.modificar(objeto);
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean eliminar(T objeto) {
        try {
            return this.dao.eliminar(objeto);
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public List<T> buscarTodos(){
        try {
            return this.dao.buscarTodos();
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public T buscarPorId(Object id){
        try {
            return this.dao.buscarPorId(id);
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public List<T> buscar(String nombre){
        try {
            return this.dao.buscar(nombre);
        } catch (Exception ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public int conteo(){
        return this.dao.contar();
    }
    
    public void prepararCrear(){
        try {
            seleccionado = seleccionadoClass.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public boolean accion(int accion) {
        switch (accion) {
            case Controlador.NUEVO:
                return this.guardar(seleccionado);
            case Controlador.MODIFICAR:
                return this.modificar(seleccionado);
            case Controlador.ELIMINAR:
                return this.eliminar(seleccionado);
            default:
                break;
        }
        return false;
    }
}
