/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores;

import com.biosis.biosislite.entidades.TCSistema;


/**
 *
 * @author fesquivelc
 */
public class TCSistemaControlador extends Controlador<TCSistema>{
    
    private TCSistemaControlador() {
        super(TCSistema.class);
    }
    
    public static TCSistemaControlador getInstance() {
        return TCSistemaControladorHolder.INSTANCE;
    }
    
    private static class TCSistemaControladorHolder {

        private static final TCSistemaControlador INSTANCE = new TCSistemaControlador();
    }        
}
