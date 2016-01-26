/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.vistas.render;

import com.biosis.biosislite.vistas.modelos.MCFiltro.TipoFiltro;
import static com.biosis.biosislite.vistas.modelos.MCFiltro.TipoFiltro.POR_EMPLEADO;
import static com.biosis.biosislite.vistas.modelos.MCFiltro.TipoFiltro.POR_GRUPO_HORARIO;
import static com.biosis.biosislite.vistas.modelos.MCFiltro.TipoFiltro.POR_OFICINA;
import static com.biosis.biosislite.vistas.modelos.MCFiltro.TipoFiltro.TODO;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author RyuujiMD
 */
public class RenderFiltro extends DefaultListCellRenderer{

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if(value != null){
            if(value instanceof TipoFiltro){
                TipoFiltro filtro = (TipoFiltro)value;
                switch(filtro){
                    case TODO:
                        value = "TODO";
                        break;
                    case POR_EMPLEADO:
                        value = "EMPLEADO";
                        break;
                    case POR_OFICINA:
                        value = "OFICINA";
                        break;
                    case POR_GRUPO_HORARIO:
                        value = "GRUPO HORARIO";
                        break;
                }                
            }
        }
        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); //To change body of generated methods, choose Tools | Templates.
    }
    
}
