/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.algoritmo;

import com.biosis.biosislite.entidades.asistencia.Asistencia;
import java.util.List;

public interface Interprete<T> {
    List<T> interpretar(List<Asistencia> registroAsistencia);
}
