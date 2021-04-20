package br.com.arcom.signpad.api;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Optional;

import br.com.arcom.signpad.connection.RestSSLConnection;
import br.com.arcom.signpad.model.ResponseRequest;
import br.com.arcom.signpad.util.UtilConnection;
import br.com.arcom.signpad.util.UtilMobile;

public class SigaService {

    public static ResponseRequest buscarToken(Context context) {
        String caCertString;
        InputStream caInput;
        try {
            String fileCertPath = UtilConnection.getCerificatePath(context);
            String senhaDiaria = UtilMobile.getPassword();
            caInput = new BufferedInputStream(new FileInputStream(fileCertPath));
            caCertString = UtilConnection.readFully(caInput);
            caInput.close();

            String url = "http://siga.arcom.com.br/api/seguranca/v2/login";
            RestSSLConnection connection = new RestSSLConnection(url, caCertString, "Busca Token...");
            connection.addRequestProperty("Authorization", "Bearer " + "");
            connection.addRequestProperty("Content-type", "application/json");
            String json = String.format( "{\n\"idUsuario\": 43,\n\"senha\": \"%s\",\n\"apenasToken\": true\n}", senhaDiaria);
            connection.addBodyParam(json);

            String dados = connection.doPost();
            int responseCode = connection.getResponseCode();

            return new ResponseRequest(responseCode, dados);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ResponseRequest salvarDados(Context context, String token, String pathPdf, String mUsuarioNomeCom, String mUsuarioCpf, Date date) {
        return null;
    }

}
