/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.utiles;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Francis
 */
public class HerramientaGeneral {
    public static final DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    public static final DateFormat formatoNombreDiaFecha = new SimpleDateFormat("EEEE dd/MM/yyyy");
    public static final DateFormat formatoHoraMinuto = new SimpleDateFormat("HH:mm");
    public static final DateFormat formatoHoraMinutoSegundo = new SimpleDateFormat("HH:mm:ss");
    public static final DateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final Date horaFinal = new Date(104399000);
}
