package br.com.arcom.signpad.utilities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilDate {

    public static final String DATE_TIME = "dd/MM/y HH:mm:ss";
    public static final String DATE_TIME_OP2 = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_TIME_OP3 = "dd-MM-yyyy HH:mm";
    public static final String DATE = "dd/MM/y";
    public static final String DATE_OP2 = "dd-MM-y";

    public static String buscarDataAtual(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    public static String buscarDataAtual(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

}
