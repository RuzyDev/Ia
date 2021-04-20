package br.com.arcom.signpad.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {

    final static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/y HH:mm:ss");
    final static SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/y");

    public static String buscarDataAtual(Boolean time) {
        return time ? sdf1.format(new Date()) : sdf2.format(new Date());
    }

    public static String buscarDataAtual(Date date, Boolean time) {
        return time ? sdf1.format(date) : sdf2.format(new Date());
    }

}
