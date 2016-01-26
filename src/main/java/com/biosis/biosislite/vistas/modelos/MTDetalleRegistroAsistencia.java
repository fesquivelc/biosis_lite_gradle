/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.vistas.modelos;

import com.biosis.biosislite.entidades.reportes.RptAsistenciaDetallado;
import com.personal.utiles.ModeloTabla;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 *
 * @author RyuujiMD
 */
public class MTDetalleRegistroAsistencia extends ModeloTabla<RptAsistenciaDetallado> {

    private final DateFormat dfFecha = new SimpleDateFormat("dd.MM.yyyy");
    private final DateFormat dfHora = new SimpleDateFormat("HH:mm:ss");

    public MTDetalleRegistroAsistencia(List<RptAsistenciaDetallado> datos) {
        super(datos);
        this.nombreColumnas = new String[]{"IND", "NRO DOCUMENTO", "EMPLEADO", "ÁREA", "FECHA", "TIPO", "MOTIVO", "H. REGULAR", "H. ENTRADA", "TARDANZA (min)", "H. SALIDA", "EXTRA (min)"};
    }

    @Override
    public Object getValorEn(int rowIndex, int columnIndex) {
        RptAsistenciaDetallado detalle = this.datos.get(rowIndex);
        switch (columnIndex) {
            case 0:
            default:
                return "";

        }
    }

    public String evento(char ev) {
        switch (ev) {
            case 'E':
                return "ENTRADA";
            case 'S':
                return "SALIDA";
            default:
                return "";
        }
    }

    public String tipo(char t) {
        switch (t) {
            case 'P':
                return "PERMISO";
            case 'R':
                return "REFRIGERIO";
            case 'T':
                return "JORNADA";
            default:
                return "";
        }
    }

    public String resultado(String r) {
        switch (r.charAt(0)) {
            case 'V':
                return "VACACIÓN";
            case 'S':
                return "SISGEDO - SALIDA";
            case 'P':
                return "PERMISO POR FECHA";
            case 'H':
                return "PERMISO POR HORAS";
            case 'U':
                return "SUSPENSION";
            case 'E':
                return "FERIADO";
            case 'T':
                return "TARDANZA";
            case 'R':
                return "REGULAR";
            case 'F':
                return "FALTA INJ.";
            case 'O':
                return "OBSERVACIÓN";
            default:
                return "";
        }
    }

}
