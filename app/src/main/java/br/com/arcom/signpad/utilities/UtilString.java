package br.com.arcom.signpad.utilities;

public abstract class UtilString {

    public static String formatarCpf(String cpf) {
        if (cpf.length() == 10) cpf = "0"+cpf;
        if (cpf.length() == 9) cpf = "00"+cpf;
        if (cpf.length() == 8) cpf = "000"+cpf;
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
    }
}
