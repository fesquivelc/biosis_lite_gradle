/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores;

import com.biosis.biosislite.entidades.escalafon.TipoVia;


/**
 *
 * @author fesquivelc
 */
public class TipoViaControlador extends Controlador<TipoVia>{
    
    private TipoViaControlador() {
        super(TipoVia.class);
    }
    
    public static TipoViaControlador getInstance() {
        return TipoViaControladorHolder.INSTANCE;
    }
    
    private static class TipoViaControladorHolder {

        private static final TipoViaControlador INSTANCE = new TipoViaControlador();
    }
}
