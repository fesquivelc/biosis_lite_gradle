/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.controladores.biostar;

import com.biosis.biosislite.dao.DAOBiostarNativo;
import com.biosis.biosislite.dao.DAONativo;
import com.biosis.biosislite.entidades.biostar.Evento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author RyuujiMD
 */
public class EventoControlador {

    private final DAONativo dao = new DAOBiostarNativo();
    private final String cadenaINSERT = "INSERT INTO TB_EVENT_LOG (nDateTime,nReaderIdn,nEventIdn,nUserID,nIsLog,nTNAEvent,nIsUseTA,nType) "
            + "VALUES (?,?,?,?,?,?,?,?);";

    public static EventoControlador getInstance() {
        return EventoControladorHolder.INSTANCE;
    }

    private static class EventoControladorHolder {

        private static final EventoControlador INSTANCE = new EventoControlador();
    }

    public boolean guardarLote(List<Evento> evento) {
        try {
            Connection conn = this.dao.obtenerConexion();

            evento.stream().forEach(e -> {
                try {
                    PreparedStatement ps = conn.prepareStatement(cadenaINSERT);
                    ps.setInt(1, e.getFechaHora());
                    ps.setInt(2, e.getEquipoID());
                    ps.setInt(3, e.getEvento());
                    ps.setInt(4, e.getEmpleadoNroDocumento());
                    ps.setInt(5, e.getLog());
                    ps.setInt(6, e.getTnaEvent());
                    ps.setInt(7, e.getUseTA());
                    ps.setInt(8, e.getType());
                    ps.execute();
                } catch (SQLException ex) {
                    Logger.getLogger(EventoControlador.class.getName()).log(Level.ERROR, null, ex);
                }
            });
            return true;
            
        } catch (Exception e) {
            LOG.error("ERROR AL GUARDAR LOTE: "+e.getMessage()+" "+e.getLocalizedMessage());
            return false;
        } finally {
            this.dao.cerrarConexion();
        }
    }
    private static final Logger LOG = Logger.getLogger(EventoControlador.class.getName());

}
