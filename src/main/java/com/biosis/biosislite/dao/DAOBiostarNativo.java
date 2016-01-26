/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.dao;

import com.personal.utiles.ParametrosUtil;
import com.personal.utiles.PropertiesUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Francis
 */
public class DAOBiostarNativo implements DAONativo {
    private Connection conn;

    @Override
    public Connection obtenerConexion() {
        Properties configuracion = PropertiesUtil.cargarProperties("config/biostar-config.properties");
        int tipoBD = Integer.parseInt(configuracion.getProperty("tipo"));

        String driver = ParametrosUtil.obtenerDriver(tipoBD);
        String url = configuracion.getProperty("url");
        String usuario = configuracion.getProperty("usuario");
        String password = configuracion.getProperty("password");

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, usuario, password);
            return conn;
        } catch (SQLException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DAOBiostarNativo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void cerrarConexion() {
        if(conn != null){
            try {
                if(!conn.isClosed()){
                    conn.close();
                }
                conn = null;
            } catch (SQLException ex) {
                Logger.getLogger(DAOBiostarNativo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
