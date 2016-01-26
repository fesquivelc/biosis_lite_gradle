/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biosis.biosislite.algoritmo;

import com.biosis.biosislite.controladores.AsignacionHorarioControlador;
import com.biosis.biosislite.controladores.ContratoControlador;
import com.biosis.biosislite.controladores.FeriadoControlador;
import com.biosis.biosislite.controladores.MarcacionControlador;
import com.biosis.biosislite.controladores.PermisoControlador;
import com.biosis.biosislite.controladores.VacacionControlador;
import com.biosis.biosislite.controladores.sisgedo.BoletaControlador;
import com.biosis.biosislite.entidades.AsignacionHorario;
import com.biosis.biosislite.entidades.Feriado;
import com.biosis.biosislite.entidades.Marcacion;
import com.biosis.biosislite.entidades.Permiso;
import com.biosis.biosislite.entidades.Turno;
import com.biosis.biosislite.entidades.Vacacion;
import com.biosis.biosislite.entidades.asistencia.Asistencia;
import com.biosis.biosislite.entidades.asistencia.DetalleAsistencia;
import com.biosis.biosislite.entidades.escalafon.Contrato;
import com.biosis.biosislite.entidades.escalafon.Empleado;
import com.biosis.biosislite.entidades.sisgedo.Boleta;
import com.personal.utiles.FechaUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 *
 * @author Francis
 */
public class AnalizadorAsistencia {

    private final boolean boletaExterna = false;
    /*
     VALORES PARA COMPARAR EN LA ASISTENCIA
     */

    static final int NINGUNO = 0;
    public static final int PERMISO_FECHA = 1;
    public static final int BOLETA_VACACION = 13;
    public static final int BOLETA_PERMISO = 11;
    public static final int FERIADO = 2;
    public static final int VACACION = 3;
    public static final int PERMISO_HORA = 4;
    public static final int ASISTENCIA = 5;
    /*
     RESULTADOS DE ASISTENCIA
     */
    public static final int REGULAR = 0;
    public static final int TARDANZA = -1;
    public static final int INASISTENCIA = -2;
    public static final int INCONSISTENCIA = -3;
    /*
     LISTADOS QUE CONTIENEN LA INFORMACION TANTO DE FERIADOS, PERMISOS Y VACACIONES PARA EL EMPLEADO
     */
    private List<Feriado> feriadoList;
    private List<Permiso> permisoList;
    private List<Vacacion> vacacionList;
    private List<Boleta> boletaXFechaList;
    private List<Boleta> boletaXHoraList;
    /*
     LISTADO DE LAS MARCACIONES DEL EMPLEADO
     */
    private List<Marcacion> marcacionList;
    /*
     CONTROLADORES
     */
    private final AsignacionHorarioControlador asghorc = new AsignacionHorarioControlador();
    private final BoletaControlador bolc = BoletaControlador.getInstance();
    private final ContratoControlador contc = ContratoControlador.getInstance();
    private final FeriadoControlador ferc = new FeriadoControlador();
    private final PermisoControlador permc = new PermisoControlador();
    private final VacacionControlador vacc = new VacacionControlador();
    private final MarcacionControlador marc = new MarcacionControlador();

    private final AnalizadorDiario analizadorDiario = new AnalizadorDiario();

    public List<Asistencia> analizarAsistencia(List<Empleado> empleadoList, Date fechaInicio, Date fechaFin) {
        List<Asistencia> asistenciaList = new ArrayList<>();
        cargarFeriados(fechaInicio, fechaFin);
//        Object objetoPermiso;
//        int tipoPermiso;
        empleadoList.stream().forEach(empleado -> {
            cargarSalidas(empleado, fechaInicio, fechaFin);
            if (this.boletaExterna) {
                cargarBoletas(empleado, fechaInicio, fechaFin);
            }
            cargarVacaciones(empleado, fechaInicio, fechaFin);
            List<Contrato> contratos = contc.obtenerContratosXFechas(empleado, fechaInicio, fechaFin);
            Date desde1 = fechaInicio;
            Date hasta1 = fechaFin;

            this.cargarMarcaciones(empleado, desde1, hasta1);
            this.analizadorDiario.setMarcaciones(marcacionList);

            List<AsignacionHorario> asignaciones = asghorc.buscarXEmpleado(empleado, desde1, hasta1);

            asignaciones.stream().forEach(asignacion -> {
                Date desde2 = desde1.before(asignacion.getFechaInicio()) ? asignacion.getFechaInicio() : desde1;
                Date hasta2 = hasta1.before(asignacion.getFechaFin()) ? hasta1 : asignacion.getFechaFin();

                Calendar iteradorDia = Calendar.getInstance();
                iteradorDia.setTime(desde2);
                Boleta boletaXFecha = null;
                Permiso permisoXFecha = null;
                Vacacion vacacion = null;
                List<Turno> turnos = asignacion.getHorario().getTurnoList();
                System.out.println("EMPLEADO: " + empleado.getNombreCompleto() + " TURNOS: ");
                turnos.stream().forEach(t -> System.out.println(String.format("ID: %s JORNADA: %s", t.getId(), t.getJornada().getNombre())));
                while (iteradorDia.getTime().compareTo(hasta2) <= 0) {
                    boletaXFecha = this.boletaExterna ? this.buscarBoletaXFecha(iteradorDia.getTime()) : null;
                    if (boletaXFecha == null) {
                        vacacion = this.buscarVacacion(iteradorDia.getTime());
                        if (vacacion == null) {
                            permisoXFecha = this.buscarPermisoXFecha(iteradorDia.getTime());
                            if (permisoXFecha == null) {
                                if (isDiaLaboral(iteradorDia.getTime(), turnos)) {
                                    Feriado feriado = this.buscarFeriado(iteradorDia.getTime());
                                    if (feriado == null) {
                                        asistenciaList.addAll(this.generarAsistencia(
                                                empleado,
                                                iteradorDia.getTime(),
                                                turnos.stream().filter(t -> this.isDiaLaboral(iteradorDia.getTime(), t)).collect(Collectors.toList())));
                                    } else {
                                        asistenciaList.add(this.generarAsistencia(empleado, iteradorDia.getTime(), feriado));
                                    }
                                }
                            } else {
                                Asistencia asistencia = this.generarAsistencia(empleado, iteradorDia.getTime(), permisoXFecha);
                                asistencia.setPermisoConGoce(permisoXFecha.getTipoPermiso().getTipoDescuento() == 'C');
                                asistenciaList.add(asistencia);

                            }
                        } else {
                            asistenciaList.add(this.generarAsistencia(empleado, iteradorDia.getTime(), vacacion));
                        }
                    } else {
                        asistenciaList.add(this.generarAsistencia(empleado, iteradorDia.getTime(), boletaXFecha));
                    }

                    iteradorDia.add(Calendar.DATE, 1);
                }

            });
        });

        return asistenciaList;
    }

    private List<DetalleAsistencia> desglosar(Turno turno) {
        List<DetalleAsistencia> desglose = new ArrayList<>();
        turno.getJornada().getDetalleJornadaList().forEach(detJorn -> {

            DetalleAsistencia detalle1 = new DetalleAsistencia();
            detalle1.setBandera(true);
            detalle1.setDiaSiguiente(false);
            detalle1.setHoraReferencia(detJorn.getEntrada());
            detalle1.setHoraReferenciaDesde(detJorn.getEntradaDesde());
            detalle1.setHoraReferenciaHasta(detJorn.getEntradaHasta());
            detalle1.setHoraReferenciaTolerancia(detJorn.getEntradaTolerancia());
            System.out.println(String.format("-- ENTRADA -- REFERENCIA: %s DESDE: %s HASTA: %s TOLERANCIA: %s", detalle1.getHoraReferencia(), detalle1.getHoraReferenciaDesde(), detalle1.getHoraReferenciaHasta(), detalle1.getHoraReferenciaTolerancia()));
            detalle1.setTipo('A');

            DetalleAsistencia detalle2 = new DetalleAsistencia();
            detalle2.setBandera(false);
            detalle2.setDiaSiguiente(detJorn.getSalida().before(detJorn.getEntrada()));
            detalle2.setHoraReferencia(detJorn.getSalida());
            detalle2.setHoraReferenciaDesde(detJorn.getSalidaDesde());
            detalle2.setHoraReferenciaHasta(detJorn.getSalidaHasta());
            detalle2.setTipo('A');

            desglose.add(detalle1);
            desglose.add(detalle2);
        });
        return desglose;
    }

    /*
    
     */
    private List<DetalleAsistencia> desglosar(List<Permiso> permisoList) {
        List<DetalleAsistencia> desglose = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        permisoList.stream().forEach(perm -> {

            DetalleAsistencia detalleI = new DetalleAsistencia();
            detalleI.setBandera(true);
            detalleI.setDiaSiguiente(false); //POR REVISAR
            detalleI.setHoraReferencia(perm.getHoraInicio());
            detalleI.setHoraReferenciaDesde(perm.getHoraInicio());
            cal.setTime(perm.getHoraInicio());
            cal.add(Calendar.MINUTE, 40); //VARIABLE
            detalleI.setHoraReferenciaHasta(cal.getTime());
            detalleI.setTipo('B');
            detalleI.setPermisoConGoce(perm.getTipoPermiso().getTipoDescuento() == 'C');

            DetalleAsistencia detalleF = new DetalleAsistencia();
            detalleF.setBandera(false);
            detalleF.setDiaSiguiente(false); //POR REVISAR
            detalleF.setHoraReferencia(perm.getHoraFin());
            cal.add(Calendar.SECOND, 1);
            detalleF.setHoraReferenciaDesde(cal.getTime());
            detalleF.setMotivo(perm.getTipoPermiso().getNombre());
            detalleF.setPermisoConGoce(perm.getTipoPermiso().getTipoDescuento() == 'C');
            detalleF.setTipo('B');
            desglose.add(detalleI);
            desglose.add(detalleF);
        });
        return desglose;
    }

    private List<DetalleAsistencia> desglosarBoleta(List<Boleta> permisoList) {
        List<DetalleAsistencia> desglose = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        permisoList.stream().forEach(perm -> {
            //RECORDAR QUE LOS TIPOS DE PERMISO SIN GOCE SON : 19,22,23,25,26
            int idMotivo = perm.getMotivo().getId().intValue();
            System.out.println("MOTIVO: " + idMotivo);
            DetalleAsistencia detalleI = new DetalleAsistencia();
            detalleI.setBandera(true);
            detalleI.setDiaSiguiente(false); //POR REVISAR
            detalleI.setHoraReferencia(FechaUtil.soloHora(perm.getInicioFechaHora()));
            detalleI.setHoraReferenciaDesde(FechaUtil.soloHora(perm.getInicioFechaHora()));
            cal.setTime(FechaUtil.soloHora(perm.getInicioFechaHora()));
            cal.add(Calendar.MINUTE, 40); //VARIABLE
            detalleI.setHoraReferenciaHasta(cal.getTime());
            detalleI.setTipo('S');
            detalleI.setPermisoConGoce(isConGoce(idMotivo));

            DetalleAsistencia detalleF = new DetalleAsistencia();
            detalleF.setBandera(false);
            detalleF.setDiaSiguiente(false); //POR REVISAR
            detalleF.setHoraReferencia(FechaUtil.soloHora(perm.getRetornoFechaHora()));
            cal.add(Calendar.SECOND, 1);
            detalleF.setHoraReferenciaDesde(cal.getTime());
            detalleF.setMotivo(perm.getMotivo().getDescripcion());
            detalleF.setPermisoConGoce(isConGoce(idMotivo));
            detalleF.setTipo('S');

            desglose.add(detalleI);
            desglose.add(detalleF);
        });
        return desglose;
    }

    private void cargarVacaciones(Empleado empleado, Date fechaInicio, Date fechaFin) {
        this.vacacionList = vacc.buscarXEmpleadoEntreFecha(empleado, fechaInicio, fechaFin);
    }

    private void cargarFeriados(Date fechaInicio, Date fechaFin) {
        this.feriadoList = ferc.buscarXFechas(fechaInicio, fechaFin);
        System.out.println("TAMAÃ‘O FERIADOS: " + this.feriadoList.size());
    }

    private void cargarSalidas(Empleado empleado, Date fechaInicio, Date fechaFin) {
        this.permisoList = permc.buscarXEmpleadoXFechaEntreFecha(empleado, fechaInicio, fechaFin);
    }

    private Feriado buscarFeriado(Date dia) {
        Date diaSoloFecha = FechaUtil.soloFecha(dia);
        try {
//            System.out.println("TAMAÃ‘O FERIADOS: "+this.feriadoList.size());
            Feriado feriado = this.feriadoList
                    .stream()
                    .filter(fer -> fer.getFechaInicio().compareTo(diaSoloFecha) <= 0 && fer.getFechaFin().compareTo(diaSoloFecha) >= 0)
                    .findFirst()
                    .get();
            return feriado;
        } catch (NoSuchElementException e) {
            System.out.println("NO HAY FERIADOS EN ESTA FECHA: " + diaSoloFecha);
            return null;
        }
    }

    private Vacacion buscarVacacion(Date dia) {
        try {
            Date soloFechaComparacion = FechaUtil.soloFecha(dia);
            Vacacion vacacion = this.vacacionList
                    .stream()
                    .filter(vac
                            -> {
                        if (vac.getFechaInicio().compareTo(soloFechaComparacion) <= 0
                        && vac.getFechaFin().compareTo(soloFechaComparacion) >= 0) {
                            if (vac.isHayReprogramacion()) {
                                return dia.compareTo(vac.getFechaInterrupcion()) < 0;
                            } else if (vac.isHayInterrupcion()) {
                                return dia.compareTo(vac.getInterrupcionVacacion().getFechaInicio()) < 0
                                || dia.compareTo(vac.getInterrupcionVacacion().getFechaFin()) > 0;
                            } else {
                                return true;
                            }

                        } else {
                            return false;
                        }
                    })
                    .findFirst()
                    .get();
            return vacacion;
        } catch (NoSuchElementException e) {
            System.out.println("NO HAY FERIADOS EN ESTA FECHA");
            return null;
        }
    }

    private Permiso buscarPermisoXFecha(Date dia) {
        try {
            Date soloFechaComparacion = FechaUtil.soloFecha(dia);
            Permiso permiso = this.permisoList
                    .stream()
                    .filter(perm
                            -> perm.getFechaInicio().compareTo(soloFechaComparacion) <= 0 && perm.getFechaFin().compareTo(soloFechaComparacion) >= 0
                    )
                    .findFirst()
                    .get();
            return permiso;
        } catch (NoSuchElementException e) {
            System.out.println("NO HAY PERMISOS EN ESA FECHA");
            return null;
        }

    }

    private boolean isDiaLaboral(Date dia, List<Turno> turnos) {
        boolean resultado = false;
        for (Turno turno : turnos) {
            resultado = resultado || isDiaLaboral(dia, turno);
        }
        return resultado;
    }

    private boolean isDiaLaboral(Date fecha, Turno turno) {
        System.out.println(String.format("TIPO TURNO: %s", turno.getTipo()));
        if (turno.getTipo() == 'S') {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fecha);

            switch (cal.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    return turno.isLunes();
                case Calendar.TUESDAY:
                    return turno.isMartes();
                case Calendar.WEDNESDAY:
                    return turno.isMiercoles();
                case Calendar.THURSDAY:
                    return turno.isJueves();
                case Calendar.FRIDAY:
                    return turno.isViernes();
                case Calendar.SATURDAY:
                    return turno.isSabado();
                case Calendar.SUNDAY:
                    return turno.isDomingo();
                default:
                    return false;
            }
        } else {

            return turno.getFechaInicio().compareTo(fecha) <= 0
                    && turno.getFechaFin().compareTo(fecha) >= 0;
        }
    }

    /*
     Genera un registro de asistencia para el permiso por fecha
     */
    private Asistencia generarAsistencia(Empleado empleado, Date dia, Permiso permisoXFecha) {
        Asistencia asistencia = new Asistencia();
        asistencia.setEmpleado(empleado);
        asistencia.setFecha(dia);
        asistencia.setResultado(PERMISO_FECHA);
        asistencia.setPermiso(permisoXFecha);
        return asistencia;
    }

    /*
     Genera un registro de asistencia para la vacacion
     */
    private Asistencia generarAsistencia(Empleado empleado, Date dia, Vacacion vacacion) {
        Asistencia asistencia = new Asistencia();
        asistencia.setEmpleado(empleado);
        asistencia.setFecha(dia);
        asistencia.setResultado(VACACION);
        asistencia.setVacacion(vacacion);
        return asistencia;
    }

    private Asistencia generarAsistencia(Empleado empleado, Date dia, Feriado feriado) {
        Asistencia asistencia = new Asistencia();
        asistencia.setEmpleado(empleado);
        asistencia.setFecha(dia);
        asistencia.setResultado(FERIADO);
        asistencia.setFeriado(feriado);
        return asistencia;
    }

    private Asistencia generarAsistencia(Empleado empleado, Date dia, Boleta boletaXFecha) {
        //TENER EN CUENTA QUE 12 Y 13 SON VACACIONES
        int idMotivo = boletaXFecha.getMotivo().getId().intValue();
        Asistencia asistencia = new Asistencia();
        asistencia.setEmpleado(empleado);
        asistencia.setFecha(dia);
        asistencia.setResultado(idMotivo == 12 || idMotivo == 13 ? BOLETA_VACACION : BOLETA_PERMISO);
        asistencia.setBoleta(boletaXFecha);
        return asistencia;
    }

    /*
     Genera un registro de asistencia para el anÃ¡lisis de los turnos
     */
    private List<Asistencia> generarAsistencia(Empleado empleado, Date dia, List<Turno> turnos) {
        /*
         Debemos realizar un anÃ¡lisis por turno buscando los permisos de cada uno
         */
        List<Asistencia> asistenciaList = new ArrayList<>();

        turnos.stream().forEach(turno -> {
            Asistencia asistencia = new Asistencia();
            asistencia.setPermisoList(this.desglosar(this.buscarPermisoXHora(empleado, dia)));
            if (this.boletaExterna) {
                asistencia.getPermisoList().addAll(this.desglosarBoleta(this.buscarBoletaXHora(empleado, dia)));
            }

            asistencia.setFecha(dia);
            asistencia.setDetalleAsistenciaList(this.desglosar(turno));
            asistencia.setEmpleado(empleado);
            asistencia.setResultado(ASISTENCIA);
            analizadorDiario.setPermisos(asistencia.getPermisoList());
            analizadorDiario.setAsistencia(asistencia);
            analizadorDiario.iniciar();
            asistenciaList.add(asistencia);
        });

        return asistenciaList;
    }

    private void cargarMarcaciones(Empleado empleado, Date desde1, Date hasta1) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(hasta1);
        cal.add(Calendar.DATE, 1);
        
        this.marcacionList = this.marc.buscarXEmpleadoEntreFecha(empleado, desde1, cal.getTime());
        if(this.marcacionList == null){
            System.out.println(String.format("Listado NULL -> Empleado: %s, desde1: %s hasta: %s", empleado.getNombreCompleto(),desde1,hasta1));
        }
    }

    private List<Permiso> buscarPermisoXHora(Empleado empleado, Date dia) {
        return this.permc.buscarPermisosPorHoraEnFecha(empleado, dia);
    }

    private Boleta buscarBoletaXFecha(Date dia) {
        try {
            Date soloFechaComparacion = FechaUtil.soloFecha(dia);
            Boleta permiso = this.boletaXFechaList
                    .stream()
                    .filter(perm
                            -> FechaUtil.soloFecha(perm.getInicioFechaHora()).compareTo(soloFechaComparacion) <= 0
                            && FechaUtil.soloFecha(perm.getFinFechaHora()).compareTo(soloFechaComparacion) >= 0
                    )
                    .findFirst()
                    .get();
            return permiso;
        } catch (NoSuchElementException e) {
            System.out.println("NO HAY BOLETAS POR FECHA EN ESTA FECHA");
            return null;
        }
    }

    private List<Boleta> buscarBoletaXHora(Empleado empleado, Date dia) {

        try {
            
            Date soloFechaComparacion = FechaUtil.soloFecha(dia);

            for (int i = 0; i < this.boletaXHoraList.size(); i++) {
                if (this.boletaXHoraList.get(i).getInicioFechaHora() == null || this.boletaXHoraList.get(i).getRetornoFechaHora() == null) {
                    this.boletaXHoraList.remove(i);
                }
            }

            List<Boleta> permiso = this.boletaXHoraList
                    .stream()
                    .filter(perm
                            -> FechaUtil.soloFecha(perm.getInicioFechaHora()).compareTo(soloFechaComparacion) == 0
                    )
                    .collect(Collectors.toList());

            return permiso;
        } catch (NoSuchElementException e) {
            System.out.println("NO HAY BOLETAS POR FECHA EN ESTA FECHA");
            return null;
        }
    }

    private boolean isConGoce(int idMotivo) {
        //RECORDAR QUE LOS TIPOS DE PERMISO SIN GOCE SON : 19,22,23,25,26
        return !(idMotivo == 19 || idMotivo == 22 || idMotivo == 23 || idMotivo == 25 || idMotivo == 26);
    }

    private void cargarBoletas(Empleado empleado, Date fechaInicio, Date fechaFin) {
//        List<Boleta> boletas = this.bolc.permisoXFechaXEmpleadoEntreFecha(empleado, fechaInicio, fechaFin);
//        System.out.println("BOLETAS: " + boletas.size());

        this.boletaXFechaList = this.bolc.permisoXFechaXEmpleadoEntreFecha(empleado, fechaInicio, fechaFin);
//        System.out.println("TAMAÑO BOLETAS FECHA: "+this.boletaXFechaList.size());
//        for (int i = 0 ; i<boletaXFechaList.size(); i++) {
//            if (boletaXFechaList.get(i).getFinFechaHora() == null || boletaXFechaList.get(i).getInicioFechaHora() == null) {
//                this.boletaXFechaList.remove(i);
//            }
//        }
//        System.out.println("TAMAÑO BOLETAS FECHA: "+this.boletaXFechaList.size());
//        System.out.println("BOLETA ES NULL? "+this.boletaXFechaList.get(0).getFinFechaHora()+" - "+this.boletaXFechaList.get(0).getInicioFechaHora());
//        this.boletaXFechaList = boletas.stream().filter(bol -> {
//            if (bol.getInicioFechaHora() == null || bol.getFinFechaHora() == null) {
//                return false;
//            } else {
//
//                return FechaUtil.soloFecha(bol.getInicioFechaHora()).compareTo(FechaUtil.soloFecha(bol.getFinFechaHora())) < 0;
//            }
//        }).collect(Collectors.toList());
        System.out.println("BOLETAS POR FECHA: " + boletaXFechaList.size());
        this.boletaXHoraList = this.bolc.permisoXHoraXEmpleadoEntreFecha(empleado, fechaInicio, fechaFin);
        for (int i = 0; i < this.boletaXHoraList.size(); i++) {
            if (this.boletaXHoraList.get(i).getInicioFechaHora() == null || this.boletaXHoraList.get(i).getRetornoFechaHora() == null || this.boletaXHoraList.get(i).getMotivo() == null) {
                this.boletaXHoraList.remove(i);
                System.out.println("Encontro null...");
            }
        }

//        for (int i = 0; i<boletaXHoraList.size();i++ ){
//            System.out.println("DATOS HORA :"+ boletaXFechaList.get(i).getInicioFechaHora() );
//        }
//        this.boletaXHoraList = boletas.stream().filter(bol -> {
//            if (bol.getInicioFechaHora() == null || bol.getFinFechaHora() == null) {
//                return false;
//            } else {
//                return FechaUtil.soloFecha(bol.getInicioFechaHora()).compareTo(FechaUtil.soloFecha(bol.getFinFechaHora())) == 0;
//            }
//        }).collect(Collectors.toList());
        System.out.println("BOLETAS POR HORA: " + boletaXHoraList.size());
//        this.boletaXHoraList = boletas.stream().filter(bol -> FechaUtil.soloFecha(bol.getInicioFechaHora()).compareTo(FechaUtil.soloFecha(bol.getRetornoFechaHora())) == 0).collect(Collectors.toList());
    }
}
