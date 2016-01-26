/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores;

import com.biosis.biosislite.entidades.escalafon.TipoDocumento;


/**
 *
 * @author fesquivelc
 */
public class TipoDocumentoControlador extends Controlador<TipoDocumento>{
    
    private TipoDocumentoControlador() {
        super(TipoDocumento.class);
    }
    
    public static TipoDocumentoControlador getInstance() {
        return TipoDocumentoControladorHolder.INSTANCE;
    }
    
    private static class TipoDocumentoControladorHolder {

        private static final TipoDocumentoControlador INSTANCE = new TipoDocumentoControlador();
    }
}
