package br.com.arcom.signpad.api;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import br.com.arcom.signpad.data.LgpdVisitante;
import br.com.arcom.signpad.models.BuscarLgpdVisitanteResponse;
import br.com.arcom.signpad.models.LgpdVisitanteRequest;
import br.com.arcom.signpad.models.SigaResponse;
import br.com.arcom.signpad.utilities.UtilDate;
import br.com.arcom.signpad.utilities.UtilMobile;
import retrofit2.Response;

public class SigaRepository {

    private static SigaRepository sigaRepository;

    public static SigaRepository getInstance() {
        if (sigaRepository == null) sigaRepository = new SigaRepository();
        return sigaRepository;
    }

    private final SigaApi sigaApi;

    public SigaRepository() {
        sigaApi = new SigaService().getApi();
    }

    public SigaResponse buscarToken() {
        try {
            Response<String> response = sigaApi.buscarToken(new LgpdVisitanteRequest(43L, UtilMobile.getPassword(), true)).execute();
            return new SigaResponse(false, response.body());
        } catch (IOException e) {
            return new SigaResponse(true, e.getMessage());
        }
    }

    public SigaResponse salvarDadosVisitante(
            String sigaToken, String pathPdf,
            String mUsuarioNomeCom, Long mUsuarioCpf,
            Date dataAss
    ) throws Exception {
        File file = new File(pathPdf);
        String base64 = encodeFileToBase64Binary(file);
        try {
            Response<Void> response = sigaApi.salvarDadosLgpdVisitante(
                    ("Bearer " + sigaToken),
                    new DadosLgpdVisitante(
                            mUsuarioNomeCom,
                            mUsuarioCpf,
                            UtilDate.buscarDataAtual(dataAss, UtilDate.DATE_TIME_OP2),
                            base64
                    )
            ).execute();
            if (response.isSuccessful()) {
                return new SigaResponse(false, "Dados salvos com sucesso!");
            }
            throw new Exception(response.errorBody().string());
        } catch (Exception e) {
            throw e;
        }

    }

    public BuscarLgpdVisitanteResponse buscarLpgdVisitante(String sigaToken, String modoPesquisa, String nome, Long cpf) throws Exception {
        try {
            if (modoPesquisa.equals("nome")) {
                Response<List<LgpdVisitante>> response = sigaApi.buscarLpgdVisitante(("Bearer " + sigaToken), modoPesquisa, nome).execute();
                if (response.isSuccessful()) {
                    return new BuscarLgpdVisitanteResponse(false, "", response.body());
                }
                throw new Exception(response.errorBody().string());
            } else {
                Response<LgpdVisitante> response = sigaApi.buscarLpgdVisitante(("Bearer " + sigaToken), modoPesquisa, cpf).execute();
                if (response.isSuccessful()) {
                    return new BuscarLgpdVisitanteResponse(false, "", response.body());
                }
                throw new Exception(response.errorBody().string());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public SigaResponse enviarPdfPorEmail(String sigaToken, String email, Long cpf) throws Exception {
        try {
            Response<Void> response = sigaApi.enviarPdfPorEmail(("Bearer " + sigaToken), email, cpf).execute();
            if (response.isSuccessful()) {
               return new SigaResponse(false, "Termo de consentimento enviado com sucesso!");
            }
            throw new Exception(response.errorBody().string());
        } catch (Exception e) {
            throw e;
        }
    }

    private String encodeFileToBase64Binary(File yourFile) {
        int size = (int) yourFile.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(yourFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String encoded = Base64.encodeToString(bytes, Base64.NO_WRAP);
        return encoded;
    }
}
