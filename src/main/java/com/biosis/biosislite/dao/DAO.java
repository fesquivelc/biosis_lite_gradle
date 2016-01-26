/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;

/**
 *
 * @author RyuujiMD
 * @param <T>
 */
public interface DAO<T> {
    public EntityManager getEntityManager();
    public Connection getConexion();
    public Boolean guardar(T objeto);
    public Boolean eliminar(T objeto);
    public Boolean modificar(T objeto);
    public Boolean guardarLote(List<T> objeto);
    public List<T> buscarTodos();
    public List<T> buscar(String queryJPQL);
    public List<T> buscar(String queryJPQL, Map<String, Object> parametros);
    public List<T> buscar(String queryJPQL, Map<String, Object> parametros, int inicio, int tamanio);
    public int contar();
    public int contar(String queryJPQL, Map<String, Object> parametros);
    public T buscarPorId(Object id);
    
}
