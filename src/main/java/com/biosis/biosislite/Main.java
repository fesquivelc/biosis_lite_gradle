/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite;

import com.biosis.biosislite.vistas.dialogos.DlgLogin;
import com.personal.utiles.PropertiesUtil;
import java.io.File;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author fesquivelc
 */
public class Main {

    public static String APLICACION_TITULO = "";
    public static String APLICACION_FONDO = "";
    public static String LOGIN_TITULO = "";
    public static String LOGIN_SUBTITULO = "";
    public static String LOGIN_IMAGEN = "";
    public static String REPORTE_INSTITUCION = "";
    public static String REPORTE_RUC = "";
    public static String REPORTE_LOGO = "";
    
    public static File FICHERO_REPORTE_SALIDA;

    public static void main(String[] args) {
        // TODO code application logic here
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equalsIgnoreCase(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {

        }

//        System.setProperty(
//                "Quaqua.tabLayoutPolicy", "wrap"
//        );
//        try {
//            UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
//        } catch (Exception e) {
//
//        }
        Properties props = PropertiesUtil.cargarProperties("config/interfaz.properties");
        Properties recursos = PropertiesUtil.cargarProperties("config/recursos.properties");
        APLICACION_TITULO = props.getProperty("aplicacion_titulo");
        LOGIN_TITULO = props.getProperty("login_titulo");
        LOGIN_SUBTITULO = props.getProperty("login_subtitulo");
        LOGIN_IMAGEN = props.getProperty("login_imagen");
        APLICACION_FONDO = props.getProperty("aplicacion_fondo");
        REPORTE_INSTITUCION = props.getProperty("reporte_institucion");
        REPORTE_LOGO = props.getProperty("reporte_logo");
        REPORTE_RUC = props.getProperty("reporte_ruc");
        
        FICHERO_REPORTE_SALIDA = new File(recursos.getProperty("reporte_permisos"));
        DlgLogin principal = new DlgLogin(null, true);
        principal.setVisible(true);

    }

}
