package br.com.arcom.signpad.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilDate {

    private final static SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/y HH:mm:ss");

    public static String buscarDataAtual() { return sdf1.format(new Date()); }

}
