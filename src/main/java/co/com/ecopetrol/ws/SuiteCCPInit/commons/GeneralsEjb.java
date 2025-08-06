package co.com.ecopetrol.ws.SuiteCCPInit.commons;

import java.util.Calendar;

/**
 *
 * @author STC
 */
public class GeneralsEjb {

    public static String getDesFechaSqlFormatFromCalendarV2(Calendar calendar) {
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        while (day.length() < 2) {
            day = "0" + day;
        }
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        while (month.length() < 2) {
            month = "0" + month;
        }
        return calendar.get(Calendar.YEAR) + month + day;
    }

    public static String getDesFechaSqlFormatFromCalendar(Calendar calendar) {
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        while (day.length() < 2) {
            day = "0" + day;
        }
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        while (month.length() < 2) {
            month = "0" + month;
        }
        return calendar.get(Calendar.YEAR) + "-" + month + "-" + day;
    }

    public static String getDesFechaDiaMesAnioHorasSqlFormat(Calendar calendar) {
        String desFecha = GeneralsEjb.getDesFechaSqlFormatFromCalendar(calendar);
        String horas = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minutos = String.valueOf(calendar.get(Calendar.MINUTE));
        String segundos = String.valueOf(calendar.get(Calendar.SECOND));
        String milisegundos = String.valueOf(calendar.get(Calendar.MILLISECOND));
        while (horas.length() < 2) {
            horas = "0" + horas;
        }
        while (minutos.length() < 2) {
            minutos = "0" + minutos;
        }
        while (segundos.length() < 2) {
            segundos = "0" + segundos;
        }
        while (milisegundos.length() < 3) {
            milisegundos = "0" + milisegundos;
        }
        desFecha += " " + horas + ":" + minutos + ":" + segundos + "." + milisegundos;
        return desFecha;
    }
}
