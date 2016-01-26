/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores.biostar;

import com.biosis.biosislite.controladores.Controlador;
import com.biosis.biosislite.dao.DAOBiostar;
import com.biosis.biosislite.entidades.biostar.Reader;


/**
 *
 * @author RyuujiMD
 */
public class ReaderControlador extends Controlador<Reader>{
    
    private ReaderControlador() {
        super(Reader.class, new DAOBiostar<>(Reader.class));
    }
    
    public static ReaderControlador getInstance() {
        return EventoControladorHolder.INSTANCE;
    }
    
    private static class EventoControladorHolder {

        private static final ReaderControlador INSTANCE = new ReaderControlador();
    }
}
