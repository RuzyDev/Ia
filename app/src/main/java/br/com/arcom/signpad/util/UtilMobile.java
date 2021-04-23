package br.com.arcom.signpad.util;

public class UtilMobile {

    public static String getPassword() {
        long senha;
        long lAno;
        String sSenha;

        // Calcula Senha do dia
        String dateHoje = UtilDate.buscarDataAtual(false);
        lAno = Long.parseLong(dateHoje.substring(6, 10));
        sSenha = dateHoje.replace("/", "");
        senha = Long.parseLong(sSenha);
        senha = senha ^ lAno;
        senha = senha >> 1;

        return String.valueOf(senha);
    }

}
