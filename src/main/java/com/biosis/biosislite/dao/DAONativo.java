/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.dao;

import java.sql.Connection;

/**
 *
 * @author Francis
 */
public interface DAONativo {
    Connection obtenerConexion();
    void cerrarConexion();    
}
